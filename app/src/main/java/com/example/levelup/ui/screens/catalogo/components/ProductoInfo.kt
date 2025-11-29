package com.example.levelup.ui.screens.catalogo.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.levelup.utils.formatPrecioCLP

@Composable
fun ProductoInfo(nombre: String, precio: Double) {
    Column(modifier = Modifier.padding(12.dp)) {
        Text(
            text = nombre,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            maxLines = 2,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = formatPrecioCLP(precio),
            fontSize = 14.sp,
            color = Color(0xFF00C853)
        )
    }
}
