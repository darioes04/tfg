package com.myprojects.myTickets.database

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.myprojects.myTickets.data.Producto
import com.myprojects.myTickets.data.Ticket
import kotlinx.coroutines.tasks.await

class FireBaseHelper {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Obtener el userId del usuario autenticado
    private fun getUserId(): String? {
        return auth.currentUser?.uid
    }

    /**
     * Agregar un ticket para el usuario autenticado
     */
    suspend fun addTicket(ticket: Ticket): Boolean {

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            Log.e("Firestore", "Usuario no autenticado")
            throw IllegalStateException("Usuario no autenticado")
        }

        val userId = user.uid
        val db = FirebaseFirestore.getInstance()

        try {
            // Crear un nuevo documento en la colección "tickets"
            val ticketRef = db.collection("users")
                .document(userId)
                .collection("tickets")
                .document() // ID autogenerado

            // Obtener el ID generado
            val generatedId = ticketRef.id

            // Crear los datos del ticket
            val ticketData = hashMapOf(
                "id" to generatedId,
                "restaurante" to ticket.restaurante,
                "cif" to ticket.cif,
                "fecha" to ticket.fecha,
                "hora" to ticket.hora,
                "precio_sin_iva" to ticket.precioSinIva,
                "precio_con_iva" to ticket.precioConIva,
                "iva" to ticket.iva
            )

            // Guardar el ticket en Firestore
            ticketRef.set(ticketData).await()
            Log.d("Firestore", "Ticket guardado correctamente con ID: $generatedId")

            // Añadir los productos como subcolección "items"
            val itemsCollection = ticketRef.collection("items") // Subcolección "items"
            val batch = db.batch() // Transacción para eficiencia

            ticket.items.forEach { product ->
                val itemRef = itemsCollection.document() // ID autogenerado para cada producto
                val itemData = hashMapOf(
                    "item" to product.item,
                    "cantidad" to product.cantidad,
                    "precio_unidad" to product.precioUnidad,
                    "precio_final" to product.precioFinal
                )
                batch.set(itemRef, itemData) // Añadir producto al batch
            }

            // Aplicar el batch para guardar todos los productos a la vez
            batch.commit().await()
            Log.d("Firestore", "Productos guardados correctamente")

            return true // Operación exitosa

        } catch (e: Exception) {
            Log.e("Firestore", "Error al guardar el ticket y productos: ${e.message}")
            return false // Error en la operación
        }
    }


    /**
     * Obtener todos los tickets del usuario autenticado
     */
    suspend fun getTickets(): List<Ticket> {
        // Obtener el userId del usuario autenticado
        val userId = getUserId() ?: throw IllegalStateException("Usuario no autenticado")

        // Obtener todos los documentos de la colección "tickets"
        val documents = db.collection("users")
            .document(userId)
            .collection("tickets")
            .get()
            .await()

        // Lista para almacenar los tickets recuperados
        val tickets = mutableListOf<Ticket>()

        // Iterar sobre los documentos recuperados
        for (document in documents) {
            // Usar el ID generado por Firestore como identificador único
            val id = document.getString("id") ?: "" // Recuperar como String
            val restaurante = document.getString("restaurante") ?: ""
            val cif = document.getString("cif") ?: ""
            val fecha = document.getString("fecha") ?: ""
            val hora = document.getString("hora") ?: ""
            val precioSinIva = document.getString("precio_sin_iva") ?: ""
            val precioConIva = document.getString("precio_con_iva") ?: ""
            val iva = document.getString("iva") ?: ""

            // Obtener los productos (items) dentro del ticket
            val itemsDocs = document.reference.collection("items").get().await()
            val items = itemsDocs.map { itemDoc ->
                Producto(
                    item = itemDoc.getString("item") ?: "",
                    cantidad = itemDoc.getString("cantidad") ?: "",
                    precioUnidad = itemDoc.getString("precio_unidad") ?: "",
                    precioFinal = itemDoc.getString("precio_final") ?: ""
                )
            }

            // Añadir el ticket a la lista
            tickets.add(
                Ticket(
                    id = id,
                    restaurante = restaurante,
                    cif = cif,
                    fecha = fecha,
                    hora = hora,
                    items = items.toMutableList(),
                    precioSinIva = precioSinIva,
                    precioConIva = precioConIva,
                    iva = iva
                )
            )
        }

        // Devolver la lista de tickets
        return tickets
    }


    /**
     * Actualizar un ticket específico
     */
    suspend fun updateTicket(ticket: Ticket) {
        val userId = getUserId() ?: throw IllegalStateException("Usuario no autenticado")
        val db = FirebaseFirestore.getInstance()

        // Referencia al documento específico
        val ticketRef = db.collection("users")
            .document(userId)
            .collection("tickets")
            .document(ticket.id)

        val ticketData = hashMapOf(
            "id" to ticket.id,
            "restaurante" to ticket.restaurante,
            "cif" to ticket.cif,
            "fecha" to ticket.fecha,
            "hora" to ticket.hora,
            "precio_sin_iva" to ticket.precioSinIva,
            "precio_con_iva" to ticket.precioConIva,
            "iva" to ticket.iva
        )

        // Actualizar datos principales
        ticketRef.set(ticketData).await()

        // Actualizar subcolección 'items'
        val itemsCollection = ticketRef.collection("items")
        val batch = db.batch()

        // Eliminar los productos anteriores
        val existingItems = itemsCollection.get().await()
        for (item in existingItems) {
            batch.delete(item.reference)
        }

        // Añadir los productos nuevos
        ticket.items.forEach { item ->
            val itemData = hashMapOf(
                "item" to item.item,
                "cantidad" to item.cantidad,
                "precio_unidad" to item.precioUnidad,
                "precio_final" to item.precioFinal
            )
            val itemRef = itemsCollection.document()
            batch.set(itemRef, itemData)
        }

        // Confirmar todas las actualizaciones en lote
        batch.commit().await()
    }




    /**
     * Eliminar un ticket específico
     */
    suspend fun deleteTicket(ticketId: String) {
        val userId = getUserId() ?: throw IllegalStateException("Usuario no autenticado")
        db.collection("users").document(userId).collection("tickets").document(ticketId).delete().await()
    }
}
