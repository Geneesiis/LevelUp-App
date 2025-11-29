package com.example.levelup.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.levelup.model.Producto
import com.example.levelup.ui.screens.catalogo.DetalleProductoScreen
import com.example.levelup.viewmodel.CarritoViewModel
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DetalleProductoScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var viewModel: CarritoViewModel
    private var onBackCalled = false

    @Before
    fun setUp() {
        viewModel = mockk(relaxed = true)
        onBackCalled = false
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun detalleProducto_conStock_muestraNombreYPrecio() {
        val producto = Producto(
            id = "1",
            nombre = "PlayStation 5",
            descripcion = "Consola de nueva generación con gráficos 8K",
            precio = 499990.0,
            imagen = "https://example.com/ps5.jpg",
            stock = 10,
            categoria = "Consolas"
        )

        composeTestRule.setContent {
            DetalleProductoScreen(
                producto = producto,
                viewModel = viewModel,
                onBack = { onBackCalled = true }
            )
        }

        composeTestRule
            .onNodeWithText("PlayStation 5")
            .assertExists()
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("$499.990", substring = true)
            .assertExists()

        composeTestRule
            .onNodeWithText("Consolas")
            .assertExists()

        composeTestRule
            .onNodeWithText("AGREGAR AL CARRITO")
            .assertIsEnabled()
    }

    @Test
    fun detalleProducto_sinStock_muestraAgotadoYBotonDeshabilitado() {
        val productoAgotado = Producto(
            id = "2",
            nombre = "Nintendo Switch",
            descripcion = "Consola híbrida",
            precio = 349990.0,
            imagen = "https://example.com/switch.jpg",
            stock = 0,
            categoria = "Consolas"
        )

        composeTestRule.setContent {
            DetalleProductoScreen(
                producto = productoAgotado,
                viewModel = viewModel,
                onBack = { onBackCalled = true }
            )
        }

        composeTestRule
            .onNodeWithText("Agotado")
            .assertExists()

        composeTestRule
            .onNodeWithText("PRODUCTO AGOTADO")
            .assertIsNotEnabled()
    }

    @Test
    fun detalleProducto_stockBajo_muestraBadgeUltimasUnidades() {
        val productoBajo = Producto(
            id = "3",
            nombre = "DualSense",
            descripcion = "Control",
            precio = 69990.0,
            imagen = "",
            stock = 3,
            categoria = "Periféricos"
        )

        composeTestRule.setContent {
            DetalleProductoScreen(
                producto = productoBajo,
                viewModel = viewModel,
                onBack = { onBackCalled = true }
            )
        }

        composeTestRule
            .onNodeWithText("¡ÚLTIMAS UNIDADES!")
            .assertExists()
    }

    @Test
    fun detalleProducto_sinDescripcion_muestraTextoPorDefecto() {
        val productoSinDesc = Producto(
            id = "4",
            nombre = "Mouse",
            descripcion = "",
            precio = 29990.0,
            imagen = "",
            stock = 15,
            categoria = "Accesorios"
        )

        composeTestRule.setContent {
            DetalleProductoScreen(
                producto = productoSinDesc,
                viewModel = viewModel,
                onBack = { onBackCalled = true }
            )
        }

        composeTestRule
            .onNodeWithText("Sin descripción disponible")
            .assertExists()
    }

    @Test
    fun detalleProducto_clickEnAgregarCarrito_llamaViewModel() {
        val producto = Producto(
            id = "5",
            nombre = "Xbox",
            descripcion = "Test",
            precio = 499990.0,
            imagen = "",
            stock = 5,
            categoria = "Consolas"
        )

        composeTestRule.setContent {
            DetalleProductoScreen(
                producto = producto,
                viewModel = viewModel,
                onBack = { onBackCalled = true }
            )
        }

        composeTestRule
            .onNodeWithText("AGREGAR AL CARRITO")
            .performClick()

        verify(exactly = 1) { viewModel.agregarAlCarrito(producto) }
    }

    @Test
    fun detalleProducto_clickEnVolver_ejecutaCallback() {
        val producto = Producto(
            id = "6",
            nombre = "Test",
            descripcion = "Test",
            precio = 99990.0,
            imagen = "",
            stock = 10,
            categoria = "Test"
        )

        composeTestRule.setContent {
            DetalleProductoScreen(
                producto = producto,
                viewModel = viewModel,
                onBack = { onBackCalled = true }
            )
        }

        composeTestRule
            .onNodeWithContentDescription("Volver")
            .performClick()

        assert(onBackCalled)
    }
}
