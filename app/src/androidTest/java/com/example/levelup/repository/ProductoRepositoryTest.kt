package com.example.levelup.repository

import com.example.levelup.data.local.ProductoDao
import com.example.levelup.data.remote.ApiService
import com.example.levelup.data.remote.RetrofitInstance
import com.example.levelup.model.Producto
import com.example.levelup.model.ProductoApi
import io.mockk.*
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class ProductoRepositoryTest {

    // Dispatcher para corrutinas de prueba
    private val testDispatcher = UnconfinedTestDispatcher()

    // Mocks
    private lateinit var productoDao: ProductoDao
    private lateinit var mockApiService: ApiService
    private lateinit var repository: ProductoRepository

    @Before
    fun setUp() {
        // Establecer el dispatcher principal para las pruebas
        Dispatchers.setMain(testDispatcher)

        // Inicializar mocks
        productoDao = mockk(relaxUnitFun = true)
        mockApiService = mockk(relaxUnitFun = true)

        // Mockear el objeto singleton RetrofitInstance
        mockkObject(RetrofitInstance)
        every { RetrofitInstance.api } returns mockApiService

        // Crear una instancia real del repositorio con el DAO mockeado
        repository = ProductoRepository(productoDao)
    }

    @After
    fun tearDown() {
        // Limpiar mocks y resetear el dispatcher
        unmockkAll()
        Dispatchers.resetMain()
    }

    @Test
    fun getProductos_deberiaRetornarFlowConListaDeProductosDelDAO() = runTest {
        // Given
        val productosEsperados = listOf(
            Producto(id = "1", nombre = "PlayStation 5", precio = 499990.0, imagen = "", stock = 10, categoria = "Consolas", descripcion = ""),
            Producto(id = "2", nombre = "Xbox Series X", precio = 499990.0, imagen = "", stock = 5, categoria = "Consolas", descripcion = "")
        )
        every { productoDao.getAllProductos() } returns flowOf(productosEsperados)

        // When
        val resultado = repository.getProductos().first()

        // Then
        assertNotNull(resultado)
        assertEquals(2, resultado.size)
        assertEquals("PlayStation 5", resultado[0].nombre)
    }

    @Test
    fun sincronizarProductosDesdeApi_deberiaObtenerProductosDeLaApiYGuardarlosEnRoom() = runTest {
        // Given
        val productosApi = listOf(
            ProductoApi(id = 1, title = "Producto 1", price = 10.0, description = "", category = "electronics", image = ""),
            ProductoApi(id = 2, title = "Producto 2", price = 20.0, description = "", category = "electronics", image = "")
        )
        coEvery { mockApiService.getProductosElectronics() } returns productosApi

        // When
        repository.sincronizarProductosDesdeApi()

        // Then
        coVerify(exactly = 1) { mockApiService.getProductosElectronics() }
        coVerify(exactly = 2) { productoDao.insert(any()) }
    }
    
    @Test(expected = Exception::class)
    fun sincronizarProductosDesdeApi_deberiaLanzarExcepcionSiLaApiFalla() = runTest {
        // Given
        val errorMessage = "Network error"
        coEvery { mockApiService.getProductosElectronics() } throws Exception(errorMessage)

        // When
        repository.sincronizarProductosDesdeApi()

        // Then se espera la excepción
    }

    @Test
    fun refrescarProductos_deberiaBorrarBDYCargarDesdeApi() = runTest {
        // Given
        val productosApi = listOf(
            ProductoApi(id = 1, title = "Nuevo Producto", price = 99.0, description = "", category = "electronics", image = "")
        )
        coEvery { mockApiService.getProductosElectronics() } returns productosApi

        // When
        repository.refrescarProductos()

        // Then
        coVerifyOrder {
            productoDao.deleteAll()
            mockApiService.getProductosElectronics()
            productoDao.insert(any())
        }
    }

    @Test
    fun getProductoById_deberiaRetornarElProductoCorrectoDelDAO() = runTest {
        // Given
        val productoEsperado = Producto(id = "1", nombre = "PlayStation 5", precio = 499990.0, imagen = "", stock = 10, categoria = "Consolas", descripcion = "")
        coEvery { productoDao.getProductoById("1") } returns productoEsperado

        // When
        val resultado = repository.getProductoById("1")

        // Then
        assertNotNull(resultado)
        assertEquals("1", resultado?.id)
        assertEquals("PlayStation 5", resultado?.nombre)
    }

    @Test
    fun actualizarStock_deberiaLlamarAlDAOConLosParametrosCorrectos() = runTest {
        // Given
        val productoId = "1"
        val nuevoStock = 20

        // When
        repository.actualizarStock(productoId, nuevoStock)

        // Then
        coVerify(exactly = 1) { productoDao.updateStock(productoId, nuevoStock) }
    }
}
