
package com.example.mi_pequeno_emprendedor.feature.inventario.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mi_pequeno_emprendedor.feature.inventario.presentation.viewmodel.InventarioViewModel
import com.example.mi_pequeno_emprendedor.ui.components.NavegacionInferior

private val AzulInv = Color(0xFF0066E8)
private val AzulOscuroInv = Color(0xFF003BA6)
private val TextoInv = Color(0xFF101560)
private val GrisInv = Color(0xFF667092)
private val BordeInv = Color(0xFFE4EBF6)

@Composable
fun InventarioScreen(
    inventarioViewModel: InventarioViewModel,
    alAbrirCarpeta: (String) -> Unit = {},
    alAbrirNotificaciones: () -> Unit = {},
    alCambiarSeccion: (String) -> Unit = {}
) {
    var busqueda by rememberSaveable { mutableStateOf("") }
    var filtro by rememberSaveable { mutableStateOf("Todas") }
    var menuFiltros by remember { mutableStateOf(false) }
    var mostrarDialogo by remember { mutableStateOf(false) }

    val carpetasVisibles = inventarioViewModel.carpetas.filter { nombre ->
        val cantidad = inventarioViewModel
            .obtenerProductosDeCarpeta(nombre).size

        nombre.contains(busqueda.trim(), ignoreCase = true) &&
                when (filtro) {
                    "Con productos" -> cantidad > 0
                    "Vacías" -> cantidad == 0
                    else -> true
                }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            NavegacionInferior(
                seccionSeleccionada = "Inicio",
                alCambiarSeccion = alCambiarSeccion
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .background(
                        Brush.linearGradient(
                            listOf(AzulInv, AzulOscuroInv)
                        )
                    )
            ) {
                Canvas(Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val onda = Path().apply {
                        moveTo(0f, h * 0.78f)
                        cubicTo(
                            w * 0.25f, h * 0.65f,
                            w * 0.33f, h * 1.05f,
                            w * 0.57f, h * 0.86f
                        )
                        cubicTo(
                            w * 0.80f, h * 0.63f,
                            w * 0.89f, h * 0.82f,
                            w, h * 0.69f
                        )
                        lineTo(w, h)
                        lineTo(0f, h)
                        close()
                    }
                    drawPath(
                        onda,
                        Color(0xFF49A2FF).copy(alpha = 0.43f)
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = alAbrirNotificaciones) {
                            Icon(
                                Icons.Default.NotificationsNone,
                                contentDescription = "Notificaciones",
                                tint = Color.White
                            )
                        }
                    }

                    Column(modifier = Modifier.padding(bottom = 15.dp)) {
                        Text(
                            "Inventario",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Mi Pequeño Emprendedor",
                            color = Color(0xFFE0EDFF),
                            fontSize = 16.sp
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Indicadores calculados desde el ViewModel
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IndicadorCompartido(
                        "Productos activos",
                        inventarioViewModel.productosActivos,
                        AzulInv,
                        Modifier.weight(1f)
                    )
                    IndicadorCompartido(
                        "Stock bajo",
                        inventarioViewModel.productosStockBajo,
                        Color(0xFFFF654B),
                        Modifier.weight(1f)
                    )
                    IndicadorCompartido(
                        "Agotados",
                        inventarioViewModel.productosAgotados,
                        Color(0xFFE90046),
                        Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color(0xFFEAF3FF),
                            RoundedCornerShape(14.dp)
                        )
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = AzulInv
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        "Selecciona una carpeta para administrar lotes o cajas.",
                        color = Color(0xFF425ED0),
                        fontSize = 13.sp
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = busqueda,
                        onValueChange = { busqueda = it },
                        modifier = Modifier.weight(1f),
                        placeholder = {
                            Text(
                                "Buscar carpeta",
                                fontSize = 13.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = AzulInv
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp)
                    )

                    Box {
                        TextButton(onClick = { menuFiltros = true }) {
                            Icon(
                                Icons.Default.FilterList,
                                contentDescription = null
                            )
                            Text("Filtrar")
                        }

                        DropdownMenu(
                            expanded = menuFiltros,
                            onDismissRequest = { menuFiltros = false }
                        ) {
                            listOf(
                                "Todas", "Con productos", "Vacías"
                            ).forEach { opcion ->
                                DropdownMenuItem(
                                    text = { Text(opcion) },
                                    onClick = {
                                        filtro = opcion
                                        menuFiltros = false
                                    }
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Carpetas digitales",
                        color = TextoInv,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(
                        onClick = { mostrarDialogo = true }
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null
                        )
                        Text("Nueva carpeta", fontSize = 12.sp)
                    }
                }

                if (filtro == "Todas" && busqueda.isBlank()) {
                    TarjetaCarpetaCompartida(
                        nombre = "Todos los productos",
                        cantidad = inventarioViewModel.productos.size,
                        color = Color(0xFF398CF5),
                        modifier = Modifier.fillMaxWidth(),
                        alAbrir = {
                            alAbrirCarpeta("Todos los productos")
                        }
                    )
                }

                if (carpetasVisibles.isEmpty()) {
                    Text(
                        "No se encontraron carpetas.",
                        modifier = Modifier.padding(16.dp),
                        color = GrisInv
                    )
                } else {
                    carpetasVisibles.chunked(2).forEach { fila ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement =
                                Arrangement.spacedBy(8.dp)
                        ) {
                            fila.forEach { nombre ->
                                val cantidad = inventarioViewModel
                                    .obtenerProductosDeCarpeta(nombre).size

                                val color = when (nombre) {
                                    "Botanas" -> Color(0xFFFFB93D)
                                    "Dulces" -> Color(0xFFFF7885)
                                    "Accesorios" -> Color(0xFF9E67F5)
                                    "Sin clasificar" -> Color(0xFFA8ADB5)
                                    else -> Color(0xFF398CF5)
                                }

                                TarjetaCarpetaCompartida(
                                    nombre = nombre,
                                    cantidad = cantidad,
                                    color = color,
                                    modifier = Modifier.weight(1f),
                                    alAbrir = { alAbrirCarpeta(nombre) }
                                )
                            }
                            if (fila.size == 1) {
                                Spacer(Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }

    if (mostrarDialogo) {
        var nombre by remember { mutableStateOf("") }
        val duplicado = inventarioViewModel.carpetas.any {
            it.equals(nombre.trim(), ignoreCase = true)
        } || nombre.trim().equals(
            "Todos los productos",
            ignoreCase = true
        )

        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("Nueva carpeta") },
            text = {
                Column {
                    Text("Escribe el nombre de la carpeta.")
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre") },
                        singleLine = true,
                        isError = duplicado,
                        supportingText = {
                            if (duplicado) {
                                Text("Esta carpeta ya existe.")
                            }
                        }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    enabled = nombre.isNotBlank() && !duplicado,
                    onClick = {
                        if (inventarioViewModel.agregarCarpeta(nombre)) {
                            mostrarDialogo = false
                            busqueda = ""
                            filtro = "Todas"
                        }
                    }
                ) {
                    Text("Crear")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun IndicadorCompartido(
    titulo: String,
    cantidad: Int,
    color: Color,
    modifier: Modifier
) {
    Surface(
        modifier = modifier.height(122.dp),
        shape = RoundedCornerShape(15.dp),
        color = Color.White,
        border = BorderStroke(1.dp, BordeInv),
        shadowElevation = 3.dp
    ) {
        Column(
            modifier = Modifier.padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                modifier = Modifier
                    .size(37.dp)
                    .background(
                        color.copy(alpha = 0.10f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Inventory2,
                    contentDescription = null,
                    tint = color
                )
            }

            Text(
                titulo,
                fontSize = 11.sp,
                lineHeight = 13.sp,
                color = TextoInv,
                textAlign = TextAlign.Center
            )

            Text(
                cantidad.toString(),
                color = color,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun TarjetaCarpetaCompartida(
    nombre: String,
    cantidad: Int,
    color: Color,
    modifier: Modifier = Modifier,
    alAbrir: () -> Unit
) {
    Surface(
        onClick = alAbrir,
        modifier = modifier.height(88.dp),
        color = Color.White,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, BordeInv),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(9.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Icon(
                Icons.Default.Folder,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(39.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    nombre,
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextoInv,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    "$cantidad productos",
                    fontSize = 11.sp,
                    color = Color(0xFF716FE3)
                )
            }

            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color(0xFF6059D5),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
