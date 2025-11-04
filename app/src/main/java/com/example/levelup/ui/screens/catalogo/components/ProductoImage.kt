package com.example.levelup.ui.screens.catalogo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.levelup.ui.theme.AppDimensions
import com.example.levelup.ui.theme.GamingColors

//Imagen del producto con badges de stock
@Composable
fun ProductoImage(
    imageUrl: String,
    productName: String,
    stock: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(AppDimensions.productImageSize)
            .clip(RoundedCornerShape(AppDimensions.cornerRadiusLarge))
    ) {
        if (imageUrl.isNotEmpty()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = productName,
                modifier = Modifier
                    .fillMaxSize()
                    .background(GamingColors.SurfaceVariant)
                    .border(
                        2.dp,
                        GamingColors.Primary.copy(alpha = 0.3f),
                        RoundedCornerShape(AppDimensions.cornerRadiusLarge)
                    ),
                contentScale = ContentScale.Crop
            )
        } else {
            PlaceholderImage()
        }

        // Badges
        when {
            stock == 0 -> OutOfStockBadge()
            stock in 1..5 -> StockBadge(
                stock = stock,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
            )
        }
    }
}

@Composable
private fun PlaceholderImage() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        GamingColors.SurfaceVariant,
                        GamingColors.Surface
                    )
                )
            )
            .border(
                2.dp,
                GamingColors.Primary.copy(alpha = 0.3f),
                RoundedCornerShape(AppDimensions.cornerRadiusLarge)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            Icons.Default.ShoppingCart,
            contentDescription = null,
            tint = Color(0xFF333333),
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
private fun BoxScope.OutOfStockBadge() {
    Surface(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .padding(6.dp),
        shape = RoundedCornerShape(6.dp),
        color = Color.Black.copy(alpha = 0.8f)
    ) {
        Text(
            "AGOTADO",
            modifier = Modifier.padding(vertical = 4.dp),
            fontSize = 10.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Black,
            color = GamingColors.Error,
            letterSpacing = 1.sp,
            textAlign = TextAlign.Center
        )
    }
}
