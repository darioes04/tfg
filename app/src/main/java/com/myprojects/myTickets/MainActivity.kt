package com.myprojects.myTickets

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
import com.myprojects.myTickets.permissions.PermissionManager
import com.myprojects.myTickets.ui.theme.MyTicketsTheme
import com.myprojects.myTickets.utils.CameraUtils
import com.myprojects.myTickets.utils.GalleryUtils
import com.google.gson.Gson
import com.myprojects.myTickets.data.Ticket
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.myprojects.myTickets.database.TicketDatabaseHelper
import com.myprojects.myTickets.ticketView.ListTicketScreen
import com.myprojects.myTickets.ticketView.TicketScreen
import com.myprojects.myTickets.utils.GeminiUtils
import com.myprojects.myTickets.utils.OpenAiUtils

class MainActivity : ComponentActivity() {


    private var selectedImageUri by mutableStateOf<Uri?>(null)



    // Declarar los lanzadores
    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private val ticketState = mutableStateOf<Ticket?>(null)
    private lateinit var dbHelper: TicketDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar la base de datos
        dbHelper = TicketDatabaseHelper(this)

        // Verifica y solicita permisos en tiempo de ejecución
        PermissionManager.checkAndRequestPermissions(this)

        // Inicializar los lanzadores aquí
        setupActivityResultLaunchers()

        setContent {
            MyTicketsTheme {
                // Configuración del NavController para la navegación
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    composable("home") {
                        HomeScreen(
                            onCameraClick = {
                                CameraUtils.takePicture(this@MainActivity, takePictureLauncher) { uri ->
                                    selectedImageUri = uri
                                }
                            },
                            onGalleryClick = {
                                GalleryUtils.selectImageFromGallery(this@MainActivity, galleryLauncher) { uri ->
                                    selectedImageUri = uri
                                }
                            },
                            selectedImageUri = selectedImageUri,
                            onConfirmImage = {
                                processImage(selectedImageUri, navController)
                            },
                            onNavigateToList = {
                                navController.navigate("listTicketScreen")
                            }
                        )
                    }

                    // Pantalla de edición de tickets
                    composable("ticketScreen") {
                        ticketState.value?.let { ticket ->
                            TicketScreen(
                                ticket = ticket,
                                onConfirmClick = { updatedTicket ->
                                    // Guardar ticket o actualizar ticket
                                    val success = dbHelper.saveTicketWithProducts(updatedTicket)
                                    if (success) {
                                        Toast.makeText(this@MainActivity, "Ticket guardado correctamente", Toast.LENGTH_SHORT).show()
                                        navController.navigate("home")
                                    } else {
                                        Toast.makeText(this@MainActivity, "Error al guardar el ticket", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                onClickDelete = {
                                    Toast.makeText(this@MainActivity, "Ticket cancelado", Toast.LENGTH_SHORT).show()
                                    navController.navigate("home")
                                }

                            )
                        }
                    }

                    composable("editTicketScreen") {
                        ticketState.value?.let { ticket ->
                            TicketScreen(
                                ticket = ticket,
                                onConfirmClick = { updatedTicket ->
                                    // Guardar ticket o actualizar ticket
                                    val success = dbHelper.updateTicket(updatedTicket)
                                    if (success) {
                                        Toast.makeText(this@MainActivity, "Ticket guardado correctamente", Toast.LENGTH_SHORT).show()
                                        navController.navigate("home")
                                    } else {
                                        Toast.makeText(this@MainActivity, "Error al guardar el ticket", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                onClickDelete = {idTicket ->
                                    val success = dbHelper.deleteTicket(idTicket)
                                    if (success) {
                                        Toast.makeText(this@MainActivity, "Ticket eliminado correctamente", Toast.LENGTH_SHORT).show()
                                        navController.navigate("home")
                                    } else {
                                        Toast.makeText(this@MainActivity, "Error al eliminar el ticket", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )
                        }
                    }

                    composable("listTicketScreen") {
                        ListTicketScreen(
                            tickets = dbHelper.getAllTickets(), // Recuerda definir esta función
                            onTicketClick = { ticket ->
                                ticketState.value = ticket // Guardar el ticket seleccionado
                                navController.navigate("editTicketScreen")}, // Navegar a la pantalla de edición
                            onHomeClick = {
                                navController.navigate("home") // Navegar de vuelta a HomeScreen
                            }
                        )
                    }

                }

            }
        }
    }

    // Procesar la imagen confirmada
    private fun processImage(imageUri: Uri?, navController: NavHostController) {
        GeminiUtils.processImageWithGemini(this, imageUri) { apiResponse ->
            // Aquí recibes la respuesta de la API en apiResponse
            if (apiResponse.startsWith("Error")) {
                // Manejar el error
                Toast.makeText(this, apiResponse, Toast.LENGTH_SHORT).show()
            } else {
                Log.d("MainActivity", "Respuesta de la API: $apiResponse")

                // Parsear el ticket
                val ticket = parseTicketJson(apiResponse)

                // Actualizar el estado del ticket que será observado por Compose
                ticketState.value = ticket

                // Navegar a la nueva pantalla ticketScreen
                navController.navigate("ticketScreen")
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

    // Función para parsear el JSON del ticket
    private fun parseTicketJson(json: String): Ticket {
        val gson = Gson()
        return gson.fromJson(json, Ticket::class.java)
    }
}
