package com.myprojects.myTickets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import android.net.Uri
import android.window.OnBackInvokedCallback
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp


@Composable
fun ConfirmImageScreen(
    selectedImageUri: Uri?,
    onConfirmImage: () -> Unit,
    onCancelClick: () -> Unit,
    isLoading: Boolean,
    onBackPressed: () -> Unit
) {

    BackHandler {
        onBackPressed()
    }

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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween, // Divide automáticamente el espacio entre los elementos
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically, // Asegura que el icono y el texto estén alineados verticalmente
                            horizontalArrangement = Arrangement.Center // Centra el contenido en el eje X
                        ) {
                            Icon(
                                imageVector = Icons.Default.Image, // Ícono de Material Icons
                                contentDescription = "Icono de Imagen",
                                tint = MaterialTheme.colorScheme.primary, // Ajusta el color del ícono
                                modifier = Modifier
                                    .size(35.dp) // Tamaño del ícono
                                    .padding(end = 8.dp) // Espacio entre el ícono y el texto
                            )

                            Text(
                                text = "Imagen Seleccionada",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center
                            )
                        }

                        // Contenedor para la imagen
                        selectedImageUri?.let { uri ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(4f / 3f),
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.elevatedCardElevation(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                                ),
                                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                            ) {
                                AsyncImage(
                                    model = uri,
                                    contentDescription = "Imagen seleccionada",
                                    modifier = Modifier.fillMaxSize(),
                                )
                            }

                        } ?: Text(
                            text = "No se seleccionó ninguna imagen",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1.5f))
                    // Columna del texto de confirmación
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Por favor, confirma si esta es la imagen correcta",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }


                }
            }
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 25.dp
            ) {


                if (isLoading) {
                    LoadingAnimation() // Mostrar la animación de carga
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly // Espacio uniforme entre botones
                    ) {
                        OutlinedButton(
                            onClick = { onCancelClick() },
                        ) {
                            Text("Cancelar")
                        }

                        TextButton(
                            onClick = { onConfirmImage() },
                            // Igual peso que el botón de cancelar
                            colors = ButtonDefaults.buttonColors(
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text("Confirmar")
                        }
                    }
                }

            }
        }
    )
}


@Composable
fun LoadingAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val animatedProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.size(90.dp),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 6.dp,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Cargando...",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

