package com.example.levelup.ui.screens.catalogo.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.levelup.ui.theme.AppDimensions
import com.example.levelup.ui.theme.GamingColors

// Controles para agregar/eliminar productos del carrito
@Composable
fun ProductoControles(
    cantidadEnCarrito: Int,
    stockDisponible: Int,
    stock: Int,
    onAgregar: () -> Unit,
    onRemover: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Usar key para recomposición eficiente
    key(cantidadEnCarrito, stockDisponible) {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (cantidadEnCarrito > 0) {
                ControlesCantidad(
                    cantidad = cantidadEnCarrito,
                    stockDisponible = stockDisponible,
                    onAgregar = onAgregar,
                    onRemover = onRemover
                )
            } else {
                BotonAgregar(
                    stock = stock,
                    onAgregar = onAgregar
                )
            }
        }
    }
}

@Composable
private fun ControlesCantidad(
    cantidad: Int,
    stockDisponible: Int,
    onAgregar: () -> Unit,
    onRemover: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Controles de cantidad
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = GamingColors.SurfaceVariant,
            shadowElevation = AppDimensions.elevationSmall,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    1.dp,
                    GamingColors.Primary.copy(alpha = 0.3f),
                    RoundedCornerShape(24.dp)
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
                RoundActionButton(
                    icon = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = GamingColors.Error,
                    onClick = onRemover
                )

                CantidadDisplay(cantidad = cantidad)

                RoundActionButton(
                    icon = Icons.Default.Add,
                    contentDescription = "Agregar",
                    tint = if (stockDisponible > 0)
                        GamingColors.Primary
                    else
                        GamingColors.TextDisabled,
                    onClick = onAgregar,
                    enabled = stockDisponible > 0
                )
            }
        }

        // Indicador de stock
        StockIndicator(stockDisponible = stockDisponible)
    }
}

@Composable
private fun CantidadDisplay(cantidad: Int) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = GamingColors.Primary.copy(alpha = 0.2f)
    ) {
        Text(
            text = cantidad.toString(),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
            fontWeight = FontWeight.Black,
            color = GamingColors.Primary,
            fontSize = 18.sp
        )
    }
}

@Composable
private fun StockIndicator(stockDisponible: Int) {
    when {
        stockDisponible == 0 -> {
            StatusChip(
                text = "LÍMITE ALCANZADO",
                color = GamingColors.Error,
                icon = Icons.Default.Close
            )
        }
        stockDisponible <= 3 -> {
            StatusChip(
                text = "¡QUEDAN $stockDisponible!",
                color = GamingColors.Warning,
                icon = Icons.Default.Warning
            )
        }
    }
}

@Composable
private fun BotonAgregar(
    stock: Int,
    onAgregar: () -> Unit
) {
    // Usar Surface en lugar de Button para mejor control y sin lag
    Surface(
        onClick = { if (stock > 0) onAgregar() },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(AppDimensions.cornerRadiusMedium),
        color = if (stock > 0) GamingColors.Primary else Color(0xFF333333),
        shadowElevation = if (stock > 0) AppDimensions.elevationMedium else 0.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    if (stock > 0) Icons.Default.Add else Icons.Default.Close,
                    contentDescription = null,
                    tint = if (stock > 0) Color.Black else GamingColors.TextDisabled,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    if (stock > 0) "AGREGAR" else "AGOTADO",
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.2.sp,
                    fontSize = 14.sp,
                    color = if (stock > 0) Color.Black else GamingColors.TextDisabled
                )
            }
        }
    }
}