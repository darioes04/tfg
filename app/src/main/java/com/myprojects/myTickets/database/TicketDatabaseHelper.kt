package com.myprojects.myTickets.database

/*
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.myprojects.myTickets.data.Producto
import com.myprojects.myTickets.data.Ticket

class TicketDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "tickets.db"
        private const val DATABASE_VERSION = 4

        // Nombre de la tabla y columnas
        const val TABLE_NAME = "tickets"
        const val COLUMN_ID = "id"
        const val COLUMN_RESTAURANTE = "restaurante"
        const val COLUMN_CIF = "cif"
        const val COLUMN_FECHA = "fecha"
        const val COLUMN_HORA = "hora"
        const val COLUMN_PRECIO_SIN_IVA = "precio_sin_iva"
        const val COLUMN_PRECIO_CON_IVA = "precio_con_iva"
        const val COLUMN_IVA = "iva"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Tabla principal de Tickets
        val createTicketTable = """
        CREATE TABLE $TABLE_NAME (
            $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_RESTAURANTE TEXT,
            $COLUMN_CIF TEXT,
            $COLUMN_FECHA TEXT,
            $COLUMN_HORA TEXT,
            $COLUMN_PRECIO_SIN_IVA TEXT,
            $COLUMN_PRECIO_CON_IVA TEXT,
            $COLUMN_IVA TEXT
        )
    """.trimIndent()
        db.execSQL(createTicketTable)

        // Tabla para los productos
        val createProductTable = """
        CREATE TABLE products (
            product_id INTEGER PRIMARY KEY AUTOINCREMENT,
            item_name TEXT,
            precio_unidad TEXT,
            cantidad TEXT,
            precio_final TEXT,
            ticket_id TEXT, 
            FOREIGN KEY (ticket_id) REFERENCES $TABLE_NAME($COLUMN_ID)
            ON DELETE CASCADE
        )
    """.trimIndent()
        db.execSQL(createProductTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS products")
        onCreate(db)
    }

    // Función para guardar ticket con productos
    fun saveTicketWithProducts(ticket: Ticket): Boolean {
        val db = this.writableDatabase
        var success = false

        db.beginTransaction()
        try {
            val contentValues = ContentValues().apply {
                put("restaurante", ticket.restaurante)
                put("cif", ticket.cif)
                put("fecha", ticket.fecha)
                put("hora", ticket.hora)
                put("precio_sin_iva", ticket.precioSinIva)
                put("precio_con_iva", ticket.precioConIva)
                put("iva", ticket.iva)
            }

            val ticketId = db.insert("tickets", null, contentValues)

            if (ticketId != -1L) {
                ticket.items.forEach { product ->
                    val productValues = ContentValues().apply {
                        put("item_name", product.item)
                        put("precio_unidad", product.precioUnidad)
                        put("cantidad", product.cantidad)
                        put("precio_final", product.precioFinal)
                        put("ticket_id", ticketId)
                    }
                    db.insert("products", null, productValues)
                }
                db.setTransactionSuccessful()
                success = true
            }
        } finally {
            db.endTransaction()
        }
        return success
    }

    fun getAllTickets(): List<Ticket> {
        val tickets = mutableListOf<Ticket>()
        val db = this.readableDatabase
        val ticketQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(ticketQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val ticketId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val restaurante = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RESTAURANTE))
                val cif = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CIF))
                val fecha = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA))
                val hora = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HORA))
                val precioSinIva = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRECIO_SIN_IVA))
                val precioConIva = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRECIO_CON_IVA))
                val iva = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IVA))

                // Obtener los productos asociados a este ticket
                val productQuery = "SELECT * FROM products WHERE ticket_id = ?"
                val productCursor = db.rawQuery(productQuery, arrayOf(ticketId.toString()))
                val products = mutableListOf<Producto>()

                if (productCursor.moveToFirst()) {
                    do {
                        val itemName = productCursor.getString(productCursor.getColumnIndexOrThrow("item_name"))
                        val precioUnidad = productCursor.getString(productCursor.getColumnIndexOrThrow("precio_unidad"))
                        val cantidad = productCursor.getString(productCursor.getColumnIndexOrThrow("cantidad"))
                        val precioFinal = productCursor.getString(productCursor.getColumnIndexOrThrow("precio_final"))

                        val product = Producto(item = itemName, precioUnidad = precioUnidad, cantidad = cantidad, precioFinal = precioFinal)
                        products.add(product)
                    } while (productCursor.moveToNext())
                }
                productCursor.close()

                // Crear el objeto Ticket y añadirlo a la lista
                val ticket = Ticket(
                    restaurante = restaurante,
                    cif = cif,
                    fecha = fecha,
                    hora = hora,
                    precioSinIva = precioSinIva,
                    precioConIva = precioConIva,
                    iva = iva,
                    items = products
                )
                tickets.add(ticket)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return tickets
    }


    fun updateTicket(ticket: Ticket): Boolean {
        val db = this.writableDatabase
        var success = false

        // Iniciar una transacción para garantizar consistencia
        db.beginTransaction()
        try {
            // Valores a actualizar en la tabla de tickets
            val contentValues = ContentValues().apply {
                put("restaurante", ticket.restaurante)
                put("cif", ticket.cif)
                put("fecha", ticket.fecha)
                put("hora", ticket.hora)
                put("precio_sin_iva", ticket.precioSinIva)
                put("precio_con_iva", ticket.precioConIva)
                put("iva", ticket.iva)
            }

            // Actualizar el ticket en la base de datos
            val rowsAffected = db.update(
                "tickets",
                contentValues,
                "id = ?",  // Actualiza donde el ID coincida
                arrayOf(ticket.id.toString())  // Usamos el ID del ticket como referencia
            )

            if (rowsAffected > 0) {
                // El ticket fue actualizado correctamente
                // Primero eliminamos los productos antiguos del ticket para reemplazarlos
                db.delete("products", "ticket_id = ?", arrayOf(ticket.id.toString()))

                // Insertamos los productos actualizados del ticket
                ticket.items.forEach { product ->
                    val productValues = ContentValues().apply {
                        put("item_name", product.item)
                        put("precio_unidad", product.precioUnidad)
                        put("cantidad", product.cantidad)
                        put("precio_final", product.precioFinal)
                        put("ticket_id", ticket.id)
                    }
                    db.insert("products", null, productValues)
                }

                db.setTransactionSuccessful()  // Marcar la transacción como exitosa
                success = true
            }
        } finally {
            db.endTransaction()  // Finalizar la transacción
        }

        return success
    }

    fun deleteTicket(idTicket: Long): Boolean {
        val db = this.writableDatabase
        return try {
            val affectedRows = db.delete("tickets", "id=?", arrayOf(idTicket.toString()))
            affectedRows > 0  // Devuelve true si se eliminó al menos una fila
        } catch (e: Exception) {
            e.printStackTrace()
            false  // Devuelve false si hubo algún error
        } finally {
            db.close()  // Cierra la base de datos después de la operación
        }
    }


}
*/