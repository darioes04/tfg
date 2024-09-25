package com.myprojects.myTickets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.dp
import com.myprojects.prueba1.R


@Composable
fun IconRowSection(onCameraClick: () -> Unit, onGalleryClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .background(Color.Cyan)
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Ícono para abrir la cámara
        IconItem(
            iconRes = R.drawable.camara,
            description = "Icono de la Cámara",
            onClick = onCameraClick
        )

        // Ícono para abrir la galería
        IconItem(
            iconRes = R.drawable.galeria,
            description = "Icono de la Galería",
            onClick = onGalleryClick
        )
    }
}


