package com.myprojects.myTickets.ticketView

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myprojects.myTickets.data.Ticket
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.*
import androidx.compose.runtime.produceState
import androidx.compose.ui.unit.LayoutDirection
import com.myprojects.myTickets.database.FireBaseHelper
import java.time.Instant
import java.time.ZoneId

@Composable
fun ListTicketScreen(
    firebaseHelper: FireBaseHelper,
    onTicketClick: (Ticket) -> Unit,
    onHomeClick: () -> Unit,
    onDownloadClick: (List<Ticket>) -> Unit
) {
    val tickets by produceState<List<Ticket>>(initialValue = emptyList()) {
        try {
            value = firebaseHelper.getTickets()
        } catch (e: Exception) {
            Log.e("ListTicketScreen", "Error al cargar tickets: ${e.message}")
        }
    }

    var showDialog by remember { mutableStateOf(false) } // Manage dialog visibility
    var searchQuery by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }
    var filteredTickets by remember { mutableStateOf(tickets) }

    // Filtrar los tickets dinámicamente al cambiar el texto de búsqueda
    LaunchedEffect(searchQuery, tickets) {
        filteredTickets = tickets.filter { ticket ->
            ticket.restaurante.contains(searchQuery, ignoreCase = true) ||
                    ticket.fecha.contains(searchQuery, ignoreCase = true) ||
                    ticket.precioConIva.contains(searchQuery, ignoreCase = true) ||
                    ticket.items.any { it.item.contains(searchQuery, ignoreCase = true) }
        }
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onHomeClick() }) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = "Volver a Home",
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Tickets",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = { showDialog = true }) {
                    Icon(
                        imageVector = Icons.Filled.SaveAlt,
                        contentDescription = "Descargar Tickets",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = paddingValues.calculateTopPadding() -20.dp,
                        start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                        end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
                        bottom = paddingValues.calculateBottomPadding()
                    )
                    .padding(horizontal = 10.dp) // Padding adicional
            ) {
                SearchBar(
                    searchQuery = searchQuery,
                    onSearchQueryChanged = { query -> searchQuery = query }
                )

                CalendarButton(onDateSelected = { date ->
                    selectedDate = date
                    searchQuery = date // Actualiza la barra de búsqueda con la fecha seleccionada si quieres
                })


                Spacer(modifier = Modifier.height(8.dp))

                // Lista de tickets filtrados
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredTickets) { ticket ->
                        TicketCard(ticket = ticket, onClick = { onTicketClick(ticket) })
                    }
                }
            }
        }
    )
    // Show the dialog if needed
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Export Tickets") },
            text = { Text(text = "¿Estás seguro de que deseas exportar los tickets a un archivo .csv?") },
            confirmButton = {
                Button(onClick = {
                    onDownloadClick(filteredTickets)
                    showDialog = false // Close dialog
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("No")
                }
            }
        )
    }
}


@Composable
fun TicketCard(ticket: Ticket, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = ticket.restaurante,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.CalendarToday,
                        contentDescription = "Fecha",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = ticket.fecha,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            Icon(
                imageVector = Icons.Filled.Restaurant,
                contentDescription = "Detalles del ticket",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit
) {
    TextField(
        value = searchQuery,
        onValueChange = onSearchQueryChanged,
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        placeholder = { Text("Buscar restaurante, comida...") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Buscar"
            )
        },
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = MaterialTheme.colorScheme.surface, // Fija el fondo del campo de texto
            focusedIndicatorColor = MaterialTheme.colorScheme.primary, // Color de la línea al enfocarse
            unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) // Color de la línea sin enfoque
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarButton(onDateSelected: (String) -> Unit) {
    val state = rememberDatePickerState()
    var showDialog by remember { mutableStateOf(false) }


    Button(
        onClick = { showDialog = true },
    ) {
        Row {
            Icon(
                imageVector = Icons.Filled.CalendarMonth,
                contentDescription = "Fecha",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text("Seleccionar Fecha")
        }
    }

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(onClick = {
                    showDialog = false
                    val date = state.selectedDateMillis
                    date?.let {
                        val formattedDate = Instant.ofEpochMilli(it)
                            .atZone(ZoneId.of("UTC"))
                            .toLocalDate()

                        // Formatea manualmente asegurando dos dígitos
                        val day = formattedDate.dayOfMonth.toString().padStart(2, '0')
                        val month = formattedDate.monthValue.toString().padStart(2, '0')
                        val year = formattedDate.year.toString()

                        val localDate = "$day/$month/$year"
                        onDateSelected(localDate)
                    }

                }) {
                    Text(text = "Confirmar")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDialog = false }) {
                    Text(text = "Cancelar")
                }
            }
        ) {
            DatePicker(state = state)
        }
    }
}
