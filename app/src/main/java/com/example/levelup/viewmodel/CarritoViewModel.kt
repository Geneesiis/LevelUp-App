package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.model.EstadoPedido
import com.example.levelup.model.ItemCarrito
import com.example.levelup.model.Pedido
import com.example.levelup.model.Producto
import com.example.levelup.repository.PedidoRepository
import com.example.levelup.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date

class CarritoViewModel(
    private val productoRepository: ProductoRepository,
    private val pedidoRepository: PedidoRepository
) : ViewModel() {

    // --- BÚSQUEDA ---
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _productosOriginales = productoRepository.getProductos()

    val productos: StateFlow<List<Producto>> = _searchQuery
        .combine(_productosOriginales) { query, productos ->
            if (query.isBlank()) {
                productos
            } else {
                productos.filter { it.nombre.contains(query, ignoreCase = true) }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // --- CARRITO ---
    private val _carrito = MutableStateFlow<List<Producto>>(emptyList())
    val carrito: StateFlow<List<Producto>> = _carrito.asStateFlow()

    // --- DESEADOS ---
    private val _deseados = MutableStateFlow<List<Producto>>(emptyList())
    val deseados: StateFlow<List<Producto>> = _deseados.asStateFlow()

    // --- HISTORIAL DE PEDIDOS ---
    private val _historialPedidos = MutableStateFlow<List<Pedido>>(emptyList())
    val historialPedidos: StateFlow<List<Pedido>> = _historialPedidos.asStateFlow()

    // Alias para compatibilidad con HistorialScreen
    val historial: StateFlow<List<Pedido>> = historialPedidos

    init {
        viewModelScope.launch {
            pedidoRepository.getPedidos().collect { pedidos ->
                _historialPedidos.value = pedidos
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    // --- Funciones de Carrito ---
    fun agregarAlCarrito(producto: Producto) {
        _carrito.value = _carrito.value + producto
    }

    fun eliminarDelCarrito(producto: Producto) {
        _carrito.value = _carrito.value - producto
    }

    fun removerDelCarrito(producto: Producto) {
        val index = _carrito.value.indexOfFirst { it.id == producto.id }
        if (index >= 0) {
            _carrito.value = _carrito.value.toMutableList().also { it.removeAt(index) }
        }
    }

    fun vaciarCarrito() {
        _carrito.value = emptyList()
    }

    // --- Funciones de Deseados ---
    fun agregarADeseados(producto: Producto) {
        if (!_deseados.value.contains(producto)) {
            _deseados.value = _deseados.value + producto
        }
    }

    fun eliminarDeDeseados(producto: Producto) {
        _deseados.value = _deseados.value - producto
    }

    fun toggleDeseado(producto: Producto) {
        if (_deseados.value.contains(producto)) {
            eliminarDeDeseados(producto)
        } else {
            agregarADeseados(producto)
        }
    }

    fun vaciarDeseados() {
        _deseados.value = emptyList()
    }

    fun moverDeseadoAlCarrito(producto: Producto) {
        if (producto.stock > 0) {
            agregarAlCarrito(producto)
            eliminarDeDeseados(producto)
        }
    }

    fun moverTodosDeseadosAlCarrito() {
        val productosDisponibles = _deseados.value.filter { it.stock > 0 }
        productosDisponibles.forEach { producto ->
            agregarAlCarrito(producto)
        }
        _deseados.value = _deseados.value.filter { it.stock == 0 }
    }

    // --- Funciones de Pedidos ---
    fun realizarPedido(usuarioId: String) {
        viewModelScope.launch {
            val productosEnCarrito = _carrito.value
            if (productosEnCarrito.isEmpty()) return@launch

            val totalPedido = productosEnCarrito.sumOf { it.precio }

            val itemsParaPedido = productosEnCarrito
                .groupBy { it.id }
                .map { (_, productosAgrupados) ->
                    ItemCarrito(
                        producto = productosAgrupados.first(),
                        cantidad = productosAgrupados.size
                    )
                }

            // --- INICIO DE LA MODIFICACIÓN ---
            // Actualizar el stock de cada producto
            itemsParaPedido.forEach { item ->
                val producto = item.producto
                val cantidadComprada = item.cantidad
                val nuevoStock = producto.stock - cantidadComprada
                if (nuevoStock >= 0) { // Asegurarse de no tener stock negativo
                    actualizarStockProducto(producto.id, nuevoStock)
                }
            }
            // --- FIN DE LA MODIFICACIÓN ---

            val nuevoPedido = Pedido(
                id = "",
                clienteId = usuarioId,
                productos = itemsParaPedido,
                total = totalPedido,
                fecha = Date(),
                estado = EstadoPedido.PENDIENTE
            )

            pedidoRepository.addPedido(nuevoPedido)
            vaciarCarrito()
        }
    }


    fun actualizarEstadoPedido(pedidoId: String, nuevoEstado: String) {
        viewModelScope.launch {
            pedidoRepository.updateEstadoPedido(pedidoId, nuevoEstado)
        }
    }

    fun actualizarStockProducto(productoId: String, nuevoStock: Int) {
        viewModelScope.launch {
            productoRepository.actualizarStock(productoId, nuevoStock)
        }
    }

    // --- FUNCIONES PARA HISTORIAL ---
    fun obtenerCantidadPedidos(): Int {
        return _historialPedidos.value.size
    }

    fun obtenerTotalGastado(): Double {
        return _historialPedidos.value
            .filter { it.estado != EstadoPedido.CANCELADO }
            .sumOf { it.total }
    }

    fun recomprarPedido(pedido: Pedido) {
        pedido.productos.forEach { item ->
            repeat(item.cantidad) {
                agregarAlCarrito(item.producto)
            }
        }
    }

    fun cancelarPedido(pedidoId: String) {
        viewModelScope.launch {
            pedidoRepository.updateEstadoPedido(pedidoId, EstadoPedido.CANCELADO.name)
        }
    }

    fun cargarProductosDesdeApi() {
        viewModelScope.launch {
            try {
                productoRepository.sincronizarProductosDesdeApi()
            } catch (e: Exception) {
                // Manejar error si lo deseas
                println("Error al cargar productos: ${e.message}")
            }
        }
    }
}