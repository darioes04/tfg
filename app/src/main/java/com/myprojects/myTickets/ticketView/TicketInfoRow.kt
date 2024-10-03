package com.myprojects.myTickets.ticketView

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TicketInfoRow(label: String, value: String, onValueChange: (String) -> Unit) {
    var textFieldValue by remember { mutableStateOf(value) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),  // Espaciado vertical entre las filas
        verticalAlignment = Alignment.CenterVertically  // Alinear verticalmente en el centro
    ) {
        // Atributo (etiqueta)
        Text(
            text = "$label: ",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .weight(1f)  // Peso del texto de la etiqueta
                .padding(end = 8.dp)  // Espacio entre la etiqueta y el campo de texto
        )

        // Campo editable (TextField)
        OutlinedTextField(
            value = textFieldValue,
            onValueChange = {
                textFieldValue = it
                onValueChange(it)  // Actualiza el valor en la lógica principal
            },
            modifier = Modifier
                .weight(2f)  // Peso del campo de texto
                .height(45.dp)  // Altura del campo de texto ajustada
                .background(Color.Cyan),
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = androidx.compose.ui.unit.TextUnit.Unspecified
            ),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(40.dp),
        )
    }
}



