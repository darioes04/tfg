package com.myprojects.myTickets

import androidx.compose.foundation.Image

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.myprojects.prueba1.R


@Composable
fun IconRowSection(onCameraClick: () -> Unit, onGalleryClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // Ícono para abrir la galería
        Image(
            painter = painterResource(R.drawable.galeria),
            contentDescription = "Icono de la Galería",
            modifier = Modifier
                .size(120.dp) // Tamaño del ícono
                .padding(8.dp)
                .clip(shape = RoundedCornerShape(40.dp))// Padding alrededor del ícono
                .clickable { onGalleryClick() } // Agrega comportamiento al hacer clic
        )

        // Ícono para abrir la cámara
        Image(
            painter = painterResource(R.drawable.camera),
            contentDescription = "Icono de la Cámara",
            modifier = Modifier
                .size(120.dp) // Tamaño del ícono
                .padding(8.dp)
                .clip(shape = RoundedCornerShape(30.dp))// Padding alrededor del ícono
                .clickable { onCameraClick() } // Agrega comportamiento al hacer clic
        )
    }
}


