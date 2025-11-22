package com.example.levelup.ui.screens.carrito

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.levelup.model.Producto
import com.example.levelup.viewmodel.CarritoViewModel
import java.text.NumberFormat
import java.util.*

@Composable
fun CarritoScreen(
    viewModel: CarritoViewModel,
    usuarioId: String,
    onVolverAlCatalogo: () -> Unit = {},
    onConfirmarPago: () -> Unit = {}
) {
    val carrito by viewModel.carrito.collectAsState()

    // Agrupar productos por ID y contar cantidades
    val productosAgrupados = remember(carrito) {
        carrito.groupBy { it.id }
            .map { (_, productos) ->
                productos.first() to productos.size
            }
    }

    val total = remember(carrito) {
        carrito.sumOf { it.precio }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 24.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
        ) {
            // Header con botón volver
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onVolverAlCatalogo,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color(0xFF00FFAA)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "MI CARRITO",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color(0xFF00FFAA),
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (carrito.isEmpty()) {
                // Carrito vacío
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = "Carrito vacío",
                            modifier = Modifier.size(80.dp),
                            tint = Color(0xFF666666)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "EL CARRITO ESTÁ VACÍO",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFF888888),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Agrega productos desde el catálogo",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF666666)
                        )
                    }
                }
            } else {
                // Lista de productos en el carrito
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(productosAgrupados, key = { it.first.id }) { (producto, cantidad) ->
                        ItemCarritoCard(
                            producto = producto,
                            cantidad = cantidad,
                            onEliminar = { viewModel.eliminarDelCarrito(producto) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Resumen y botones de acción
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF111111)
                    ),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        // Información del pedido
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "TOTAL DE PRODUCTOS:",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF888888),
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = carrito.size.toString(),
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "TOTAL A PAGAR:",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF888888),
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = formatPrecioCLP(total),
                                style = MaterialTheme.typography.headlineSmall,
                                color = Color(0xFF00FFAA),
                                fontWeight = FontWeight.Black
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Botones de acción
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Botón vaciar carrito
                            OutlinedButton(
                                onClick = { viewModel.vaciarCarrito() },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color(0xFFFF5555)
                                )
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Vaciar carrito",
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "VACIAR",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            // Botón confirmar compra
                            Button(
                                onClick = {
                                    viewModel.realizarPedido(usuarioId)
                                    onConfirmarPago()
                                },
                                modifier = Modifier.weight(1.5f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF00FFAA),
                                    contentColor = Color.Black
                                ),
                                elevation = ButtonDefaults.buttonElevation(
                                    defaultElevation = 8.dp,
                                    pressedElevation = 4.dp
                                )
                            ) {
                                Text(
                                    "CONFIRMAR COMPRA",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }

                        // Advertencia de confirmación
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Warning,
                                contentDescription = "Advertencia",
                                modifier = Modifier.size(16.dp),
                                tint = Color(0xFFFFAA00)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Al confirmar, no podrás modificar tu pedido",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFFFAA00)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemCarritoCard(
    producto: Producto,
    cantidad: Int,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF111111)
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Nombre del producto
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Precio unitario con formato CLP
                Text(
                    text = "Precio: ${formatPrecioCLP(producto.precio)} c/u",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF888888)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Cantidad
                Text(
                    text = "Cantidad: $cantidad",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF00FFAA),
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Subtotal con formato CLP
                Text(
                    text = "Subtotal: ${formatPrecioCLP(producto.precio * cantidad)}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF00FFAA)
                )
            }

            // Botón eliminar producto
            IconButton(
                onClick = onEliminar,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Eliminar producto",
                    tint = Color(0xFFFF5555)
                )
            }
        }
    }
}

// Función para formatear precios en formato CLP
fun formatPrecioCLP(precio: Double): String {
    val formatter = NumberFormat.getNumberInstance(Locale("es", "CL"))
    return "$${formatter.format(precio)}"
}