package com.example.levelup.ui.screens

import android.app.Activity
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
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
import com.example.levelup.viewmodel.AuthState
import com.example.levelup.viewmodel.AuthViewModel
import com.example.levelup.viewmodel.CarritoViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenWithDrawer(
    authViewModel: AuthViewModel, // PASO 1: ACEPTAMOS EL VIEWMODEL
    carritoViewModel: CarritoViewModel,
    onLogout: () -> Unit = {},
    onNavigateToDetail: (String) -> Unit,
    onCleanDatabase: () -> Unit,
    onNavigateToEditProfile: () -> Unit,
    navController: NavHostController = rememberNavController()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "catalogo"

    val carrito by carritoViewModel.carrito.collectAsState()
    val deseados by carritoViewModel.deseados.collectAsState()

    // PASO 2: OBSERVAMOS EL ESTADO DE AUTENTICACIÓN
    val authState by authViewModel.authState.collectAsState()
    val (nombreUsuario, esAdmin) = when (val state = authState) {
        is AuthState.Authenticated -> state.user.nombre to state.user.isAdmin
        else -> "Cliente" to false
    }

    // Control status bar color
    val view = LocalView.current
    val window = (view.context as? Activity)?.window
    val newColor = Color(0xFF00FFAA)

    LaunchedEffect(drawerState.isOpen) {
        if (drawerState.isOpen) {
            window?.statusBarColor = newColor.toArgb()
        } else {
            // Revert to transparent when drawer is closed for edge-to-edge
            window?.statusBarColor = Color.Transparent.toArgb()
        }
    }

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
                deseadosCount = deseados.size,
                isAdmin = esAdmin,
                onCleanDatabase = onCleanDatabase
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = "catalogo"
        ) {
            composable("catalogo") {
                CatalogoScreen(
                    viewModel = carritoViewModel,
                    onProductoClick = { productoId -> onNavigateToDetail(productoId) },
                    onToggleDrawer = { scope.launch { if (drawerState.isClosed) drawerState.open() else drawerState.close() } }
                )
            }

            composable("deseados") {
                DeseadosScreen(
                    viewModel = carritoViewModel,
                    navController = navController,
                    onVerCarrito = { scope.launch { navController.navigate("carrito") } },
                    onToggleDrawer = { scope.launch { if (drawerState.isClosed) drawerState.open() else drawerState.close() } }
                )
            }

            composable("carrito") {
                CarritoScreen(
                    viewModel = carritoViewModel,
                    usuarioId = nombreUsuario,
                    onVolverAlCatalogo = { navController.navigate("catalogo") { popUpTo("catalogo") { inclusive = false } } },
                    onConfirmarPago = { navController.navigate("pago") }
                )
            }

            composable("perfil") {
                PerfilClienteScreen(
                    nombre = nombreUsuario, // PASO 3: USAMOS EL NOMBRE REACTIVO
                    onBack = { navController.popBackStack() },
                    onLogout = onLogout,
                    onEditProfile = onNavigateToEditProfile,
                    onMyOrders = { navController.navigate("historial") },
                    onWishlist = { navController.navigate("deseados") }
                )
            }

            composable("historial") {
                HistorialScreen(
                    viewModel = carritoViewModel,
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