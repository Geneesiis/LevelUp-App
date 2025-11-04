package com.example.levelup.ui.screens.catalogo.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.levelup.ui.theme.AppDimensions
import com.example.levelup.ui.theme.GamingColors

//Estado vacío del catálogo
@Composable
fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(AppDimensions.cornerRadiusLarge),
            color = GamingColors.SurfaceVariant,
            modifier = Modifier.padding(32.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(40.dp)
            ) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = null,
                    tint = GamingColors.TextDisabled,
                    modifier = Modifier.size(64.dp)
                )
                Text(
                    "NO HAY PRODUCTOS",
                    color = GamingColors.TextSecondary,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp,
                    fontSize = 18.sp
                )
                Text(
                    "Vuelve pronto para ver nuestro catálogo",
                    color = GamingColors.TextDisabled,
                    fontSize = 12.sp
                )
            }
        }
    }
}