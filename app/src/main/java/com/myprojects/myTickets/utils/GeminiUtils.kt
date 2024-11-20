package com.myprojects.myTickets.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.FunctionType
import com.google.ai.client.generativeai.type.Schema
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import com.myprojects.myTickets.data.Constants
import com.myprojects.prueba1.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object GeminiUtils {



    private fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
        return try {
            context.contentResolver.openInputStream(uri).use { stream ->
                BitmapFactory.decodeStream(stream) // Carga el Bitmap directamente sin optimización
            }
        } catch (e: Exception) {
            Log.e("uriToBitmap", "Error decoding stream: ${e.localizedMessage}")
            null
        }
    }



    // Function to process the image with Gemini API optimized for ticket data
    fun processImageWithGemini(context: Context, uri: Uri?, onResult: (String) -> Unit) {
        if (uri == null) {
            onResult("Error: URI es nula.")
            return
        }


        val config = generationConfig {
            temperature = 1f
            responseMimeType = "application/json"
            responseSchema = Schema(
                name = "ticket",
                description = "ticket de restaurante",
                type = FunctionType.OBJECT,
                properties = mapOf(
                    "restaurante" to Schema(
                        name = "restaurante",
                        description = "Nombre del restaurante en mayusculas",
                        type = FunctionType.STRING,
                        nullable = false
                    ),
                    "cif" to Schema(
                        name = "cif",
                        description = "Código de identificación fiscal del restaurante",
                        type = FunctionType.STRING,
                        nullable = false
                    ),
                    "fecha" to Schema(
                        name = "fecha",
                        description = "Fecha del ticket (DD/MM/YYYY)",
                        type = FunctionType.STRING,
                        nullable = false
                    ),
                    "hora" to Schema(
                        name = "hora",
                        description = "Hora del ticket (HH:MM), no deben indicarse los segundos",
                        type = FunctionType.STRING,
                        nullable = false
                    ),
                    "items" to Schema(
                        name = "items",
                        description = "Lista de productos comprados",
                        type = FunctionType.ARRAY,
                        items = Schema(
                            name = "Producto",
                            description = "Nombre del producto en el ticket",
                            type = FunctionType.OBJECT,
                            properties = mapOf(
                                "item" to Schema(
                                    name = "item",
                                    description = "Nombre del producto en letras mayúsculas (ejemplo: 'PAELLA DE MARISCOS')",
                                    type = FunctionType.STRING,
                                    nullable = false
                                ),
                                "cantidad" to Schema(
                                    name = "cantidad",
                                    description = "Cantidad del producto comprada",
                                    type = FunctionType.STRING,
                                    nullable = false
                                ),
                                "precioUnidad" to Schema(
                                    name = "precioUnidad",
                                    description = "Precio por unidad del producto",
                                    type = FunctionType.STRING,
                                    nullable = false
                                ),
                                "precioFinal" to Schema(
                                    name = "precioFinal",
                                    description = "Precio total por este producto (cantidad x precioUnidad)",
                                    type = FunctionType.STRING,
                                    nullable = false
                                )
                            ),
                            required = listOf("item", "cantidad", "precioUnidad", "precioFinal")
                        )
                    ),
                    "precioSinIva" to Schema(
                        name = "precioSinIva",
                        description = "Precio total del ticket sin IVA (precioConIva / (1 + iva/100)) ",
                        type = FunctionType.STRING,
                        nullable = false
                    ),
                    "iva" to Schema(
                        name = "iva",
                        description = "Valor del IVA aplicado al ticket, por defecto debe ser 10.00",
                        type = FunctionType.STRING,
                        nullable = false
                    ),
                    "precioConIva" to Schema(
                        name = "precioConIva",
                        description = "Precio total del ticket con IVA incluido (suma de todos los " +
                                "de cada producto preciosFinal)",
                        type = FunctionType.STRING,
                        nullable = false
                    )
                ),
                required = listOf("restaurante", "cif", "fecha", "hora", "items", "precioSinIva", "iva", "precioConIva")
            )
        }

        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash-002",
            apiKey = BuildConfig.API_KEY,
            generationConfig = config


        )


        val prompt = Constants.PROMPT

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val image1: Bitmap? = uriToBitmap(context, uri)

                if (image1 == null) {
                    withContext(Dispatchers.Main) {
                        onResult("Error: No se pudo convertir la imagen.")
                    }
                    return@launch
                }

                val inputContent = content {
                    image(image1)
                    text(prompt)
                }

                val res = generativeModel.generateContent(inputContent).text

                val responseContent = res ?: "Respuesta vacía de la API"

                withContext(Dispatchers.Main) {
                    onResult(responseContent)
                }
            } catch (e: Exception) {
                Log.e("GeminiUtils", "Error generando contenido: ${e.message}")
                withContext(Dispatchers.Main) {
                    onResult("Error: ${e.message}")
                }
            }
        }
    }
}
