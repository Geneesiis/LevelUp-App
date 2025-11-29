package com.example.levelup.repository

import com.example.levelup.data.local.ProductoDao
import com.example.levelup.data.remote.ApiService
import com.example.levelup.data.remote.RetrofitInstance
import com.example.levelup.model.Producto
import com.example.levelup.model.ProductoApi
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest

class ProductoRepositoryTest : BehaviorSpec({

    // Mocks
    lateinit var productoDao: ProductoDao
    lateinit var repository: ProductoRepository
    lateinit var mockApiService: ApiService

    beforeTest {
        // Inicializar mocks antes de cada test
        productoDao = mockk()
        repository = ProductoRepository(productoDao)
        mockApiService = mockk()

        // Mockear RetrofitInstance para usar nuestro mock
        mockkObject(RetrofitInstance)
        every { RetrofitInstance.api } returns mockApiService
    }

    afterTest {
        // Limpiar mocks después de cada test
        clearAllMocks()
        unmockkAll()
    }

    Given("un repositorio de productos") {

        When("se obtienen todos los productos") {
            val productosEsperados = listOf(
                Producto(
                    id = "1",
                    nombre = "PlayStation 5",
                    precio = 499990.0,
                    imagen = "https://example.com/ps5.jpg",
                    stock = 10,
                    categoria = "Consolas",
                    descripcion = "Consola de nueva generación"
                ),
                Producto(
                    id = "2",
                    nombre = "Xbox Series X",
                    precio = 499990.0,
                    imagen = "https://example.com/xbox.jpg",
                    stock = 5,
                    categoria = "Consolas",
                    descripcion = "Consola Xbox"
                )
            )

            coEvery { productoDao.getAllProductos() } returns flowOf(productosEsperados)

            Then("debería retornar un Flow con la lista de productos") {
                runTest {
                    var resultado: List<Producto>? = null
                    repository.getProductos().collect { resultado = it }

                    resultado shouldNotBe null
                    resultado!!.size shouldBe 2
                    resultado!![0].nombre shouldBe "PlayStation 5"
                    resultado!![1].nombre shouldBe "Xbox Series X"
                }
            }
        }

        When("se sincroniza productos desde la API") {
            val productosApi = listOf(
                ProductoApi(
                    id = 1,
                    title = "WD 2TB External Hard Drive",
                    price = 64.99,
                    description = "USB 3.0 and USB 2.0 Compatibility",
                    category = "electronics",
                    image = "https://fakestoreapi.com/img/61IBBVJvSDL._AC_SY879_.jpg"
                ),
                ProductoApi(
                    id = 2,
                    title = "SanDisk SSD PLUS 1TB",
                    price = 109.99,
                    description = "Easy upgrade for faster boot up",
                    category = "electronics",
                    image = "https://fakestoreapi.com/img/61U7T1koQqL._AC_SX679_.jpg"
                )
            )

            coEvery { mockApiService.getProductosElectronics() } returns productosApi
            coEvery { productoDao.insert(any()) } just Runs

            Then("debería convertir los productos y guardarlos en Room") {
                runTest {
                    repository.sincronizarProductosDesdeApi()

                    // Verificar que se llamó a la API
                    coVerify(exactly = 1) { mockApiService.getProductosElectronics() }

                    // Verificar que se insertaron los productos
                    coVerify(exactly = 2) { productoDao.insert(any()) }
                }
            }
        }

        When("ocurre un error al sincronizar desde la API") {
            coEvery { mockApiService.getProductosElectronics() } throws Exception("Network error")

            Then("debería lanzar una excepción con el mensaje apropiado") {
                runTest {
                    try {
                        repository.sincronizarProductosDesdeApi()
                        throw AssertionError("Debería haber lanzado una excepción")
                    } catch (e: Exception) {
                        e.message shouldContain "Error al sincronizar productos"
                        e.message shouldContain "Network error"
                    }
                }
            }
        }

        When("se refrescan los productos") {
            val productosApi = listOf(
                ProductoApi(
                    id = 1,
                    title = "Test Product",
                    price = 99.99,
                    description = "Test description",
                    category = "electronics",
                    image = "https://test.com/image.jpg"
                )
            )

            coEvery { productoDao.deleteAll() } just Runs
            coEvery { mockApiService.getProductosElectronics() } returns productosApi
            coEvery { productoDao.insert(any()) } just Runs

            Then("debería borrar todos los productos y cargar desde la API") {
                runTest {
                    repository.refrescarProductos()

                    // Verificar que se borraron los productos
                    coVerify(exactly = 1) { productoDao.deleteAll() }

                    // Verificar que se llamó a la API
                    coVerify(exactly = 1) { mockApiService.getProductosElectronics() }

                    // Verificar que se insertaron los nuevos productos
                    coVerify(exactly = 1) { productoDao.insert(any()) }
                }
            }
        }

        When("se busca un producto por ID") {
            val productoEsperado = Producto(
                id = "1",
                nombre = "PlayStation 5",
                precio = 499990.0,
                imagen = "https://example.com/ps5.jpg",
                stock = 10,
                categoria = "Consolas",
                descripcion = "Consola de nueva generación"
            )

            coEvery { productoDao.getProductoById("1") } returns productoEsperado

            Then("debería retornar el producto correcto") {
                runTest {
                    val resultado = repository.getProductoById("1")

                    resultado shouldNotBe null
                    resultado!!.id shouldBe "1"
                    resultado.nombre shouldBe "PlayStation 5"
                    resultado.precio shouldBe 499990.0
                }
            }
        }

        When("se busca un producto que no existe") {
            coEvery { productoDao.getProductoById("999") } returns null

            Then("debería retornar null") {
                runTest {
                    val resultado = repository.getProductoById("999")
                    resultado shouldBe null
                }
            }
        }

        When("se actualiza el stock de un producto") {
            coEvery { productoDao.updateStock("1", 20) } just Runs

            Then("debería llamar al DAO con los parámetros correctos") {
                runTest {
                    repository.actualizarStock("1", 20)

                    coVerify(exactly = 1) { productoDao.updateStock("1", 20) }
                }
            }
        }

        When("se buscan productos por query") {
            val productosEncontrados = listOf(
                Producto(
                    id = "1",
                    nombre = "PlayStation 5",
                    precio = 499990.0,
                    imagen = "https://example.com/ps5.jpg",
                    stock = 10,
                    categoria = "Consolas",
                    descripcion = "Consola PlayStation"
                )
            )

            coEvery { productoDao.searchProductos("%Play%") } returns productosEncontrados

            Then("debería retornar los productos que coincidan con el query") {
                runTest {
                    val resultado = repository.buscarProductos("Play")

                    resultado.size shouldBe 1
                    resultado[0].nombre shouldContain "PlayStation"
                }
            }
        }

        When("se inserta un nuevo producto") {
            val nuevoProducto = Producto(
                id = "3",
                nombre = "Nintendo Switch",
                precio = 299990.0,
                imagen = "https://example.com/switch.jpg",
                stock = 15,
                categoria = "Consolas",
                descripcion = "Consola híbrida"
            )

            coEvery { productoDao.insert(nuevoProducto) } just Runs

            Then("debería llamar al DAO para insertar el producto") {
                runTest {
                    repository.insertProducto(nuevoProducto)

                    coVerify(exactly = 1) { productoDao.insert(nuevoProducto) }
                }
            }
        }

        When("se actualiza un producto existente") {
            val productoActualizado = Producto(
                id = "1",
                nombre = "PlayStation 5 Digital",
                precio = 399990.0,
                imagen = "https://example.com/ps5-digital.jpg",
                stock = 8,
                categoria = "Consolas",
                descripcion = "Edición digital"
            )

            coEvery { productoDao.update(productoActualizado) } just Runs

            Then("debería llamar al DAO para actualizar el producto") {
                runTest {
                    repository.updateProducto(productoActualizado)

                    coVerify(exactly = 1) { productoDao.update(productoActualizado) }
                }
            }
        }

        When("se elimina un producto") {
            val productoAEliminar = Producto(
                id = "1",
                nombre = "PlayStation 5",
                precio = 499990.0,
                imagen = "https://example.com/ps5.jpg",
                stock = 10,
                categoria = "Consolas",
                descripcion = "Consola"
            )

            coEvery { productoDao.delete(productoAEliminar) } just Runs

            Then("debería llamar al DAO para eliminar el producto") {
                runTest {
                    repository.deleteProducto(productoAEliminar)

                    coVerify(exactly = 1) { productoDao.delete(productoAEliminar) }
                }
            }
        }
    }
})