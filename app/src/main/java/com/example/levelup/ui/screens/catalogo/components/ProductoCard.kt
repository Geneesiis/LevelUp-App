package com.example.levelup.ui.screens.catalogo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
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
    onVerDetalle: () -> Unit, // CAMBIO: sin parámetro Int
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
        onClick = onVerDetalle // CAMBIO: llamada directa sin parámetro
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
                    onRemover = onRemover
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
    onRemover: () -> Unit
) {
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