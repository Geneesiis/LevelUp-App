package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.model.ItemCarrito
import com.example.levelup.model.Producto
import com.example.levelup.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CarritoViewModel : ViewModel() {
    private val repository = ProductoRepository()

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos

    private val _carrito = MutableStateFlow<List<ItemCarrito>>(emptyList())
    val carrito: StateFlow<List<ItemCarrito>> = _carrito

    private val _deseados = MutableStateFlow<List<Producto>>(emptyList())
    val deseados: StateFlow<List<Producto>> = _deseados

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando

    // Estado para el buscador
    private val _textoBusqueda = MutableStateFlow("")
    val textoBusqueda: StateFlow<String> = _textoBusqueda

    // Productos filtrados por búsqueda
    val productosFiltrados: StateFlow<List<Producto>> = combine(
        _productos,
        _textoBusqueda
    ) { productos, busqueda ->
        if (busqueda.isBlank()) {
            productos
        } else {
            productos.filter { producto ->
                producto.nombre.contains(busqueda, ignoreCase = true) ||
                        producto.descripcion.contains(busqueda, ignoreCase = true)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

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

    // Funciones de búsqueda
    fun actualizarBusqueda(texto: String) {
        _textoBusqueda.value = texto
    }

    fun limpiarBusqueda() {
        _textoBusqueda.value = ""
    }

    // Funciones del carrito
    fun agregarAlCarrito(producto: Producto) {
        val productoEnCatalogo = _productos.value.find { it.id == producto.id } ?: return
        val cantidadEnCarrito = _carrito.value.find { it.producto.id == producto.id }?.cantidad ?: 0

        if (cantidadEnCarrito < productoEnCatalogo.stock) {
            _carrito.value = _carrito.value.find { it.producto.id == producto.id }?.let {
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
            _carrito.value.map { item ->
                if (item.producto.id == producto.id) {
                    item.copy(cantidad = item.cantidad - 1)
                } else {
                    item
                }
            }
        } else {
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
        vaciarCarrito()
    }

    fun obtenerTotal(): Double {
        return _carrito.value.sumOf { it.producto.precio * it.cantidad }
    }

    fun obtenerCantidadEnCarrito(productoId: String): Int {
        return _carrito.value.find { it.producto.id == productoId }?.cantidad ?: 0
    }

    // Funciones de deseados
    fun agregarADeseados(producto: Producto) {
        if (!_deseados.value.any { it.id == producto.id }) {
            _deseados.value = _deseados.value + producto
        }
    }

    fun removerDeDeseados(producto: Producto) {
        _deseados.value = _deseados.value.filter { it.id != producto.id }
    }

    fun toggleDeseado(producto: Producto) {
        if (esDeseado(producto.id)) {
            removerDeDeseados(producto)
        } else {
            agregarADeseados(producto)
        }
    }

    fun esDeseado(productoId: String): Boolean {
        return _deseados.value.any { it.id == productoId }
    }

    fun moverDeseadoAlCarrito(producto: Producto) {
        agregarAlCarrito(producto)
        removerDeDeseados(producto)
    }

    fun vaciarDeseados() {
        _deseados.value = emptyList()
    }
}