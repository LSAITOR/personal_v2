package com.example.personal.data.model

// Primer paso
data class Usuario(
    var id: Int = 0,
    var nombre: String = "",
    var nick: String = "",
    var clave: String = "",
    var vigente: Int = 0
)

// Formatear el codigo, presionar CTRL + AlT + L
