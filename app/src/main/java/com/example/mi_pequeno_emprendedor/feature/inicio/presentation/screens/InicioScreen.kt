
package com.example.mi_pequeno_emprendedor.feature.inicio.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.TrendingUp

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.mi_pequeno_emprendedor.ui.components.NavegacionInferior

// ==========================================
// COLORES
// ==========================================

private val AzulPrincipal = Color(0xFF0066E8)
private val AzulOscuro = Color(0xFF003BA6)
private val AzulClaro = Color(0xFF3295FF)
private val AzulTexto = Color(0xFF101560)
private val Verde = Color(0xFF00965D)
private val Coral = Color(0xFFFC6650)
private val GrisTexto = Color(0xFF7783A5)
private val BordeTarjeta = Color(0xFFE5EBF5)

// ==========================================
// PANTALLA PRINCIPAL
// ==========================================

@Composable
fun InicioScreen(
    alAbrirNotificaciones: () -> Unit = {},
    alAbrirPerfil: () -> Unit = {},
    alAbrirHistorial: () -> Unit = {},
    alAbrirInventario: () -> Unit = {},
    alVerTodasActividades: () -> Unit = {},
    alAbrirActividad: (Int) -> Unit = {},
    alRealizarVenta: () -> Unit = {},
    alCambiarSeccion: (String) -> Unit = {}
) {
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
            EncabezadoInicio(
                nombre = "Marely",
                idUsuario = "A00-001",
                alAbrirNotificaciones = alAbrirNotificaciones,
                alAbrirPerfil = alAbrirPerfil
            )

            FilaTarjetasInicio(
                alAbrirHistorial = alAbrirHistorial,
                alAbrirInventario = alAbrirInventario
            )

            ActividadReciente(
                alVerTodas = alVerTodasActividades,
                alAbrirActividad = alAbrirActividad
            )

            IndicadorRealizarVenta(
                alRealizarVenta = alRealizarVenta
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ==========================================
// ENCABEZADO
// ==========================================

@Composable
private fun EncabezadoInicio(
    nombre: String,
    idUsuario: String,
    alAbrirNotificaciones: () -> Unit,
    alAbrirPerfil: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(
                Brush.linearGradient(
                    listOf(AzulPrincipal, AzulOscuro)
                )
            )
    ) {
        OndasEncabezado()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 14.dp
                )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Notificaciones
                Box {
                    Icon(
                        imageVector = Icons.Default.NotificationsNone,
                        contentDescription = "Notificaciones",
                        tint = Color.White,
                        modifier = Modifier
                            .size(30.dp)
                            .clickable(onClick = alAbrirNotificaciones)
                    )

                    Box(
                        modifier = Modifier
                            .size(9.dp)
                            .align(Alignment.TopEnd)
                            .background(
                                Color(0xFFFF7956),
                                CircleShape
                            )
                    )
                }

                Spacer(Modifier.width(22.dp))

                // Avatar
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFF66A5FF),
                                    Color(0xFF1370F4)
                                )
                            ),
                            CircleShape
                        )
                        .clickable(onClick = alAbrirPerfil),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = nombre.firstOrNull()
                            ?.uppercaseChar()?.toString() ?: "?",
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Hola, $nombre",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(2.dp))

            Text(
                text = "ID: $idUsuario",
                color = Color(0xFFE0EDFF),
                fontSize = 17.sp
            )
        }
    }
}

// ==========================================
// ONDAS DECORATIVAS
// ==========================================

@Composable
private fun OndasEncabezado() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val ancho = size.width
        val alto = size.height

        val ondaTrasera = Path().apply {
            moveTo(0f, alto * 0.76f)

            cubicTo(
                ancho * 0.20f, alto * 0.65f,
                ancho * 0.27f, alto * 1.02f,
                ancho * 0.48f, alto * 0.83f
            )

            cubicTo(
                ancho * 0.70f, alto * 0.65f,
                ancho * 0.79f, alto * 0.52f,
                ancho, alto * 0.72f
            )

            lineTo(ancho, alto)
            lineTo(0f, alto)
            close()
        }

        drawPath(
            path = ondaTrasera,
            brush = Brush.verticalGradient(
                listOf(
                    AzulClaro.copy(alpha = 0.72f),
                    AzulPrincipal.copy(alpha = 0.35f)
                )
            )
        )

        val ondaDelantera = Path().apply {
            moveTo(0f, alto * 0.90f)

            cubicTo(
                ancho * 0.18f, alto * 0.77f,
                ancho * 0.27f, alto * 1.03f,
                ancho * 0.49f, alto * 0.90f
            )

            cubicTo(
                ancho * 0.70f, alto * 0.78f,
                ancho * 0.78f, alto * 1.03f,
                ancho, alto * 0.89f
            )

            lineTo(ancho, alto)
            lineTo(0f, alto)
            close()
        }

        drawPath(
            path = ondaDelantera,
            color = Color(0xFF66B5FF).copy(alpha = 0.30f)
        )
    }
}

