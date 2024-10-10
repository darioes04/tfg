package com.myprojects.myTickets.ticketView

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myprojects.myTickets.data.Ticket

@Composable
fun TicketScreen(ticket: Ticket, onConfirmClick: (Ticket) -> Unit) {
    var ticket by remember { mutableStateOf(ticket) }
    var restaurante by remember { mutableStateOf(ticket.restaurante) }
    var cif by remember { mutableStateOf(ticket.cif) }
    var fecha by remember { mutableStateOf(ticket.fecha) }
    var hora by remember { mutableStateOf(ticket.hora) }
    var totalSinIva by remember { mutableStateOf(ticket.precioSinIva) }  // Ahora es String
    var iva by remember { mutableStateOf(ticket.iva) }  // Ahora es String
    var totalConIva by remember { mutableStateOf(ticket.precioConIva) }  // Ahora es String

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 55.dp)
            .background(Color.White),
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

        items(ticket.items.size) { index ->
            val product = ticket.items[index]

            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicTextField(
                        value = product.item,
                        onValueChange = { newValue ->
                            val updatedProducts = ticket.items.toMutableList()
                            updatedProducts[index] = updatedProducts[index].copy(item = newValue)
                            ticket = ticket.copy(items = updatedProducts)
                        },
                        modifier = Modifier
                            .alignByBaseline()
                            .background(Color.Transparent),
                        textStyle = TextStyle(
                            color = Color.DarkGray,
                            fontSize = 16.sp
                        ),
                        singleLine = true
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Etiqueta "Importe ="
                    Text(
                        text = "Importe = ",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold
                    )

                    // Campo editable para el precio por unidad (String)
                    var precioUnidad by remember { mutableStateOf(product.precioUnidad) }
                    BasicTextField(
                        value = precioUnidad,
                        onValueChange = { newValue ->
                            precioUnidad = newValue
                            val updatedProducts = ticket.items.toMutableList()
                            updatedProducts[index] = updatedProducts[index].copy(precioUnidad = newValue)
                            ticket = ticket.copy(items = updatedProducts)
                        },
                        textStyle = TextStyle(
                            color = Color.DarkGray,
                            fontSize = 16.sp
                        ),
                        modifier = Modifier.width(IntrinsicSize.Min)
                    )

                    // Multiplicador "*"
                    Text(
                        text = " * ",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold
                    )

                    // Campo editable para la cantidad (String)
                    var cantidad by remember { mutableStateOf(product.cantidad) }
                    BasicTextField(
                        value = cantidad,
                        onValueChange = { newValue ->
                            cantidad = newValue
                            val updatedProducts = ticket.items.toMutableList()
                            updatedProducts[index] = updatedProducts[index].copy(cantidad = newValue)
                            ticket = ticket.copy(items = updatedProducts)
                        },
                        textStyle = TextStyle(
                            color = Color.DarkGray,
                            fontSize = 16.sp
                        ),
                        modifier = Modifier.width(IntrinsicSize.Min)
                    )

                    // Resultado final "= X.XX€"
                    Text(
                        text = " = ${product.precioFinal}€",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold
                    )
                }
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

    // Botón para confirmar y guardar los cambios
    Button(
        onClick = {
            // Crear un nuevo ticket actualizado con los valores modificados
            val updatedTicket = ticket.copy(
                restaurante = restaurante,
                cif = cif,
                fecha = fecha,
                hora = hora,
                precioSinIva = totalSinIva,
                precioConIva = totalConIva,
                iva = iva
            )
            // Llamar al callback con el ticket actualizado
            onConfirmClick(updatedTicket)
        },
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text("Confirmar Cambios")
    }
}
