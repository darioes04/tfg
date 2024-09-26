package com.myprojects.myTickets.utils

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

// Función para procesar la imagen y extraer el texto usando ML Kit OCR
object MLKitUtils {
    fun processImageWithMLKit(imageUri: Uri, context: Context, onTextExtracted: (String) -> Unit) {
        // Convierte la imagen URI en un objeto InputImage para ML Kit
        val inputImage = InputImage.fromFilePath(context, imageUri)

        // Inicializar el reconocedor de texto de ML Kit
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        // Procesar la imagen para extraer el texto
        recognizer.process(inputImage)
            .addOnSuccessListener { visionText ->
                // Texto extraído de la imagen
                val extractedText = visionText.text
                onTextExtracted(extractedText)  // Llamar al callback con el texto extraído
            }
            .addOnFailureListener { e ->
                // Manejar errores
                Toast.makeText(context, "Error al procesar la imagen: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
