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
import java.io.InputStream
import android.util.Base64
import org.json.JSONException
import java.util.concurrent.TimeUnit


object OpenAiUtils{
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS) // Tiempo de espera para establecer la conexión
        .readTimeout(30, TimeUnit.SECONDS)    // Tiempo de espera para leer la respuesta
        .writeTimeout(30, TimeUnit.SECONDS)   // Tiempo de espera para escribir la solicitud
        .build()

    fun processImageWithOpenAi(context: Context, uri: Uri?, onResult: (String) -> Unit) {
        if (uri == null) {
            onResult("Error: URI es nula.")
            return
        }

        // Definir el modelo y la clave de la API
        val apiKey = "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"
        val prompt = Constants.Prompt

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val imageBase64: String? = uriToBase64(context, uri)

                if (imageBase64 == null) {
                    withContext(Dispatchers.Main) {
                        onResult("Error: No se pudo convertir la imagen.")
                    }
                    return@launch
                }

                // Construir el JSON del cuerpo de la solicitud
                val json = JSONObject().apply {
                    put("model", "gpt-4o")
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
                                    put("url", "data:image/jpeg;base64,$imageBase64")
                                })
                            })
                        })
                        put("max_tokens", 1000)
                    }))

                }

                val requestBody = json.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                val request = Request.Builder()
                    .url("https://api.openai.com/v1/chat/completions")
                    .addHeader("Authorization", "Bearer $apiKey")
                    .post(requestBody)
                    .build()

                val response = client.newCall(request).execute()

                if (!response.isSuccessful) {
                    Log.e("OpenAI Error", "Solicitud fallida con código: ${response.code}")
                    Log.e("OpenAI Error", "Cuerpo de la respuesta: ${response.body?.string()}")
                    return@launch
                }

                val responseData = response.body?.string()
                val responseContent = responseData?.let {
                    try {
                        val jsonResponse = JSONObject(it)

                        if (jsonResponse.has("choices")) {
                            val choices = jsonResponse.getJSONArray("choices")
                            val res = choices.getJSONObject(0).getJSONObject("message").getString("content")
                            try {
                                JSONObject(res).toString()
                            } catch (e: JSONException) {
                                res
                            }
                        } else {
                            "No se encontró el campo 'choices' en la respuesta."
                        }
                    } catch (e: JSONException) {
                        Log.e("OpenAI Error", "Error de JSON: ${e.message}")
                        "Error al procesar la respuesta JSON."
                    }
                } ?: "Error: Respuesta nula de la API."

                withContext(Dispatchers.Main) {
                    onResult(responseContent)
                }


            } catch (e: Exception) {
                Log.e("OpenAI Error", "Error procesando la solicitud: ${e.message}")
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
