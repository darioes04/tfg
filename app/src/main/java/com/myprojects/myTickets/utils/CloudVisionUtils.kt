package com.myprojects.myTickets.utils

import android.content.Context
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.myprojects.myTickets.MainActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object CloudVisionUtils {
    private const val API_KEY = "AIzaSyBy23EB0irc-uhTki1LRlBqAnJQkMmkIUM"

    // Función para procesar la imagen con Cloud Vision
    fun processImageWithCloudVision(context: Context, imageUri: Uri, onResult: (String) -> Unit) {
        val imageBase64 = encodeImageToBase64(context, imageUri)

        val requestJson = """
            {
              "requests": [
                {
                  "image": {
                    "content": "$imageBase64"
                  },
                  "features": [
                    {
                      "type": "TEXT_DETECTION"
                    }
                  ]
                }
              ]
            }
        """.trimIndent()

        val client = OkHttpClient()
        val mediaType = "application/json".toMediaTypeOrNull()
        val body = requestJson.toRequestBody(mediaType)

        val request = Request.Builder()
            .url("https://vision.googleapis.com/v1/images:annotate?key=$API_KEY")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
                Log.e("CloudVisionAPI", "Error en la llamada a Cloud Vision: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val jsonResponse = response.body?.string()

                if (jsonResponse != null) {
                    saveJsonToExternalStorage(context, jsonResponse, "response.json")

                    // Ejecutar en el hilo principal para actualizar la UI
                    (context as MainActivity).runOnUiThread {
                        onResult(jsonResponse) // Devuelve el resultado a la función de callback
                        Toast.makeText(context, "Resultado guardado en JSON", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        })
    }

    // Función para convertir la imagen a Base64
    private fun encodeImageToBase64(context: Context, imageUri: Uri): String {
        val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
        val bytes = inputStream?.readBytes()
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    // Función para guardar la respuesta JSON en un archivo
    private fun saveJsonToExternalStorage(context: Context, json: String, fileName: String) {
        val externalDir =
            context.getExternalFilesDir(null)  // Almacenamiento externo privado para la app
        val file = File(externalDir, fileName)
        FileOutputStream(file).use { fos ->
            fos.write(json.toByteArray())
        }
        Log.d("CloudVisionAPI", "Archivo guardado en: ${file.absolutePath}")
    }

    fun extractFullTextFromJson(jsonResponse: String): String? {
        val jsonObject = JSONObject(jsonResponse)
        val responsesArray = jsonObject.getJSONArray("responses")

        if (responsesArray.length() > 0) {
            val firstResponse = responsesArray.getJSONObject(0)

            // Verifica si `fullTextAnnotation` existe
            if (firstResponse.has("fullTextAnnotation")) {
                return firstResponse.getJSONObject("fullTextAnnotation").getString("text")
            } else if (firstResponse.has("textAnnotations")) {
                // Si por alguna razón no existe `fullTextAnnotation`, usar `textAnnotations`
                val textAnnotationsArray = firstResponse.getJSONArray("textAnnotations")
                if (textAnnotationsArray.length() > 0) {
                    return textAnnotationsArray.getJSONObject(0).getString("description")
                }
            }
        }

        return null
    }

}
