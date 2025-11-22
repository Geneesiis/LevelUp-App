package com.example.levelup.repository

import com.example.levelup.data.local.ProductoDao
import com.example.levelup.model.Producto
import kotlinx.coroutines.flow.Flow

class ProductoRepository(private val productoDao: ProductoDao) {

    fun getProductos(): Flow<List<Producto>> {
        return productoDao.getAllProductos()
    }

    suspend fun getProductoById(id: String): Producto? {
        return productoDao.getProductoById(id)
    }

    suspend fun insertProducto(producto: Producto) {
        productoDao.insert(producto)
    }

    suspend fun updateProducto(producto: Producto) {
        productoDao.update(producto)
    }

    suspend fun deleteProducto(producto: Producto) {
        productoDao.delete(producto)
    }

    suspend fun actualizarStock(productoId: String, nuevoStock: Int) {
        productoDao.updateStock(productoId, nuevoStock)
    }

    suspend fun buscarProductos(query: String): List<Producto> {
        return productoDao.searchProductos("%$query%")
    }
}