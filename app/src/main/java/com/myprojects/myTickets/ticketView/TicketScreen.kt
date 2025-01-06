package com.myprojects.myTickets.ticketView

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myprojects.myTickets.data.Ticket
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TicketScreen(ticket: Ticket, onConfirmClick: (Ticket) -> Unit, onClickDelete: (String) -> Unit) {
    var ticket by remember { mutableStateOf(ticket) }
    var id by remember { mutableStateOf(ticket.id) } // Usa el ID real
    var restaurante by remember { mutableStateOf(ticket.restaurante) }
    var cif by remember { mutableStateOf(ticket.cif) }
    var fecha by remember { mutableStateOf(ticket.fecha) }
    var hora by remember { mutableStateOf(ticket.hora) }
    var totalSinIva by remember { mutableStateOf(ticket.precioSinIva) }
    var iva by remember { mutableStateOf(ticket.iva) }
    var totalConIva by remember { mutableStateOf(ticket.precioConIva) }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showSaveDialog by remember { mutableStateOf(false) }


    if(id == null){
        id = ""
    }


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
                                verticalAlignment = Alignment.Top
                            ) {
                                EditableCell(
                                    label = "Producto",
                                    value = product.item,
                                    modifier = Modifier.weight(2f)
                                ) { newValue ->
                                    val updatedProducts = ticket.items.toMutableList()
                                    updatedProducts[index] = updatedProducts[index].copy(item = newValue)
                                    ticket = ticket.copy(items = updatedProducts)
                                }
                                EditableCell(
                                    label = "Precio Unidad (€)",
                                    value = product.precioUnidad,
                                    modifier = Modifier.weight(1f)
                                ) { newValue ->
                                    val updatedProducts = ticket.items.toMutableList()
                                    updatedProducts[index] = updatedProducts[index].copy(precioUnidad = newValue)
                                    ticket = ticket.copy(items = updatedProducts)
                                }

                                EditableCell(
                                    label = "Cantidad",
                                    value = product.cantidad,
                                    modifier = Modifier.weight(1f)
                                ) { newValue ->
                                    val updatedProducts = ticket.items.toMutableList()
                                    updatedProducts[index] = updatedProducts[index].copy(cantidad = newValue)
                                    ticket = ticket.copy(items = updatedProducts)
                                }

                                EditableCell(
                                    label = "Precio Total (€)",
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
                                onClick = { showDeleteDialog = true }
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
                                    showSaveDialog = true
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
    // Diálogo de Confirmación para Eliminar Ticket
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false // Cierra el diálogo
            },
            confirmButton = {
                Button( // Botón con contorno
                    onClick = {
                        showDeleteDialog = false
                        onClickDelete(ticket.id ?: "") // Elimina el ticket
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary // Color primario
                    ),
                ) {
                    Text("Sí")
                }
            },
            dismissButton = {
                Button( // Botón con contorno
                    onClick = {
                        showDeleteDialog = false // Cancela la acción
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary // Color primario
                    ),
                ) {
                    Text("No")

                }
            },
            title = {
                Text(
                    text = "Eliminar Ticket",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface // Color coherente
                )
            },
            text = {
                if(ticket.id == null){
                    Text(
                        text = "¿Estás seguro de que deseas cancelar este ticket?",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface // Color coherente
                    )
                }
                else{
                    Text(
                        text = "¿Estás seguro de que deseas eliminar este ticket?",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface // Color coherente
                    )
                }

            },
            containerColor = MaterialTheme.colorScheme.surface, // Fondo del diálogo
            tonalElevation = 8.dp, // Elevación para sombra
            shape = MaterialTheme.shapes.medium // Bordes redondeados
        )
    }

    // Diálogo de Confirmación para Guardar Ticket
    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = {
                showSaveDialog = false // Cierra el diálogo
            },
            confirmButton = {
                Button( // Botón con contorno
                    onClick = {
                        showSaveDialog = false
                        val updatedTicket = ticket.copy(
                            id = id,
                            restaurante = restaurante,
                            cif = cif,
                            fecha = fecha,
                            hora = hora,
                            precioSinIva = totalSinIva,
                            precioConIva = totalConIva,
                            iva = iva
                        )
                        onConfirmClick(updatedTicket) // Guarda el ticket
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary // Color primario
                    ),
                ) {
                    Text("Sí")
                }
            },
            dismissButton = {
                Button( // Botón con contorno
                    onClick = {
                        showSaveDialog = false // Cancela la acción
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary // Color primario
                    ),
                ) {
                    Text("No")
                }
            },
            title = {
                Text(
                    text = "Guardar Ticket",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface // Color coherente
                )
            },
            text = {
                Text(
                    text = "¿Estás seguro de que deseas guardar los cambios en este ticket?",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface // Color coherente
                )
            },
            containerColor = MaterialTheme.colorScheme.surface, // Fondo del diálogo
            tonalElevation = 8.dp, // Elevación para sombra
            shape = MaterialTheme.shapes.medium // Bordes redondeados
        )
    }

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
fun EditableCell(
    label: String,
    value: String,
    modifier: Modifier,
    onValueChange: (String) -> Unit
) {
    Box(
        modifier = modifier
            .border(1.dp, Color.Gray) // Añade un borde gris de 1dp
            .padding(4.dp), // Espaciado interno dentro de la celda
        contentAlignment = Alignment.BottomStart // Alineación inferior
    ) {
        BasicTextField(
            value = value,
            onValueChange = { newValue ->
                val sanitizedValue = newValue.replace(",", "")
                onValueChange(sanitizedValue) // Notifica el cambio al controlador de estado
            },
            textStyle = TextStyle(
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = if (label == "Precio Unidad (€)" || label == "Cantidad" || label == "Precio Total (€)") {
                KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number // Usa el teclado numérico
                )
            } else {
                KeyboardOptions.Default // Usa el teclado por defecto
            },
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

