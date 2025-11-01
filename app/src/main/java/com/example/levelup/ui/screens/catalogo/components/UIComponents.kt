package com.example.levelup.ui.screens.catalogo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.levelup.ui.theme.AppDimensions
import com.example.levelup.ui.theme.AppTypography
import com.example.levelup.ui.theme.GamingColors

/**
 * Botón circular con icono (perfil, carrito, etc)
 */
@Composable
fun CircularIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    badge: Int? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.size(AppDimensions.iconSizeLarge),
        shape = CircleShape,
        color = GamingColors.SurfaceVariant,
        onClick = onClick
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.border(
                1.dp,
                GamingColors.Primary.copy(alpha = 0.4f),
                CircleShape
            )
        ) {
            BadgedBox(
                badge = {
                    if (badge != null && badge > 0) {
                        Badge(
                            containerColor = GamingColors.Error,
                            modifier = Modifier.scale(1.15f)
                        ) {
                            Text(
                                badge.toString(),
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            ) {
                Icon(
                    icon,
                    contentDescription = contentDescription,
                    tint = GamingColors.Primary,
                    modifier = Modifier.size(AppDimensions.iconSizeMedium)
                )
            }
        }
    }
}

/**
 * Item de estadística (productos, carrito, etc)
 */
@Composable
fun StatsItem(
    icon: ImageVector,
    text: String,
    tint: Color = GamingColors.Primary.copy(alpha = 0.6f),
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(AppDimensions.iconSizeSmall)
        )
        Text(
            text,
            fontSize = AppTypography.bodySize,
            color = GamingColors.TextSecondary,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * Divisor vertical
 */
@Composable
fun VerticalDivider(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(1.dp)
            .height(AppDimensions.iconSizeSmall)
            .background(GamingColors.Border)
    )
}

/**
 * Indicador de estado online
 */
@Composable
fun OnlineIndicator(
    glowAlpha: Float,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(
                    GamingColors.Success.copy(alpha = glowAlpha),
                    CircleShape
                )
        )
        Text(
            "ONLINE",
            fontSize = 11.sp,
            color = GamingColors.Success.copy(alpha = 0.8f),
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}

/**
 * Chip de categoría
 */
@Composable
fun CategoryChip(
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        color = GamingColors.SurfaceVariant
    ) {
        Text(
            text.uppercase(),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = GamingColors.Primary.copy(alpha = 0.7f),
            letterSpacing = 1.sp
        )
    }
}

/**
 * Chip de precio con icono
 */
@Composable
fun PriceChip(
    precio: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = GamingColors.Primary.copy(alpha = 0.15f)
    ) {
        Box(
            modifier = Modifier.border(
                1.dp,
                GamingColors.Primary.copy(alpha = 0.4f),
                RoundedCornerShape(10.dp)
            )
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = GamingColors.Primary,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = precio,
                    color = GamingColors.Primary,
                    fontWeight = FontWeight.Black,
                    fontSize = AppTypography.priceSize
                )
            }
        }
    }
}

/**
 * Badge de stock con icono
 */
@Composable
fun StockBadge(
    stock: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        color = GamingColors.Error,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                "x$stock",
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                letterSpacing = 0.5.sp
            )
        }
    }
}

/**
 * Chip de estado (LÍMITE, ÚLTIMAS, etc)
 */
@Composable
fun StatusChip(
    text: String,
    color: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.2f)
    ) {
        Box(
            modifier = Modifier.border(
                1.dp,
                color.copy(alpha = 0.5f),
                RoundedCornerShape(8.dp)
            )
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = color,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

/**
 * Botón de acción circular (agregar, eliminar)
 */
@Composable
fun RoundActionButton(
    icon: ImageVector,
    contentDescription: String,
    tint: Color,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = tint.copy(alpha = if (enabled) 0.2f else 0.1f),
        onClick = onClick,
        enabled = enabled
    ) {
        Box(
            modifier = Modifier.size(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = contentDescription,
                tint = if (enabled) tint else tint.copy(alpha = 0.5f),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}