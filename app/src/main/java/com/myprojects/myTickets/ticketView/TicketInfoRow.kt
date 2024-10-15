package com.myprojects.myTickets.ticketView

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
            modifier = Modifier.alignByBaseline(),
            color = MaterialTheme.colorScheme.onBackground // Ajuste automático al modo oscuro/claro
        )

        // TextField sin contorno
        BasicTextField(
            value = textFieldValue,
            onValueChange = {
                textFieldValue = it
                onValueChange(it)
            },
            modifier = Modifier
                .alignByBaseline()
                .weight(1f)
                .padding(horizontal = 4.dp)
                .background(Color.Transparent),
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onBackground, // Ajuste de color automático
                fontSize = 16.sp
            ),
            singleLine = true
        )
    }
}




