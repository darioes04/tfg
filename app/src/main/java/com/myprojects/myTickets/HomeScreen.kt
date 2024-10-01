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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.myprojects.prueba1.R
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont


@Composable
fun HomeScreen(
    onCameraClick: () -> Unit = {},
    onGalleryClick: () -> Unit = {},
    selectedImageUri: Uri? = null,
    onConfirmImage: () -> Unit = {}
) {

    /*val customFontFamily = FontFamily(
        Font(R.font.playfairdisplay_variable_font_wght) // Reemplaza con el nombre de tu archivo de fuente
    )*/

    val googleFontProvider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )

    val fontName = GoogleFont("Roboto")
    val fontFamily = FontFamily(
        Font(googleFont = fontName, fontProvider = googleFontProvider, weight = FontWeight.Bold)
    )

    Scaffold(
        topBar = {
            Text(
                text = "MyTickets",
                fontSize = 40.sp,
                fontFamily = fontFamily,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .padding(top= 26.dp)
                    .padding(16.dp)
                    .fillMaxWidth(),
                color = Color.Black,

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

                Spacer(modifier = Modifier.height(32.dp))

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


