package com.example.levelup.ui.screens.catalogo.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.levelup.ui.screens.catalogo.utils.createGlowBrush
import com.example.levelup.ui.screens.catalogo.utils.rememberGlowAnimation
import com.example.levelup.ui.theme.AppDimensions
import com.example.levelup.ui.theme.GamingColors

/**
 * Header del catálogo con logo, estadísticas y acciones
 */
@Composable
fun CatalogoHeader(
    cantidadEnCarrito: Int,
    totalProductos: Int,
    onVerCarrito: () -> Unit,
    onVerPerfil: () -> Unit,
    modifier: Modifier = Modifier
) {
    val glowAlpha = rememberGlowAnimation()

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                AppDimensions.elevationLarge,
                RoundedCornerShape(AppDimensions.cornerRadiusLarge)
            ),
        shape = RoundedCornerShape(AppDimensions.cornerRadiusLarge),
        color = GamingColors.Surface.copy(alpha = 0.98f)
    ) {
        Box(
            modifier = Modifier.border(
                2.dp,
                createGlowBrush(glowAlpha),
                RoundedCornerShape(AppDimensions.cornerRadiusLarge)
            )
        ) {
            Column(
                modifier = Modifier.padding(AppDimensions.paddingLarge)
            ) {
                HeaderTopRow(
                    cantidadEnCarrito = cantidadEnCarrito,
                    onVerCarrito = onVerCarrito,
                    onVerPerfil = onVerPerfil
                )

                Spacer(modifier = Modifier.height(12.dp))

                HeaderStatsBar(
                    totalProductos = totalProductos,
                    cantidadEnCarrito = cantidadEnCarrito,
                    glowAlpha = glowAlpha
                )
            }
        }
    }
}

@Composable
private fun HeaderTopRow(
    cantidadEnCarrito: Int,
    onVerCarrito: () -> Unit,
    onVerPerfil: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        HeaderLogo()
        HeaderActions(
            cantidadEnCarrito = cantidadEnCarrito,
            onVerCarrito = onVerCarrito,
            onVerPerfil = onVerPerfil
        )
    }
}

@Composable
private fun HeaderLogo() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = GamingColors.Primary.copy(alpha = 0.2f),
            modifier = Modifier.size(32.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = GamingColors.Primary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Column {
            Text(
                "LEVEL UP",
                fontSize = 11.sp,
                color = GamingColors.Primary.copy(alpha = 0.7f),
                fontWeight = FontWeight.Light,
                letterSpacing = 3.sp
            )
            Text(
                "GAMING STORE",
                fontSize = 20.sp,
                color = GamingColors.Primary,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp
            )
        }
    }
}

@Composable
private fun HeaderActions(
    cantidadEnCarrito: Int,
    onVerCarrito: () -> Unit,
    onVerPerfil: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        CircularIconButton(
            icon = Icons.Default.Person,
            contentDescription = "Perfil",
            onClick = onVerPerfil
        )

        CircularIconButton(
            icon = Icons.Default.ShoppingCart,
            contentDescription = "Carrito",
            onClick = onVerCarrito,
            badge = cantidadEnCarrito.takeIf { it > 0 }
        )
    }
}

@Composable
private fun HeaderStatsBar(
    totalProductos: Int,
    cantidadEnCarrito: Int,
    glowAlpha: Float
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = GamingColors.SurfaceVariant.copy(alpha = 0.6f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatsItem(
                icon = Icons.Default.ShoppingCart,
                text = "$totalProductos productos"
            )

            VerticalDivider()

            StatsItem(
                icon = Icons.Default.CheckCircle,
                text = "Carrito: $cantidadEnCarrito",
                tint = if (cantidadEnCarrito > 0)
                    GamingColors.Primary
                else
                    GamingColors.TextDisabled
            )

            VerticalDivider()

            OnlineIndicator(glowAlpha = glowAlpha)
        }
    }
}