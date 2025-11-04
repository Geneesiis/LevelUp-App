package com.example.levelup.ui.screens.catalogo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

//Fondo degradado para el catálogo
@Composable
fun CatalogoBackground(
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF000000),
                        Color(0xFF0A0A0A),
                        Color(0xFF050505),
                        Color(0xFF000000)
                    )
                )
            )
    ) {
        content()
    }
}
