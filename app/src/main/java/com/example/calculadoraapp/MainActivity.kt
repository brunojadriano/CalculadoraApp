package com.example.calculadoraapp

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Dados.logado) {
            abrirCalculadora()
            return
        }

        setContentView(R.layout.activity_main)

        val etCpf = findViewById<EditText>(R.id.etCpf)
        val etSenha = findViewById<EditText>(R.id.etSenha)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnCadastrar = findViewById<Button>(R.id.btnCadastrar)

        btnCadastrar.setOnClickListener {
            val cpf = etCpf.text.toString()
            val senha = etSenha.text.toString()

            if (cpf.length != 11 || senha.isEmpty()) {
                Toast.makeText(this, "Dados inválidos", Toast.LENGTH_SHORT).show()

            } else if (Dados.usuarios.any { it.cpf == cpf }) {
                Toast.makeText(this, "CPF já cadastrado", Toast.LENGTH_SHORT).show()

            } else {
                Dados.usuarios.add(Usuario(cpf, senha))
                Toast.makeText(this, "Usuário cadastrado!", Toast.LENGTH_SHORT).show()
            }
        }

        btnLogin.setOnClickListener {
            val cpf = etCpf.text.toString()
            val senha = etSenha.text.toString()

            val usuarioValido = Dados.usuarios.any {
                it.cpf == cpf && it.senha == senha
            }

            if (usuarioValido) {
                Dados.logado = true
                abrirCalculadora()

            } else {
                AlertDialog.Builder(this)
                    .setTitle("Erro no login")
                    .setMessage("CPF ou senha incorretos!")
                    .setPositiveButton("OK", null)
                    .show()
            }
        }
    }

    private fun abrirCalculadora() {
        val intent = Intent(this, CalculatorActivity::class.java)
        startActivity(intent)
        finish()
    }
}
