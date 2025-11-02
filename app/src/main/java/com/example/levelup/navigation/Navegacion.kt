package com.example.levelup.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.levelup.ui.screens.carrito.CarritoScreen
import com.example.levelup.ui.screens.catalogo.DetalleProductoScreen
import com.example.levelup.ui.screens.login.LoginScreen
import com.example.levelup.ui.screens.pago.PagoConfirmacionScreen
import com.example.levelup.ui.screens.perfil.PerfilAdminScreen
import com.example.levelup.ui.screens.perfil.PerfilClienteScreen
import com.example.levelup.ui.screens.registro.RegistroScreen
import com.example.levelup.viewmodel.CarritoViewModel
import com.example.levelup.ui.screens.MainScreenWithDrawer

@Composable
fun AppNavegacion() {
    val navController = rememberNavController()
    val carritoViewModel: CarritoViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // Login
        composable("login") {
            LoginScreen(
                onRegisterClick = {
                    navController.navigate("register")
                },
                onLoginSuccess = { user ->
                    // Navegar según el rol pasando el nombre como parámetro
                    when (user.rol) {
                        "admin" -> navController.navigate("perfil_admin/${user.nombre}") {
                            popUpTo("login") { inclusive = true }
                        }
                        else -> navController.navigate("main/${user.nombre}") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
            )
        }

        // Registro
        composable("register") {
            RegistroScreen(
                onBack = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.popBackStack()
                }
            )
        }

        // 🆕 APP PRINCIPAL CON DRAWER (Catálogo, Deseados, Carrito, Perfil)
        composable(
            "main/{nombre}",
            arguments = listOf(navArgument("nombre") { type = NavType.StringType })
        ) { backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre") ?: "Cliente"

            // Aquí va el MainScreenWithDrawer que incluye navegación interna
            MainScreenWithDrawer(
                viewModel = carritoViewModel,
                nombreUsuario = nombre,
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // Detalle Producto (se puede acceder desde el drawer también)
        composable(
            "detalle_producto/{productoId}",
            arguments = listOf(navArgument("productoId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productoId = backStackEntry.arguments?.getString("productoId") ?: ""
            val producto = carritoViewModel.productos.collectAsState().value.find { it.id == productoId }

            if (producto != null) {
                DetalleProductoScreen(
                    producto = producto,
                    onVolverAlCatalogo = { navController.popBackStack() },
                    onAgregarAlCarrito = {
                        carritoViewModel.agregarAlCarrito(producto)
                    }
                )
            } else {
                Text("Producto no encontrado")
            }
        }

        // Perfil Admin (sin cambios)
        composable(
            "perfil_admin/{nombre}",
            arguments = listOf(navArgument("nombre") { type = NavType.StringType })
        ) { backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre") ?: "Administrador"
            PerfilAdminScreen(
                nombre = nombre,
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}

/*
 * NOTA: Las rutas individuales de "catalogo", "carrito", "perfil_cliente", etc.
 * ahora están DENTRO de MainScreenWithDrawer, que maneja su propia navegación interna.
 *
 * MainScreenWithDrawer incluye:
 * - Catálogo (con buscador)
 * - Deseados
 * - Carrito
 * - Perfil Cliente
 * - Historial (si lo tienes)
 *
 * Y todas tienen acceso al Drawer (sidebar) para navegar entre ellas.
 */