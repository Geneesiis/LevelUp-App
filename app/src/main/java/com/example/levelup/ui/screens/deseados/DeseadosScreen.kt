package com.example.levelup.ui.screens.deseados

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
import androidx.navigation.NavController
import com.example.levelup.viewmodel.CarritoViewModel
import com.example.levelup.ui.screens.catalogo.components.ProductoCard
import com.example.levelup.ui.screens.catalogo.models.ProductoUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeseadosScreen(
    viewModel: CarritoViewModel,
    navController: NavController,
    onVerCarrito: () -> Unit = {},
    onToggleDrawer: () -> Unit = {}
) {
    val deseados by viewModel.deseados.collectAsState()
    val carrito by viewModel.carrito.collectAsState()

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
                                    Color(0xFFFF0055).copy(alpha = glowAlpha),
                                    Color(0xFFFF0055).copy(alpha = glowAlpha * 0.8f),
                                    Color(0xFFAA00FF).copy(alpha = glowAlpha * 0.6f),
                                    Color(0xFFFF0055).copy(alpha = glowAlpha)
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
                                            Color(0xFFFF0055).copy(alpha = 0.4f),
                                            CircleShape
                                        )
                                    ) {
                                        Icon(
                                            Icons.Default.Menu,
                                            contentDescription = "Menú",
                                            tint = Color(0xFFFF0055),
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }

                                // Logo de deseados
                                Column {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Surface(
                                            shape = CircleShape,
                                            color = Color(0xFFFF0055).copy(alpha = 0.2f),
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    Icons.Default.Favorite,
                                                    contentDescription = null,
                                                    tint = Color(0xFFFF0055),
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }
                                        }

                                        Column {
                                            Text(
                                                "LISTA DE",
                                                style = MaterialTheme.typography.titleSmall,
                                                fontSize = 11.sp,
                                                color = Color(0xFFFF0055).copy(alpha = 0.7f),
                                                fontWeight = FontWeight.Light,
                                                letterSpacing = 3.sp
                                            )
                                            Text(
                                                "DESEADOS",
                                                style = MaterialTheme.typography.headlineSmall,
                                                fontSize = 20.sp,
                                                color = Color(0xFFFF0055),
                                                fontWeight = FontWeight.Black,
                                                letterSpacing = 2.sp
                                            )
                                        }
                                    }
                                }
                            }

                            // Botón del carrito
                            Surface(
                                modifier = Modifier.size(48.dp),
                                shape = CircleShape,
                                color = Color(0xFF1A1A1A),
                                onClick = onVerCarrito
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.border(
                                        1.dp,
                                        Color(0xFF00FFAA).copy(alpha = 0.4f),
                                        CircleShape
                                    )
                                ) {
                                    BadgedBox(
                                        badge = {
                                            if (carrito.isNotEmpty()) {
                                                Badge(
                                                    containerColor = Color(0xFFFF0055),
                                                    modifier = Modifier.shadow(6.dp, CircleShape)
                                                ) {
                                                    Text(
                                                        carrito.sumOf { it.cantidad }.toString(),
                                                        color = Color.White,
                                                        fontWeight = FontWeight.Black,
                                                        fontSize = 11.sp
                                                    )
                                                }
                                            }
                                        }
                                    ) {
                                        Icon(
                                            Icons.Default.ShoppingCart,
                                            contentDescription = "Carrito",
                                            tint = Color(0xFF00FFAA),
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Barra de stats
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF1A1A1A).copy(alpha = 0.6f)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Favorite,
                                        contentDescription = null,
                                        tint = Color(0xFFFF0055).copy(alpha = 0.6f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        "${deseados.size} productos",
                                        fontSize = 12.sp,
                                        color = Color(0xFF888888),
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .width(1.dp)
                                        .height(16.dp)
                                        .background(Color(0xFF333333))
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    val totalPrecio = deseados.sumOf { it.precio }
                                    Icon(
                                        Icons.Default.AccountBalanceWallet,
                                        contentDescription = null,
                                        tint = Color(0xFF00FFAA).copy(alpha = 0.6f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        "Total: ${String.format("%,.0f", totalPrecio)} CLP",
                                        fontSize = 12.sp,
                                        color = Color(0xFF00FFAA),
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                if (deseados.isNotEmpty()) {
                                    Box(
                                        modifier = Modifier
                                            .width(1.dp)
                                            .height(16.dp)
                                            .background(Color(0xFF333333))
                                    )

                                    TextButton(
                                        onClick = { viewModel.vaciarDeseados() },
                                        colors = ButtonDefaults.textButtonColors(
                                            contentColor = Color(0xFFFF0055)
                                        ),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = null,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            "Vaciar",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Contenido
            if (deseados.isEmpty()) {
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
                                Icons.Default.FavoriteBorder,
                                contentDescription = null,
                                tint = Color(0xFF666666),
                                modifier = Modifier.size(64.dp)
                            )
                            Text(
                                "LISTA VACÍA",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color(0xFF888888),
                                fontWeight = FontWeight.Black,
                                letterSpacing = 2.sp
                            )
                            Text(
                                "Agrega productos a tu lista de deseados",
                                color = Color(0xFF555555),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            } else {
                // Título de sección
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = null,
                        tint = Color(0xFFFF0055),
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        "TUS PRODUCTOS FAVORITOS",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFF0055),
                        letterSpacing = 2.sp
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFFFF0055).copy(alpha = 0.5f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(deseados, key = { it.id }) { producto ->
                        val cantidadEnCarrito = carrito.find { it.producto.id == producto.id }?.cantidad ?: 0
                        val productoUi = ProductoUiModel(
                            producto = producto,
                            cantidadEnCarrito = cantidadEnCarrito,
                            stockDisponible = producto.stock - cantidadEnCarrito,
                            disponible = producto.stock > 0
                        )

                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = Color.Transparent
                        ) {
                            Column {
                                ProductoCard(
                                    productoUi = productoUi,
                                    onAgregar = { viewModel.agregarAlCarrito(producto) },
                                    onRemover = { viewModel.removerDelCarrito(producto) },
                                    onVerDetalle = {
                                        navController.navigate("detalle_producto/${producto.id}")
                                    },
                                    onToggleDeseado = { viewModel.toggleDeseado(producto) },
                                    esDeseado = true
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // Botón especial: Mover al carrito
                                Button(
                                    onClick = { viewModel.moverDeseadoAlCarrito(producto) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF00FFAA).copy(alpha = 0.15f),
                                        contentColor = Color(0xFF00FFAA)
                                    ),
                                    enabled = producto.stock > 0
                                ) {
                                    Icon(
                                        Icons.Default.AddShoppingCart,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "MOVER AL CARRITO Y QUITAR DE DESEADOS",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        letterSpacing = 1.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}