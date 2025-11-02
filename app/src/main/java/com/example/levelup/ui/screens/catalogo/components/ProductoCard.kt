package com.example.levelup.ui.screens.catalogo.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.levelup.ui.screens.catalogo.models.ProductoUiModel
import com.example.levelup.ui.screens.catalogo.utils.createProductBorderBrush
import com.example.levelup.ui.screens.catalogo.utils.rememberCardScaleAnimation
import com.example.levelup.ui.theme.AppDimensions
import com.example.levelup.ui.theme.GamingColors

/**
 * Card completa del producto con imagen, info y controles
 */
@Composable
fun ProductoCard(
    productoUi: ProductoUiModel,
    onAgregar: () -> Unit,
    onRemover: () -> Unit,
    onVerDetalle: () -> Unit,
    onToggleDeseado: () -> Unit = {},
    esDeseado: Boolean = false,
    modifier: Modifier = Modifier
) {
    val scale by rememberCardScaleAnimation()

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .shadow(
                AppDimensions.elevationLarge,
                RoundedCornerShape(AppDimensions.cornerRadiusExtraLarge)
            ),
        shape = RoundedCornerShape(AppDimensions.cornerRadiusExtraLarge),
        color = GamingColors.Surface,
        onClick = onVerDetalle
    ) {
        Box(
            modifier = Modifier.border(
                2.dp,
                createProductBorderBrush(),
                RoundedCornerShape(AppDimensions.cornerRadiusExtraLarge)
            )
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Contenido principal
                ProductoCardContent(
                    productoUi = productoUi,
                    onAgregar = onAgregar,
                    onRemover = onRemover,
                    onToggleDeseado = onToggleDeseado,
                    esDeseado = esDeseado
                )

                // Footer con información adicional
                if (productoUi.disponible) {
                    ProductoCardFooter(
                        stock = productoUi.producto.stock
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductoCardContent(
    productoUi: ProductoUiModel,
    onAgregar: () -> Unit,
    onRemover: () -> Unit,
    onToggleDeseado: () -> Unit,
    esDeseado: Boolean
) {
    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppDimensions.paddingLarge)
        ) {
            ProductoImage(
                imageUrl = productoUi.producto.imagen,
                productName = productoUi.producto.nombre,
                stock = productoUi.producto.stock
            )

            Spacer(modifier = Modifier.width(AppDimensions.paddingLarge))

            Column(modifier = Modifier.weight(1f)) {
                ProductoInfo(productoUi = productoUi)

                Spacer(modifier = Modifier.height(14.dp))

                ProductoControles(
                    cantidadEnCarrito = productoUi.cantidadEnCarrito,
                    stockDisponible = productoUi.stockDisponible,
                    stock = productoUi.producto.stock,
                    onAgregar = onAgregar,
                    onRemover = onRemover
                )
            }
        }

        // Botón de favorito (corazón) - Con shadow para que no se pegue
        Surface(
            onClick = onToggleDeseado,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .size(40.dp)
                .shadow(8.dp, androidx.compose.foundation.shape.CircleShape),
            shape = androidx.compose.foundation.shape.CircleShape,
            color = if (esDeseado)
                Color(0xFFFF0055).copy(alpha = 0.9f)
            else
                Color(0xFF1A1A1A).copy(alpha = 0.95f)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        2.dp,
                        if (esDeseado) Color(0xFFFF0055) else Color(0xFF00FFAA).copy(alpha = 0.4f),
                        androidx.compose.foundation.shape.CircleShape
                    )
            ) {
                Icon(
                    if (esDeseado) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (esDeseado) "Quitar de deseados" else "Agregar a deseados",
                    tint = if (esDeseado) Color.White else Color(0xFF00FFAA),
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
private fun ProductoCardFooter(stock: Int) {
    Divider(
        color = GamingColors.SurfaceVariant,
        thickness = 1.dp
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        GamingColors.Surface,
                        GamingColors.SurfaceVariant.copy(alpha = 0.5f),
                        GamingColors.Surface
                    )
                )
            )
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FooterItem(
            icon = Icons.Default.CheckCircle,
            text = "Envío incluido"
        )

        VerticalDivider()

        FooterItem(
            icon = Icons.Default.Star,
            text = "Garantía oficial"
        )

        VerticalDivider()

        FooterItem(
            icon = Icons.Default.CheckCircle,
            text = "$stock unidades",
            isGreen = stock > 5
        )
    }
}

@Composable
private fun FooterItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    isGreen: Boolean = false
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(
                    if (isGreen) GamingColors.Success else GamingColors.Primary.copy(alpha = 0.6f),
                    androidx.compose.foundation.shape.CircleShape
                )
        )
        Text(
            text,
            fontSize = 10.sp,
            color = GamingColors.TextSecondary,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun VerticalDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(14.dp)
            .background(GamingColors.SurfaceVariant)
    )
}