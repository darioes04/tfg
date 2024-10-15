package com.myprojects.myTickets

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.myprojects.myTickets.ui.theme.MyTicketsTheme


@Composable
fun HomeScreen(
    onCameraClick: () -> Unit = {},
    onGalleryClick: () -> Unit = {},
    selectedImageUri: Uri? = null,
    onConfirmImage: () -> Unit = {},
    onNavigateToList: () -> Unit
) {
    MyTicketsTheme {
        Scaffold(
            topBar = {
                Text(
                    text = "MyTickets",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 50.dp),
                    fontSize = 50.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground, // Cambia según el tema
                    textAlign = TextAlign.Center
                )
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background) // Fondo adaptado al tema
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    Button(onClick = onNavigateToList, modifier = Modifier.padding(16.dp)) {
                        Text("Ver Tickets Guardados")
                    }

                    selectedImageUri?.let { uri ->
                        AsyncImage(
                            model = uri,
                            contentDescription = "Imagen seleccionada",
                            modifier = Modifier
                                .size(400.dp)
                                .padding(16.dp)
                        )

                        Button(
                            onClick = onConfirmImage,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text("Confirmar imagen")
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    IconRowSection(onCameraClick = onCameraClick, onGalleryClick = onGalleryClick)
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreen() {
}
