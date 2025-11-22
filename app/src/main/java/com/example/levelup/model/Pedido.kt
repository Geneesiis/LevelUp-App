package com.example.levelup.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "pedidos")
@TypeConverters(Converters::class)
data class Pedido(
    @PrimaryKey
    val id: String = "",
    val clienteId: String = "",
    val productos: List<ItemCarrito> = emptyList(),
    val total: Double = 0.0,
    val fecha: Date = Date(),
    val estado: EstadoPedido = EstadoPedido.PENDIENTE
) {
    fun getFechaFormateada(): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("es", "CL"))
        return formatter.format(fecha)
    }

    fun getTotalItems(): Int {
        return productos.sumOf { it.cantidad }
    }

    fun puedeCancelarse(): Boolean {
        return estado == EstadoPedido.PENDIENTE || estado == EstadoPedido.CONFIRMADO
    }
}