// ==========================================
// CUATRO TARJETAS
// ==========================================

@Composable
private fun FilaTarjetasInicio(
    alAbrirHistorial: () -> Unit,
    alAbrirInventario: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        TarjetaResumen(
            titulo = "Ventas de hoy",
            cantidad = "$1,290.00",
            porcentaje = "10% vs ayer",
            colorPrincipal = AzulPrincipal,
            icono = {
                Icon(
                    Icons.Default.ShoppingCart,
                    contentDescription = null
                )
            },
            modifier = Modifier.weight(1f)
        )

        TarjetaResumen(
            titulo = "Ganancia estimada",
            cantidad = "$412.00",
            porcentaje = "9% vs ayer",
            colorPrincipal = Verde,
            icono = {
                Icon(
                    Icons.Default.TrendingUp,
                    contentDescription = null
                )
            },
            modifier = Modifier.weight(1f)
        )

        TarjetaAcceso(
            titulo = "Historial",
            descripcion = "Consulta tus movimientos",
            colorPrincipal = Verde,
            colorFondo = Color(0xFFEAF9F4),
            icono = {
                Icon(
                    Icons.Default.ShoppingCart,
                    contentDescription = null
                )
            },
            alHacerClick = alAbrirHistorial,
            modifier = Modifier.weight(1f)
        )

        TarjetaAcceso(
            titulo = "Inventario",
            descripcion = "Controla existencias",
            colorPrincipal = Coral,
            colorFondo = Color(0xFFFFF2EC),
            icono = {
                Icon(
                    Icons.Default.Inventory2,
                    contentDescription = null
                )
            },
            alHacerClick = alAbrirInventario,
            modifier = Modifier.weight(1f)
        )
    }
}

// ==========================================
// TARJETA RESUMEN
// ==========================================

@Composable
private fun TarjetaResumen(
    titulo: String,
    cantidad: String,
    porcentaje: String,
    colorPrincipal: Color,
    icono: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(204.dp),
        shape = RoundedCornerShape(15.dp),
        color = Color(0xFFF8FBFF),
        shadowElevation = 3.dp,
        border = BorderStroke(1.dp, BordeTarjeta)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 3.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        colorPrincipal.copy(alpha = 0.09f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides colorPrincipal
                ) {
                    icono()
                }
            }

            Text(
                text = titulo,
                fontSize = 12.sp,
                lineHeight = 14.sp,
                color = AzulTexto,
                textAlign = TextAlign.Center
            )

            Text(
                text = cantidad,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = colorPrincipal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "↑ $porcentaje",
                fontSize = 10.sp,
                color = Verde,
                textAlign = TextAlign.Center
            )
        }
    }
}

// ==========================================
// TARJETA ACCESO
// ==========================================

@Composable
private fun TarjetaAcceso(
    titulo: String,
    descripcion: String,
    colorPrincipal: Color,
    colorFondo: Color,
    icono: @Composable () -> Unit,
    alHacerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = alHacerClick,
        modifier = modifier.height(204.dp),
        shape = RoundedCornerShape(15.dp),
        color = colorFondo,
        shadowElevation = 3.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        Color.White.copy(alpha = 0.50f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides colorPrincipal
                ) {
                    icono()
                }
            }

            Text(
                text = titulo,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = AzulTexto,
                textAlign = TextAlign.Center
            )

            Text(
                text = descripcion,
                fontSize = 11.sp,
                lineHeight = 13.sp,
                color = GrisTexto,
                textAlign = TextAlign.Center
            )

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Abrir $titulo",
                    tint = AzulTexto,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// ==========================================
// ACTIVIDAD RECIENTE
// ==========================================

