package com.example.levelup.ui.screens.catalogo.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.levelup.model.Producto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoCard(
    producto: Producto,
    isDeseado: Boolean,
    onProductoClick: () -> Unit,
    onAddToDeseados: () -> Unit
) {
    Card(
        onClick = onProductoClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2C))
    ) {
        Column {
            ProductoImage(
                imageUrl = producto.imagen,
                isDeseado = isDeseado,
                onToggleDeseado = onAddToDeseados
            )
            ProductoInfo(
                nombre = producto.nombre,
                precio = producto.precio
            )
        }
    }
}