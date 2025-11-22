package com.example.levelup.repository

import com.example.levelup.data.local.PedidoDao
import com.example.levelup.model.Pedido
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class PedidoRepository(private val pedidoDao: PedidoDao) {

    fun getPedidos(): Flow<List<Pedido>> {
        return pedidoDao.getAllPedidos()
    }

    fun getPedidosPorUsuario(userId: String): Flow<List<Pedido>> {
        return pedidoDao.getPedidosByUsuario(userId)
    }

    suspend fun getPedidoById(pedidoId: String): Pedido? {
        return pedidoDao.getPedidoById(pedidoId)
    }

    suspend fun addPedido(pedido: Pedido) {
        // Generar ID si está vacío
        val pedidoConId = if (pedido.id.isEmpty()) {
            pedido.copy(id = UUID.randomUUID().toString())
        } else {
            pedido
        }
        pedidoDao.insertPedido(pedidoConId)
    }

    suspend fun updatePedido(pedido: Pedido) {
        pedidoDao.updatePedido(pedido)
    }

    suspend fun updateEstadoPedido(pedidoId: String, nuevoEstado: String) {
        pedidoDao.updateEstado(pedidoId, nuevoEstado)
    }

    suspend fun deletePedido(pedido: Pedido) {
        pedidoDao.deletePedido(pedido)
    }

    suspend fun deletePedidoById(pedidoId: String) {
        pedidoDao.deletePedidoById(pedidoId)
    }
}