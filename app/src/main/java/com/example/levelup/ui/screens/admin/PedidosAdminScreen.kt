package com.example.levelup.ui.screens.admin

import androidx.compose.animation.AnimatedVisibility
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
import com.example.levelup.model.EstadoPedido
import com.example.levelup.model.Pedido
import com.example.levelup.viewmodel.CarritoViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidosAdminScreen(
    viewModel: CarritoViewModel,
    onBack: () -> Unit = {}
) {
    val historial by viewModel.historial.collectAsState()
    var filtroEstado by remember { mutableStateOf<EstadoPedido?>(null) }

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

    val pedidosFiltrados = remember(historial, filtroEstado) {
        if (filtroEstado == null) {
            historial.sortedByDescending { it.fecha }
        } else {
            historial.filter { it.estado == filtroEstado }.sortedByDescending { it.fecha }
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
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
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
                                    "GESTIÓN DE PEDIDOS",
                                    fontSize = 18.sp,
                                    color = Color(0xFFFF0055),
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Stats
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            PedidoStatChip(
                                label = "TOTAL",
                                value = historial.size.toString(),
                                color = Color(0xFFFF0055)
                            )
                            PedidoStatChip(
                                label = "PENDIENTES",
                                value = historial.count { it.estado == EstadoPedido.PENDIENTE }.toString(),
                                color = Color(0xFFFFAA00)
                            )
                            PedidoStatChip(
                                label = "ENVIADOS",
                                value = historial.count { it.estado == EstadoPedido.ENVIADO }.toString(),
                                color = Color(0xFF00AAFF)
                            )
                            PedidoStatChip(
                                label = "ENTREGADOS",
                                value = historial.count { it.estado == EstadoPedido.ENTREGADO }.toString(),
                                color = Color(0xFF00FF00)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Filtros
            ScrollableTabRow(
                selectedTabIndex = if (filtroEstado == null) 0 else EstadoPedido.values().indexOf(filtroEstado) + 1,
                containerColor = Color.Transparent,
                contentColor = Color(0xFFFF0055),
                edgePadding = 0.dp
            ) {
                Tab(
                    selected = filtroEstado == null,
                    onClick = { filtroEstado = null },
                    text = { Text("TODOS", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                )
                EstadoPedido.values().forEach { estado ->
                    Tab(
                        selected = filtroEstado == estado,
                        onClick = { filtroEstado = estado },
                        text = { Text(estado.displayName.uppercase(), fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de pedidos
            if (pedidosFiltrados.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.ShoppingBag,
                            contentDescription = null,
                            tint = Color(0xFF666666),
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            "No hay pedidos",
                            color = Color(0xFF888888),
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(pedidosFiltrados, key = { it.id }) { pedido ->
                        PedidoAdminCard(
                            pedido = pedido,
                            onChangeStatus = { nuevoEstado ->
                                viewModel.actualizarEstadoPedido(pedido.id, nuevoEstado)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PedidoStatChip(
    label: String,
    value: String,
    color: Color
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.15f),
        modifier = Modifier.border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = color
            )
            Text(
                label,
                fontSize = 8.sp,
                color = Color(0xFF888888),
                letterSpacing = 1.sp
            )
        }
    }
}

@Composable
private fun PedidoAdminCard(
    pedido: Pedido,
    onChangeStatus: (EstadoPedido) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var showStatusDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFF0D0000),
        onClick = { expanded = !expanded }
    ) {
        Box(
            modifier = Modifier.border(
                1.dp,
                Color(pedido.estado.color).copy(alpha = 0.3f),
                RoundedCornerShape(14.dp)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "PEDIDO #${pedido.id.takeLast(8).uppercase()}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            pedido.getFechaFormateada(),
                            fontSize = 10.sp,
                            color = Color(0xFF888888)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "${pedido.getTotalItems()} items • ${formatPrecioCLP(pedido.total)}",
                            fontSize = 11.sp,
                            color = Color(0xFFFF0055),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(pedido.estado.color).copy(alpha = 0.2f),
                        onClick = { showStatusDialog = true }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                pedido.estado.displayName.uppercase(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(pedido.estado.color),
                                letterSpacing = 1.sp
                            )
                            Icon(
                                Icons.Default.Edit,
                                null,
                                tint = Color(pedido.estado.color),
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }

                AnimatedVisibility(visible = expanded) {
                    Column {
                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = Color(0xFF333333))
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            "PRODUCTOS:",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF888888),
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        pedido.productos.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "${item.cantidad}x ${item.producto.nombre}",
                                    fontSize = 11.sp,
                                    color = Color.White,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    formatPrecioCLP(item.producto.precio * item.cantidad),
                                    fontSize = 11.sp,
                                    color = Color(0xFFFF0055),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Dialog para cambiar estado
    if (showStatusDialog) {
        AlertDialog(
            onDismissRequest = { showStatusDialog = false },
            title = { Text("Cambiar Estado", color = Color.White) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    EstadoPedido.values().forEach { estado ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            color = if (pedido.estado == estado)
                                Color(estado.color).copy(alpha = 0.3f)
                            else
                                Color(0xFF1A1A1A),
                            onClick = {
                                onChangeStatus(estado)
                                showStatusDialog = false
                            }
                        ) {
                            Text(
                                estado.displayName,
                                modifier = Modifier.padding(12.dp),
                                color = Color(estado.color),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showStatusDialog = false }) {
                    Text("Cancelar", color = Color(0xFFFF0055))
                }
            },
            containerColor = Color(0xFF0D0000)
        )
    }
}

private fun formatPrecioCLP(precio: Double): String {
    val formatter = NumberFormat.getNumberInstance(Locale("es", "CL"))
    return "$${formatter.format(precio)}"
}