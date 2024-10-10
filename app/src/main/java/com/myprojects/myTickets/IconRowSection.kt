package com.myprojects.myTickets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import com.myprojects.prueba1.R


@Composable
fun IconRowSection(onCameraClick: () -> Unit, onGalleryClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .padding(bottom = 10.dp)

    ) {
        // Ícono para abrir la cámara
        Image(
            painter = painterResource(R.drawable.camara),
            contentDescription = "Icono de la Cámara",
            modifier = Modifier
                .size(150.dp) // Tamaño del ícono
                .padding(8.dp) // Padding alrededor del ícono
                .clickable { onCameraClick() } // Agrega comportamiento al hacer clic
        )


        // Ícono para abrir la galería
        Image(
            painter = painterResource(R.drawable.galeria),
            contentDescription = "Icono de la Galería",
            modifier = Modifier
                .size(108.dp) // Tamaño del ícono
                .padding(8.dp) // Padding alrededor del ícono
                .clickable { onGalleryClick() } // Agrega comportamiento al hacer clic
        )
    }
}


