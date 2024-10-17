package com.myprojects.myTickets.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.myprojects.myTickets.data.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import android.util.Base64


object OpenAiUtils{
    private val client = OkHttpClient()

    fun processImageWithOpenAi(context: Context, uri: Uri?, onResult: (String) -> Unit) {

        if (uri == null) {
            onResult("Error: URI es nula.")
            return
        }
        // Ejecutar en una corrutina
        CoroutineScope(Dispatchers.IO).launch {
            // Concatenar el texto extraído con el prompt
            val prompt = Constants.Prompt

            val image1: String? = uriToBase64(context, uri)
            val image2: Bitmap? = uriToBitmap(context, uri)

            if (image1 == null) {
                withContext(Dispatchers.Main) {
                    onResult(prompt)
                }
                return@launch
            }

            // Construir el JSON del cuerpo de la solicitud
            val json = JSONObject().apply {
                put("model", "gpt-4o-mini")
                put("messages", JSONArray().put(JSONObject().apply {
                    put("role", "user")
                    put("content", JSONArray().apply {
                        put(JSONObject().apply {
                            put("type", "text")
                            put("text", prompt)
                        })
                        put(JSONObject().apply {
                            put("type", "image_url")
                            put("image_url", JSONObject().apply {
                                put("url", "data:image/jpeg;base64,$image1")
                            })
                        })
                    })
                }))
                put("max_tokens", 300)
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
                            onResult(responseText)
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

    private fun uriToBase64(context: Context, uri: Uri): String? {
        try {
            // Abrir el InputStream desde el ContentResolver usando la URI
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)

            // Decodificar el InputStream en un Bitmap
            val bitmap: Bitmap? = BitmapFactory.decodeStream(inputStream)

            // Convertir el Bitmap en un array de bytes
            val outputStream = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            val byteArray = outputStream.toByteArray()

            // Codificar los bytes en Base64
            val base64String = Base64.encodeToString(byteArray, Base64.DEFAULT)

            // Cerrar los flujos
            inputStream?.close()
            outputStream.close()

            return base64String
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

}
