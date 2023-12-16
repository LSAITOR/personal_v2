package com.example.personal.data.model

data class DetalleTicket(
    var id: Int = 0,
    var mticket: Ticket? = null, // el campo mticket podría aceptar valores nulos (?)
    var mproducto: Producto? = null,
    var cantidad: Int = 0,
    var costo: Double = 0.0,
    var precio: Double = 0.0,
    var importe: Double = 0.0

    // Solo sera necesario si en la base de datos NO existe el campo importe
    //var importe: Double = cantidad * precio
)
