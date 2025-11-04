package com.example.levelup.repository

import com.example.levelup.model.EstadoPedido
import com.example.levelup.model.ItemCarrito
import com.example.levelup.model.Pedido
import com.example.levelup.model.Producto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class PedidoRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val pedidosCollection = firestore.collection("pedidos")

    // Guarda un pedido en Firestore
    suspend fun guardarPedido(pedido: Pedido): Result<String> {
        return try {
            val pedidoMap = hashMapOf(
                "id" to pedido.id,
                "fecha" to pedido.fecha,
                "total" to pedido.total,
                "estado" to pedido.estado.name,
                "metodoPago" to pedido.metodoPago,
                "direccionEntrega" to pedido.direccionEntrega,
                "productos" to pedido.productos.map { item ->
                    hashMapOf(
                        "productoId" to item.producto.id,
                        "productoNombre" to item.producto.nombre,
                        "productoDescripcion" to item.producto.descripcion,
                        "productoPrecio" to item.producto.precio,
                        "productoImagen" to item.producto.imagen,
                        "productoStock" to item.producto.stock,
                        "productoCategoria" to item.producto.categoria,
                        "cantidad" to item.cantidad
                    )
                }
            )

            pedidosCollection.document(pedido.id).set(pedidoMap).await()
            Result.success(pedido.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Obtiene todos los pedidos desde Firestore
    suspend fun obtenerPedidos(): Result<List<Pedido>> {
        return try {
            val snapshot = pedidosCollection
                .orderBy("fecha", Query.Direction.DESCENDING)
                .get()
                .await()

            val pedidos = snapshot.documents.mapNotNull { doc ->
                try {
                    val productos = (doc.get("productos") as? List<Map<String, Any>>)?.map { prodMap ->
                        ItemCarrito(
                            producto = Producto(
                                id = prodMap["productoId"] as? String ?: "",
                                nombre = prodMap["productoNombre"] as? String ?: "",
                                descripcion = prodMap["productoDescripcion"] as? String ?: "",
                                precio = (prodMap["productoPrecio"] as? Number)?.toDouble() ?: 0.0,
                                imagen = prodMap["productoImagen"] as? String ?: "",
                                stock = (prodMap["productoStock"] as? Number)?.toInt() ?: 0,
                                categoria = prodMap["productoCategoria"] as? String ?: "Sin categoría"
                            ),
                            cantidad = (prodMap["cantidad"] as? Number)?.toInt() ?: 0
                        )
                    } ?: emptyList()

                    Pedido(
                        id = doc.getString("id") ?: "",
                        fecha = doc.getLong("fecha") ?: 0L,
                        productos = productos,
                        total = doc.getDouble("total") ?: 0.0,
                        estado = EstadoPedido.fromString(doc.getString("estado") ?: "PENDIENTE"),
                        metodoPago = doc.getString("metodoPago") ?: "Tarjeta",
                        direccionEntrega = doc.getString("direccionEntrega") ?: ""
                    )
                } catch (e: Exception) {
                    null
                }
            }

            Result.success(pedidos)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Actualiza el estado de un pedido
    suspend fun actualizarEstadoPedido(pedidoId: String, nuevoEstado: EstadoPedido): Result<Unit> {
        return try {
            pedidosCollection.document(pedidoId)
                .update("estado", nuevoEstado.name)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Elimina un pedido
    suspend fun eliminarPedido(pedidoId: String): Result<Unit> {
        return try {
            pedidosCollection.document(pedidoId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}