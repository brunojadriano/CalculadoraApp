package com.example.calculadoraapp

data class Usuario(
    val cpf: String,
    val senha: String
)

object Dados {

    val usuarios = mutableListOf(
        Usuario("12345678900", "1234")
    )

    var logado = false
}