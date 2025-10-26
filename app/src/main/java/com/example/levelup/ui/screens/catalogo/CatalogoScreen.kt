package com.example.levelup.ui.screens.catalogo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.levelup.viewmodel.CarritoViewModel
import java.text.NumberFormat
import java.util.*

@Composable
fun CatalogoScreen(
    viewModel: CarritoViewModel,
    onVerCarrito: () -> Unit = {},
    onVerPerfil: () -> Unit = {},
    onConfirmarPago: () -> Unit = {}
) {
    val productos by viewModel.productos.collectAsState()
    val cargando by viewModel.cargando.collectAsState()
    val carrito by viewModel.carrito.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 24.dp, // Espacio para la barra de notificaciones
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
        ) {
            // Header con temática cyber
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "CATÁLOGO DE PRODUCTOS",
                    style = MaterialTheme.typography.headlineSmall,
                    fontSize = 18.sp,
                    color = Color(0xFF00FFAA),
                    fontWeight = FontWeight.Black
                )

                Row {
                    // Botón Perfil
                    IconButton(
                        onClick = onVerPerfil,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Perfil",
                            tint = Color(0xFF00FFAA)
                        )
                    }

                    // Botón del carrito con badge
                    BadgedBox(
                        badge = {
                            if (carrito.isNotEmpty()) {
                                Badge(
                                    containerColor = Color(0xFFFF5555)
                                ) {
                                    Text(
                                        carrito.sumOf { it.cantidad }.toString(),
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    ) {
                        IconButton(
                            onClick = onVerCarrito,
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                Icons.Default.ShoppingCart,
                                contentDescription = "Carrito",
                                tint = Color(0xFF00FFAA)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (cargando) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFF00FFAA)
                    )
                }
            } else {
                if (productos.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "No hay productos disponibles",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFF888888)
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(productos) { producto ->
                            ProductoItem(
                                producto = producto,
                                onAgregar = { viewModel.agregarAlCarrito(producto) },
                                onEliminar = { viewModel.removerDelCarrito(producto) },
                                cantidadEnCarrito = carrito.find { it.producto.id == producto.id }?.cantidad ?: 0
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductoItem(
    producto: com.example.levelup.model.Producto,
    onAgregar: () -> Unit,
    onEliminar: () -> Unit,
    cantidadEnCarrito: Int
) {
    // Calcular stock disponible
    val stockDisponible = producto.stock - cantidadEnCarrito

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
                .padding(16.dp)
        ) {
            // Imagen del producto
            if (producto.imagen.isNotEmpty()) {
                AsyncImage(
                    model = producto.imagen,
                    contentDescription = "Imagen de ${producto.nombre}",
                    modifier = Modifier
                        .size(80.dp)
                        .padding(end = 16.dp),
                    onError = {
                        // Si hay error cargando la imagen, mostrar un placeholder
                    }
                )
            } else {
                // Placeholder si no hay imagen
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .padding(end = 16.dp)
                        .background(Color(0xFF333333)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.ShoppingCart,
                        contentDescription = "Sin imagen",
                        tint = Color(0xFF666666),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            // Información del producto
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Nombre del producto
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Precio con formato CLP
                Text(
                    text = formatPrecioCLP(producto.precio),
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color(0xFF00FFAA),
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Controles de cantidad
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Espacio vacío donde antes estaba el stock
                    Spacer(modifier = Modifier.width(1.dp))

                    // Controles de cantidad
                    if (cantidadEnCarrito > 0) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Botón eliminar - SIEMPRE habilitado si hay items
                            IconButton(
                                onClick = onEliminar,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Eliminar",
                                    tint = Color(0xFFFF5555)
                                )
                            }

                            // Cantidad
                            Text(
                                text = cantidadEnCarrito.toString(),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF00FFAA),
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )

                            // Botón agregar - solo habilitado si hay stock disponible
                            IconButton(
                                onClick = onAgregar,
                                modifier = Modifier.size(36.dp),
                                enabled = stockDisponible > 0
                            ) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = "Agregar",
                                    tint = if (stockDisponible > 0) Color(0xFF00FFAA) else Color(0xFF666666)
                                )
                            }
                        }
                    } else {
                        // Botón Agregar al carrito
                        Button(
                            onClick = onAgregar,
                            modifier = Modifier
                                .height(36.dp)
                                .width(120.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF00FFAA),
                                contentColor = Color.Black
                            ),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 4.dp,
                                pressedElevation = 2.dp
                            ),
                            enabled = producto.stock > 0
                        ) {
                            Text(
                                "AGREGAR",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Indicador de disponibilidad
                if (producto.stock == 0) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Producto no disponible",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFFF5555)
                    )
                } else if (stockDisponible <= 3 && stockDisponible > 0) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Últimas unidades",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFFFAA00)
                    )
                } else if (cantidadEnCarrito > 0 && stockDisponible == 0) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Límite de stock alcanzado",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFFF5555)
                    )
                }
            }
        }
    }
}

// Función para formatear precios en formato CLP
fun formatPrecioCLP(precio: Double): String {
    val formatter = NumberFormat.getNumberInstance(Locale("es", "CL"))
    return "$${formatter.format(precio)}"
}