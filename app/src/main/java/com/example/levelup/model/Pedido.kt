package com.example.levelup.model

import java.text.SimpleDateFormat
import java.util.*

//Modelo de datos para un pedido/compra realizada
data class Pedido(
    val id: String = UUID.randomUUID().toString(),
    val fecha: Long = System.currentTimeMillis(),
    val productos: List<ItemCarrito>,
    val total: Double,
    val estado: EstadoPedido = EstadoPedido.PENDIENTE,
    val metodoPago: String = "Tarjeta",
    val direccionEntrega: String = "Dirección por defecto"
) {

     //Obtiene la fecha formateada del pedido
    fun getFechaFormateada(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date(fecha))
    }

     //Obtiene la fecha corta del pedido
    fun getFechaCorta(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(Date(fecha))
    }

    //Obtiene la hora del pedido
    fun getHora(): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date(fecha))
    }

    //Obtiene el total de items en el pedido
    fun getTotalItems(): Int {
        return productos.sumOf { it.cantidad }
    }

    //Verifica si el pedido puede ser cancelado
    fun puedeCancelarse(): Boolean {
        return estado == EstadoPedido.PENDIENTE || estado == EstadoPedido.CONFIRMADO
    }
}

//Estados posibles de un pedido
enum class EstadoPedido(val displayName: String, val color: Long) {
    PENDIENTE("Pendiente", 0xFFFFAA00),
    CONFIRMADO("Confirmado", 0xFF00AAFF),
    ENVIADO("Enviado", 0xFF00FFAA),
    EN_CAMINO("En camino", 0xFF00FFAA),
    ENTREGADO("Entregado", 0xFF00FF00),
    CANCELADO("Cancelado", 0xFFFF0055);

    companion object {
        fun fromString(value: String): EstadoPedido {
            return values().find { it.name == value } ?: PENDIENTE
        }
    }
}