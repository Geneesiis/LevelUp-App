package com.example.levelup.ui.screens.perfil

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.levelup.R

@Composable
fun PerfilClienteScreen(
    nombre: String = "Cliente",
    onBack: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 24.dp,
                    start = 24.dp,
                    end = 24.dp,
                    bottom = 24.dp)
        ) {
            // Header con botón volver
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color(0xFF00FFAA)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Volver al Catálogo",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF00FFAA)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Contenido principal centrado
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Tarjeta de perfil
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF111111)
                    ),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Icono de perfil
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Perfil",
                            modifier = Modifier.size(64.dp),
                            tint = Color(0xFF00FFAA)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            "PERFIL DE CLIENTE",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color(0xFF00FFAA),
                            fontWeight = FontWeight.Black
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        // Información del usuario
                        InfoRow(
                            icon = Icons.Default.Person,
                            label = "Nombre",
                            value = nombre
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        InfoRow(
                            icon = Icons.Default.ShoppingCart,
                            label = "Rol",
                            value = "Cliente"
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        // Botón Cerrar Sesión
                        Button(
                            onClick = onLogout,
                            modifier = Modifier
                                .fillMaxWidth()
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
                            Icon(
                                Icons.Default.Logout,
                                contentDescription = "Cerrar sesión",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "CERRAR SESIÓN",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            tint = Color(0xFF00FFAA)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                label,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF888888)
            )
            Text(
                value,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}