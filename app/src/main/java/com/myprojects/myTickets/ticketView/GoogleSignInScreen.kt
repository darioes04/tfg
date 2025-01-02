package com.myprojects.myTickets.ticketView

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.myprojects.prueba1.R

@Composable
fun GoogleSignInScreen(onSignInClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                // Icono de Google
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Google Icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(80.dp)
                        .padding(end = 8.dp) // Espacio entre el icono y la imagen
                )

                // Imagen personalizada
                Image(
                    painter = painterResource(id = R.drawable.icono), // Imagen desde recursos
                    contentDescription = "Google Logo",
                    modifier = Modifier
                        .size(62.dp)
                        .clip(CircleShape)
                )
            }

            Text(
                text = "Bienvenido a MyTickets",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Inicia sesión con Google para continuar",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onSignInClick,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(60.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.google_icon),
                        contentDescription = "Google Logo",
                        modifier = Modifier
                            .size(62.dp)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Iniciar sesión con Google",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "No compartimos tus datos personales.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
        }
    }

}
