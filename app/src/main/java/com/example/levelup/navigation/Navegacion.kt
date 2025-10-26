package com.example.levelup.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.levelup.ui.screens.carrito.CarritoScreen
import com.example.levelup.ui.screens.catalogo.CatalogoScreen
import com.example.levelup.ui.screens.login.LoginScreen
import com.example.levelup.ui.screens.pago.PagoConfirmacionScreen
import com.example.levelup.ui.screens.registro.RegistroScreen
import com.example.levelup.ui.screens.perfil.PerfilAdminScreen
import com.example.levelup.ui.screens.perfil.PerfilClienteScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelup.viewmodel.CarritoViewModel

@Composable
fun AppNavegacion() {
    val navController = rememberNavController()
    val carritoViewModel: CarritoViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        //Login
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
                        else -> navController.navigate("catalogo/${user.nombre}") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
            )
        }

        //Registro
        composable("register") {
            RegistroScreen(
                onBack = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.popBackStack()
                }
            )
        }

        //Catálogo (pantalla principal para clientes)
        composable(
            "catalogo/{nombre}",
            arguments = listOf(navArgument("nombre") { type = NavType.StringType })
        ) { backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre") ?: "Cliente"
            CatalogoScreen(
                viewModel = carritoViewModel,
                onVerCarrito = {
                    navController.navigate("carrito/$nombre")
                },
                onVerPerfil = {
                    navController.navigate("perfil_cliente/$nombre")
                }
            )
        }

        //Carrito
        composable(
            "carrito/{nombre}",
            arguments = listOf(navArgument("nombre") { type = NavType.StringType })
        ) { backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre") ?: "Cliente"
            CarritoScreen(
                viewModel = carritoViewModel,
                onVolverAlCatalogo = {
                    navController.popBackStack()
                },
                onConfirmarPago = {
                    navController.navigate("pago/$nombre")
                }
            )
        }

        //Pago/Confirmación
        composable(
            "pago/{nombre}",
            arguments = listOf(navArgument("nombre") { type = NavType.StringType })
        ) { backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre") ?: "Cliente"
            PagoConfirmacionScreen(
                nombreUsuario = nombre,
                onVolverAlPerfil = {
                    navController.navigate("catalogo/$nombre") {
                        popUpTo("catalogo/$nombre") { inclusive = true }
                    }
                }
            )
        }

        //Perfil Cliente
        composable(
            "perfil_cliente/{nombre}",
            arguments = listOf(navArgument("nombre") { type = NavType.StringType })
        ) { backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre") ?: "Cliente"
            PerfilClienteScreen(
                nombre = nombre,
                onBack = {
                    navController.popBackStack()
                },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        //Perfil Admin
        composable(
            "perfil_admin/{nombre}",
            arguments = listOf(navArgument("nombre") { type = NavType.StringType })
        ) { backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre") ?: "Administrador"
            PerfilAdminScreen(
                nombre = nombre,
                onLogout = {
                    // Volver al login limpiando el back stack
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}