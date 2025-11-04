package com.example.levelup.ui.screens.catalogo.models

import com.example.levelup.model.Producto
import com.example.levelup.model.ItemCarrito

//Estado de UI para la pantalla de catálogo
data class CatalogoUiState(
    val productos: List<ProductoUiModel> = emptyList(),
    val cargando: Boolean = false,
    val error: String? = null,
    val cantidadTotalEnCarrito: Int = 0
)

// Modelo de UI para un producto con información calculada
data class ProductoUiModel(
    val producto: Producto,
    val cantidadEnCarrito: Int = 0,
    val stockDisponible: Int = 0,
    val disponible: Boolean = true,
    val esUltimaUnidad: Boolean = false,
    val precioFormateado: String = ""
)

// Convierte lista de productos y carrito a modelos de UI
fun createProductosUiModels(
    productos: List<Producto>,
    carrito: List<ItemCarrito>
): List<ProductoUiModel> {
    return productos.map { producto ->
        val cantidadEnCarrito = carrito.find { it.producto.id == producto.id }?.cantidad ?: 0
        val stockDisponible = producto.stock - cantidadEnCarrito

        ProductoUiModel(
            producto = producto,
            cantidadEnCarrito = cantidadEnCarrito,
            stockDisponible = stockDisponible,
            disponible = producto.stock > 0,
            esUltimaUnidad = stockDisponible in 1..3,
            precioFormateado = formatearPrecio(producto.precio)
        )
    }
}

private fun formatearPrecio(precio: Double): String {
    return "$${String.format("%,.0f", precio).replace(",", ".")}"
}
