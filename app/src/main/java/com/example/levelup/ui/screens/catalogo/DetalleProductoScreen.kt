package com.example.levelup.ui.screens.catalogo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.levelup.model.Producto
import java.text.NumberFormat
import java.util.*

@Composable
fun DetalleProductoScreen(
    producto: Producto,
    onVolverAlCatalogo: () -> Unit = {},
    onAgregarAlCarrito: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Header con botón volver
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onVolverAlCatalogo,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Volver al catálogo",
                        tint = Color(0xFF00FFAA)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "DETALLE DEL PRODUCTO",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color(0xFF00FFAA),
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp
                )
            }

            // Imagen del producto en grande
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(Color(0xFF111111)),
                contentAlignment = Alignment.Center
            ) {
                if (producto.imagen.isNotEmpty()) {
                    AsyncImage(
                        model = producto.imagen,
                        contentDescription = "Imagen de ${producto.nombre}",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        Icons.Default.ShoppingCart,
                        contentDescription = "Sin imagen",
                        tint = Color(0xFF666666),
                        modifier = Modifier.size(80.dp)
                    )
                }
            }

            // Información del producto
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Nombre del producto
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Precio
                Text(
                    text = formatPrecioCLP(producto.precio),
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color(0xFF00FFAA),
                    fontWeight = FontWeight.Black
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Descripción
                Text(
                    text = "DESCRIPCIÓN",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF00FFAA),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = producto.descripcion.ifEmpty { "No hay descripción disponible para este producto." },
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFFCCCCCC),
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Justify
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Especificaciones
                Text(
                    text = "ESPECIFICACIONES",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF00FFAA),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Stock (solo visible para ver información)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Disponibilidad:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF888888)
                    )
                    Text(
                        text = if (producto.stock > 0) "En stock" else "Agotado",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (producto.stock > 0) Color(0xFF00FFAA) else Color(0xFFFF5555),
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Unidades disponibles:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF888888)
                    )
                    Text(
                        text = producto.stock.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Botón Agregar al Carrito
                Button(
                    onClick = onAgregarAlCarrito,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00FFAA),
                        contentColor = Color.Black
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 4.dp
                    ),
                    enabled = producto.stock > 0
                ) {
                    Text(
                        "AGREGAR AL CARRITO",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
    }
}