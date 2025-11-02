package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.model.ItemCarrito
import com.example.levelup.model.Pedido
import com.example.levelup.model.Producto
import com.example.levelup.model.EstadoPedido
import com.example.levelup.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

class CarritoViewModel : ViewModel() {
    private val repository = ProductoRepository()

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos

    private val _carrito = MutableStateFlow<List<ItemCarrito>>(emptyList())
    val carrito: StateFlow<List<ItemCarrito>> = _carrito

    // Historial de pedidos
    private val _historial = MutableStateFlow<List<Pedido>>(emptyList())
    val historial: StateFlow<List<Pedido>> = _historial

    // Cambio: Usar Set para búsquedas más rápidas
    private val _deseadosIds = MutableStateFlow<Set<String>>(emptySet())
    private val _deseados = MutableStateFlow<List<Producto>>(emptyList())
    val deseados: StateFlow<List<Producto>> = _deseados

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando

    // Estado para el buscador
    private val _textoBusqueda = MutableStateFlow("")
    val textoBusqueda: StateFlow<String> = _textoBusqueda

    // Job para debounce de búsqueda
    private var searchJob: Job? = null

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

    // Funciones de búsqueda con debounce
    fun actualizarBusqueda(texto: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300) // Debounce de 300ms
            _textoBusqueda.value = texto
        }
    }

    fun limpiarBusqueda() {
        searchJob?.cancel()
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

    fun obtenerTotal(): Double {
        return _carrito.value.sumOf { it.producto.precio * it.cantidad }
    }

    fun obtenerCantidadEnCarrito(productoId: String): Int {
        return _carrito.value.find { it.producto.id == productoId }?.cantidad ?: 0
    }

    // Funciones de deseados - OPTIMIZADAS
    fun agregarADeseados(producto: Producto) {
        if (!_deseadosIds.value.contains(producto.id)) {
            _deseadosIds.value = _deseadosIds.value + producto.id
            _deseados.value = _deseados.value + producto
        }
    }

    fun removerDeDeseados(producto: Producto) {
        if (_deseadosIds.value.contains(producto.id)) {
            _deseadosIds.value = _deseadosIds.value - producto.id
            _deseados.value = _deseados.value.filter { it.id != producto.id }
        }
    }

    // OPTIMIZADO: Toggle instantáneo con Set
    fun toggleDeseado(producto: Producto) {
        viewModelScope.launch {
            if (_deseadosIds.value.contains(producto.id)) {
                _deseadosIds.value = _deseadosIds.value - producto.id
                _deseados.value = _deseados.value.filter { it.id != producto.id }
            } else {
                _deseadosIds.value = _deseadosIds.value + producto.id
                _deseados.value = _deseados.value + producto
            }
        }
    }

    // OPTIMIZADO: Búsqueda O(1) en lugar de O(n)
    fun esDeseado(productoId: String): Boolean {
        return _deseadosIds.value.contains(productoId)
    }

    fun moverDeseadoAlCarrito(producto: Producto) {
        agregarAlCarrito(producto)
        removerDeDeseados(producto)
    }

    fun vaciarDeseados() {
        _deseadosIds.value = emptySet()
        _deseados.value = emptyList()
    }

    // Función para obtener todos los productos deseados
    fun obtenerDeseados(): List<Producto> {
        return _deseados.value
    }

    // Función para agregar múltiples deseados al carrito
    fun moverTodosDeseadosAlCarrito() {
        _deseados.value.forEach { producto ->
            agregarAlCarrito(producto)
        }
        vaciarDeseados()
    }

    // ==================== FUNCIONES DE HISTORIAL ====================

    /**
     * Confirma la compra y crea un pedido en el historial
     */
    fun confirmarCompra(metodoPago: String = "Tarjeta", direccion: String = "Dirección por defecto") {
        if (_carrito.value.isEmpty()) return

        val pedido = Pedido(
            productos = _carrito.value.map { it.copy() }, // Copia de los items
            total = obtenerTotal(),
            estado = EstadoPedido.CONFIRMADO,
            metodoPago = metodoPago,
            direccionEntrega = direccion
        )

        // Agregar al historial
        _historial.value = listOf(pedido) + _historial.value

        // Vaciar el carrito
        vaciarCarrito()
    }

    /**
     * Obtiene un pedido por su ID
     */
    fun obtenerPedido(pedidoId: String): Pedido? {
        return _historial.value.find { it.id == pedidoId }
    }

    /**
     * Actualiza el estado de un pedido
     */
    fun actualizarEstadoPedido(pedidoId: String, nuevoEstado: EstadoPedido) {
        _historial.value = _historial.value.map { pedido ->
            if (pedido.id == pedidoId) {
                pedido.copy(estado = nuevoEstado)
            } else {
                pedido
            }
        }
    }

    /**
     * Cancela un pedido (solo si está en estado PENDIENTE o CONFIRMADO)
     */
    fun cancelarPedido(pedidoId: String): Boolean {
        val pedido = obtenerPedido(pedidoId) ?: return false

        if (!pedido.puedeCancelarse()) return false

        actualizarEstadoPedido(pedidoId, EstadoPedido.CANCELADO)
        return true
    }

    /**
     * Recomprar - Agrega todos los productos de un pedido al carrito
     */
    fun recomprarPedido(pedido: Pedido) {
        pedido.productos.forEach { item ->
            // Verificar que el producto aún exista y tenga stock
            val productoActual = _productos.value.find { it.id == item.producto.id }
            if (productoActual != null && productoActual.stock > 0) {
                repeat(item.cantidad.coerceAtMost(productoActual.stock)) {
                    agregarAlCarrito(productoActual)
                }
            }
        }
    }

    /**
     * Obtiene el total gastado en todos los pedidos
     */
    fun obtenerTotalGastado(): Double {
        return _historial.value
            .filter { it.estado != EstadoPedido.CANCELADO }
            .sumOf { it.total }
    }

    /**
     * Obtiene la cantidad de pedidos realizados
     */
    fun obtenerCantidadPedidos(): Int {
        return _historial.value
            .filter { it.estado != EstadoPedido.CANCELADO }
            .size
    }

    /**
     * Obtiene pedidos filtrados por estado
     */
    fun obtenerPedidosPorEstado(estado: EstadoPedido): List<Pedido> {
        return _historial.value.filter { it.estado == estado }
    }

    /**
     * Limpia todo el historial (usar con cuidado)
     */
    fun limpiarHistorial() {
        _historial.value = emptyList()
    }
}