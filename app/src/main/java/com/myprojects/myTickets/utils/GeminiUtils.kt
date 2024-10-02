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

        val prompt = "dame estos datos extraidos en formato json  con: Nombre Local,"+
                "CIF, Fecha y hora, Tipo de comida(en caso de ser una franja horaria de 7am-12pm es desayuno, " +
                "12pm-16pm es comida, 19pm-11pm es cena), items y sus precios (debes añadir" +
                "el caracter € detras de los precios), precio total sin iva, IVA, precio"+
                "con iva. Asegurate de que solo se incluyan estos datos, hazlo en español. En"+
                "caso de haber caracteres extraños, eliminalos. Genera el json con todos los items,"+
                "no te saltes ninguno ni lo des por hecho." +
                "Además, debes tener en cuenta que puede haber varias unidades de cada item, " +
                "y eso puede afectar al precio final de cada item. Si se especifica las unidades del" +
                "producto, debes indicar el precio de la unidad, y el precio final de la suma" +
                "de la cantidad de items."


        // Launch a coroutine in the scope of the caller
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Convert the Uri to a Bitmap
                val image1: Bitmap? = uriToBitmap(context, uri)

                if (image1 == null) {
                    withContext(Dispatchers.Main) {
                        onResult("Error: No se pudo convertir la imagen.")
                    }
                    return@launch
                }

                // Build the content input with the image and the prompt
                val inputContent = content {
                    image(image1)  // Aquí es donde se usa la imagen en formato Bitmap
                    text(prompt)
                }

                // Call the Gemini API to generate content
                val response = generativeModel.generateContent(inputContent)
                val res = response.text
                Log.d("GeminiAPI", "Respuesta API: $res")

                // Convert the response to a string or extract meaningful data from it
                val responseContent = res ?: "Respuesta vacía de la API"

                // Switch to Main thread to update the UI or return the result
                withContext(Dispatchers.Main) {
                    onResult(responseContent)
                }
            } catch (e: Exception) {
                Log.e("GeminiAPI", "Error generando contenido: ${e.message}")
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
