package com.myprojects.myTickets.data

data class Ticket(
    val restaurante: String,
    val cif: String,
    val fecha: String,
    val hora: String,
    val items: List<Producto>,
    val precioSinIva: String,
    val iva: String,
    val precioConIva: String
)

data class Producto(
    val item: String,
    val cantidad: String,
    val precioUnidad: String,
    val precioFinal: String
)