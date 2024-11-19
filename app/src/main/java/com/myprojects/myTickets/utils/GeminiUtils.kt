package com.myprojects.myTickets.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
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
        }

        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = BuildConfig.API_KEY,
            generationConfig = config


        )


        val prompt = Constants.Prompt

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
                Log.d("GeminiUtils", "Respuesta de la API: $res")

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
