package com.myprojects.myTickets.data

import com.google.android.gms.analytics.ecommerce.Product

data class Ticket(
    val id: Long,
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
    val item: String,
    val cantidad: String,
    val precioUnidad: String,
    val precioFinal: String
)