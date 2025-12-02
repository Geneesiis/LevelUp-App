package com.example.levelup.repository

import com.example.levelup.data.local.ProductoDao
import com.example.levelup.data.remote.RetrofitInstance
import com.example.levelup.model.Producto
import com.example.levelup.model.toProducto
import kotlinx.coroutines.flow.Flow

class ProductoRepository(private val productoDao: ProductoDao) : IProductoRepository {

    override fun getProductos(): Flow<List<Producto>> {
        return productoDao.getAllProductos()
    }

    override suspend fun sincronizarProductosDesdeApi() {
        try {
            // Obtener productos de la API
            val productosApi = RetrofitInstance.api.getProductosElectronics()

            // Convertir a modelo local y guardar en Room
            productosApi.forEach { productoApi ->
                val producto = productoApi.toProducto()
                productoDao.insert(producto)
            }
        } catch (e: Exception) {
            throw Exception("Error al sincronizar productos: ${e.message}")
        }
    }

    suspend fun refrescarProductos() {
        try {
            // Borrar todos los productos actuales
            productoDao.deleteAll()

            // Cargar desde API
            sincronizarProductosDesdeApi()
        } catch (e: Exception) {
            throw Exception("Error al refrescar productos: ${e.message}")
        }
    }

    suspend fun getProductoById(id: String): Producto? {
        return productoDao.getProductoById(id)
    }

    override suspend fun insertProducto(producto: Producto) {
        productoDao.insert(producto)
    }

    override suspend fun updateProducto(producto: Producto) {
        productoDao.update(producto)
    }

    override suspend fun deleteProducto(producto: Producto) {
        productoDao.delete(producto)
    }

    override suspend fun actualizarStock(productoId: String, nuevoStock: Int) {
        productoDao.updateStock(productoId, nuevoStock)
    }

    suspend fun buscarProductos(query: String): List<Producto> {
        return productoDao.searchProductos("%$query%")
    }
}