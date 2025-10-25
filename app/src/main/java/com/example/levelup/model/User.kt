package com.example.levelup.model

data class User (
    val correo: String = "",
    val clave: String = "",
    val nombre: String = "",
    val rol: String = "" // Varible que dice si es admin o cliente
)