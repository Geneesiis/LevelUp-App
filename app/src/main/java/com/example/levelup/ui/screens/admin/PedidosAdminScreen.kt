package com.example.levelup.ui.screens.admin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
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
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidosAdminScreen(
    viewModel: CarritoViewModel,
    onBack: () -> Unit = {}
) {
    val historial by viewModel.historialPedidos.collectAsState()
    var filtroEstado by remember { mutableStateOf<EstadoPedido?>(null) }

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
                    colors = listOf(Color(0xFF000000), Color(0xFF0A0000), Color(0xFF000000))
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.White)
                }
                Text("Gestión de Pedidos", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.width(48.dp))
            }

            // Filtros
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                EstadoPedido.values().forEach { estado ->
                    FilterChip(
                        selected = filtroEstado == estado,
                        onClick = { filtroEstado = if (filtroEstado == estado) null else estado },
                        label = { Text(estado.displayName) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(estado.color),
                            selectedLabelColor = Color.Black
                        )
                    )
                }
            }

            // Lista de pedidos
            if (pedidosFiltrados.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay pedidos que coincidan.", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(pedidosFiltrados, key = { it.id }) { pedido ->
                        PedidoAdminCard(
                            pedido = pedido,
                            onChangeStatus = { nuevoEstadoEnum ->
                                viewModel.actualizarEstadoPedido(pedido.id, nuevoEstadoEnum.name)
                            }
                        )
                    }
                }
            }
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

    val estadoEnum = pedido.estado

    Surface(
        modifier = Modifier.fillMaxWidth().shadow(8.dp, RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFF1A1A1A),
        onClick = { expanded = !expanded }
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            // Fila Superior
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Pedido #${pedido.id.take(6).uppercase()}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                    Spacer(Modifier.height(4.dp))
                    Text(SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(pedido.fecha), fontSize = 10.sp, color = Color.Gray)
                }
                IconButton(onClick = { showStatusDialog = true }) {
                    Icon(Icons.Default.Edit, contentDescription = "Cambiar Estado", tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Fila Inferior
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("${pedido.productos.sumOf { it.cantidad }} items • ${formatPrecioCLP(pedido.total)}", fontSize = 12.sp, color = Color(0xFF00C853), fontWeight = FontWeight.Bold)
                Box(
                    modifier = Modifier.background(Color(estadoEnum.color).copy(alpha = 0.2f), CircleShape).border(1.dp, Color(estadoEnum.color), CircleShape).padding(horizontal = 12.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(estadoEnum.displayName.uppercase(), color = Color(estadoEnum.color), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
            
            // Sección Desplegable
            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    Divider(color = Color.Gray.copy(alpha = 0.3f))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Productos:", fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    pedido.productos.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("(${item.cantidad}x) ${item.producto.nombre}", color = Color.LightGray, fontSize = 14.sp)
                            Text(formatPrecioCLP(item.producto.precio * item.cantidad), color = Color.LightGray, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }

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
                            color = if (estadoEnum == estado) Color(estado.color).copy(alpha = 0.3f) else Color(0xFF2C2C2C),
                            onClick = {
                                onChangeStatus(estado)
                                showStatusDialog = false
                            },
                            border = BorderStroke(1.dp, if (estadoEnum == estado) Color(estado.color) else Color.Transparent)
                        ) {
                            Text(text = estado.displayName, modifier = Modifier.padding(16.dp), color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            },
            confirmButton = { TextButton(onClick = { showStatusDialog = false }) { Text("Cancelar", color = Color(0xFF00C853)) } },
            containerColor = Color(0xFF222222)
        )
    }
}

private fun formatPrecioCLP(precio: Double): String {
    return NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply {
        maximumFractionDigits = 0
    }.format(precio)
}
