package com.example.levelup.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.levelup.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerContent(
    currentRoute: String,
    onNavigateToCatalogo: () -> Unit,
    onNavigateToDeseados: () -> Unit,
    onNavigateToCarrito: () -> Unit,
    onNavigateToPerfil: () -> Unit,
    onNavigateToHistorial: () -> Unit,
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
                    .background(Color(0xFF00C853))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    tint = Color.White,
                    modifier = Modifier.size(50.dp)
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

            // Tupla para evitar errores de tipo
            val navItems = listOf(
                mapOf("route" to "catalogo", "title" to "Catálogo", "icon" to R.drawable.ic_store, "action" to onNavigateToCatalogo, "count" to 0),
                mapOf("route" to "deseados", "title" to "Deseados", "icon" to R.drawable.ic_favorite, "action" to onNavigateToDeseados, "count" to deseadosCount),
                mapOf("route" to "carrito", "title" to "Carrito", "icon" to R.drawable.ic_shopping_cart, "action" to onNavigateToCarrito, "count" to carritoCount),
                mapOf("route" to "perfil", "title" to "Mi Perfil", "icon" to R.drawable.ic_person, "action" to onNavigateToPerfil, "count" to 0),
                mapOf("route" to "historial", "title" to "Mis Pedidos", "icon" to R.drawable.ic_history, "action" to onNavigateToHistorial, "count" to 0)
            )

            navItems.forEach { item ->
                NavigationDrawerItem(
                    icon = { Icon(painterResource(id = item["icon"] as Int), contentDescription = item["title"] as String) },
                    label = { Text(item["title"] as String) },
                    selected = currentRoute == item["route"],
                    onClick = { (item["action"] as () -> Unit).invoke() },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                    badge = {
                        if (item["count"] as Int > 0) {
                            Badge { Text(text = (item["count"] as Int).toString()) }
                        }
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = Color(0xFF00C853),
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
