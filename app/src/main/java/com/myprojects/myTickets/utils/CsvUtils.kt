package com.myprojects.myTickets.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import com.myprojects.myTickets.data.Ticket
import java.io.File
import java.io.FileWriter
import java.io.IOException

object CsvUtils {

    fun exportTicketsToCSV(context: Context, tickets: List<Ticket>): Uri? {
        val csvHeader = "Restaurante;CIF;Fecha;Hora;PrecioSinIVA;IVA;PrecioConIVA\n"
        val fileName = "tickets.csv"

        // Directorio de almacenamiento
        val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (storageDir == null) {
            Toast.makeText(context, "Error: No se puede acceder al almacenamiento.", Toast.LENGTH_SHORT).show()
            return null
        }

        val csvFile = File(storageDir, fileName)

        try {
            FileWriter(csvFile).use { writer ->
                writer.append(csvHeader)

                // Agregar los datos de los tickets
                for (ticket in tickets) {
                    val csvRow = "${ticket.restaurante};${ticket.cif};" +
                            "${ticket.fecha};${ticket.hora};${ticket.precioSinIva};${ticket.iva};" +
                            "${ticket.precioConIva}\n"
                    writer.append(csvRow)
                }

                writer.flush()
            }

            // Mostrar mensaje de éxito
            Toast.makeText(context, "Archivo CSV generado en la carpeta de Descargas", Toast.LENGTH_SHORT).show()

            // Devuelve la URI del archivo para compartir (opcional)
            return Uri.fromFile(csvFile)

        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "Error al generar el archivo CSV.", Toast.LENGTH_SHORT).show()
            return null
        }
    }


}