@Composable
private fun ActividadReciente(
    alVerTodas: () -> Unit,
    alAbrirActividad: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Actividad reciente",
                color = AzulTexto,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Ver todas",
                color = AzulPrincipal,
                fontSize = 15.sp,
                modifier = Modifier.clickable(onClick = alVerTodas)
            )
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, BordeTarjeta),
            shadowElevation = 3.dp
        ) {
            Column {
                FilaActividad(
                    titulo = "Venta de combo registrada · Outfit completo",
                    fecha = "Hoy, 10:12 AM",
                    valor = "+$950.00",
                    colorValor = Verde,
                    colorIcono = Verde,
                    fondoIcono = Color(0xFFE5F8EF),
                    icono = {
                        Icon(
                            Icons.Default.CardGiftcard,
                            contentDescription = null
                        )
                    },
                    alHacerClick = { alAbrirActividad(1) }
                )

                HorizontalDivider(
                    color = BordeTarjeta,
                    thickness = 1.dp
                )

                FilaActividad(
                    titulo = "Nueva venta de Ana",
                    fecha = "Hoy, 9:40 AM",
                    valor = "+$230.00",
                    colorValor = Verde,
                    colorIcono = AzulPrincipal,
                    fondoIcono = Color(0xFFEAF3FF),
                    icono = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null
                        )
                    },
                    alHacerClick = { alAbrirActividad(2) }
                )

                HorizontalDivider(
                    color = BordeTarjeta,
                    thickness = 1.dp
                )

                FilaActividad(
                    titulo = "Inventario actualizado · Collares",
                    fecha = "Hoy, 8:55 AM",
                    valor = "-2 unidades",
                    colorValor = AzulPrincipal,
                    colorIcono = Coral,
                    fondoIcono = Color(0xFFFFF0EB),
                    icono = {
                        Icon(
                            Icons.Default.Inventory2,
                            contentDescription = null
                        )
                    },
                    alHacerClick = { alAbrirActividad(3) }
                )
            }
        }
    }
}

// ==========================================
// FILA DE ACTIVIDAD
// ==========================================

@Composable
private fun FilaActividad(
    titulo: String,
    fecha: String,
    valor: String,
    colorValor: Color,
    colorIcono: Color,
    fondoIcono: Color,
    icono: @Composable () -> Unit,
    alHacerClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = alHacerClick)
            .padding(horizontal = 10.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(45.dp)
                .background(fondoIcono, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            CompositionLocalProvider(
                LocalContentColor provides colorIcono
            ) {
                icono()
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = titulo,
                fontSize = 13.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Medium,
                color = AzulTexto,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = fecha,
                fontSize = 12.sp,
                color = GrisTexto
            )
        }

        Text(
            text = valor,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = colorValor,
            maxLines = 2,
            textAlign = TextAlign.End
        )

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Ver detalle",
            tint = GrisTexto,
            modifier = Modifier.size(20.dp)
        )
    }
}

// ==========================================
// INDICADOR REALIZAR VENTA
// ==========================================

@Composable
private fun IndicadorRealizarVenta(
    alRealizarVenta: () -> Unit
) {
    val density = LocalDensity.current
    val umbralDeslizamiento = with(density) { 65.dp.toPx() }

    var desplazamiento by remember {
        mutableFloatStateOf(0f)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
            .pointerInput(umbralDeslizamiento) {
                detectVerticalDragGestures(
                    onDragStart = {
                        desplazamiento = 0f
                    },
                    onVerticalDrag = { change, dragAmount ->
                        change.consume()
                        desplazamiento += dragAmount
                    },
                    onDragEnd = {
                        if (desplazamiento < -umbralDeslizamiento) {
                            alRealizarVenta()
                        }
                        desplazamiento = 0f
                    },
                    onDragCancel = {
                        desplazamiento = 0f
                    }
                )
            }
            .clickable(onClick = alRealizarVenta),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy((-18).dp)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = null,
                tint = AzulPrincipal,
                modifier = Modifier.size(66.dp)
            )

            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = null,
                tint = AzulPrincipal.copy(alpha = 0.55f),
                modifier = Modifier.size(66.dp)
            )

            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = null,
                tint = AzulPrincipal.copy(alpha = 0.25f),
                modifier = Modifier.size(66.dp)
            )
        }

        Spacer(Modifier.height(10.dp))

        Text(
            text = "Desliza para arriba\ny realiza una venta",
            color = AzulPrincipal,
            fontSize = 24.sp,
            lineHeight = 29.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}
