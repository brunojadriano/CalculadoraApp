package com.example.calculadoraapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class CalculatorActivity : Activity() {
    private lateinit var etNumero1: EditText
    private lateinit var etNumero2: EditText
    private lateinit var tvResultado: TextView
    private lateinit var btnCompartilhar: Button

    private lateinit var botoes: List<Button>

    private var mensagem = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!Dados.logado) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_calculator)

        etNumero1 = findViewById(R.id.etNumero1)
        etNumero2 = findViewById(R.id.etNumero2)
        tvResultado = findViewById(R.id.tvResultado)
        btnCompartilhar = findViewById(R.id.btnCompartilhar)

        val btnSomar = findViewById<Button>(R.id.btnSomar)
        val btnSubtrair = findViewById<Button>(R.id.btnSubtrair)
        val btnMultiplicar = findViewById<Button>(R.id.btnMultiplicar)
        val btnDividir = findViewById<Button>(R.id.btnDividir)

        botoes = listOf(
            btnSomar,
            btnSubtrair,
            btnMultiplicar,
            btnDividir
        )

        val observador = object : TextWatcher {

            override fun beforeTextChanged(
                s: CharSequence?, start: Int, count: Int, after: Int
            ) {}

            override fun onTextChanged(
                s: CharSequence?, start: Int, before: Int, count: Int
            ) {}

            override fun afterTextChanged(s: Editable?) {
                atualizarBotoes()
            }
        }

        etNumero1.addTextChangedListener(observador)
        etNumero2.addTextChangedListener(observador)

        atualizarBotoes()

        btnSomar.setOnClickListener {
            calcular('+')
        }

        btnSubtrair.setOnClickListener {
            calcular('-')
        }

        btnMultiplicar.setOnClickListener {
            calcular('*')
        }

        btnDividir.setOnClickListener {
            calcular('/')
        }

        btnCompartilhar.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, mensagem)

            startActivity(
                Intent.createChooser(intent, "Compartilhar com")
            )
        }

        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            Dados.logado = false

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
            finish()
        }
    }

    private fun atualizarBotoes() {
        val a = etNumero1.text.toString()
            .replace(',', '.').toDoubleOrNull()

        val b = etNumero2.text.toString()
            .replace(',', '.').toDoubleOrNull()

        val habilitado = a != null && b != null

        botoes.forEach {
            it.isEnabled = habilitado
        }

        tvResultado.text = "Resultado:"
        btnCompartilhar.visibility = View.GONE
        mensagem = ""
    }

    private fun calcular(operacao: Char) {
        val a = etNumero1.text.toString()
            .replace(',', '.').toDoubleOrNull()

        val b = etNumero2.text.toString()
            .replace(',', '.').toDoubleOrNull()

        if (a == null || b == null) {
            tvResultado.text = "Números inválidos"
            return
        }

        if (operacao == '/' && b == 0.0) {
            tvResultado.text = "Não é possível dividir por zero"
            btnCompartilhar.visibility = View.GONE
            return
        }

        val resultado = when (operacao) {
            '+' -> a + b
            '-' -> a - b
            '*' -> a * b
            '/' -> a / b
            else -> return
        }

        val operadorTexto = when (operacao) {
            '+' -> "mais"
            '-' -> "menos"
            '*' -> "vezes"
            '/' -> "dividido por"
            else -> ""
        }

        tvResultado.text = "Resultado: ${formatar(resultado)}"

        mensagem = "Oi! Você sabia que ${formatar(a)} " +
                "$operadorTexto ${formatar(b)} é igual a " +
                "${formatar(resultado)}?"

        btnCompartilhar.visibility = View.VISIBLE
    }

    private fun formatar(numero: Double): String {
        return if (
            numero.isFinite() &&
            numero % 1.0 == 0.0 &&
            numero >= Long.MIN_VALUE.toDouble() &&
            numero < 9223372036854775808.0
        ) {
            numero.toLong().toString()
        } else {
            numero.toString()
        }
    }
}
