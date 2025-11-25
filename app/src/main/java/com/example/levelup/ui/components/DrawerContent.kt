package com.example.levelup.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.levelup.R

private data class NavDrawerItemData(
    val route: String,
    val title: String,
    val icon: @Composable () -> Unit,
    val action: () -> Unit,
    val count: Int = 0
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerContent(
    currentRoute: String,
    onNavigateToCatalogo: () -> Unit,
    onNavigateToDeseados: () -> Unit,
    onNavigateToCarrito: () -> Unit,
    onNavigateToPerfil: () -> Unit,
    onNavigateToHistorial: () -> Unit,
    onCleanDatabase: () -> Unit,
    isAdmin: Boolean,
    carritoCount: Int,
    deseadosCount: Int
) {
    ModalDrawerSheet(
        modifier = Modifier.background(Color(0xFF2C2C2C))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1E1E1E))
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1C9670))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(50.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "LevelUp",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Navigation Items
            Spacer(modifier = Modifier.height(16.dp))

            val navItems = listOf(
                NavDrawerItemData("catalogo", "Catálogo", { Icon(painterResource(id = R.drawable.ic_store), contentDescription = "Catálogo") }, onNavigateToCatalogo),
                NavDrawerItemData("deseados", "Deseados", { Icon(painterResource(id = R.drawable.ic_favorite), contentDescription = "Deseados") }, onNavigateToDeseados, deseadosCount),
                NavDrawerItemData("carrito", "Carrito", { Icon(painterResource(id = R.drawable.ic_shopping_cart), contentDescription = "Carrito") }, onNavigateToCarrito, carritoCount),
                NavDrawerItemData("perfil", "Mi Perfil", { Icon(painterResource(id = R.drawable.ic_person), contentDescription = "Mi Perfil") }, onNavigateToPerfil),
                NavDrawerItemData("historial", "Mis Pedidos", { Icon(painterResource(id = R.drawable.ic_history), contentDescription = "Mis Pedidos") }, onNavigateToHistorial)
            )

            navItems.forEach { item ->
                NavigationDrawerItem(
                    icon = item.icon,
                    label = { Text(item.title) },
                    selected = currentRoute == item.route,
                    onClick = item.action,
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                    badge = {
                        if (item.count > 0) {
                            Badge { Text(text = item.count.toString()) }
                        }
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = Color(0xFF1C9670),
                        unselectedContainerColor = Color.Transparent,
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.White,
                        selectedTextColor = Color.Black,
                        unselectedTextColor = Color.White
                    )
                )
            }

            if (isAdmin) {
                NavigationDrawerItem(
                    icon = { Icon(Icons.Filled.Delete, contentDescription = "Limpiar Base de Datos") },
                    label = { Text("Limpiar Base de Datos") },
                    selected = false,
                    onClick = { onCleanDatabase() },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = Color(0xFF1C9670),
                        unselectedContainerColor = Color.Transparent,
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.White,
                        selectedTextColor = Color.Black,
                        unselectedTextColor = Color.White
                    )
                )
            }
        }
    }
}
