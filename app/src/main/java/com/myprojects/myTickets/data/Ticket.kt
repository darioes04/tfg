package com.myprojects.myTickets.data

data class Ticket(
    val restaurante: String,
    val cif: String,
    val fecha: String,
    val hora: String,
    val items: List<Producto>,
    val precioSinIva: Double,
    val iva: Double,
    val precioConIva: Double
)

data class Producto(
    val item: String,
    val cantidad: Int,
    val precioUnidad: Double,
    val precioFinal: Double
)