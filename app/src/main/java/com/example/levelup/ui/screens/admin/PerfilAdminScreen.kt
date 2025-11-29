package com.example.levelup.ui.screens.admin

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.levelup.model.EstadoPedido
import com.example.levelup.viewmodel.CarritoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilAdminScreen(
    viewModel: CarritoViewModel,
    nombre: String,
    onLogout: () -> Unit,
    onNavigateToProductos: () -> Unit,
    onNavigateToPedidos: () -> Unit
) {
    val productos by viewModel.productos.collectAsState()
    val historial by viewModel.historialPedidos.collectAsState()

    val totalProductos = productos.size
    val totalPedidos = historial.size
    val pedidosPendientes = historial.count { it.estado == EstadoPedido.PENDIENTE }
    val productosStockBajo = productos.count { it.stock <= 5 }

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF000000),
                        Color(0xFF0A0000),
                        Color(0xFF000000)
                    )
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 50.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFFFF0055).copy(alpha = 0.2f),
                                modifier = Modifier.size(100.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Default.AdminPanelSettings,
                                        contentDescription = "Admin",
                                        tint = Color(0xFFFF0055),
                                        modifier = Modifier.size(60.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                nombre.uppercase(),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                letterSpacing = 2.sp
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                "ADMINISTRADOR",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFF0055).copy(alpha = 0.8f),
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "PRODUCTOS",
                        value = totalProductos.toString(),
                        icon = Icons.Default.Inventory,
                        color = Color(0xFFFF0055),
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "PEDIDOS",
                        value = totalPedidos.toString(),
                        icon = Icons.Default.ShoppingBag,
                        color = Color(0xFF00AAFF),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            if (pedidosPendientes > 0 || productosStockBajo > 0) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (pedidosPendientes > 0) {
                            AlertCard(
                                text = "$pedidosPendientes pedidos pendientes",
                                icon = Icons.Default.Warning,
                                color = Color(0xFFFFAA00)
                            )
                        }
                        if (productosStockBajo > 0) {
                            AlertCard(
                                text = "$productosStockBajo productos con stock bajo",
                                icon = Icons.Default.Inventory2,
                                color = Color(0xFFFF5555)
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    "GESTIÓN",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFFFF0055),
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            item {
                AdminActionCard(
                    title = "Gestionar Productos",
                    description = "Ver, agregar y editar productos",
                    icon = Icons.Default.Inventory2,
                    onClick = onNavigateToProductos
                )
            }

            item {
                AdminActionCard(
                    title = "Ver Pedidos",
                    description = "Administrar y cambiar estados",
                    icon = Icons.Default.LocalShipping,
                    onClick = onNavigateToPedidos
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onLogout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF0055),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(14.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 2.dp
                    )
                ) {
                    Icon(
                        Icons.Default.Logout,
                        contentDescription = "Cerrar sesión",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "CERRAR SESIÓN",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun StatCard(
    title: String, value: String, icon: ImageVector, color: Color, modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.shadow(8.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF0D0000)
    ) {
        Box(
            modifier = Modifier.border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text(value, fontSize = 24.sp, fontWeight = FontWeight.Black, color = color)
                Text(title, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF888888), letterSpacing = 1.sp)
            }
        }
    }
}

@Composable
private fun AlertCard(text: String, icon: ImageVector, color: Color) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = color.copy(alpha = 0.15f)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            Text(text, fontSize = 13.sp, color = color, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun AdminActionCard(title: String, description: String, icon: ImageVector, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFF0D0000),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.border(
                2.dp,
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFFF0055).copy(alpha = 0.4f),
                        Color(0xFFFF0055).copy(alpha = 0.2f)
                    )
                ),
                RoundedCornerShape(14.dp)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFF0055).copy(alpha = 0.15f),
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(icon, contentDescription = null, tint = Color(0xFFFF0055), modifier = Modifier.size(28.dp))
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(title, fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White, letterSpacing = 0.5.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(description, fontSize = 11.sp, color = Color(0xFF888888))
                }

                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFFFF0055).copy(alpha = 0.6f), modifier = Modifier.size(24.dp))
            }
        }
    }
}