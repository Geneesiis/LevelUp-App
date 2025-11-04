package com.example.levelup.ui.screens.pago

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.levelup.R

@Composable
fun PagoConfirmacionScreen(
    nombreUsuario: String = "Cliente",
    onVolverAlPerfil: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 32.dp)
            )

            // Icono de confirmación
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = "Pago confirmado",
                modifier = Modifier.size(80.dp),
                tint = Color(0xFF00FFAA)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "¡PAGO CONFIRMADO!",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF00FFAA),
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Tu pedido ha sido procesado exitosamente",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Recibirás una confirmación por correo electrónico",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF888888),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botón
            Button(
                onClick = onVolverAlPerfil,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00FFAA),
                    contentColor = Color.Black
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 4.dp
                )
            ) {
                Text(
                    "VOLVER AL PERFIL DE ${nombreUsuario.uppercase()}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black
                    ),
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
    }
}