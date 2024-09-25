package com.myprojects.myTickets.utils

import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object CameraUtils {

    lateinit var photoUri: Uri

    fun takePicture(activity: ComponentActivity, takePictureLauncher: ActivityResultLauncher<Uri>, onPhotoCaptured: (Uri?) -> Unit) {
        val photoFile: File? = try {
            createImageFile(activity)  // Crea el archivo de imagen temporal
        } catch (ex: IOException) {
            ex.printStackTrace()
            Toast.makeText(activity, "Error al crear el archivo", Toast.LENGTH_SHORT).show()
            null
        }

        if (photoFile != null) {
            photoUri = FileProvider.getUriForFile(
                activity,
                "${activity.applicationContext.packageName}.provider",
                photoFile
            )
            takePictureLauncher.launch(photoUri)
        } else {
            Toast.makeText(activity, "No se pudo crear el archivo de imagen", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createImageFile(context: ComponentActivity): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )
    }
}
