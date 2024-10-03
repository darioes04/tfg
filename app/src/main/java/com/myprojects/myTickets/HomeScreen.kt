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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage



@Composable
fun HomeScreen(
    onCameraClick: () -> Unit = {},
    onGalleryClick: () -> Unit = {},
    selectedImageUri: Uri? = null,
    onConfirmImage: () -> Unit = {}
) {


    Scaffold(
        topBar = {
            Text(
                text = "MyTickets",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(50.dp),
                fontSize = 50.5.sp, // Un poco más grande para crear el efecto de borde
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Serif,
                color = Color.Black, // Borde negro
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black,
                        offset = Offset(4f, 4f),
                        blurRadius = 6f
                    )
                ),
                textAlign = TextAlign.Center
            )

            // Texto principal encima del "borde"
            Text(
                text = "MyTickets",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp),
                fontSize = 50.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White, // Texto blanco
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Gray,
                        offset = Offset(4f, 4f),
                        blurRadius = 8f
                    )
                ),
                textAlign = TextAlign.Center
            )
        },

        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Cyan)
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Si se selecciona una imagen, se muestra aquí
                selectedImageUri?.let { uri ->
                    AsyncImage(
                        model = uri,
                        contentDescription = "Imagen seleccionada",
                        modifier = Modifier
                            .size(400.dp)
                            .padding(16.dp)
                    )

                    // Botón para confirmar la imagen seleccionada
                    Button(
                        onClick = onConfirmImage,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("Confirmar imagen")
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Iconos para la cámara y galería
                IconRowSection(onCameraClick = onCameraClick, onGalleryClick = onGalleryClick)
            }
        }
    )
}
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()  // Aquí no necesitamos pasar los callbacks, ya que los valores por defecto son funciones vacías.
}


