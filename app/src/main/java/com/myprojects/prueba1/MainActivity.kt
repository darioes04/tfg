package com.myprojects.prueba1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.myprojects.prueba1.permissions.PermissionManager
import com.myprojects.prueba1.ui.theme.Prueba1Theme
import com.myprojects.prueba1.utils.CameraUtils
import com.myprojects.prueba1.utils.GalleryUtils

class MainActivity : ComponentActivity() {

    private var selectedImageUri by mutableStateOf<Uri?>(null)

    // Declarar los lanzadores
    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Verifica y solicita permisos en tiempo de ejecución
        PermissionManager.checkAndRequestPermissions(this)

        // Inicializar los lanzadores aquí
        setupActivityResultLaunchers()



        setContent {
            Prueba1Theme {
                HomeScreen(
                    onCameraClick = {
                        CameraUtils.takePicture(this, takePictureLauncher) { uri ->
                            selectedImageUri = uri
                        }
                    },
                    onGalleryClick = {
                        GalleryUtils.selectImageFromGallery(this, galleryLauncher) { uri ->
                            selectedImageUri = uri
                        }
                    },
                    selectedImageUri = selectedImageUri,
                    onConfirmImage = { processImage(selectedImageUri) }
                )
            }
        }
    }

    // Configurar los ActivityResultLauncher
    private fun setupActivityResultLaunchers() {
        takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                // Asegurarse de que la URI de la imagen capturada se actualiza
                selectedImageUri = CameraUtils.photoUri
                Toast.makeText(this, "Foto capturada", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error al capturar la foto", Toast.LENGTH_SHORT).show()
            }
        }

        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val imageUri: Uri? = result.data?.data
                imageUri?.let {
                    selectedImageUri = it
                    Toast.makeText(this, "Imagen seleccionada", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Procesar la imagen confirmada
    private fun processImage(imageUri: Uri?) {
        imageUri?.let {
            Toast.makeText(this, "Imagen confirmada y lista para ser procesada", Toast.LENGTH_SHORT).show()
        }
    }



}
