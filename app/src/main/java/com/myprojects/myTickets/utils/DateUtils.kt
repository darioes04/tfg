package com.myprojects.myTickets.utils

import android.icu.text.SimpleDateFormat
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import java.util.Locale

object DateUtils {



    @Composable
    fun DateFormatAlertDialog(onDismiss: () -> Unit) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = {
                Text(
                    text = "Formato de Fecha/Hora inválido",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Text(
                    text = "La fecha y la hora deben ser válidas y tener los formatos: dd/MM/YYYY - HH:mm." +
                            " Compruebe estos campos, por favor.",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            confirmButton = {
                Button( // Botón con contorno
                    onClick = { onDismiss() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Aceptar")
                }
            }
        )
    }


    fun isDateTimeValid(fecha: String, hora: String): Boolean {
        // Expresión regular para validar el formato dd/MM/yyyy
        val dateRegex = Regex("^\\d{2}/\\d{2}/\\d{4}$")
        // Expresión regular para validar el formato HH:mm
        val timeRegex = Regex("^([01]?\\d|2[0-3]):[0-5]\\d$")

        // Validar formato de fecha
        if (!dateRegex.matches(fecha)) {
            return false
        }

        // Validar formato de hora
        if (!timeRegex.matches(hora)) {
            return false
        }

        // Validar si la fecha es válida
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateFormat.isLenient = false

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        timeFormat.isLenient = false

        return try {
            dateFormat.parse(fecha) // Intentar parsear la fecha
            timeFormat.parse(hora)  // Intentar parsear la hora
            true // Ambas son válidas
        } catch (e: Exception) {
            false // Alguna es inválida
        }
    }

}