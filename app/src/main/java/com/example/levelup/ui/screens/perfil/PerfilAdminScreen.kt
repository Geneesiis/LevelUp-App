package com.example.levelup.ui.screens.perfil

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
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
fun PerfilAdminScreen(
    nombre: String = "Administrador",
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
                    bottom = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tarjeta de administrador
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
                    // Icono de administrador
                    Icon(
                        Icons.Default.AdminPanelSettings,
                        contentDescription = "Administrador",
                        modifier = Modifier.size(72.dp),
                        tint = Color(0xFFFF5555) // Rojo cyber para admin
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        "PANEL DE ADMINISTRADOR",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color(0xFFFF5555),
                        fontWeight = FontWeight.Black
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Información del administrador
                    InfoRow(
                        icon = Icons.Default.Person,
                        label = "Nombre",
                        value = nombre,
                        iconColor = Color(0xFFFF5555)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    InfoRow(
                        icon = Icons.Default.Security,
                        label = "Rol",
                        value = "Administrador",
                        iconColor = Color(0xFFFF5555)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    InfoRow(
                        icon = Icons.Default.AdminPanelSettings,
                        label = "Privilegios",
                        value = "Acceso Total",
                        iconColor = Color(0xFFFF5555)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Mensaje de advertencia
                    Text(
                        "Tienes acceso completo al sistema\nGestiona productos, usuarios y pedidos",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF888888),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Botón Cerrar Sesión
                    Button(
                        onClick = onLogout,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF5555), // Rojo cyber
                            contentColor = Color.White
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
                            "CERRAR SESIÓN ADMIN",
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

// Versión de InfoRow con color personalizable para admin
@Composable
fun InfoRow(icon: ImageVector, label: String, value: String, iconColor: Color = Color(0xFF00FFAA)) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            tint = iconColor
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