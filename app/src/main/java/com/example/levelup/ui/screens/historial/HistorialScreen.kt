package com.example.levelup.ui.screens.historial

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialScreen(
    onToggleDrawer: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "background")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
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
                        Color(0xFF0A0A0A),
                        Color(0xFF050505),
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
                color = Color(0xFF0D0D0D).copy(alpha = 0.98f)
            ) {
                Box(
                    modifier = Modifier
                        .border(
                            2.dp,
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF00AAFF).copy(alpha = glowAlpha),
                                    Color(0xFF00FFAA).copy(alpha = glowAlpha * 0.8f),
                                    Color(0xFFAA00FF).copy(alpha = glowAlpha * 0.6f),
                                    Color(0xFF00AAFF).copy(alpha = glowAlpha)
                                )
                            ),
                            RoundedCornerShape(16.dp)
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Botón de menú
                            Surface(
                                modifier = Modifier.size(40.dp),
                                shape = CircleShape,
                                color = Color(0xFF1A1A1A),
                                onClick = onToggleDrawer
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.border(
                                        1.dp,
                                        Color(0xFF00AAFF).copy(alpha = 0.4f),
                                        CircleShape
                                    )
                                ) {
                                    Icon(
                                        Icons.Default.Menu,
                                        contentDescription = "Menú",
                                        tint = Color(0xFF00AAFF),
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }

                            // Logo
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = Color(0xFF00AAFF).copy(alpha = 0.2f),
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                Icons.Default.History,
                                                contentDescription = null,
                                                tint = Color(0xFF00AAFF),
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }

                                    Column {
                                        Text(
                                            "TU",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontSize = 11.sp,
                                            color = Color(0xFF00AAFF).copy(alpha = 0.7f),
                                            fontWeight = FontWeight.Light,
                                            letterSpacing = 3.sp
                                        )
                                        Text(
                                            "HISTORIAL",
                                            style = MaterialTheme.typography.headlineSmall,
                                            fontSize = 20.sp,
                                            color = Color(0xFF00AAFF),
                                            fontWeight = FontWeight.Black,
                                            letterSpacing = 2.sp
                                        )
                                    }
                                }
                            }
                        }

                        // Botón de volver
                        Surface(
                            modifier = Modifier.size(40.dp),
                            shape = CircleShape,
                            color = Color(0xFF1A1A1A),
                            onClick = onBack
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.border(
                                    1.dp,
                                    Color(0xFF00FFAA).copy(alpha = 0.4f),
                                    CircleShape
                                )
                            ) {
                                Icon(
                                    Icons.Default.ArrowBack,
                                    contentDescription = "Volver",
                                    tint = Color(0xFF00FFAA),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Contenido - Estado vacío
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF1A1A1A),
                    modifier = Modifier.padding(32.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(40.dp)
                    ) {
                        Icon(
                            Icons.Default.History,
                            contentDescription = null,
                            tint = Color(0xFF666666),
                            modifier = Modifier.size(64.dp)
                        )
                        Text(
                            "HISTORIAL VACÍO",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color(0xFF888888),
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp
                        )
                        Text(
                            "Tus compras anteriores aparecerán aquí",
                            color = Color(0xFF555555),
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            "Próximamente podrás ver:",
                            color = Color(0xFF666666),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            HistorialFeature("Tus pedidos realizados")
                            HistorialFeature("Estado de entregas")
                            HistorialFeature("Historial de pagos")
                            HistorialFeature("Productos comprados")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HistorialFeature(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF00AAFF).copy(alpha = 0.6f),
            modifier = Modifier.size(16.dp)
        )
        Text(
            text,
            fontSize = 11.sp,
            color = Color(0xFF888888),
            fontWeight = FontWeight.Medium
        )
    }
}