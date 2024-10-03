package com.myprojects.myTickets.ticketView

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.myprojects.myTickets.data.Ticket

@Composable
fun TicketScreen(ticket: Ticket) {
    var restaurante by remember { mutableStateOf(ticket.restaurante) }
    var cif by remember { mutableStateOf(ticket.cif) }
    var fecha by remember { mutableStateOf(ticket.fecha) }
    var hora by remember { mutableStateOf(ticket.hora) }
    var totalSinIva by remember { mutableStateOf("${ticket.precioSinIva}") }
    var iva by remember { mutableStateOf("${ticket.iva}") }
    var totalConIva by remember { mutableStateOf("${ticket.precioConIva}") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            TicketInfoRow(label = "Restaurante", value = restaurante) { newValue ->
                restaurante = newValue
            }
        }
        item {
            TicketInfoRow(label = "CIF", value = cif) { newValue ->
                cif = newValue
            }
        }
        item {
            TicketInfoRow(label = "Fecha", value = fecha) { newValue ->
                fecha = newValue
            }
        }
        item {
            TicketInfoRow(label = "Hora", value = hora) { newValue ->
                hora = newValue
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Productos:", fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold)
        }

        item {
            ticket.items.forEach { product ->
                Text(text = "${product.item}:\n\t\t\t\tImporte = ${product.precioUnidad} * " +
                        "${product.cantidad} = ${product.precioFinal}€"
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            TicketInfoRow(label = "Total sin IVA", value = totalSinIva) { newValue ->
                totalSinIva = newValue
            }
        }
        item {
            TicketInfoRow(label = "IVA", value = iva) { newValue ->
                iva = newValue
            }
        }
        item {
            TicketInfoRow(label = "Total con IVA", value = totalConIva) { newValue ->
                totalConIva = newValue
            }
        }
    }
}
