package com.example.levelup.ui.screens.catalogo.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.levelup.ui.screens.catalogo.models.ProductoUiModel

/**
 * Información del producto (nombre, precio, categoría)
 */
@Composable
fun ProductoInfo(
    productoUi: ProductoUiModel,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        CategoryChip(text = "TECH GEAR")

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = productoUi.producto.nombre.uppercase(),
            fontWeight = FontWeight.Black,
            color = Color.White,
            letterSpacing = 0.5.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 18.sp,
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        PriceChip(
            precio = productoUi.precioFormateado,
            icon = Icons.Default.Star
        )
    }
}