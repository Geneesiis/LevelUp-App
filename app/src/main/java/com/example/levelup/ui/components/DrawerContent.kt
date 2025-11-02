package com.example.levelup.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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

@Composable
fun DrawerContent(
    currentRoute: String,
    onNavigateToCatalogo: () -> Unit,
    onNavigateToDeseados: () -> Unit,
    onNavigateToCarrito: () -> Unit,
    onNavigateToPerfil: () -> Unit,
    onNavigateToHistorial: () -> Unit,
    carritoCount: Int,
    deseadosCount: Int
) {
    val infiniteTransition = rememberInfiniteTransition(label = "drawer_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Surface(
        modifier = Modifier
            .fillMaxHeight()
            .width(280.dp),
        color = Color(0xFF0A0A0A)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 24.dp)
        ) {
            // Header del Drawer
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .shadow(12.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF0D0D0D)
            ) {
                Box(
                    modifier = Modifier
                        .border(
                            2.dp,
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF00FFAA).copy(alpha = glowAlpha),
                                    Color(0xFF00AAFF).copy(alpha = glowAlpha * 0.8f),
                                    Color(0xFFAA00FF).copy(alpha = glowAlpha * 0.6f),
                                    Color(0xFF00FFAA).copy(alpha = glowAlpha)
                                )
                            ),
                            RoundedCornerShape(16.dp)
                        )
                        .padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF00FFAA).copy(alpha = 0.2f),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFF00FFAA),
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }

                        Column {
                            Text(
                                "LEVEL UP",
                                fontSize = 20.sp,
                                color = Color(0xFF00FFAA),
                                fontWeight = FontWeight.Black,
                                letterSpacing = 2.sp
                            )
                            Text(
                                "Gaming Store",
                                fontSize = 11.sp,
                                color = Color(0xFF666666),
                                fontWeight = FontWeight.Medium,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Sección: NAVEGACIÓN
            DrawerSectionTitle("NAVEGACIÓN")

            DrawerMenuItem(
                icon = Icons.Default.Home,
                title = "Catálogo",
                isSelected = currentRoute.contains("catalogo"),
                onClick = onNavigateToCatalogo
            )

            DrawerMenuItem(
                icon = Icons.Default.Favorite,
                title = "Deseados",
                badge = if (deseadosCount > 0) deseadosCount.toString() else null,
                badgeColor = Color(0xFFFF0055),
                isSelected = currentRoute.contains("deseados"),
                onClick = onNavigateToDeseados
            )

            DrawerMenuItem(
                icon = Icons.Default.ShoppingCart,
                title = "Carrito",
                badge = if (carritoCount > 0) carritoCount.toString() else null,
                badgeColor = Color(0xFF00FFAA),
                isSelected = currentRoute.contains("carrito"),
                onClick = onNavigateToCarrito
            )

            Spacer(modifier = Modifier.height(16.dp))
            DrawerDivider()
            Spacer(modifier = Modifier.height(16.dp))

            // Sección: CUENTA
            DrawerSectionTitle("CUENTA")

            DrawerMenuItem(
                icon = Icons.Default.Person,
                title = "Mi Perfil",
                isSelected = currentRoute.contains("perfil"),
                onClick = onNavigateToPerfil
            )

            DrawerMenuItem(
                icon = Icons.Default.History,
                title = "Historial",
                isSelected = currentRoute.contains("historial"),
                onClick = onNavigateToHistorial
            )

            Spacer(modifier = Modifier.weight(1f))

            // Footer
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF0D0D0D).copy(alpha = 0.5f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                Color(0xFF00FF00).copy(alpha = glowAlpha),
                                CircleShape
                            )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "SISTEMA ACTIVO",
                        fontSize = 11.sp,
                        color = Color(0xFF00FF00).copy(alpha = 0.8f),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun DrawerSectionTitle(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            title,
            fontSize = 11.sp,
            color = Color(0xFF666666),
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(Color(0xFF333333))
        )
    }
}

@Composable
private fun DrawerMenuItem(
    icon: ImageVector,
    title: String,
    badge: String? = null,
    badgeColor: Color = Color(0xFF00FFAA),
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected)
            Color(0xFF00FFAA).copy(alpha = 0.15f)
        else
            Color.Transparent,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = if (isSelected) 1.dp else 0.dp,
                    color = if (isSelected) Color(0xFF00FFAA).copy(alpha = 0.4f) else Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                icon,
                contentDescription = title,
                tint = if (isSelected) Color(0xFF00FFAA) else Color(0xFF666666),
                modifier = Modifier.size(24.dp)
            )

            Text(
                title,
                fontSize = 14.sp,
                color = if (isSelected) Color(0xFF00FFAA) else Color(0xFF999999),
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            if (badge != null) {
                Surface(
                    shape = CircleShape,
                    color = badgeColor,
                    modifier = Modifier.shadow(4.dp, CircleShape)
                ) {
                    Box(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            badge,
                            fontSize = 11.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DrawerDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(1.dp)
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color(0xFF333333),
                        Color.Transparent
                    )
                )
            )
    )
}