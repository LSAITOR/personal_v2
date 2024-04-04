package com.example.personal.data.model

// Paso 1: Mapaear el modelo de la tabla cliente de sqlserver
// data class -> Son clases mas ligeras, creadas especalmente para almacenar datos
data class Cliente(
    var id: Int = 0,
    var nombre: String = "",
    var rfc: String = "",
    var direccion: String = "",
    var email: String = "",
    var estatus: Int = 0
)
