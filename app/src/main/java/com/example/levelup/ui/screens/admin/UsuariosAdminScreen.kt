package com.example.levelup.ui.screens.admin

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Modelo de usuario simple para demo
data class Usuario(
    val id: String,
    val nombre: String,
    val email: String,
    val rol: String = "Cliente",
    val activo: Boolean = true,
    val totalCompras: Int = 0,
    val fechaRegistro: String = "2024-01-15"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuariosAdminScreen(
    onBack: () -> Unit = {}
) {
    // Usuarios de ejemplo (en producción vendrían de un ViewModel/Repository)
    val usuariosEjemplo = remember {
        listOf(
            Usuario("1", "Juan Pérez", "juan@email.com", "Cliente", true, 5, "2024-01-10"),
            Usuario("2", "María González", "maria@email.com", "Cliente", true, 12, "2024-02-05"),
            Usuario("3", "Carlos Silva", "carlos@email.com", "Admin", true, 3, "2023-12-01"),
            Usuario("4", "Ana Torres", "ana@email.com", "Cliente", false, 2, "2024-03-20"),
            Usuario("5", "Pedro Ruiz", "pedro@email.com", "Cliente", true, 8, "2024-01-25")
        )
    }

    var searchQuery by remember { mutableStateOf("") }
    var filtroRol by remember { mutableStateOf<String?>(null) }

    val infiniteTransition = rememberInfiniteTransition(label = "admin_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    val usuariosFiltrados = remember(usuariosEjemplo, searchQuery, filtroRol) {
        usuariosEjemplo.filter { usuario ->
            val matchQuery = if (searchQuery.isBlank()) true else {
                usuario.nombre.contains(searchQuery, ignoreCase = true) ||
                        usuario.email.contains(searchQuery, ignoreCase = true)
            }
            val matchRol = filtroRol == null || usuario.rol == filtroRol
            matchQuery && matchRol
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF000000),
                        Color(0xFF0A0000),
                        Color(0xFF050000),
                        Color(0xFF000000)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 50.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
        ) {
            // Header
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(16.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF0D0000).copy(alpha = 0.98f)
            ) {
                Box(
                    modifier = Modifier.border(
                        2.dp,
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFFF0055).copy(alpha = glowAlpha),
                                Color(0xFFFF5555).copy(alpha = glowAlpha * 0.8f)
                            )
                        ),
                        RoundedCornerShape(16.dp)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Surface(
                                    modifier = Modifier.size(40.dp),
                                    shape = CircleShape,
                                    color = Color(0xFF1A0000),
                                    onClick = onBack
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.border(
                                            1.dp,
                                            Color(0xFFFF0055).copy(alpha = 0.4f),
                                            CircleShape
                                        )
                                    ) {
                                        Icon(
                                            Icons.Default.ArrowBack,
                                            contentDescription = "Volver",
                                            tint = Color(0xFFFF0055),
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }

                                Column {
                                    Text(
                                        "GESTIÓN DE",
                                        fontSize = 11.sp,
                                        color = Color(0xFFFF0055).copy(alpha = 0.7f),
                                        fontWeight = FontWeight.Light,
                                        letterSpacing = 3.sp
                                    )
                                    Text(
                                        "USUARIOS",
                                        fontSize = 20.sp,
                                        color = Color(0xFFFF0055),
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 2.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Stats
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            UserStatChip(
                                label = "TOTAL",
                                value = usuariosEjemplo.size.toString(),
                                icon = Icons.Default.People,
                                color = Color(0xFFFF0055)
                            )
                            UserStatChip(
                                label = "ACTIVOS",
                                value = usuariosEjemplo.count { it.activo }.toString(),
                                icon = Icons.Default.CheckCircle,
                                color = Color(0xFF00FF00)
                            )
                            UserStatChip(
                                label = "ADMINS",
                                value = usuariosEjemplo.count { it.rol == "Admin" }.toString(),
                                icon = Icons.Default.Shield,
                                color = Color(0xFFFF5555)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Buscador y filtros
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF0D0000)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Buscar por nombre o email...", color = Color(0xFF666666)) },
                        leadingIcon = {
                            Icon(Icons.Default.Search, null, tint = Color(0xFFFF0055))
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Close, null, tint = Color(0xFF888888))
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFFFF0055),
                            unfocusedBorderColor = Color(0xFF333333),
                            cursorColor = Color(0xFFFF0055)
                        ),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Filtros de rol
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = filtroRol == null,
                            onClick = { filtroRol = null },
                            label = { Text("TODOS", fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFFFF0055),
                                selectedLabelColor = Color.White
                            )
                        )
                        FilterChip(
                            selected = filtroRol == "Cliente",
                            onClick = { filtroRol = "Cliente" },
                            label = { Text("CLIENTES", fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF00AAFF),
                                selectedLabelColor = Color.White
                            )
                        )
                        FilterChip(
                            selected = filtroRol == "Admin",
                            onClick = { filtroRol = "Admin" },
                            label = { Text("ADMINS", fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFFFF5555),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de usuarios
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(usuariosFiltrados, key = { it.id }) { usuario ->
                    UsuarioAdminCard(usuario)
                }

                if (usuariosFiltrados.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    Icons.Default.PersonOff,
                                    contentDescription = null,
                                    tint = Color(0xFF666666),
                                    modifier = Modifier.size(48.dp)
                                )
                                Text(
                                    "No se encontraron usuarios",
                                    color = Color(0xFF888888),
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UserStatChip(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.15f),
        modifier = Modifier.border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(icon, null, tint = color, modifier = Modifier.size(16.dp))
            Column {
                Text(
                    value,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = color
                )
                Text(
                    label,
                    fontSize = 9.sp,
                    color = Color(0xFF888888),
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
private fun UsuarioAdminCard(usuario: Usuario) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFF0D0000)
    ) {
        Box(
            modifier = Modifier.border(
                1.dp,
                if (usuario.activo) Color(0xFFFF0055).copy(alpha = 0.3f) else Color(0xFF666666).copy(alpha = 0.3f),
                RoundedCornerShape(14.dp)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar
                Surface(
                    shape = CircleShape,
                    color = if (usuario.rol == "Admin") Color(0xFFFF5555).copy(alpha = 0.2f) else Color(0xFF00AAFF).copy(alpha = 0.2f),
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            if (usuario.rol == "Admin") Icons.Default.AdminPanelSettings else Icons.Default.Person,
                            contentDescription = null,
                            tint = if (usuario.rol == "Admin") Color(0xFFFF5555) else Color(0xFF00AAFF),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Info
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        usuario.nombre,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (usuario.activo) Color.White else Color(0xFF888888)
                    )
                    Text(
                        usuario.email,
                        fontSize = 11.sp,
                        color = Color(0xFF888888)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        RolBadge(usuario.rol)
                        StatusBadge(usuario.activo)
                        Text(
                            "${usuario.totalCompras} compras",
                            fontSize = 10.sp,
                            color = Color(0xFF666666)
                        )
                    }
                }

                // Acciones
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(onClick = { /* TODO: Ver detalles */ }) {
                        Icon(
                            Icons.Default.Visibility,
                            "Ver detalles",
                            tint = Color(0xFF00AAFF),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(onClick = { /* TODO: Editar */ }) {
                        Icon(
                            Icons.Default.Edit,
                            "Editar",
                            tint = Color(0xFFFF0055),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RolBadge(rol: String) {
    val color = if (rol == "Admin") Color(0xFFFF5555) else Color(0xFF00AAFF)
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = color.copy(alpha = 0.2f)
    ) {
        Text(
            rol.uppercase(),
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            fontSize = 9.sp,
            fontWeight = FontWeight.Black,
            color = color,
            letterSpacing = 1.sp
        )
    }
}

@Composable
private fun StatusBadge(activo: Boolean) {
    val color = if (activo) Color(0xFF00FF00) else Color(0xFF666666)
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = color.copy(alpha = 0.2f)
    ) {
        Text(
            if (activo) "ACTIVO" else "INACTIVO",
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            fontSize = 9.sp,
            fontWeight = FontWeight.Black,
            color = color,
            letterSpacing = 1.sp
        )
    }
}