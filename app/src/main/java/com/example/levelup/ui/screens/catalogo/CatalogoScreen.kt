package com.example.levelup.ui.screens.catalogo

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.levelup.viewmodel.CarritoViewModel
import com.example.levelup.ui.screens.catalogo.components.ProductoCard
import com.example.levelup.ui.screens.catalogo.models.ProductoUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(
    viewModel: CarritoViewModel,
    navController: NavController,
    onVerCarrito: () -> Unit = {},
    onVerPerfil: () -> Unit = {},
    onConfirmarPago: () -> Unit = {}
) {
    val productos by viewModel.productos.collectAsState()
    val cargando by viewModel.cargando.collectAsState()
    val carrito by viewModel.carrito.collectAsState()

    // Categorías disponibles
    var categoriaSeleccionada by remember { mutableStateOf("TODOS") }
    val categorias = listOf("TODOS", "CONSOLAS", "ACCESORIOS", "JUEGOS", "PC GAMING")

    // Filtrar productos por categoría
    val productosFiltrados = remember(productos, categoriaSeleccionada) {
        if (categoriaSeleccionada == "TODOS") {
            productos
        } else {
            productos
        }
    }

    // Animación de pulsación para efectos
    val infiniteTransition = rememberInfiniteTransition(label = "background")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF000000),
                        Color(0xFF0A0A0A),
                        Color(0xFF050505),
                        Color(0xFF000000)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 50.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
        ) {
            // Header mejorado
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(16.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF0D0D0D).copy(alpha = 0.98f)
            ) {
                Box(
                    modifier = Modifier
                        .border(
                            2.dp,
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF00FFAA).copy(alpha = glowAlpha),
                                    Color(0xFF00AAFF).copy(alpha = glowAlpha * 0.8f),
                                    Color(0xFFAA00FF).copy(alpha = glowAlpha * 0.6f),
                                    Color(0xFF00FFAA).copy(alpha = glowAlpha)
                                )
                            ),
                            RoundedCornerShape(16.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        // Fila superior: Logo y botones
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // Icono decorativo
                                    Surface(
                                        shape = CircleShape,
                                        color = Color(0xFF00FFAA).copy(alpha = 0.2f),
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                Icons.Default.Star,
                                                contentDescription = null,
                                                tint = Color(0xFF00FFAA),
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }

                                    Column {
                                        Text(
                                            "TECH STORE",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontSize = 11.sp,
                                            color = Color(0xFF00FFAA).copy(alpha = 0.7f),
                                            fontWeight = FontWeight.Light,
                                            letterSpacing = 3.sp
                                        )
                                        Text(
                                            "LEVEL - UP",
                                            style = MaterialTheme.typography.headlineSmall,
                                            fontSize = 20.sp,
                                            color = Color(0xFF00FFAA),
                                            fontWeight = FontWeight.Black,
                                            letterSpacing = 2.sp
                                        )
                                    }
                                }
                            }

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                // Botón Perfil
                                Surface(
                                    modifier = Modifier.size(48.dp),
                                    shape = CircleShape,
                                    color = Color(0xFF1A1A1A),
                                    onClick = onVerPerfil
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.border(
                                            1.dp,
                                            Color(0xFF00FFAA).copy(alpha = 0.4f),
                                            CircleShape
                                        )
                                    ) {
                                        Icon(
                                            Icons.Default.Person,
                                            contentDescription = "Perfil",
                                            tint = Color(0xFF00FFAA),
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }

                                // Botón del carrito con badge
                                Surface(
                                    modifier = Modifier.size(48.dp),
                                    shape = CircleShape,
                                    color = Color(0xFF1A1A1A),
                                    onClick = onVerCarrito
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.border(
                                            1.dp,
                                            Color(0xFF00FFAA).copy(alpha = 0.4f),
                                            CircleShape
                                        )
                                    ) {
                                        BadgedBox(
                                            badge = {
                                                if (carrito.isNotEmpty()) {
                                                    Badge(
                                                        containerColor = Color(0xFFFF0055),
                                                        modifier = Modifier.shadow(6.dp, CircleShape)
                                                    ) {
                                                        Text(
                                                            carrito.sumOf { it.cantidad }.toString(),
                                                            color = Color.White,
                                                            fontWeight = FontWeight.Black,
                                                            fontSize = 11.sp
                                                        )
                                                    }
                                                }
                                            }
                                        ) {
                                            Icon(
                                                Icons.Default.ShoppingCart,
                                                contentDescription = "Carrito",
                                                tint = Color(0xFF00FFAA),
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Barra de stats
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF1A1A1A).copy(alpha = 0.6f)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Total productos
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        Icons.Default.ShoppingCart,
                                        contentDescription = null,
                                        tint = Color(0xFF00FFAA).copy(alpha = 0.6f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        "${productos.size} productos",
                                        fontSize = 12.sp,
                                        color = Color(0xFF888888),
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                // Separador
                                Box(
                                    modifier = Modifier
                                        .width(1.dp)
                                        .height(16.dp)
                                        .background(Color(0xFF333333))
                                )

                                // Items en carrito
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = if (carrito.isNotEmpty())
                                            Color(0xFF00FFAA).copy(alpha = 0.6f)
                                        else
                                            Color(0xFF555555),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        "Carrito: ${carrito.sumOf { it.cantidad }}",
                                        fontSize = 12.sp,
                                        color = if (carrito.isNotEmpty())
                                            Color(0xFF00FFAA)
                                        else
                                            Color(0xFF888888),
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                // Separador
                                Box(
                                    modifier = Modifier
                                        .width(1.dp)
                                        .height(16.dp)
                                        .background(Color(0xFF333333))
                                )

                                // Status online
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .background(
                                                Color(0xFF00FF00).copy(alpha = glowAlpha),
                                                CircleShape
                                            )
                                    )
                                    Text(
                                        "ONLINE",
                                        fontSize = 11.sp,
                                        color = Color(0xFF00FF00).copy(alpha = 0.8f),
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Sección de Categorías con scroll horizontal
            if (!cargando && productos.isNotEmpty()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Título de la sección
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFF00FFAA),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            "CATEGORÍAS",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF00FFAA),
                            letterSpacing = 2.sp
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(1.dp)
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(0xFF00FFAA).copy(alpha = 0.5f),
                                            Color.Transparent
                                        )
                                    )
                                )
                        )
                    }

                    // Chips de categorías con scroll horizontal
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        categorias.forEach { categoria ->
                            val isSelected = categoria == categoriaSeleccionada
                            FilterChip(
                                selected = isSelected,
                                onClick = { categoriaSeleccionada = categoria },
                                label = {
                                    Text(
                                        categoria,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.5.sp
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF00FFAA),
                                    selectedLabelColor = Color.Black,
                                    containerColor = Color(0xFF1A1A1A),
                                    labelColor = Color(0xFF888888)
                                ),
                                border = null,
                                leadingIcon = if (isSelected) {
                                    {
                                        Icon(
                                            Icons.Default.Star,
                                            contentDescription = null,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                } else null,
                                modifier = Modifier.border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected)
                                        Color(0xFF00FFAA)
                                    else
                                        Color(0xFF00FFAA).copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Subtítulo de productos
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.ShoppingCart,
                        contentDescription = null,
                        tint = Color(0xFF00FFAA),
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        "PRODUCTOS DISPONIBLES",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF00FFAA),
                        letterSpacing = 2.sp
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFF00FFAA).copy(alpha = 0.5f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            if (cargando) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                color = Color(0xFF00FFAA),
                                strokeWidth = 3.dp,
                                modifier = Modifier.size(56.dp)
                            )
                            Icon(
                                Icons.Default.ShoppingCart,
                                contentDescription = null,
                                tint = Color(0xFF00FFAA).copy(alpha = 0.3f),
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                "CARGANDO ARSENAL",
                                color = Color(0xFF00FFAA),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 2.sp
                            )
                            Text(
                                "Preparando los mejores productos...",
                                color = Color(0xFF666666),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            } else {
                if (productos.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color(0xFF1A1A1A),
                            modifier = Modifier.padding(32.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.padding(40.dp)
                            ) {
                                Icon(
                                    Icons.Default.Info,
                                    contentDescription = null,
                                    tint = Color(0xFF666666),
                                    modifier = Modifier.size(64.dp)
                                )
                                Text(
                                    "NO HAY PRODUCTOS",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color(0xFF888888),
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 2.sp
                                )
                                Text(
                                    "Vuelve pronto para ver nuestro catálogo",
                                    color = Color(0xFF555555),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(productosFiltrados, key = { it.id }) { producto ->
                            val cantidadEnCarrito = carrito.find { it.producto.id == producto.id }?.cantidad ?: 0
                            val productoUi = ProductoUiModel(
                                producto = producto,
                                cantidadEnCarrito = cantidadEnCarrito,
                                stockDisponible = producto.stock - cantidadEnCarrito,
                                disponible = producto.stock > 0
                            )

                            ProductoCard(
                                productoUi = productoUi,
                                onAgregar = { viewModel.agregarAlCarrito(producto) },
                                onRemover = { viewModel.removerDelCarrito(producto) },
                                onVerDetalle = {
                                    navController.navigate("detalle_producto/${producto.id}")
                                }
                            )
                        }

                        // Footer
                        item {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFF0D0D0D).copy(alpha = 0.5f)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = Color(0xFF00FFAA).copy(alpha = 0.6f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "Has llegado al final del catálogo",
                                        fontSize = 12.sp,
                                        color = Color(0xFF666666),
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}