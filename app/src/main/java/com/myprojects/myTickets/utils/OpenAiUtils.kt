package com.myprojects.myTickets.utils

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

val client = OkHttpClient()

fun callOpenAIWithCoroutines(textExtracted: String, prompt: String) {
    // Ejecutar en una corrutina
    CoroutineScope(Dispatchers.IO).launch {
        // Concatenar el texto extraído con el prompt
        val fullPrompt = "$prompt\n\nTexto extraído del OCR: $textExtracted"

        // Construir el JSON del cuerpo de la solicitud
        val json = JSONObject().apply {
            put("model", "gpt-3.5-turbo")
            put("messages", JSONArray().put(JSONObject().apply {
                put("role", "user")
                put("content", fullPrompt)
            }))
            put("max_tokens", 1000)
        }

        // Crear la solicitud POST
        val requestBody = json.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .addHeader("Authorization", "Bearer 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")
            .post(requestBody)
            .build()

        try {
            // Hacer la llamada de red de forma sincrónica dentro de la corrutina
            val response = client.newCall(request).execute()

            // Verificar el código de estado HTTP
            if (!response.isSuccessful) {
                Log.e("OpenAI Error", "Solicitud fallida con código: ${response.code}")
                Log.e("OpenAI Error", "Cuerpo de la respuesta: ${response.body?.string()}")
                return@launch
            }

            // Procesar la respuesta
            val responseData = response.body?.string()
            if (responseData != null) {
                Log.d("OpenAI Response", responseData)  // Imprime el cuerpo de la respuesta para depuración

                val jsonResponse = JSONObject(responseData)

                // Verificar si `choices` está presente
                if (jsonResponse.has("choices")) {
                    val choices = jsonResponse.getJSONArray("choices")
                    val responseText = choices.getJSONObject(0).getJSONObject("message").getString("content")

                    // Volver al hilo principal para mostrar la respuesta en la UI
                    withContext(Dispatchers.Main) {
                        Log.d("OpenAI Response", responseText)
                    }
                } else {
                    // Manejar el caso cuando no hay `choices` en la respuesta
                    Log.e("OpenAI Error", "No se encontró el campo 'choices' en la respuesta.")
                    Log.e("OpenAI Error", "Cuerpo de la respuesta: $responseData")
                }
            }

        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("OpenAI Error", "Error de red: ${e.message}")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("OpenAI Error", "Error procesando la respuesta: ${e.message}")
        }
    }
}
