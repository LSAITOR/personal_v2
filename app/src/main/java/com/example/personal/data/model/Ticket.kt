package com.example.personal.data.model

import java.sql.Date

data class Ticket(
    var id: Int = 0,
    var fecha: Date? = null,
    var cliente: String = "",
    var total: Double = 0.0,
    var estado: String = "",

    // detalle: Almacenara una lista (List) de tipo DetalleTicket (esto representa un conjunto de datos)
    var detalle: List<DetalleTicket> = listOf()
)
