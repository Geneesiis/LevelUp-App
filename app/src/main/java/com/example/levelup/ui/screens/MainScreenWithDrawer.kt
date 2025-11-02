package com.example.levelup.ui.screens

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.levelup.ui.components.DrawerContent
import com.example.levelup.ui.screens.catalogo.CatalogoScreen
import com.example.levelup.ui.screens.catalogo.DetalleProductoScreen
import com.example.levelup.ui.screens.carrito.CarritoScreen
import com.example.levelup.ui.screens.deseados.DeseadosScreen
import com.example.levelup.ui.screens.perfil.PerfilClienteScreen
import com.example.levelup.ui.screens.historial.HistorialScreen
import com.example.levelup.ui.screens.pago.PagoConfirmacionScreen
import com.example.levelup.viewmodel.CarritoViewModel
import kotlinx.coroutines.launch

/**
 * Pantalla principal con Navigation Drawer (Sidebar)
 * Contiene la navegación interna de la app: Catálogo, Deseados, Carrito, Perfil, etc.
 *
 * @param viewModel ViewModel compartido para el carrito y productos
 * @param nombreUsuario Nombre del usuario logueado
 * @param onLogout Callback para cerrar sesión
 * @param navController NavController para la navegación interna
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenWithDrawer(
    viewModel: CarritoViewModel,
    nombreUsuario: String = "Cliente",
    onLogout: () -> Unit = {},
    navController: NavHostController = rememberNavController()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "catalogo"

    val carrito by viewModel.carrito.collectAsState()
    val deseados by viewModel.deseados.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                currentRoute = currentRoute,
                onNavigateToCatalogo = {
                    scope.launch {
                        navController.navigate("catalogo") {
                            popUpTo("catalogo") { inclusive = true }
                        }
                        drawerState.close()
                    }
                },
                onNavigateToDeseados = {
                    scope.launch {
                        navController.navigate("deseados")
                        drawerState.close()
                    }
                },
                onNavigateToCarrito = {
                    scope.launch {
                        navController.navigate("carrito")
                        drawerState.close()
                    }
                },
                onNavigateToPerfil = {
                    scope.launch {
                        navController.navigate("perfil")
                        drawerState.close()
                    }
                },
                onNavigateToHistorial = {
                    scope.launch {
                        // navController.navigate("historial")
                        // Por ahora no hace nada, implementa cuando tengas HistorialScreen
                        drawerState.close()
                    }
                },
                carritoCount = carrito.sumOf { it.cantidad },
                deseadosCount = deseados.size
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = "catalogo"
        ) {
            // Catálogo
            composable("catalogo") {
                CatalogoScreen(
                    viewModel = viewModel,
                    navController = navController,
                    onVerCarrito = {
                        scope.launch {
                            navController.navigate("carrito")
                        }
                    },
                    onVerPerfil = {
                        scope.launch {
                            navController.navigate("perfil")
                        }
                    },
                    onVerDeseados = {
                        scope.launch {
                            navController.navigate("deseados")
                        }
                    },
                    onToggleDrawer = {
                        scope.launch {
                            if (drawerState.isClosed) {
                                drawerState.open()
                            } else {
                                drawerState.close()
                            }
                        }
                    }
                )
            }

            // Deseados
            composable("deseados") {
                DeseadosScreen(
                    viewModel = viewModel,
                    navController = navController,
                    onVerCarrito = {
                        scope.launch {
                            navController.navigate("carrito")
                        }
                    },
                    onToggleDrawer = {
                        scope.launch {
                            if (drawerState.isClosed) {
                                drawerState.open()
                            } else {
                                drawerState.close()
                            }
                        }
                    }
                )
            }

            // Carrito
            composable("carrito") {
                CarritoScreen(
                    viewModel = viewModel,
                    onVolverAlCatalogo = {
                        navController.navigate("catalogo") {
                            popUpTo("catalogo") { inclusive = false }
                        }
                    },
                    onConfirmarPago = {
                        navController.navigate("pago")
                    }
                )
            }

            // Perfil Cliente
            composable("perfil") {
                PerfilClienteScreen(
                    nombre = nombreUsuario,
                    onBack = {
                        navController.popBackStack()
                    },
                    onLogout = onLogout
                )
            }

            // Pago/Confirmación
            composable("pago") {
                PagoConfirmacionScreen(
                    nombreUsuario = nombreUsuario,
                    onVolverAlPerfil = {
                        navController.navigate("catalogo") {
                            popUpTo("catalogo") { inclusive = true }
                        }
                    }
                )
            }

            // Detalle Producto
            composable(
                "detalle_producto/{productoId}",
                arguments = listOf(navArgument("productoId") { type = NavType.StringType })
            ) { backStackEntry ->
                val productoId = backStackEntry.arguments?.getString("productoId") ?: ""
                val producto = viewModel.productos.collectAsState().value.find { it.id == productoId }

                if (producto != null) {
                    DetalleProductoScreen(
                        producto = producto,
                        onVolverAlCatalogo = { navController.popBackStack() },
                        onAgregarAlCarrito = {
                            viewModel.agregarAlCarrito(producto)
                        }
                    )
                }
            }

            // Historial
            composable("historial") {
                // HistorialScreen(...) cuando la tengas
                // Por ahora muestra un placeholder o deja vacío
            }
        }
    }
}