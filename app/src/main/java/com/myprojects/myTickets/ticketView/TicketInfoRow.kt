package com.myprojects.myTickets.ticketView

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketInfoRow(label: String, value: String, onValueChange: (String) -> Unit) {
    var textFieldValue by remember { mutableStateOf(value) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Etiqueta
        Text(
            text = "$label: ",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.alignByBaseline() // Para alinear el texto
        )

        // TextField sin contorno
        BasicTextField(
            value = textFieldValue,
            onValueChange = {
                textFieldValue = it
                onValueChange(it)
            },
            modifier = Modifier
                .alignByBaseline() // Alinea el campo con el texto de la etiqueta
                .weight(1f) // El campo ocupará el resto del espacio disponible
                .background(Color.Transparent), // Sin fondo ni contorno
            textStyle = TextStyle(
                color = Color.DarkGray,
                fontSize = 16.sp // Puedes ajustar el tamaño de fuente según tus necesidades
            ),
            singleLine = true
        )
    }
}




