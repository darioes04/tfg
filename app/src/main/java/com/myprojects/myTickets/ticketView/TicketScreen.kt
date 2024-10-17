package com.myprojects.myTickets.ticketView

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TicketScreen(ticket: Ticket, onConfirmClick: (Ticket) -> Unit, onClickDelete: (Long) -> Unit) {
    var ticket by remember { mutableStateOf(ticket) }
    var restaurante by remember { mutableStateOf(ticket.restaurante) }
    var cif by remember { mutableStateOf(ticket.cif) }
    var fecha by remember { mutableStateOf(ticket.fecha) }
    var hora by remember { mutableStateOf(ticket.hora) }
    var totalSinIva by remember { mutableStateOf(ticket.precioSinIva) }
    var iva by remember { mutableStateOf(ticket.iva) }
    var totalConIva by remember { mutableStateOf(ticket.precioConIva) }

    Scaffold(
        topBar = { /* Si necesitas un top bar, lo defines aquí */ },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp)
                            .background(MaterialTheme.colorScheme.background),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            TicketInfoRow(label = "Restaurante", value = restaurante, "") { newValue ->
                                restaurante = newValue
                            }
                        }
                        item {
                            TicketInfoRow(label = "CIF", value = cif, "") { newValue ->
                                cif = newValue
                            }
                        }
                        item {
                            TicketInfoRow(label = "Fecha", value = fecha, "") { newValue ->
                                fecha = newValue
                            }
                        }
                        item {
                            TicketInfoRow(label = "Hora", value = hora, "") { newValue ->
                                hora = newValue
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Productos:",
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                            )
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
                                            .background(Color.Transparent),
                                        textStyle = TextStyle(
                                            color = MaterialTheme.colorScheme.onBackground,
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
                                    Text(
                                        text = "Importe = ",
                                        fontFamily = FontFamily.Serif,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )

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
                                            color = MaterialTheme.colorScheme.onBackground,
                                            fontSize = 16.sp
                                        ),
                                        modifier = Modifier.width(IntrinsicSize.Min)
                                    )

                                    Text(
                                        text = " * ",
                                        fontFamily = FontFamily.Serif,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )

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
                                            color = MaterialTheme.colorScheme.onBackground,
                                            fontSize = 16.sp
                                        ),
                                        modifier = Modifier.width(IntrinsicSize.Min)
                                    )

                                    Text(
                                        text = " = ",
                                        fontFamily = FontFamily.Serif,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )

                                    var precioFinal by remember { mutableStateOf(product.precioFinal) }
                                    BasicTextField(
                                        value = precioFinal,
                                        onValueChange = { newValue ->
                                            precioFinal = newValue
                                            val updatedProducts = ticket.items.toMutableList()
                                            updatedProducts[index] = updatedProducts[index].copy(precioFinal = newValue)
                                            ticket = ticket.copy(items = updatedProducts)
                                        },
                                        textStyle = TextStyle(
                                            color = MaterialTheme.colorScheme.onBackground,
                                            fontSize = 16.sp
                                        ),
                                        modifier = Modifier.width(IntrinsicSize.Min)
                                    )

                                    Text(
                                        text = "€",
                                        fontFamily = FontFamily.Serif,
                                        fontWeight = FontWeight.Bold,
                                    )
                                }
                            }
                        }

                        item { Spacer(modifier = Modifier.height(16.dp)) }

                        item {
                            TicketInfoRow(label = "Total sin IVA", value = ticket.precioSinIva, "€") { newValue ->
                                totalSinIva = newValue
                            }
                        }

                        item {
                            TicketInfoRow(label = "IVA", value = ticket.iva, "%") { newValue ->
                                iva = newValue
                            }
                        }

                        item {
                            TicketInfoRow(label = "Total con IVA", value = ticket.precioConIva, "€") { newValue ->
                                totalConIva = newValue
                            }
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                val updatedTicket = ticket.copy(
                                    restaurante = restaurante,
                                    cif = cif,
                                    fecha = fecha,
                                    hora = hora,
                                    precioSinIva = totalSinIva,
                                    precioConIva = totalConIva,
                                    iva = iva
                                )
                                onConfirmClick(updatedTicket)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Confirmar Cambios",
                                modifier = Modifier
                                    .size(48.dp)

                            )
                        }
                        IconButton(
                            onClick = { onClickDelete(ticket.id) }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Eliminar Ticket",
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }
                }
            }
        }
    )
}
