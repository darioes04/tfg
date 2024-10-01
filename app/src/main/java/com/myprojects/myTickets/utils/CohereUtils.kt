package com.myprojects.myTickets.utils

import android.content.Context
import android.util.Log
import com.google.android.gms.common.util.JsonUtils
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import kotlinx.coroutines.*

import org.json.JSONObject
import java.io.IOException



// Función para hacer la llamada a la API de Cohere
fun callCohereWithCoroutines(textExtracted: String, prompt: String) {
    // Ejecutar en una corrutina
    CoroutineScope(Dispatchers.IO).launch {
        // Crear el prompt concatenando el texto extraído
        val fullPrompt = "$prompt\n\nTexto extraído del OCR: $textExtracted"

        // Crear el JSON del cuerpo de la solicitud para la API de Cohere
        val json1 = JSONObject().apply {
            put("model", "command-xlarge-nightly")  // Cambia el modelo si es necesario
            put("prompt", fullPrompt)
            put("max_tokens", 400)  // Limita la cantidad de tokens de la respuesta
        }

        // Crear el cuerpo de la solicitud
        val requestBody = json1.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        // Crear la solicitud HTTP
        val request = Request.Builder()
            .url("https://api.cohere.ai/v1/generate")
            .addHeader("Authorization", "Bearer ihTz0Vjq2afQ1hIY76DJJyZQ1Pn506HNqGd34TFf")
            .post(requestBody)
            .build()

        try {
            // Hacer la llamada de red de forma sincrónica dentro de la corrutina
            val response = client.newCall(request).execute()

            // Verificar si la respuesta fue exitosa (código 200)
            if (!response.isSuccessful) {
                Log.e("Cohere Error", "Solicitud fallida con código: ${response.code}")
                Log.e("Cohere Error", "Cuerpo de la respuesta: ${response.body?.string()}")
                return@launch
            }

            // Procesar la respuesta
            val responseData = response.body?.string()
            if (responseData != null) {

                Log.d("Cohere Response", responseData)  // Imprimir el cuerpo de la respuesta para depuración

                // Parsear la respuesta JSON
                val jsonResponse = JSONObject(responseData)
                val generations = jsonResponse.getJSONArray("generations")
                val generatedText = generations.getJSONObject(0).getString("text")

                // Volver al hilo principal para mostrar la respuesta en la UI
                withContext(Dispatchers.Main) {
                    Log.d("Cohere Generated Text", generatedText)



                    // Puedes actualizar la UI, como un TextView, con el texto generado
                }
            } else {
                Log.e("Cohere Error", "El cuerpo de la respuesta es nulo.")
            }

        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("Cohere Error", "Error de red: ${e.message}")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("Cohere Error", "Error procesando la respuesta: ${e.message}")
        }

    }
}




