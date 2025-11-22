package com.example.levelup.ui.screens

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.levelup.ui.components.DrawerContent
import com.example.levelup.ui.screens.catalogo.CatalogoScreen
import com.example.levelup.ui.screens.carrito.CarritoScreen
import com.example.levelup.ui.screens.deseados.DeseadosScreen
import com.example.levelup.ui.screens.perfil.PerfilClienteScreen
import com.example.levelup.ui.screens.historial.HistorialScreen
import com.example.levelup.ui.screens.pago.PagoConfirmacionScreen
import com.example.levelup.viewmodel.CarritoViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenWithDrawer(
    viewModel: CarritoViewModel,
    nombreUsuario: String = "Cliente",
    onLogout: () -> Unit = {},
    onNavigateToDetail: (String) -> Unit,
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
                onNavigateToCatalogo = { scope.launch { navController.navigate("catalogo") { popUpTo("catalogo") { inclusive = true } }; drawerState.close() } },
                onNavigateToDeseados = { scope.launch { navController.navigate("deseados"); drawerState.close() } },
                onNavigateToCarrito = { scope.launch { navController.navigate("carrito"); drawerState.close() } },
                onNavigateToPerfil = { scope.launch { navController.navigate("perfil"); drawerState.close() } },
                onNavigateToHistorial = { scope.launch { navController.navigate("historial"); drawerState.close() } },
                carritoCount = carrito.size,
                deseadosCount = deseados.size
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = "catalogo"
        ) {
            composable("catalogo") {
                CatalogoScreen(
                    viewModel = viewModel,
                    onProductoClick = { productoId -> onNavigateToDetail(productoId) },
                    onToggleDrawer = { scope.launch { if (drawerState.isClosed) drawerState.open() else drawerState.close() } }
                )
            }

            composable("deseados") {
                DeseadosScreen(
                    viewModel = viewModel,
                    navController = navController,
                    onVerCarrito = { scope.launch { navController.navigate("carrito") } },
                    onToggleDrawer = { scope.launch { if (drawerState.isClosed) drawerState.open() else drawerState.close() } }
                )
            }

            composable("carrito") {
                CarritoScreen(
                    viewModel = viewModel,
                    usuarioId = nombreUsuario,  // ← LÍNEA AGREGADA
                    onVolverAlCatalogo = { navController.navigate("catalogo") { popUpTo("catalogo") { inclusive = false } } },
                    onConfirmarPago = { navController.navigate("pago") }
                )
            }

            composable("perfil") {
                PerfilClienteScreen(
                    nombre = nombreUsuario,
                    onBack = { navController.popBackStack() },
                    onLogout = onLogout
                )
            }

            composable("historial") {
                HistorialScreen(
                    viewModel = viewModel,
                    onToggleDrawer = { scope.launch { if (drawerState.isClosed) drawerState.open() else drawerState.close() } },
                    onBack = { navController.popBackStack() }
                )
            }

            composable("pago") {
                PagoConfirmacionScreen(
                    nombreUsuario = nombreUsuario,
                    onVolverAlPerfil = { navController.navigate("catalogo") { popUpTo("catalogo") { inclusive = true } } }
                )
            }
        }
    }
}