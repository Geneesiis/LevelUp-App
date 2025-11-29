package com.example.levelup.ui.screens.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilClienteScreen(
    nombre: String = "Cliente",
    onBack: () -> Unit = {},
    onLogout: () -> Unit = {},
    onEditProfile: () -> Unit = {},
    onMyOrders: () -> Unit = {},
    onWishlist: () -> Unit = {}
) {
    Scaffold(
        containerColor = Color.Black,
        topBar = {
            TopAppBar(
                title = { /* No title for a cleaner look */ },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color(0xFF00FFAA)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Contenido principal que puede hacer scroll
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // 1. Tarjeta de Información de Perfil
                ProfileInfoCard(nombre = nombre)

                Spacer(modifier = Modifier.height(24.dp))

                // 2. Tarjeta de Acciones del Cliente
                ActionsCard(
                    onEditProfile = onEditProfile,
                    onMyOrders = onMyOrders,
                    onWishlist = onWishlist
                )
            }

            // 3. Botón de Cerrar Sesión en la parte inferior
            Spacer(modifier = Modifier.height(16.dp))
            LogoutButton(onLogout = onLogout)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ProfileInfoCard(nombre: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF111111)),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileAvatar(name = nombre)
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "PERFIL DE CLIENTE",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFF00FFAA),
                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.height(32.dp))
            InfoRow(icon = Icons.Default.Person, label = "Nombre", value = nombre)
            Spacer(modifier = Modifier.height(16.dp))
            InfoRow(icon = Icons.Default.ShoppingCart, label = "Rol", value = "Cliente")
        }
    }
}

@Composable
fun ActionsCard(
    onEditProfile: () -> Unit,
    onMyOrders: () -> Unit,
    onWishlist: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF111111)),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)) {
            ActionRow(
                icon = Icons.Default.Edit,
                text = "Editar Perfil",
                onClick = onEditProfile
            )
            Divider(color = Color(0xFF222222), thickness = 1.dp)
            ActionRow(
                icon = Icons.Default.ListAlt,
                text = "Mis Pedidos",
                onClick = onMyOrders
            )
            Divider(color = Color(0xFF222222), thickness = 1.dp)
            ActionRow(
                icon = Icons.Default.FavoriteBorder,
                text = "Lista de Deseados",
                onClick = onWishlist
            )
        }
    }
}

@Composable
fun ActionRow(icon: ImageVector, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = text, tint = Color(0xFF00FFAA))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, color = Color.White, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.weight(1f))
        Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color(0xFF888888))
    }
}

@Composable
fun LogoutButton(onLogout: () -> Unit) {
    Button(
        onClick = onLogout,
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF00FFAA),
            contentColor = Color.Black
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp, pressedElevation = 4.dp)
    ) {
        Icon(Icons.Default.Logout, contentDescription = "Cerrar sesión", modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            "CERRAR SESIÓN",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black)
        )
    }
}


@Composable
fun ProfileAvatar(name: String) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .background(color = Color(0xFF00FFAA), shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (name.isNotEmpty()) name.first().uppercase() else "C",
            color = Color.Black,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
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

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PerfilClienteScreenPreview() {
    PerfilClienteScreen(nombre = "Carlos")
}
