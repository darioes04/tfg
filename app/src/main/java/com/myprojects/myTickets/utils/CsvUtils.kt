package com.myprojects.myTickets.utils

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import com.myprojects.myTickets.data.Ticket

object CsvUtils {

    fun exportTicketsToCSV(context: Context, tickets: List<Ticket>): Uri? {
        val csvHeader = "Restaurante;CIF;Fecha;Hora;Productos;PrecioSinIVA;IVA;PrecioConIVA\n"
        val fileName = "tickets.csv"

        // Configuración de MediaStore para almacenar el archivo en la carpeta "Descargas"
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, fileName) // Nombre del archivo
            put(MediaStore.Downloads.MIME_TYPE, "text/csv") // Tipo MIME
            put(MediaStore.Downloads.RELATIVE_PATH, "Download/") // Carpeta "Descargas"
        }

        // Crear el archivo usando MediaStore
        val uri: Uri? = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

        return if (uri != null) {
            try {
                resolver.openOutputStream(uri)?.use { outputStream ->
                    // Escribir encabezado
                    outputStream.write(csvHeader.toByteArray())

                    // Escribir datos de los tickets
                    for (ticket in tickets) {
                        val csvRow = "${ticket.restaurante};${ticket.cif};" +
                                "${ticket.fecha};${ticket.hora};${ticket.items};${ticket.precioSinIva};${ticket.iva};" +
                                "${ticket.precioConIva}\n"
                        outputStream.write(csvRow.toByteArray())
                    }
                }

                // Mostrar mensaje de éxito
                Toast.makeText(context, "Archivo CSV generado en la carpeta Descargas", Toast.LENGTH_SHORT).show()
                uri
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Error al generar el archivo .csv: ${e.message}", Toast.LENGTH_SHORT).show()
                null
            }
        } else {
            Toast.makeText(context, "No se pudo crear el archivo .csv.", Toast.LENGTH_SHORT).show()
            null
        }
    }
}
