package com.example.levelup.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.levelup.di.AppContainer
import com.example.levelup.ui.screens.MainScreenWithDrawer
import com.example.levelup.ui.screens.admin.PedidosAdminScreen
import com.example.levelup.ui.screens.admin.PerfilAdminScreen
import com.example.levelup.ui.screens.admin.ProductosAdminScreen
import com.example.levelup.ui.screens.catalogo.DetalleProductoScreen
import com.example.levelup.ui.screens.login.LoginScreen
import com.example.levelup.ui.screens.registro.RegistroScreen
import com.example.levelup.ui.viewmodels.ViewModelFactory
import com.example.levelup.viewmodel.AuthState
import com.example.levelup.viewmodel.AuthViewModel
import com.example.levelup.viewmodel.CarritoViewModel

@Composable
fun AppNavegacion(container: AppContainer) {
    val navController = rememberNavController()

    val factory = ViewModelFactory(
        authRepository = container.authRepository,
        usuarioRepository = container.usuarioRepository,
        productoRepository = container.productoRepository,
        pedidoRepository = container.pedidoRepository
    )

    val authViewModel: AuthViewModel = viewModel(factory = factory)
    val carritoViewModel: CarritoViewModel = viewModel(factory = factory)

    val authState by authViewModel.authState.collectAsState()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(authState = authState, onNavigate = { route ->
                navController.navigate(route) {
                    popUpTo("splash") { inclusive = true }
                }
            })
        }

        composable("login") {
            LoginScreen(
                authViewModel = authViewModel,
                onRegisterClick = { navController.navigate("register") },
                onLoginSuccess = { user ->
                    val route = if (user.isAdmin) "perfil_admin/${user.nombre}" else "main/${user.nombre}"
                    navController.navigate(route) {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("register") {
            RegistroScreen(
                authViewModel = authViewModel,
                onBack = { navController.popBackStack() },
                onRegisterSuccess = { user ->
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable(
            "main/{nombre}",
            arguments = listOf(navArgument("nombre") { type = NavType.StringType })
        ) { backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre") ?: "Cliente"
            MainScreenWithDrawer(
                viewModel = carritoViewModel,
                nombreUsuario = nombre,
                onLogout = {
                    authViewModel.logout()
                    navController.navigate("login") { popUpTo(0) }
                },
                onNavigateToDetail = { productoId ->
                    navController.navigate("detalle_producto/$productoId")
                }
            )
        }

        composable(
            "detalle_producto/{productoId}",
            arguments = listOf(navArgument("productoId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productoId = backStackEntry.arguments?.getString("productoId")
            val productos by carritoViewModel.productos.collectAsState()
            val producto = productos.find { it.id == productoId }

            if (producto != null) {
                DetalleProductoScreen(
                    producto = producto,
                    viewModel = carritoViewModel,
                    onBack = { navController.popBackStack() }
                )
            } else {
                // Si el producto no se encuentra (por ejemplo, al volver a esta pantalla
                // después de un tiempo), simplemente volvemos atrás.
                LaunchedEffect(Unit) {
                    navController.popBackStack()
                }
            }
        }

        composable(
            "perfil_admin/{nombre}",
            arguments = listOf(navArgument("nombre") { type = NavType.StringType })
        ) { backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre") ?: "Admin"
            PerfilAdminScreen(
                viewModel = carritoViewModel,
                nombre = nombre,
                onLogout = {
                    authViewModel.logout()
                    navController.navigate("login") { popUpTo(0) }
                },
                onNavigateToProductos = { navController.navigate("admin_productos") },
                onNavigateToPedidos = { navController.navigate("admin_pedidos") }
            )
        }

        composable("admin_productos") {
            ProductosAdminScreen(
                viewModel = carritoViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable("admin_pedidos") {
            PedidosAdminScreen(
                viewModel = carritoViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun SplashScreen(authState: AuthState, onNavigate: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        LaunchedEffect(authState) {
            when (authState) {
                is AuthState.Authenticated -> {
                    val user = authState.user
                    val route = if (user.isAdmin) "perfil_admin/${user.nombre}" else "main/${user.nombre}"
                    onNavigate(route)
                }
                is AuthState.Unauthenticated -> {
                    onNavigate("login")
                }
                is AuthState.Error -> {
                    onNavigate("login") // O a una pantalla de error si prefieres
                }
                AuthState.Loading -> {
                    // No hagas nada, solo muestra el indicador de carga
                }
            }
        }
        CircularProgressIndicator(color = Color(0xFF00FFAA))
    }
}
