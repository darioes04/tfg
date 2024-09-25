package com.myprojects.myTickets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun IconItem(iconRes: Int, description: String, onClick: () -> Unit = {}) {
    Image(
        painter = painterResource(id = iconRes),
        contentDescription = description,
        modifier = Modifier
            .size(150.dp) // Tamaño del ícono
            .padding(8.dp) // Padding alrededor del ícono
            .clickable { onClick() } // Agrega comportamiento al hacer clic
    )
}
