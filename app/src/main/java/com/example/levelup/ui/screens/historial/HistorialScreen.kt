package com.example.levelup.ui.screens.historial

import androidx.compose.animation.*
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.levelup.model.EstadoPedido
import com.example.levelup.model.Pedido
import com.example.levelup.viewmodel.CarritoViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialScreen(
    viewModel: CarritoViewModel,
    onToggleDrawer: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val historial by viewModel.historial.collectAsState()

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

                        if (historial.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(12.dp))

                            // Stats
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
                                            Icons.Default.ShoppingBag,
                                            contentDescription = null,
                                            tint = Color(0xFF00AAFF).copy(alpha = 0.6f),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            "${viewModel.obtenerCantidadPedidos()} pedidos",
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
                                        Icon(
                                            Icons.Default.AccountBalanceWallet,
                                            contentDescription = null,
                                            tint = Color(0xFF00FFAA).copy(alpha = 0.6f),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            "Total: ${formatPrecioCLP(viewModel.obtenerTotalGastado())}",
                                            fontSize = 12.sp,
                                            color = Color(0xFF00FFAA),
                                            fontWeight = FontWeight.Medium
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
            if (historial.isEmpty()) {
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
                                "Tus compras aparecerán aquí",
                                color = Color(0xFF555555),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            } else {
                // Título
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.ShoppingBag,
                        contentDescription = null,
                        tint = Color(0xFF00AAFF),
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        "TUS PEDIDOS",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF00AAFF),
                        letterSpacing = 2.sp
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFF00AAFF).copy(alpha = 0.5f),
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
                    items(historial, key = { it.id }) { pedido ->
                        PedidoCard(
                            pedido = pedido,
                            onRecomprar = { viewModel.recomprarPedido(pedido) },
                            onCancelar = { viewModel.cancelarPedido(pedido.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PedidoCard(
    pedido: Pedido,
    onRecomprar: () -> Unit,
    onCancelar: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFF0D0D0D),
        onClick = { expanded = !expanded }
    ) {
        Box(
            modifier = Modifier.border(
                2.dp,
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(pedido.estado.color).copy(alpha = 0.4f),
                        Color(pedido.estado.color).copy(alpha = 0.2f)
                    )
                ),
                RoundedCornerShape(20.dp)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Header del pedido
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Pedido #${pedido.id.takeLast(8).uppercase()}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            pedido.getFechaFormateada(),
                            fontSize = 11.sp,
                            color = Color(0xFF888888)
                        )
                    }

                    // Badge de estado
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(pedido.estado.color).copy(alpha = 0.2f),
                        modifier = Modifier.border(
                            1.dp,
                            Color(pedido.estado.color).copy(alpha = 0.6f),
                            RoundedCornerShape(8.dp)
                        )
                    ) {
                        Text(
                            pedido.estado.displayName.uppercase(),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(pedido.estado.color),
                            letterSpacing = 1.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Resumen
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = null,
                            tint = Color(0xFF00FFAA).copy(alpha = 0.6f),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            "${pedido.getTotalItems()} items",
                            fontSize = 12.sp,
                            color = Color(0xFF888888)
                        )
                    }

                    Text(
                        formatPrecioCLP(pedido.total),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF00FFAA),
                        letterSpacing = 0.5.sp
                    )
                }

                // Productos expandibles
                AnimatedVisibility(
                    visible = expanded,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(16.dp))

                        Divider(color = Color(0xFF1A1A1A), thickness = 1.dp)

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            "PRODUCTOS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF888888),
                            letterSpacing = 1.5.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Lista de productos
                        pedido.productos.forEach { item ->
                            ProductoEnPedido(item)
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Botones de acción
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Botón Recomprar
                            Button(
                                onClick = onRecomprar,
                                modifier = Modifier.weight(1f).height(44.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF00FFAA).copy(alpha = 0.15f),
                                    contentColor = Color(0xFF00FFAA)
                                )
                            ) {
                                Icon(
                                    Icons.Default.Refresh,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    "RECOMPRAR",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp
                                )
                            }

                            // Botón Cancelar (solo si es posible)
                            if (pedido.puedeCancelarse()) {
                                Button(
                                    onClick = onCancelar,
                                    modifier = Modifier.weight(1f).height(44.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFFF0055).copy(alpha = 0.15f),
                                        contentColor = Color(0xFFFF0055)
                                    )
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        "CANCELAR",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 1.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // Indicador de expandir
                if (!expanded) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Ver detalles",
                            fontSize = 10.sp,
                            color = Color(0xFF666666)
                        )
                        Icon(
                            Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = Color(0xFF666666),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductoEnPedido(item: com.example.levelup.model.ItemCarrito) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1A1A1A).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Imagen
        if (item.producto.imagen != 0) {
            AsyncImage(
                model = item.producto.imagen,
                contentDescription = item.producto.nombre,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF0D0D0D)),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF0D0D0D)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.ShoppingCart,
                    contentDescription = null,
                    tint = Color(0xFF333333),
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Info
        Column(modifier = Modifier.weight(1f)) {
            Text(
                item.producto.nombre,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "x${item.cantidad}",
                    fontSize = 11.sp,
                    color = Color(0xFF00FFAA),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    formatPrecioCLP(item.producto.precio),
                    fontSize = 11.sp,
                    color = Color(0xFF888888)
                )
            }
        }

        // Subtotal
        Text(
            formatPrecioCLP(item.producto.precio * item.cantidad),
            fontSize = 14.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFF00FFAA)
        )
    }
}

private fun formatPrecioCLP(precio: Double): String {
    val formatter = NumberFormat.getNumberInstance(Locale("es", "CL"))
    return "$${formatter.format(precio)}"
}