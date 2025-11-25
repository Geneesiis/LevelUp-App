package com.example.levelup.ui.screens.catalogo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.levelup.ui.screens.catalogo.components.CatalogoHeader
import com.example.levelup.ui.screens.catalogo.components.EmptyState
import com.example.levelup.ui.screens.catalogo.components.LoadingState
import com.example.levelup.ui.screens.catalogo.components.ProductoCard
import com.example.levelup.viewmodel.CarritoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(
    viewModel: CarritoViewModel,
    onProductoClick: (String) -> Unit,
    onToggleDrawer: () -> Unit
) {
    val productos by viewModel.productos.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val deseados by viewModel.deseados.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarProductosDesdeApi()
    }

    Scaffold(
        topBar = {
            CatalogoHeader(
                searchQuery = searchQuery,
                onSearchQueryChange = { viewModel.onSearchQueryChanged(it) },
                onToggleDrawer = onToggleDrawer
            )
        },
        containerColor = Color(0xFF1E1E1E)
    ) { paddingValues ->
        if (productos.isEmpty() && searchQuery.isBlank()) {
            LoadingState()
        } else if (productos.isEmpty() && searchQuery.isNotBlank()) {
            EmptyState()
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(productos, key = { it.id }) { producto ->
                    ProductoCard(
                        producto = producto,
                        isDeseado = deseados.any { it.id == producto.id },
                        onProductoClick = { onProductoClick(producto.id) },
                        onAddToDeseados = { viewModel.toggleDeseado(producto) }
                    )
                }
            }
        }
    }
}
