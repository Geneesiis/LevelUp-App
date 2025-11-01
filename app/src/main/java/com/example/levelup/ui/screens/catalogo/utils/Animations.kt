package com.example.levelup.ui.screens.catalogo.utils

import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Brush
import com.example.levelup.ui.theme.GamingColors

/**
 * Animación de brillo pulsante
 */
@Composable
fun rememberGlowAnimation(): Float {
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    return infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    ).value
}

/**
 * Animación de escala para cards
 */
@Composable
fun rememberCardScaleAnimation(): State<Float> {
    return animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "cardScale"
    )
}

/**
 * Crea un brush con efecto de brillo
 */
fun createGlowBrush(alpha: Float): Brush {
    return Brush.linearGradient(
        colors = listOf(
            GamingColors.Primary.copy(alpha = alpha),
            GamingColors.Secondary.copy(alpha = alpha * 0.8f),
            GamingColors.Tertiary.copy(alpha = alpha * 0.6f),
            GamingColors.Primary.copy(alpha = alpha)
        )
    )
}

/**
 * Crea un brush para bordes de productos
 */
fun createProductBorderBrush(): Brush {
    return Brush.horizontalGradient(
        colors = listOf(
            GamingColors.Primary.copy(alpha = 0.4f),
            GamingColors.Secondary.copy(alpha = 0.3f),
            GamingColors.Tertiary.copy(alpha = 0.2f),
            GamingColors.Primary.copy(alpha = 0.4f)
        )
    )
}