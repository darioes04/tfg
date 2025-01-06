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
                    text = "Formato de Fecha Inválido",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Text(
                    text = "La fecha debe ser válida y tener el siguiente formato: dd/MM/YYYY. Compruebe la fecha por favor.",
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


    fun isDateValid(fecha: String): Boolean {
        // Expresión regular para validar el formato dd/MM/yyyy
        val regex = Regex("^\\d{2}/\\d{2}/\\d{4}$")

        // Si no coincide con el formato exacto, retornar falso
        if (!regex.matches(fecha)) {
            return false
        }

        // Definir el formato esperado
        val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        formato.isLenient = false // No permitir fechas inválidas como 32/01/2024

        return try {
            // Intentar parsear la fecha
            formato.parse(fecha)
            true
        } catch (e: Exception) {
            false
        }
    }

}