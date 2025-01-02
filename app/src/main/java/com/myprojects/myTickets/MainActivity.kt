package com.myprojects.myTickets

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.myprojects.myTickets.auth.GoogleAuthHelper
import com.myprojects.myTickets.data.Ticket
import com.myprojects.myTickets.database.FireBaseHelper
import com.myprojects.myTickets.permissions.PermissionManager
import com.myprojects.myTickets.ticketView.*
import com.myprojects.myTickets.ui.theme.MyTicketsTheme
import com.myprojects.myTickets.utils.*
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var googleAuthHelper: GoogleAuthHelper
    private lateinit var googleSignInClient: GoogleSignInClient
    private val signInRequestCode = 100

    private var selectedImageUri by mutableStateOf<Uri?>(null)
    private val ticketState = mutableStateOf<Ticket?>(null)
    private val firebaseHelper = FireBaseHelper()

    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private lateinit var navController: NavHostController

    private var isLoading by mutableStateOf(false)
    private var isCancelled by mutableStateOf(false)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        googleAuthHelper = GoogleAuthHelper(this)
        googleSignInClient = googleAuthHelper.getGoogleSignInClient()

        PermissionManager.checkAndRequestPermissions(this)



        setContent {
            MyTicketsTheme {
                navController = rememberNavController()
                var isUserLoggedIn by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser != null) }

                LaunchedEffect(Unit) {
                    FirebaseAuth.getInstance().addAuthStateListener { auth ->
                        isUserLoggedIn = auth.currentUser != null
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = if (isUserLoggedIn) "home" else "googleSignInScreen"
                ) {
                    composable("googleSignInScreen") {
                        GoogleSignInScreen(onSignInClick = { signInWithGoogle() })
                    }

                    composable("home") {
                        HomeScreen(
                            onCameraClick = {
                                CameraUtils.takePicture(this@MainActivity, takePictureLauncher) { uri ->
                                    selectedImageUri = uri
                                    navController.navigate("confirmImageScreen")
                                }
                            },
                            onGalleryClick = {
                                GalleryUtils.selectImageFromGallery(this@MainActivity, galleryLauncher) { uri ->
                                    selectedImageUri = uri
                                    navController.navigate("confirmImageScreen")
                                }
                            },
                            onNavigateToList = {
                                navController.navigate("listTicketScreen")
                            },
                            onLogoutClick = { // Nueva función para cerrar sesión
                                FirebaseAuth.getInstance().signOut() // Cierra la sesión de Firebase
                                googleSignInClient.signOut().addOnCompleteListener { // Cierra sesión de Google
                                    Toast.makeText(this@MainActivity, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                                    navController.navigate("googleSignInScreen") // Vuelve a la pantalla de inicio de sesión
                                }
                            }
                        )
                    }

                    composable("confirmImageScreen") {
                        ConfirmImageScreen(
                            selectedImageUri = selectedImageUri,
                            onConfirmImage = {
                                isLoading = true
                                isCancelled = false
                                processImage(selectedImageUri, navController)
                            },
                            onCancelClick = {
                                isLoading = false
                                isCancelled = true
                                navController.navigate("home")
                            },
                            isLoading = isLoading,
                            onBackPressed = {
                                isLoading = false
                                isCancelled = true
                                navController.navigate("home")
                            }
                        )
                    }

                    composable("ticketScreen") {
                        ticketState.value?.let { ticket ->
                            TicketScreen(
                                ticket = ticket,
                                onConfirmClick = { updatedTicket ->
                                    lifecycleScope.launch {
                                        try {
                                            firebaseHelper.addTicket(updatedTicket)
                                            Toast.makeText(
                                                this@MainActivity,
                                                "Ticket guardado correctamente",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            navController.navigate("home")
                                        } catch (e: Exception) {
                                            Toast.makeText(
                                                this@MainActivity,
                                                "Error al guardar el ticket: ${e.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                },
                                onClickDelete = {
                                    Toast.makeText(this@MainActivity, "Ticket cancelado", Toast.LENGTH_SHORT).show()
                                    navController.navigate("home") {
                                        popUpTo("home") { inclusive = true } // Elimina pantallas previas del historial
                                    }
                                }
                            )
                        }
                    }

                    composable("editTicketScreen") {
                        ticketState.value?.let { ticket ->
                            TicketScreen(
                                ticket = ticket,
                                onConfirmClick = { updatedTicket ->
                                    lifecycleScope.launch {
                                        try {
                                            firebaseHelper.updateTicket(updatedTicket)
                                            Toast.makeText(
                                                this@MainActivity,
                                                "Ticket guardado correctamente",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            navController.navigate("home")
                                        } catch (e: Exception) {
                                            Toast.makeText(
                                                this@MainActivity,
                                                "Error al guardar el ticket: ${e.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                },
                                onClickDelete = {
                                    lifecycleScope.launch {
                                        try {
                                            firebaseHelper.deleteTicket(ticket.id) // Borra el ticket en Firebase
                                            Toast.makeText(
                                                this@MainActivity,
                                                "Ticket eliminado",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            navController.navigate("home") // Vuelve a la pantalla principal
                                        } catch (e: Exception) {
                                            Toast.makeText(
                                                this@MainActivity,
                                                "Error al eliminar el ticket: ${e.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            )
                        }
                    }


                    composable("listTicketScreen") {
                        ListTicketScreen(
                            firebaseHelper = firebaseHelper,
                            onTicketClick = { ticket ->
                                ticketState.value = ticket
                                navController.navigate("editTicketScreen")
                            },
                            onHomeClick = {
                                navController.navigate("home")
                            },
                            onDownloadClick = { filteredTickets ->
                                CsvUtils.exportTicketsToCSV(
                                    context = this@MainActivity,
                                    tickets = filteredTickets
                                )
                            }
                        )
                    }
                }
            }
        }
        setupActivityResultLaunchers()
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, signInRequestCode)
    }

    // Maneja el resultado del intento de inicio de sesión
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == signInRequestCode) {
            val task = GoogleAuthHelper.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(Exception::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: Exception) {
                Toast.makeText(this, "Error al iniciar sesión: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    // Autentica el usuario en Firebase
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                    navController.navigate("home") // Redirige a home tras iniciar sesión
                } else {
                    Toast.makeText(this, "Error al autenticar con Firebase", Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun processImage(imageUri: Uri?, navController: NavHostController) {
        GeminiUtils.processImageWithGemini(this, imageUri) { apiResponse ->
            if (isCancelled) {
                isLoading = false
                return@processImageWithGemini
            }

            if (apiResponse.startsWith("Error")) {
                Toast.makeText(this, apiResponse, Toast.LENGTH_SHORT).show()
            } else {
                val ticket = parseTicketJson(apiResponse)
                ticketState.value = ticket
                isLoading = false
                navController.navigate("ticketScreen")
            }
        }
    }

    private fun parseTicketJson(json: String): Ticket {
        if (!isValidJson(json)) {
            throw IllegalArgumentException("El formato del JSON no es válido")
        }
        return Gson().fromJson(json, Ticket::class.java)
    }

    private fun isValidJson(json: String): Boolean {
        return try {
            Gson().fromJson(json, Any::class.java)
            true
        } catch (e: JsonSyntaxException) {
            false
        }
    }



    private fun setupActivityResultLaunchers() {
        takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                selectedImageUri = CameraUtils.photoUri
                if (selectedImageUri != null) {
                    Toast.makeText(this, "Foto capturada", Toast.LENGTH_SHORT).show()
                    navController.navigate("confirmImageScreen") // Usa el NavController global
                } else {
                    Toast.makeText(this, "Error al capturar la foto", Toast.LENGTH_SHORT).show()
                }
            }
        }

        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val imageUri: Uri? = result.data?.data
            selectedImageUri = imageUri
            if (selectedImageUri != null) {
                Toast.makeText(this, "Imagen seleccionada", Toast.LENGTH_SHORT).show()
                navController.navigate("confirmImageScreen") // Usa el NavController global
            } else {
                Toast.makeText(this, "Error al seleccionar la imagen", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
