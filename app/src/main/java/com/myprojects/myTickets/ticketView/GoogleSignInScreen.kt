    package com.myprojects.myTickets.ticketView

    import androidx.compose.foundation.Image
    import androidx.compose.foundation.background
    import androidx.compose.foundation.border
    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.shape.CircleShape
    import androidx.compose.material3.Button
    import androidx.compose.material3.ButtonDefaults
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
                .padding(16.dp), contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Imagen personalizada
                Image(
                    painter = painterResource(id = R.drawable.icono), // Imagen desde recursos
                    contentDescription = "Google Logo",
                    modifier = Modifier
                        .size(62.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Bienvenido a MyTickets",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground // Cambia dinámicamente según el tema
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Eslogan
                Text(
                    text = "Organiza tus tickets, simplifica tu vida",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary, // Usa el color primario de la app
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Inicia sesión con Google para continuar",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground, // Cambia dinámicamente según el tema
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onSignInClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent, // Fondo transparente
                        contentColor = MaterialTheme.colorScheme.primary // Texto del botón
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(60.dp)
                        .border(
                            width = 1.dp,
                            color = Color.Red, // Borde rojo
                            shape = CircleShape
                        )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.google_icon),
                            contentDescription = "Google Logo",
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Iniciar sesión con Google",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary // Texto rojo
                        )
                    }
                }



                Spacer(modifier = Modifier.height(16.dp))

            }
        }
    }
