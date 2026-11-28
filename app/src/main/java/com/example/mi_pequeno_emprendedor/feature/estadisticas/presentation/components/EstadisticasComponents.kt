package com.example.mi_pequeno_emprendedor.feature.estadisticas.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.NightsStay
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material.icons.outlined.WbTwilight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Paleta de colores extraída de la maqueta
val AzulCabecera = Color(0xFF0A66C2)
val AzulBarraFuerte = Color(0xFF1976D2)
val AzulBarraSuave = Color(0xFF8AB9F1)
val VerdeMenta = Color(0xFF10B981)
val CoralSuave = Color(0xFFFB7185)
val AmarilloSol = Color(0xFFFBBF24)
val MoradoPastel = Color(0xFFA78BFA)
val FondoTarjeta = Color.White
val FondoPantalla = Color(0xFFF4F7FB)

@Composable
fun EstadisticasHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = AzulCabecera,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            )
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Estadísticas",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Resumen simple de tu negocio",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 14.sp
                )
            }
            Box {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notificaciones",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                // Punto rojo de notificación
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEF4444))
                        .align(Alignment.TopEnd)
                        .offset(x = (-10).dp, y = 10.dp)
                )
            }
        }
    }
}

@Composable
fun FilaKpisSuperiores() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Tarjeta 1: Ingresos hoy
        Card(
            colors = CardDefaults.cardColors(containerColor = FondoTarjeta),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .weight(1f)
                .height(175.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(VerdeMenta.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.AttachMoney, contentDescription = null, tint = VerdeMenta)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Ingresos hoy", fontSize = 12.sp, color = Color.DarkGray, fontWeight = FontWeight.Medium)
                    Text("$1,450", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = AzulBarraFuerte)
                }
                Surface(
                    color = VerdeMenta.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "↑ 12% vs ayer",
                        color = Color(0xFF059669),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Tarjeta 2: Meta diaria
        Card(
            colors = CardDefaults.cardColors(containerColor = FondoTarjeta),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .weight(1f)
                .height(175.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(CoralSuave.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.TrackChanges, contentDescription = null, tint = CoralSuave)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Meta diaria", fontSize = 12.sp, color = Color.DarkGray, fontWeight = FontWeight.Medium)
                    Text("$1,800", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = AzulBarraFuerte)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    LinearProgressIndicator(
                        progress = { 0.81f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape),
                        color = VerdeMenta,
                        trackColor = Color(0xFFE2E8F0)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("81% de tu meta", fontSize = 10.sp, color = Color.Gray)
                }
            }
        }

        // Tarjeta 3: Producto top
        Card(
            colors = CardDefaults.cardColors(containerColor = FondoTarjeta),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .weight(1f)
                .height(175.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(AzulBarraFuerte.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = AzulBarraFuerte)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Producto top", fontSize = 12.sp, color = Color.DarkGray, fontWeight = FontWeight.Medium)
                    Text("Sabritas", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AzulBarraFuerte)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(AmarilloSol),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Outlined.Inventory2,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "28 unidades\nvendidas hoy",
                        fontSize = 9.sp,
                        lineHeight = 11.sp,
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}

@Composable
fun TarjetaDineroSemana() {
    val datosSemana = listOf(
        Triple("L", 1200f, false),
        Triple("M", 1450f, false),
        Triple("M", 1100f, false),
        Triple("J", 1600f, false),
        Triple("V", 1800f, true), // El viernes alcanzó la meta (color fuerte)
        Triple("S", 1300f, false),
        Triple("D", 1050f, false)
    )
    val maxValor = 2400f
    val metaValor = 1800f

    Card(
        colors = CardDefaults.cardColors(containerColor = FondoTarjeta),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text("Dinero de la semana", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("Ingresos diarios vs meta", fontSize = 12.sp, color = Color.Gray)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(AzulBarraSuave)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Ingresos", fontSize = 11.sp, color = Color.DarkGray)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("--- Meta", fontSize = 11.sp, color = AzulBarraFuerte, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Área del gráfico con eje Y y línea punteada de meta
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                // Etiquetas del Eje Y
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(bottom = 20.dp, end = 8.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.End
                ) {
                    Text("$2,400", fontSize = 9.sp, color = Color.Gray)
                    Text("$1,800", fontSize = 9.sp, color = Color.Gray)
                    Text("$1,200", fontSize = 9.sp, color = Color.Gray)
                    Text("$600", fontSize = 9.sp, color = Color.Gray)
                    Text("$0", fontSize = 9.sp, color = Color.Gray)
                }

                // Contenedor de barras + Línea de Meta
                Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                    // Línea punteada horizontal dibujada en la altura exacta de $1,800 (75% de $2,400)
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 20.dp)
                    ) {
                        val yMeta = size.height * (1f - (metaValor / maxValor))
                        drawLine(
                            color = AzulBarraFuerte,
                            start = Offset(0f, yMeta),
                            end = Offset(size.width, yMeta),
                            strokeWidth = 3f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                        )
                    }

                    // Columnas de días
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        datosSemana.forEach { (dia, valor, esMeta) ->
                            val fraccionAltura = (valor / maxValor).coerceIn(0.1f, 1f)
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "$${valor.toInt()}",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.DarkGray
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Box(
                                    modifier = Modifier
                                        .width(24.dp)
                                        .fillMaxHeight(fraccionAltura * 0.82f)
                                        .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                        .background(if (esMeta) AzulBarraFuerte else AzulBarraSuave)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(dia, fontSize = 11.sp, color = Color.DarkGray)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilaProductosYPagos() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Izquierda: Top 5 productos
        Card(
            colors = CardDefaults.cardColors(containerColor = FondoTarjeta),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .weight(1f)
                .height(225.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Lo que más vendes", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text("Top 5 productos más vendidos", fontSize = 10.sp, color = Color.Gray)
                }

                val productos = listOf(
                    Triple("Sabritas", 28, AzulBarraFuerte),
                    Triple("Coca-Cola", 24, CoralSuave),
                    Triple("Agua Ciel", 18, AzulBarraSuave),
                    Triple("Galletas Marías", 16, AmarilloSol),
                    Triple("Gomitas enchiladas", 14, MoradoPastel)
                )

                productos.forEach { (nombre, cantidad, color) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = nombre,
                            fontSize = 10.sp,
                            maxLines = 1,
                            modifier = Modifier.width(68.dp),
                            color = Color.DarkGray
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(8.dp)
                                .padding(horizontal = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(cantidad / 28f)
                                    .fillMaxHeight()
                                    .clip(CircleShape)
                                    .background(color)
                            )
                        }
                        Text(
                            text = "$cantidad",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }

        // Derecha: Gráfico de Dona (¿Cómo te pagan?)
        Card(
            colors = CardDefaults.cardColors(containerColor = FondoTarjeta),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .weight(1f)
                .height(225.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("¿Cómo te pagan?", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text("Formas de pago de tus ventas", fontSize = 10.sp, color = Color.Gray)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Dona dibujada con Canvas nativo
                    Box(
                        modifier = Modifier.size(82.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val strokeWidth = 18.dp.toPx()
                            val radius = (size.minDimension - strokeWidth) / 2f
                            val topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f)
                            val arcSize = Size(radius * 2f, radius * 2f)

                            // Efectivo 62% (223.2 grados)
                            drawArc(
                                color = AzulBarraFuerte,
                                startAngle = -90f,
                                sweepAngle = 223.2f,
                                useCenter = false,
                                topLeft = topLeft,
                                size = arcSize,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                            )
                            // Transferencia 25% (90 grados)
                            drawArc(
                                color = CoralSuave,
                                startAngle = 133.2f,
                                sweepAngle = 90f,
                                useCenter = false,
                                topLeft = topLeft,
                                size = arcSize,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                            )
                            // Tarjeta 13% (46.8 grados)
                            drawArc(
                                color = VerdeMenta,
                                startAngle = 223.2f,
                                sweepAngle = 46.8f,
                                useCenter = false,
                                topLeft = topLeft,
                                size = arcSize,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("$1,450", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text("hoy", fontSize = 9.sp, color = Color.Gray)
                        }
                    }

                    // Leyenda lateral
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        LeyendaPagoItem(AzulBarraFuerte, "Efectivo", "62%  $899")
                        LeyendaPagoItem(CoralSuave, "Transferencia", "25%  $363")
                        LeyendaPagoItem(VerdeMenta, "Tarjeta", "13%  $188")
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
private fun LeyendaPagoItem(color: Color, titulo: String, detalle: String) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .padding(top = 3.dp)
                .size(7.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Column {
            Text(titulo, fontSize = 9.sp, fontWeight = FontWeight.Medium)
            Text(detalle, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
        }
    }
}

@Composable
fun FilaHorariosYClientes() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Izquierda: ¿Cuándo vendes más?
        Card(
            colors = CardDefaults.cardColors(containerColor = FondoTarjeta),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .weight(1f)
                .height(195.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("¿Cuándo vendes más?", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text("Ventas por momento del día", fontSize = 10.sp, color = Color.Gray)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(115.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    ColumnaHorario("25%", 0.45f, AzulBarraSuave, Icons.Outlined.WbTwilight, "Mañana", "$363")
                    ColumnaHorario("50%", 0.85f, CoralSuave.copy(alpha = 0.7f), Icons.Outlined.WbSunny, "Tarde", "$725")
                    ColumnaHorario("25%", 0.45f, AzulBarraSuave, Icons.Outlined.NightsStay, "Noche", "$363")
                }
            }
        }

        // Derecha: Días con más clientes
        Card(
            colors = CardDefaults.cardColors(containerColor = FondoTarjeta),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .weight(1f)
                .height(195.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Días con más clientes", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text("Total de ventas por día de la semana", fontSize = 10.sp, color = Color.Gray)
                }

                val clientesDias = listOf(
                    Triple("L", 22, false),
                    Triple("M", 28, false),
                    Triple("M", 18, false),
                    Triple("J", 25, false),
                    Triple("V", 32, true),
                    Triple("S", 24, false),
                    Triple("D", 20, false)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(115.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    clientesDias.forEach { (dia, cantidad, esTop) ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("$cantidad", fontSize = 8.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(2.dp))
                            Box(
                                modifier = Modifier
                                    .width(12.dp)
                                    .height((cantidad * 2.2).dp)
                                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                    .background(if (esTop) AzulBarraFuerte else AzulBarraSuave)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(dia, fontSize = 9.sp, color = Color.DarkGray)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ColumnaHorario(
    porcentaje: String,
    alturaRelativa: Float,
    color: Color,
    icono: ImageVector,
    etiqueta: String,
    monto: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(porcentaje, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(2.dp))
        Box(
            modifier = Modifier
                .width(30.dp)
                .height((55 * alturaRelativa).dp)
                .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                .background(color)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Icon(icono, contentDescription = null, modifier = Modifier.size(14.dp), tint = AzulBarraFuerte)
        Text(etiqueta, fontSize = 9.sp, color = Color.DarkGray)
        Text(monto, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
    }
}

@Composable
fun BannerConsejoPerfil(
    perfilTitulo: String = "Consejo Mojarra:",
    mensaje: String = "Revisa lo que más vendes para surtirte mejor."
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEBF3FE)),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(AzulBarraFuerte),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "M",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = perfilTitulo,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = mensaje,
                    fontSize = 12.sp,
                    color = Color(0xFF334155)
                )
            }
        }
    }
}