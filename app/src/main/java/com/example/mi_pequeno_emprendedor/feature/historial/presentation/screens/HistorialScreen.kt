
package com.example.mi_pequeno_emprendedor.feature.historial.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
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

import com.example.mi_pequeno_emprendedor.feature.inventario.domain.model.MovimientoInventario
import com.example.mi_pequeno_emprendedor.feature.inventario.domain.model.TipoMovimiento
import com.example.mi_pequeno_emprendedor.feature.inventario.presentation.viewmodel.InventarioViewModel
import com.example.mi_pequeno_emprendedor.ui.components.NavegacionInferior

private val AzulHistorial = Color(0xFF0066E8)
private val AzulOscuroHistorial = Color(0xFF003BA6)
private val TextoHistorial = Color(0xFF101560)
private val GrisHistorial = Color(0xFF667092)
private val VerdeHistorial = Color(0xFF008E57)
private val RojoHistorial = Color(0xFFFF564B)
private val BordeHistorial = Color(0xFFE4EBF6)

@Composable
fun HistorialScreen(
    inventarioViewModel: InventarioViewModel,
    alVolver: () -> Unit,
    alAbrirProducto: (Int) -> Unit = {},
    alCambiarSeccion: (String) -> Unit = {}
) {
    var busqueda by rememberSaveable {
        mutableStateOf("")
    }

    var filtro by rememberSaveable {
        mutableStateOf("Todos")
    }

    var mostrarFiltros by remember {
        mutableStateOf(false)
    }

    val movimientos = inventarioViewModel.movimientos

    val totalEntradas = movimientos.count {
        it.tipo == TipoMovimiento.ENTRADA
    }

    val totalBajas = movimientos.count {
        it.tipo == TipoMovimiento.BAJA
    }

    val totalAjustes = movimientos.count {
        it.tipo == TipoMovimiento.AJUSTE
    }

    val movimientosFiltrados = movimientos.filter { movimiento ->
        val coincideBusqueda = movimiento.nombreProducto.contains(
            busqueda.trim(),
            ignoreCase = true
        )

        val coincideTipo = when (filtro) {
            "Entradas" -> movimiento.tipo == TipoMovimiento.ENTRADA
            "Bajas" -> movimiento.tipo == TipoMovimiento.BAJA
            "Ajustes" -> movimiento.tipo == TipoMovimiento.AJUSTE
            else -> true
        }

        coincideBusqueda && coincideTipo
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
    ) { paddingInterno ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno)
                .background(Color.White)
                .verticalScroll(rememberScrollState())
        ) {

            EncabezadoHistorial()

            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {

                Surface(
                    onClick = alVolver,
                    shape = RoundedCornerShape(13.dp),
                    color = Color(0xFFF0F5FF)
                ) {
                    Row(
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 10.dp
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = AzulHistorial
                        )

                        Spacer(Modifier.width(9.dp))

                        Text(
                            text = "Volver a Inicio",
                            color = AzulHistorial,
                            fontSize = 14.sp
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IndicadorHistorial(
                        titulo = "Entradas",
                        cantidad = totalEntradas,
                        color = VerdeHistorial,
                        modifier = Modifier.weight(1f)
                    )

                    IndicadorHistorial(
                        titulo = "Bajas",
                        cantidad = totalBajas,
                        color = RojoHistorial,
                        modifier = Modifier.weight(1f)
                    )

                    IndicadorHistorial(
                        titulo = "Ajustes",
                        cantidad = totalAjustes,
                        color = AzulHistorial,
                        modifier = Modifier.weight(1f)
                    )
                }

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFEAF3FF),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Inventory2,
                            contentDescription = null,
                            tint = AzulHistorial
                        )

                        Spacer(Modifier.width(12.dp))

                        Text(
                            text = "Consulta los movimientos registrados en el inventario.",
                            color = Color(0xFF425ED0),
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = busqueda,
                        onValueChange = {
                            busqueda = it
                        },
                        modifier = Modifier.weight(1f),
                        placeholder = {
                            Text(
                                "Buscar producto",
                                fontSize = 13.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = AzulHistorial
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF0F5FF),
                            unfocusedContainerColor = Color(0xFFF0F5FF),
                            focusedBorderColor = AzulHistorial,
                            unfocusedBorderColor = Color.Transparent
                        )
                    )

                    Box {
                        TextButton(
                            onClick = { mostrarFiltros = true }
                        ) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = null
                            )
                            Text("Filtrar")
                        }

                        DropdownMenu(
                            expanded = mostrarFiltros,
                            onDismissRequest = {
                                mostrarFiltros = false
                            }
                        ) {
                            listOf(
                                "Todos",
                                "Entradas",
                                "Bajas",
                                "Ajustes"
                            ).forEach { opcion ->
                                DropdownMenuItem(
                                    text = { Text(opcion) },
                                    onClick = {
                                        filtro = opcion
                                        mostrarFiltros = false
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
                        text = "Historial de movimientos",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoHistorial
                    )

                    Text(
                        text = "${movimientosFiltrados.size} registros",
                        fontSize = 12.sp,
                        color = GrisHistorial
                    )
                }

                if (movimientosFiltrados.isEmpty()) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFFF7FAFF),
                        border = BorderStroke(1.dp, BordeHistorial),
                        shape = RoundedCornerShape(15.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SwapVert,
                                contentDescription = null,
                                tint = AzulHistorial,
                                modifier = Modifier.size(35.dp)
                            )

                            Text(
                                text = if (movimientos.isEmpty()) {
                                    "Todavía no hay movimientos"
                                } else {
                                    "No hay resultados"
                                },
                                fontWeight = FontWeight.Bold,
                                color = TextoHistorial
                            )

                            Text(
                                text = if (movimientos.isEmpty()) {
                                    "Registra una entrada, baja o ajuste de inventario para verlo aquí."
                                } else {
                                    "Prueba otra búsqueda o cambia el filtro."
                                },
                                textAlign = TextAlign.Center,
                                fontSize = 13.sp,
                                color = GrisHistorial
                            )
                        }
                    }
                } else {
                    movimientosFiltrados.forEach { movimiento ->
                        TarjetaMovimientoHistorial(
                            movimiento = movimiento,
                            alAbrir = {
                                alAbrirProducto(movimiento.productoId)
                            }
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

// =====================================
// ENCABEZADO
// =====================================

@Composable
private fun EncabezadoHistorial() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
            .background(
                Brush.linearGradient(
                    listOf(
                        AzulHistorial,
                        AzulOscuroHistorial
                    )
                )
            )
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val ancho = size.width
            val alto = size.height

            val onda = Path().apply {
                moveTo(0f, alto * 0.78f)

                cubicTo(
                    ancho * 0.25f, alto * 0.65f,
                    ancho * 0.33f, alto * 1.05f,
                    ancho * 0.57f, alto * 0.86f
                )

                cubicTo(
                    ancho * 0.80f, alto * 0.63f,
                    ancho * 0.89f, alto * 0.82f,
                    ancho, alto * 0.69f
                )

                lineTo(ancho, alto)
                lineTo(0f, alto)
                close()
            }

            drawPath(
                path = onda,
                color = Color(0xFF49A2FF).copy(alpha = 0.43f)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(
                    horizontal = 20.dp,
                    vertical = 18.dp
                ),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = "Historial",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Movimientos de inventario",
                color = Color(0xFFE0EDFF),
                fontSize = 16.sp
            )
        }
    }
}

