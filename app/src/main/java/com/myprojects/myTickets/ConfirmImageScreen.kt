package com.myprojects.myTickets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import android.net.Uri

@Composable
fun ConfirmImageScreen(
    selectedImageUri: Uri?,
    onConfirmImage: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        selectedImageUri?.let { uri ->
            AsyncImage(
                model = uri,
                contentDescription = "Imagen seleccionada",
                modifier = Modifier.size(400.dp)
            )

            Button(
                onClick = onConfirmImage,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Confirmar imagen", fontSize = 18.sp)
            }
        }
    }
}

