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
import com.example.levelup.model.Producto
import com.example.levelup.viewmodel.CarritoViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosAdminScreen(
    viewModel: CarritoViewModel,
    onBack: () -> Unit = {}
) {
    val productos by viewModel.productos.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var showEditDialog by remember { mutableStateOf<Producto?>(null) }

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

    val productosFiltrados = remember(productos, searchQuery) {
        if (searchQuery.isBlank()) {
            productos
        } else {
            productos.filter {
                it.nombre.contains(searchQuery, ignoreCase = true) ||
                        it.descripcion.contains(searchQuery, ignoreCase = true)
            }
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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
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

                            Text(
                                "PRODUCTOS",
                                fontSize = 18.sp,
                                color = Color(0xFFFF0055),
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Stats y buscador
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF0D0000)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatChip(
                            label = "TOTAL",
                            value = productos.size.toString(),
                            icon = Icons.Default.Inventory,
                            color = Color(0xFFFF0055)
                        )
                        StatChip(
                            label = "STOCK BAJO",
                            value = productos.count { it.stock <= 5 }.toString(),
                            icon = Icons.Default.Warning,
                            color = Color(0xFFFFAA00)
                        )
                        StatChip(
                            label = "AGOTADOS",
                            value = productos.count { it.stock == 0 }.toString(),
                            icon = Icons.Default.Error,
                            color = Color(0xFFFF5555)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Buscador
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Buscar productos...", color = Color(0xFF666666)) },
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
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de productos
            if (productosFiltrados.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.SearchOff,
                            contentDescription = null,
                            tint = Color(0xFF666666),
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            "No se encontraron productos",
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
                    items(productosFiltrados, key = { it.id }) { producto ->
                        ProductoAdminCard(
                            producto = producto,
                            onEdit = { showEditDialog = producto }
                        )
                    }
                }
            }
        }
    }

    // Dialog para editar stock
    showEditDialog?.let { producto ->
        var nuevoStock by remember { mutableStateOf(producto.stock.toString()) }

        AlertDialog(
            onDismissRequest = { showEditDialog = null },
            title = { Text("Editar Stock", color = Color.White) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        producto.nombre,
                        color = Color(0xFF888888),
                        fontSize = 14.sp
                    )
                    OutlinedTextField(
                        value = nuevoStock,
                        onValueChange = { nuevoStock = it.filter { char -> char.isDigit() } },
                        label = { Text("Stock", color = Color(0xFF888888)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFFFF0055),
                            unfocusedBorderColor = Color(0xFF666666),
                            cursorColor = Color(0xFFFF0055)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val stockInt = nuevoStock.toIntOrNull() ?: 0
                        viewModel.actualizarStockProducto(producto.id, stockInt)
                        showEditDialog = null
                    }
                ) {
                    Text("Guardar", color = Color(0xFFFF0055))
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = null }) {
                    Text("Cancelar", color = Color(0xFF888888))
                }
            },
            containerColor = Color(0xFF0D0000)
        )
    }
}

@Composable
private fun StatChip(
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
private fun ProductoAdminCard(
    producto: Producto,
    onEdit: () -> Unit
) {
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
                Color(0xFFFF0055).copy(alpha = 0.3f),
                RoundedCornerShape(14.dp)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Imagen
                if (producto.imagen.isNotEmpty()) {
                    AsyncImage(
                        model = producto.imagen,
                        contentDescription = null,
                        modifier = Modifier
                            .size(70.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF1A0000)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF1A0000)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Image,
                            null,
                            tint = Color(0xFF666666),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Info
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        producto.nombre,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        formatPrecioCLP(producto.precio),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFF0055)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StockBadge(producto.stock)
                        Text(
                            "Stock: ${producto.stock}",
                            fontSize = 11.sp,
                            color = Color(0xFF888888)
                        )
                    }
                }

                // Botón editar
                IconButton(onClick = onEdit) {
                    Icon(
                        Icons.Default.Edit,
                        "Editar stock",
                        tint = Color(0xFFFF0055),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun StockBadge(stock: Int) {
    val (color, text) = when {
        stock == 0 -> Color(0xFFFF0055) to "AGOTADO"
        stock <= 5 -> Color(0xFFFFAA00) to "BAJO"
        else -> Color(0xFF00FF00) to "OK"
    }

    Surface(
        shape = RoundedCornerShape(4.dp),
        color = color.copy(alpha = 0.2f)
    ) {
        Text(
            text,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            fontSize = 9.sp,
            fontWeight = FontWeight.Black,
            color = color,
            letterSpacing = 1.sp
        )
    }
}

private fun formatPrecioCLP(precio: Double): String {
    val formatter = NumberFormat.getNumberInstance(Locale("es", "CL"))
    return "$${formatter.format(precio)}"
}