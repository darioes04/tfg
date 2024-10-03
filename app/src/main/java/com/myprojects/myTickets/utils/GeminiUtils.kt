package com.myprojects.myTickets.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
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
            apiKey = "AIzaSyBYcj146sNM7ByeReP4D6dF7C58hXLPHkg"  // Reemplaza esto con tu clave API
        )

        val prompt = "genera un archivo json. La respuesta de la api debe ser exactamente con esta" +
                "estructura: { JSON }"+
                " Estos son los datos: restaurante," +
                "cif (si el cif contiene el caracter - de separacion, eliminalo) fecha,hora, " +
                "items(recuerda usar el termino item para" +
                "el nombre de los productos) y sus precios (precioUnidad," +
                "precioTotal), precioSinIva, iva, precioConIva. Asegurate de que solo se incluyan estos datos, hazlo en español. En" +
                "caso de haber caracteres extraños, eliminalos. Genera el json con todos los items," +
                "Los items deben contener los elementos: item, cantidad, precioUnidad, precioFinal"+
                "Además, debes tener en cuenta que puede haber varias unidades de cada item, " +
                "y eso puede afectar al precio final de cada item. Si se especifica las unidades del" +
                "producto, debes indicar la cantidad, el precioUnidad, y el precioFinal de la suma" +
                "de la cantidad de items.Recuerda usar item para el nombre de los productos"+
                "Los decimales deben mararcarse con el simbolo . no el simbolo ,"+
                "Si hay otras comas "+
                "Los precios deben ser de tipo double y no deben ir entre comillas nunca"+
                "Ten en cuenta que los nombres de los atributos" +
                "no pueden contener espacios, deben ir unidos por el caracter _"+
                "si la hora no se especifica, poner en hora: no especificada."


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
                    val jsonResponse = JSONObject(res)
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
