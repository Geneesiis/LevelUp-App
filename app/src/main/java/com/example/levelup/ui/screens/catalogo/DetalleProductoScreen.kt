package com.example.levelup.ui.screens.catalogo

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.levelup.model.Producto
import com.example.levelup.utils.formatPrecioCLP
import com.example.levelup.viewmodel.CarritoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProductoScreen(
    producto: Producto,
    viewModel: CarritoViewModel,
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()

    // Animación de brillo
    val infiniteTransition = rememberInfiniteTransition(label = "shine")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Header con gradiente y botón volver
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF1A0000),
                                Color.Transparent
                            )
                        )
                    )
                    .padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
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

                    Text(
                        "DETALLES",
                        fontSize = 16.sp,
                        color = Color(0xFF00FFAA),
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp
                    )
                }
            }

            // Imagen del producto con efecto
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(340.dp)
                    .shadow(24.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFF0D0000)
            ) {
                Box(
                    modifier = Modifier.border(
                        2.dp,
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF00FFAA).copy(alpha = glowAlpha * 0.6f),
                                Color(0xFF00FFAA).copy(alpha = glowAlpha * 0.3f),
                                Color(0xFF00FFAA).copy(alpha = glowAlpha * 0.6f)
                            )
                        ),
                        RoundedCornerShape(20.dp)
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    if (producto.imagen.isNotEmpty()) {
                        AsyncImage(
                            model = producto.imagen,
                            contentDescription = producto.nombre,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(20.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            Icons.Default.Inventory2,
                            contentDescription = null,
                            tint = Color(0xFF333333),
                            modifier = Modifier.size(100.dp)
                        )
                    }

                    // Badge de stock
                    if (producto.stock <= 5 && producto.stock > 0) {
                        Surface(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(16.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFFAA00)
                        ) {
                            Text(
                                "¡ÚLTIMAS UNIDADES!",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.Black,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Información principal
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                // Nombre
                Text(
                    text = producto.nombre,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    lineHeight = 34.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Categoría
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFF00FFAA).copy(alpha = 0.15f)
                ) {
                    Text(
                        producto.categoria,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00FFAA),
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Precio con efecto
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF00FFAA).copy(alpha = 0.1f),
                    modifier = Modifier.border(
                        1.dp,
                        Color(0xFF00FFAA).copy(alpha = 0.3f),
                        RoundedCornerShape(12.dp)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                "PRECIO",
                                fontSize = 10.sp,
                                color = Color(0xFF888888),
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                formatPrecioCLP(producto.precio),
                                fontSize = 32.sp,
                                color = Color(0xFF00FFAA),
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Descripción
                Text(
                    "DESCRIPCIÓN",
                    fontSize = 13.sp,
                    color = Color(0xFF00FFAA),
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    producto.descripcion.ifEmpty { "Sin descripción disponible" },
                    fontSize = 15.sp,
                    color = Color(0xFFCCCCCC),
                    lineHeight = 22.sp,
                    textAlign = TextAlign.Justify
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Especificaciones
                Text(
                    "DISPONIBILIDAD",
                    fontSize = 13.sp,
                    color = Color(0xFF00FFAA),
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Cards de info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoCard(
                        icon = Icons.Default.Inventory,
                        label = "STOCK",
                        value = "${producto.stock} unidades",
                        color = if (producto.stock > 5) Color(0xFF00FFAA)
                        else if (producto.stock > 0) Color(0xFFFFAA00)
                        else Color(0xFFFF5555),
                        modifier = Modifier.weight(1f)
                    )

                    InfoCard(
                        icon = Icons.Default.CheckCircle,
                        label = "ESTADO",
                        value = if (producto.stock > 0) "Disponible" else "Agotado",
                        color = if (producto.stock > 0) Color(0xFF00FFAA) else Color(0xFFFF5555),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Botón Agregar al Carrito
                Button(
                    onClick = { viewModel.agregarAlCarrito(producto) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .testTag("boton_agregar"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00FFAA),
                        contentColor = Color.Black,
                        disabledContainerColor = Color(0xFF333333),
                        disabledContentColor = Color(0xFF666666)
                    ),
                    shape = RoundedCornerShape(14.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 12.dp,
                        pressedElevation = 4.dp,
                        disabledElevation = 0.dp
                    ),
                    enabled = producto.stock > 0
                ) {
                    Icon(
                        Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        if (producto.stock > 0) "AGREGAR AL CARRITO" else "PRODUCTO AGOTADO",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
private fun InfoCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .shadow(8.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF0D0000)
    ) {
        Box(
            modifier = Modifier.border(
                1.dp,
                color.copy(alpha = 0.3f),
                RoundedCornerShape(12.dp)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    label,
                    fontSize = 9.sp,
                    color = Color(0xFF888888),
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    value,
                    fontSize = 13.sp,
                    color = color,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}