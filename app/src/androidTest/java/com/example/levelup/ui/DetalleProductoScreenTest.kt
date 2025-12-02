package com.example.levelup.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.levelup.model.Producto
import com.example.levelup.ui.screens.catalogo.DetalleProductoScreen
import com.example.levelup.viewmodel.CarritoViewModel
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Tests de UI para DetalleProductoScreen usando Compose Testing.
 *
 * Estos tests verifican:
 * - Navegación (botón volver)
 * - Visualización correcta de datos del producto
 * - Estado de disponibilidad (con stock / sin stock)
 */
class DetalleProductoScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var viewModel: CarritoViewModel
    private var onBackCalled = false

    @Before
    fun setup() {
        viewModel = mockk(relaxed = true)
        onBackCalled = false
    }

    @Test
    fun detalleProducto_clickEnVolver_ejecutaCallback() {
        // Given - Producto de prueba
        val producto = Producto(
            id = "6",
            nombre = "Test",
            descripcion = "Test",
            precio = 99990.0,
            imagen = "",
            stock = 10,
            categoria = "Test"
        )

        // When - Se renderiza la pantalla
        composeTestRule.setContent {
            DetalleProductoScreen(
                producto = producto,
                viewModel = viewModel,
                onBack = { onBackCalled = true }
            )
        }

        // Then - Se verifica el botón volver y se hace click
        composeTestRule.waitUntil(timeoutMillis = 2000) {
            composeTestRule
                .onAllNodesWithContentDescription("Volver")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule
            .onNodeWithContentDescription("Volver")
            .performClick()

        // Verificar que se ejecutó el callback
        assert(onBackCalled) {
            "El callback onBack debería haber sido ejecutado después del click"
        }
    }

    @Test
    fun detalleProducto_conStock_muestraNombreYPrecio() {
        // Given - Producto con stock disponible
        val producto = Producto(
            id = "1",
            nombre = "PlayStation 5",
            descripcion = "Consola de nueva generación con gráficos 8K",
            precio = 499990.0,
            imagen = "https://example.com/ps5.jpg",
            stock = 10,
            categoria = "Consolas"
        )

        // When - Se renderiza la pantalla
        composeTestRule.setContent {
            DetalleProductoScreen(
                producto = producto,
                viewModel = viewModel,
                onBack = { onBackCalled = true }
            )
        }

        // Then - Se verifica que se muestra el nombre del producto
        composeTestRule
            .onNodeWithText("PlayStation 5", useUnmergedTree = true)
            .assertExists()
            .assertIsDisplayed()

        // Y se verifica que se muestra el precio
        composeTestRule
            .onNodeWithText("499.990", substring = true, useUnmergedTree = true)
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun detalleProducto_sinStock_muestraAgotadoYBotonDeshabilitado() {
        // Given - Producto sin stock
        val productoAgotado = Producto(
            id = "2",
            nombre = "Nintendo Switch",
            descripcion = "Consola híbrida",
            precio = 349990.0,
            imagen = "https://example.com/switch.jpg",
            stock = 0,
            categoria = "Consolas"
        )

        // When - Se renderiza la pantalla
        composeTestRule.setContent {
            DetalleProductoScreen(
                producto = productoAgotado,
                viewModel = viewModel,
                onBack = { onBackCalled = true }
            )
        }

        // Then - Se verifica que se muestra el texto "Agotado" en el estado
        composeTestRule
            .onNodeWithText("Agotado", useUnmergedTree = true)
            .assertExists()
            .assertIsDisplayed()

        // Verificar que el botón está deshabilitado
        composeTestRule
            .onNodeWithTag("boton_agregar")
            .assertExists()
            .assertIsNotEnabled()

        // Verificar que el texto del botón es "PRODUCTO AGOTADO"
        composeTestRule
            .onNodeWithTag("boton_agregar")
            .assert(hasText("PRODUCTO AGOTADO"))
    }
}