// =====================================
// INDICADORES
// =====================================

@Composable
private fun IndicadorHistorial(
    titulo: String,
    cantidad: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(105.dp),
        color = Color.White,
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(1.dp, BordeHistorial),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = titulo,
                color = TextoHistorial,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = cantidad.toString(),
                color = color,
                fontSize = 29.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// =====================================
// MOVIMIENTO INDIVIDUAL
// =====================================

@Composable
private fun TarjetaMovimientoHistorial(
    movimiento: MovimientoInventario,
    alAbrir: () -> Unit
) {
    val entrada = movimiento.cantidad > 0

    val colorMovimiento = if (entrada) {
        VerdeHistorial
    } else {
        RojoHistorial
    }

    val titulo = when (movimiento.tipo) {
        TipoMovimiento.ENTRADA -> "Entrada de mercancía"
        TipoMovimiento.BAJA -> "Baja de mercancía"
        TipoMovimiento.AJUSTE -> "Ajuste de inventario"
    }

    Surface(
        onClick = alAbrir,
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(1.dp, BordeHistorial),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(47.dp)
                    .background(
                        colorMovimiento.copy(alpha = 0.10f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (entrada) {
                        Icons.Default.TrendingUp
                    } else {
                        Icons.Default.TrendingDown
                    },
                    contentDescription = null,
                    tint = colorMovimiento
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = movimiento.nombreProducto,
                    color = TextoHistorial,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = titulo,
                    color = GrisHistorial,
                    fontSize = 12.sp
                )

                Text(
                    text = movimiento.fecha,
                    color = Color(0xFF716FE3),
                    fontSize = 11.sp
                )

                Text(
                    text = "${movimiento.existenciasAnteriores} → " +
                            "${movimiento.existenciasNuevas} unidades",
                    color = GrisHistorial,
                    fontSize = 11.sp
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    color = colorMovimiento.copy(alpha = 0.11f),
                    shape = CircleShape
                ) {
                    Text(
                        text = if (entrada) {
                            "+${movimiento.cantidad} uds"
                        } else {
                            "${movimiento.cantidad} uds"
                        },
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 6.dp
                        ),
                        color = colorMovimiento,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Ver producto",
                    tint = Color(0xFF6059D5)
                )
            }
        }
    }
}
