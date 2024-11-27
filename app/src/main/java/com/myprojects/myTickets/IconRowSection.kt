package com.myprojects.myTickets

import androidx.compose.foundation.Image

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myprojects.prueba1.R


@Composable
fun IconRowSection(onCameraClick: () -> Unit, onGalleryClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            // Card para la galería
            Card(
                modifier = Modifier
                    .size(160.dp) // Tamaño de la tarjeta
                    .clickable { onGalleryClick() }
                    .padding(8.dp), // Acción al hacer clic
                shape = RoundedCornerShape(20.dp), // Bordes redondeados
                elevation = CardDefaults.cardElevation(8.dp) // Sombra de la tarjeta
            ) {
                Image(
                    painter = painterResource(R.drawable.galeria),
                    contentDescription = "Icono de la Galería",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .clip(shape = RoundedCornerShape(20.dp))
                )
            }

            Text(
                text = "Galería",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            // Card para la cámara
            Card(
                modifier = Modifier
                    .size(160.dp) // Tamaño de la tarjeta
                    .clickable { onCameraClick() }
                    .padding(8.dp), // Acción al hacer clic
                shape = RoundedCornerShape(20.dp), // Bordes redondeados
                elevation = CardDefaults.cardElevation(8.dp) // Sombra de la tarjeta
            ) {
                Image(
                    painter = painterResource(R.drawable.camera),
                    contentDescription = "Icono de la Cámara",
                    modifier = Modifier
                        .fillMaxSize() // La imagen llena la tarjeta
                        .padding(16.dp)
                        .clip(shape = RoundedCornerShape(20.dp))// Espaciado dentro de la tarjeta
                )
            }

            Text(
                text = "Cámara",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface
            )
       }
    }
}
