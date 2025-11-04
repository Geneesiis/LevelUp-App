package com.example.levelup.ui.screens.catalogo.utils

import java.text.NumberFormat
import java.util.*

object FormatUtils {
    // Formatea un precio en formato chileno (CLP)
    fun formatPrecioCLP(precio: Double): String {
        val formatter = NumberFormat.getNumberInstance(Locale("es", "CL"))
        return "$${formatter.format(precio)}"
    }

    // Formatea cantidad con sufijo
    fun formatCantidad(cantidad: Int): String {
        return when {
            cantidad == 0 -> "Sin stock"
            cantidad == 1 -> "1 unidad"
            cantidad < 100 -> "$cantidad unidades"
            else -> "+100 unidades"
        }
    }
}