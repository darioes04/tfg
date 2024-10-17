package com.myprojects.myTickets.ticketView

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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

@Composable
fun ListTicketScreen(tickets: List<Ticket>, onTicketClick: (Ticket) -> Unit, onHomeClick: () -> Unit) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .padding(start = 20.dp),
                verticalAlignment = Alignment.CenterVertically // Alinear los elementos verticalmente al centro
            ) {
                // Icono de casa para volver a HomeScreen
                IconButton(
                    onClick = { onHomeClick() },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = "Volver a Home",
                        modifier = Modifier
                            .size(48.dp)

                    )
                }

                Spacer(modifier = Modifier.weight(1f)) // Añadir un Spacer para empujar el texto al centro

                Text(
                    text = "Tickets",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(5f) // Ajustar el peso para centrar el texto
                )

                Spacer(modifier = Modifier.weight(3f)) // Añadir un Spacer simétrico en el otro lado
            }
        }
        ,
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(tickets) { ticket ->
                    Button(
                        onClick = { onTicketClick(ticket) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text(text = "${ticket.restaurante} - ${ticket.fecha}")
                    }
                }
            }

        }
    )
}
