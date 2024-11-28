package com.myprojects.myTickets.ticketView

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TicketInfoRow(label: String, value: String, symbol: String, onValueChange: (String) -> Unit) {

    var textFieldValue by remember { mutableStateOf(value) }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Etiqueta
        Text(
            text = "$label: ",
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.alignByBaseline(),
            color = MaterialTheme.colorScheme.onBackground // Ajuste automático al modo oscuro/claro
        )

        Box(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .drawBehind {
                    val strokeWidth = 2.dp.toPx() // Grosor de la línea
                    val y = size.height - strokeWidth / 2 // Posición de la línea en la parte inferior
                    drawLine(
                        color = Color.Magenta,
                        start = Offset(0f, y), // Extiende la línea más allá a la izquierda
                        end = Offset(size.width + 16f, y),
                        strokeWidth = strokeWidth // Grosor de la línea
                    )
                }
                .background(Color.Transparent)
                .width(IntrinsicSize.Min)
        ) {
            BasicTextField(
                value = textFieldValue,
                onValueChange = { newValue ->
                    // Filtra la coma `,` para evitar que el usuario la escriba
                    val sanitizedValue = newValue.replace(",", "")
                    textFieldValue = sanitizedValue
                    onValueChange(sanitizedValue)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
                keyboardOptions = if (label == "IVA" || label == "Precio sin IVA" || label == "Precio con IVA"){
                    KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number // Usa el teclado numérico
                    )
                } else {
                    KeyboardOptions.Default // Usa el teclado por defecto
                },
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp
                ),
            )
        }

        Text(
            text = symbol,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
        )

    }
}





