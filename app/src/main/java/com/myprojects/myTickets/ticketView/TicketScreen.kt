package com.myprojects.myTickets.ticketView

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myprojects.myTickets.data.Ticket
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

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
                        modifier = Modifier.weight(1f),
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
                            if(fecha == "DESCONOCIDA"){
                                fecha = obtenerFechaActual()
                            }
                            TicketInfoRow(label = "Fecha", value = fecha, "") { newValue ->
                                fecha = newValue
                            }
                        }
                        item {
                            if(hora == "DESCONOCIDA"){
                                hora = obtenerHoraActual()
                            }
                            TicketInfoRow(label = "Hora", value = hora, "") { newValue ->
                                hora = newValue
                            }
                        }


                        // Encabezados de la tabla
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                TableHeader(text = "Producto", modifier = Modifier.weight(2f))
                                TableHeader(text = "Precio Unidad (€)", modifier = Modifier.weight(1f))
                                TableHeader(text = "Cantidad", modifier = Modifier.weight(1f))
                                TableHeader(text = "Precio Total (€)", modifier = Modifier.weight(1f))
                            }
                        }

                        // Filas de productos
                        items(ticket.items.size) { index ->
                            val product = ticket.items[index]

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                EditableCell(
                                    value = product.item,
                                    modifier = Modifier.weight(2f)
                                ) { newValue ->
                                    val updatedProducts = ticket.items.toMutableList()
                                    updatedProducts[index] = updatedProducts[index].copy(item = newValue)
                                    ticket = ticket.copy(items = updatedProducts)
                                }
                                EditableCell(
                                    value = product.precioUnidad,
                                    modifier = Modifier.weight(1f)
                                ) { newValue ->
                                    val updatedProducts = ticket.items.toMutableList()
                                    updatedProducts[index] = updatedProducts[index].copy(precioUnidad = newValue)
                                    ticket = ticket.copy(items = updatedProducts)
                                }

                                EditableCell(
                                    value = product.cantidad,
                                    modifier = Modifier.weight(1f)
                                ) { newValue ->
                                    val updatedProducts = ticket.items.toMutableList()
                                    updatedProducts[index] = updatedProducts[index].copy(cantidad = newValue)
                                    ticket = ticket.copy(items = updatedProducts)
                                }

                                EditableCell(
                                    value = product.precioFinal,
                                    modifier = Modifier.weight(1f)
                                ) { newValue ->
                                    val updatedProducts = ticket.items.toMutableList()
                                    updatedProducts[index] = updatedProducts[index].copy(precioFinal = newValue)
                                    ticket = ticket.copy(items = updatedProducts)
                                }
                            }
                        }
                        item { Spacer(modifier = Modifier.height(16.dp)) }

                        // Fila para Precio Sin IVA
                        item {
                            TicketInfoRow(label = "Precio sin IVA", value = totalSinIva, "€") { newValue ->
                                totalSinIva = newValue
                                ticket = ticket.copy(precioSinIva = newValue)
                            }
                        }

                        // Fila para IVA
                        item {
                            TicketInfoRow(label = "IVA", value = iva, "%") { newValue ->
                                iva = newValue
                                ticket = ticket.copy(iva = newValue)
                            }
                        }

                        // Fila para Precio Con IVA
                        item {
                            TicketInfoRow(label = "Precio con IVA", value = totalConIva, "€") { newValue ->
                                totalConIva = newValue
                                ticket = ticket.copy(precioConIva = newValue)
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

                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            IconButton(
                                onClick = { onClickDelete(ticket.id) }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Eliminar Ticket",
                                    modifier = Modifier
                                        .size(48.dp)

                                )
                            }
                            Text(
                                text = "Eliminar",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }


                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
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
                                    imageVector = Icons.Filled.Save,
                                    contentDescription = "Confirmar Cambios",
                                    modifier = Modifier.size(48.dp)
                                )
                            }
                            Text(
                                text = "Guardar",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                    }
                }
            }
        }
    )
}

@Composable
fun TableHeader(text: String, modifier: Modifier) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        modifier = modifier.padding(horizontal = 8.dp)
    )
}

@Composable
fun EditableCell(value: String, modifier: Modifier, onValueChange: (String) -> Unit) {
    Box(
        modifier = modifier
            .border(1.dp, Color.Gray) // Añade un borde gris de 1dp
            .padding(4.dp) // Espaciado interno dentro de la celda
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

fun obtenerHoraActual(): String {
    val horaActual = LocalTime.now()
    val formato = DateTimeFormatter.ofPattern("HH:mm")
    return horaActual.format(formato)
}

fun obtenerFechaActual(): String {
    val fechaActual = LocalDate.now() // Obtiene la fecha actual
    val formato = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return fechaActual.format(formato)
}