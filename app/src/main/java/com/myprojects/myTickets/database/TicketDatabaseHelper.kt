package com.myprojects.myTickets.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.myprojects.myTickets.data.Ticket

class TicketDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "tickets.db"
        private const val DATABASE_VERSION = 3

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

        // Iniciar una transacción para garantizar consistencia
        db.beginTransaction()
        try {
            // Insertar el ticket en la tabla de tickets
            val contentValues = ContentValues().apply {
                put("restaurante", ticket.restaurante)
                put("cif", ticket.cif)
                put("fecha", ticket.fecha)
                put("hora", ticket.hora)
                put("precio_sin_iva", ticket.precioSinIva)
                put("precio_con_iva", ticket.precioConIva)
                put("iva", ticket.iva)
            }

            // Intentar insertar el ticket, y obtener el ID del registro
            val ticketId = db.insertWithOnConflict("tickets", null,
                contentValues, SQLiteDatabase.CONFLICT_REPLACE)
            Log.d("Database", "Ticket insertado con ID: $ticketId")


            // Comprobar si la inserción del ticket ha sido exitosa
            if (ticketId != -1L) {
                var allProductsInserted = true

                // Insertar cada producto en la tabla de productos relacionado con el ticket
                ticket.items.forEach { product ->
                    val productValues = ContentValues().apply {
                        put("item_name", product.item)
                        put("precio_unidad", product.precioUnidad)
                        put("cantidad", product.cantidad)
                        put("precio_final", product.precioFinal)
                        put("ticket_id", ticketId)
                    }

                    // Comprobar si la inserción de cada producto ha sido exitosa
                    val productInsertResult = db.insert("products", null, productValues)
                    if (productInsertResult == -1L) {
                        allProductsInserted = false  // Si algún producto falla, marcamos como fallo
                    }
                }

                // Si todos los productos fueron insertados correctamente
                if (allProductsInserted) {
                    db.setTransactionSuccessful()  // Marcar la transacción como exitosa
                    success = true  // Todos los productos se insertaron correctamente
                }
            }
        } finally {
            db.endTransaction()  // Finalizar la transacción
        }

        return success
    }



}
