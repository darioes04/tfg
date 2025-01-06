package com.myprojects.myTickets.ticketView

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TicketInfoRow(label: String, value: String, symbol: String, onValueChange: (String) -> Unit) {

    var textFieldValue by remember { mutableStateOf(value) }

    val visualTransformation = VisualTransformation { text ->
        val transformedText = "$text $symbol"
        TransformedText(
            AnnotatedString(transformedText),
            object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    return offset // Los índices originales se mantienen iguales
                }

                override fun transformedToOriginal(offset: Int): Int {
                    return offset.coerceAtMost(text.length) // Ajusta para que no exceda el tamaño del texto original
                }
            }
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp)) // Fondo redondeado
            .padding(horizontal = 12.dp, vertical = 3.dp), // Espaciado interno
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Etiqueta
        Text(
            text = "$label:",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.alignByBaseline(),
            fontSize = 16.sp // Tamaño ligeramente más grande
        )

        Box(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp)) // Borde estilizado
                .background(MaterialTheme.colorScheme.surface) // Fondo acorde al tema
                .padding(horizontal = 8.dp, vertical = 1.dp)
                .width(IntrinsicSize.Min)
        ) {
            BasicTextField(
                value = textFieldValue,
                onValueChange = { newValue ->
                    val sanitizedValue = newValue.replace(",", "")
                    textFieldValue = sanitizedValue
                    onValueChange(sanitizedValue)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
                keyboardOptions = if (label == "IVA" || label == "Precio sin IVA" || label == "Precio con IVA") {
                    KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    )
                } else {
                    KeyboardOptions.Default
                },
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp
                ),
                visualTransformation = visualTransformation
            )
        }
    }



}







