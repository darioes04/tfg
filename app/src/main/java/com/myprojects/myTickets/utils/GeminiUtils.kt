package com.myprojects.myTickets.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.myprojects.myTickets.data.Constants
import com.myprojects.prueba1.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.io.InputStream

object GeminiUtils {



    // Function to process the image with Gemini API
    fun processImageWithGemini(context: Context, uri: Uri?, onResult: (String) -> Unit) {
        if (uri == null) {
            onResult("Error: URI es nula.")
            return
        }

        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = BuildConfig.API_KEY // Reemplaza esto con tu clave API
        )

        val prompt = Constants.prompt

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

                // Llamada a la API para generar contenido
                val res = generativeModel.generateContent(inputContent).text

                // Loguear la respuesta para verificar qué devuelve la API
                Log.d("GeminiUtils", "Respuesta de la API: $res")

                // Verificar si la respuesta es un JSON o no
                val responseContent = try {
                    // Intentar convertir la respuesta en un objeto JSON
                    val jsonResponse = res?.let { JSONObject(it) }
                    jsonResponse.toString()  // Si es un JSON válido, devolverlo como cadena
                } catch (e: JSONException) {
                    // Si no es un JSON, es una cadena simple
                    res ?: "Respuesta vacía de la API"
                }

                // Actualizar la UI o devolver el resultado
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

    // Function to convert URI to Bitmap
    private fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
