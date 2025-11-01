package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.model.ItemCarrito
import com.example.levelup.model.Producto
import com.example.levelup.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CarritoViewModel : ViewModel() {
    private val repository = ProductoRepository()

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos

    private val _carrito = MutableStateFlow<List<ItemCarrito>>(emptyList())
    val carrito: StateFlow<List<ItemCarrito>> = _carrito

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando

    init {
        cargarProductos()
    }

    fun cargarProductos() {
        _cargando.value = true
        viewModelScope.launch {
            try {
                val resultado = repository.obtenerProductos()
                _productos.value = resultado.productos
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _cargando.value = false
            }
        }
    }

    fun agregarAlCarrito(producto: Producto) {
        // Verificar si hay stock disponible
        val productoEnCatalogo = _productos.value.find { it.id == producto.id } ?: return
        val cantidadEnCarrito = _carrito.value.find { it.producto.id == producto.id }?.cantidad ?: 0

        if (cantidadEnCarrito < productoEnCatalogo.stock) {
            _carrito.value = _carrito.value.find { it.producto.id == producto.id }?.let {
                // Actualizar cantidad existente
                _carrito.value.map { item ->
                    if (item.producto.id == producto.id) {
                        item.copy(cantidad = item.cantidad + 1)
                    } else {
                        item
                    }
                }
            } ?: (_carrito.value + ItemCarrito(producto = producto, cantidad = 1))
        }
    }

    fun removerDelCarrito(producto: Producto) {
        val itemExistente = _carrito.value.find { it.producto.id == producto.id } ?: return

        _carrito.value = if (itemExistente.cantidad > 1) {
            // Decrementar cantidad
            _carrito.value.map { item ->
                if (item.producto.id == producto.id) {
                    item.copy(cantidad = item.cantidad - 1)
                } else {
                    item
                }
            }
        } else {
            // Eliminar item de la lista
            _carrito.value.filter { it.producto.id != producto.id }
        }
    }

    fun eliminarProductoDelCarrito(producto: Producto) {
        _carrito.value = _carrito.value.filter { it.producto.id != producto.id }
    }

    fun vaciarCarrito() {
        _carrito.value = emptyList()
    }

    fun confirmarCompra() {
        // Aquí podrías agregar lógica para actualizar stock en Firebase
        vaciarCarrito()
    }

    fun obtenerTotal(): Double {
        return _carrito.value.sumOf { it.producto.precio * it.cantidad }
    }

    // Método para obtener la cantidad actual de un producto en el carrito
    fun obtenerCantidadEnCarrito(productoId: String): Int {
        return _carrito.value.find { it.producto.id == productoId }?.cantidad ?: 0
    }
}