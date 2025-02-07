package com.myprojects.myTickets.data

data class Ticket(
    val id: String = "",
    val restaurante: String,
    val cif: String,
    val fecha: String,
    val hora: String,
    val items: MutableList<Producto>,
    val precioSinIva: String,
    val iva: String,
    val precioConIva: String
)

data class Producto(
    var item: String,
    var cantidad: String,
    var precioUnidad: String,
    var precioFinal: String
)