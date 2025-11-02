package com.example.levelup.ui.screens.catalogo.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

        // Botón de favorito mejorado con animaciones
        FavoriteButton(
            esDeseado = esDeseado,
            onToggleDeseado = onToggleDeseado,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        )
    }
}

@Composable
private fun FavoriteButton(
    esDeseado: Boolean,
    onToggleDeseado: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Estado para controlar el debounce
    var isProcessing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Estado local para animación inmediata
    var localEsDeseado by remember(esDeseado) { mutableStateOf(esDeseado) }

    // Animación de escala para el bounce
    val scale by animateFloatAsState(
        targetValue = if (localEsDeseado) 1f else 0.9f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    // Animación de rotación para efecto de "pop"
    val rotation by animateFloatAsState(
        targetValue = if (localEsDeseado) 360f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "rotation"
    )

    // Animación de color
    val backgroundColor by animateColorAsState(
        targetValue = if (localEsDeseado)
            Color(0xFFFF0055).copy(alpha = 0.9f)
        else
            Color(0xFF1A1A1A).copy(alpha = 0.95f),
        animationSpec = tween(durationMillis = 200),
        label = "backgroundColor"
    )

    val borderColor by animateColorAsState(
        targetValue = if (localEsDeseado)
            Color(0xFFFF0055)
        else
            Color(0xFF00FFAA).copy(alpha = 0.4f),
        animationSpec = tween(durationMillis = 200),
        label = "borderColor"
    )

    val iconTint by animateColorAsState(
        targetValue = if (localEsDeseado)
            Color.White
        else
            Color(0xFF00FFAA),
        animationSpec = tween(durationMillis = 200),
        label = "iconTint"
    )

    Surface(
        onClick = {
            if (!isProcessing) {
                isProcessing = true
                localEsDeseado = !localEsDeseado

                scope.launch {
                    onToggleDeseado()
                    delay(300) // Debounce de 300ms
                    isProcessing = false
                }
            }
        },
        modifier = modifier
            .size(40.dp)
            .scale(scale)
            .shadow(12.dp, CircleShape),
        shape = CircleShape,
        color = backgroundColor
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 2.dp,
                    color = borderColor,
                    shape = CircleShape
                )
        ) {
            // Animación crossfade entre los íconos
            AnimatedContent(
                targetState = localEsDeseado,
                transitionSpec = {
                    fadeIn(animationSpec = tween(200)) +
                            scaleIn(
                                initialScale = 0.8f,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessMedium
                                )
                            ) togetherWith
                            fadeOut(animationSpec = tween(100)) +
                            scaleOut(targetScale = 0.8f, animationSpec = tween(100))
                },
                label = "iconCrossfade"
            ) { deseado ->
                Icon(
                    imageVector = if (deseado) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (deseado) "Quitar de deseados" else "Agregar a deseados",
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }

    // Efecto de partículas cuando se marca como favorito (opcional)
    if (localEsDeseado) {
        LaunchedEffect(localEsDeseado) {
            // Aquí podrías agregar efectos adicionales como vibración
            // HapticFeedback si lo necesitas
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
                    CircleShape
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