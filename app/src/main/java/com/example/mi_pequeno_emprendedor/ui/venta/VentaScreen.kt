package com.example.mi_pequeno_emprendedor.ui.venta

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.mi_pequeno_emprendedor.ui.theme.AzulBotones
import com.example.mi_pequeno_emprendedor.ui.theme.AzulEtiqueta
import com.example.mi_pequeno_emprendedor.ui.theme.AzulHeader
import com.example.mi_pequeno_emprendedor.ui.theme.AzulTexto
import com.example.mi_pequeno_emprendedor.ui.theme.BordeCampo
import com.example.mi_pequeno_emprendedor.ui.theme.RojoBotones
import com.example.mi_pequeno_emprendedor.ui.theme.RojoEtiqueta
import com.example.mi_pequeno_emprendedor.ui.theme.RojoTexto
import com.example.mi_pequeno_emprendedor.ui.theme.TextoPrincipal
import com.example.mi_pequeno_emprendedor.ui.theme.TextoSecundario
import com.example.mi_pequeno_emprendedor.ui.theme.VerdeBotones
import com.example.mi_pequeno_emprendedor.ui.theme.VerdeEtiqueta
import com.example.mi_pequeno_emprendedor.ui.theme.VerdeTexto
import kotlinx.coroutines.delay
import java.util.Locale

private data class ProductoDemo(val nombre: String, val detalle: String, val precio: Int)

private const val NOMBRE_NEGOCIO = "Tiendita Sol"

private val productosDemo = listOf(
    ProductoDemo("Coca-Cola", "600 ml", 20),
    ProductoDemo("Sabritas", "Original", 18),
    ProductoDemo("Agua Ciel", "600 ml", 12),
    ProductoDemo("Galletas Marías", "170 g", 6),
    ProductoDemo("Churritos", "46 g", 10),
    ProductoDemo("Gomitas enchiladas", "100 g", 8),
    ProductoDemo("Pan dulce", "1 pieza", 8),
    ProductoDemo("Jugo de naranja", "1 L", 20),
    ProductoDemo("Yogurt", "900 g", 25),
    ProductoDemo("Cacahuates", "150 g", 12),
    ProductoDemo("Chocolate", "40 g", 18),
    ProductoDemo("Paletas", "1 pieza", 7),
    ProductoDemo("Café molido", "250 g", 35),
    ProductoDemo("Leche", "1 L", 24),
    ProductoDemo("Pilas AA", "2 piezas", 30),
    ProductoDemo("Encendedor", "1 pieza", 10),
    ProductoDemo("Galletas de chocolate", "120 g", 15),
    ProductoDemo("Agua mineral", "600 ml", 14)
)

@Composable
fun VentaScreen(onCerrar: () -> Unit = {}) {
    var textoBusqueda by rememberSaveable { mutableStateOf("") }
    var filtro by rememberSaveable { mutableStateOf("") }
    var mostrarBusqueda by rememberSaveable { mutableStateOf(false) }
    var mostrarConfirmarEliminar by rememberSaveable { mutableStateOf(false) }
    var mostrarMetodoPago by rememberSaveable { mutableStateOf(false) }
    var mostrarEfectivo by rememberSaveable { mutableStateOf(false) }
    var mostrarTicket by rememberSaveable { mutableStateOf(false) }
    var metodoConfirmando by remember { mutableStateOf<String?>(null) }
    var metodoPago by rememberSaveable { mutableStateOf("Efectivo") }
    var pagoExacto by rememberSaveable { mutableStateOf(false) }
    var montoRecibidoCentavos by remember { mutableStateOf<Int?>(null) }
    var mostrarExito by rememberSaveable { mutableStateOf(false) }
    val cantidades = remember { mutableStateMapOf<String, Int>() }

    val productos = remember(filtro) {
        productosDemo.filter { it.nombre.contains(filtro.trim(), ignoreCase = true) }
    }
    val total = cantidades.entries.sumOf { (nombre, cantidad) ->
        (productosDemo.find { it.nombre == nombre }?.precio ?: 0) * cantidad
    }

    LaunchedEffect(total) {
        if (total <= 0) mostrarMetodoPago = false
    }

    LaunchedEffect(mostrarExito) {
        if (mostrarExito) {
            delay(2500)
            mostrarExito = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .navigationBarsPadding()
            .imePadding()
    ) {
        CabeceraVenta(onCerrar)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (mostrarBusqueda) {
                OutlinedTextField(
                    value = textoBusqueda,
                    onValueChange = { textoBusqueda = it },
                    label = { Text("Buscar producto") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = AzulBotones,
                        focusedBorderColor = AzulBotones,
                        unfocusedBorderColor = BordeCampo,
                        focusedLabelColor = AzulBotones,
                        unfocusedLabelColor = TextoSecundario
                    ),
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = { filtro = textoBusqueda },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AzulBotones),
                    modifier = Modifier.height(56.dp)
                ) {
                    Text("Buscar", fontWeight = FontWeight.SemiBold)
                }
            } else {
                Text(
                    "Productos",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = AzulTexto
                )
                Spacer(Modifier.width(6.dp))
                IconoInfo()
                Spacer(Modifier.width(6.dp))
                Text(
                    "1 toque agrega",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextoSecundario,
                    modifier = Modifier.weight(1f)
                )
            }
            BotonLupa(
                onClick = {
                    if (mostrarBusqueda) {
                        textoBusqueda = ""
                        filtro = ""
                    }
                    mostrarBusqueda = !mostrarBusqueda
                }
            )
        }

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            border = BorderStroke(1.dp, BordeCampo),
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.45f)
                .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
        ) {
            if (productos.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay productos que coincidan", color = TextoSecundario)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(productos) { producto ->
                        TarjetaProducto(
                            producto = producto,
                            onAgregar = {
                                cantidades[producto.nombre] = (cantidades[producto.nombre] ?: 0) + 1
                            },
                            onEliminar = { cantidades.remove(producto.nombre) }
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Productos seleccionados",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AzulTexto
                )
                Spacer(Modifier.width(6.dp))
                IconoInfo()
                Spacer(Modifier.width(6.dp))
                Text(
                    "2 toques resta 1 · Mantener elimina todo",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextoSecundario,
                    maxLines = 2,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (cantidades.isEmpty()) {
                    Text(
                        "Aún no hay productos",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundario,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    LazyRow(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        cantidades.forEach { (nombre, cantidad) ->
                            val producto = productosDemo.find { it.nombre == nombre } ?: return@forEach
                            item {
                                ChipSeleccionado(
                                    producto = producto,
                                    cantidad = cantidad,
                                    onAgregar = {
                                        cantidades[producto.nombre] = (cantidades[producto.nombre] ?: 0) + 1
                                    },
                                    onRestar = {
                                        val actual = cantidades[producto.nombre] ?: 0
                                        if (actual <= 1) cantidades.remove(producto.nombre)
                                        else cantidades[producto.nombre] = actual - 1
                                    },
                                    onEliminar = { cantidades.remove(producto.nombre) }
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.width(8.dp))
                TarjetaTotal(total)
            }
        }

        if (mostrarMetodoPago) {
            SelectorMetodoPago(
                onEfectivo = { mostrarEfectivo = true },
                onTarjeta = { metodoConfirmando = "Tarjeta" },
                onTransferencia = { metodoConfirmando = "Transferencia" },
                onCancelar = { mostrarMetodoPago = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { mostrarConfirmarEliminar = true },
                    enabled = cantidades.isNotEmpty(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RojoBotones,
                        contentColor = Color.White,
                        disabledContainerColor = RojoEtiqueta,
                        disabledContentColor = RojoTexto.copy(alpha = 0.4f)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                ) {
                    IconoBasura(modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Eliminar venta", fontWeight = FontWeight.SemiBold)
                }
                Button(
                    onClick = { mostrarMetodoPago = true },
                    enabled = cantidades.isNotEmpty() && total > 0,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VerdeBotones,
                        contentColor = Color.White,
                        disabledContainerColor = VerdeEtiqueta,
                        disabledContentColor = VerdeTexto.copy(alpha = 0.4f)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                ) {
                    Text("Continuar", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }

        AnimatedVisibility(
            visible = mostrarExito,
            enter = fadeIn() + slideInVertically { -it },
            exit = fadeOut() + slideOutVertically { -it },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 8.dp)
        ) {
            Surface(
                color = VerdeBotones,
                shape = RoundedCornerShape(50),
                shadowElevation = 6.dp
            ) {
                Text(
                    "Venta realizada con éxito",
                    color = Color.White,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)
                )
            }
        }
    }

    if (mostrarConfirmarEliminar) {
        DialogoEliminarVenta(
            onDismiss = { mostrarConfirmarEliminar = false },
            onConfirmar = {
                cantidades.clear()
                mostrarConfirmarEliminar = false
            }
        )
    }

    if (mostrarEfectivo) {
        DialogoEfectivo(
            total = total,
            onCerrar = { mostrarEfectivo = false },
            onConfirmar = { monto ->
                // shortcut: la venta aún no se guarda en Supabase; solo se limpia la cola
                montoRecibidoCentavos = monto
                metodoPago = "Efectivo"
                pagoExacto = monto == total * 100
                cantidades.clear()
                mostrarEfectivo = false
                mostrarMetodoPago = false
                mostrarExito = true
            },
            onVerTicket = { monto ->
                montoRecibidoCentavos = monto
                metodoPago = "Efectivo"
                pagoExacto = monto == total * 100
                mostrarTicket = true
            }
        )
    }

    metodoConfirmando?.let { metodo ->
        DialogoConfirmarVenta(
            metodo = metodo,
            onCerrar = { metodoConfirmando = null },
            onConfirmar = {
                montoRecibidoCentavos = total * 100
                metodoPago = metodo
                pagoExacto = true
                cantidades.clear()
                metodoConfirmando = null
                mostrarMetodoPago = false
                mostrarExito = true
            },
            onVerTicket = {
                montoRecibidoCentavos = total * 100
                metodoPago = metodo
                pagoExacto = true
                mostrarTicket = true
            }
        )
    }

    if (mostrarTicket) {
        TicketDialog(
            items = cantidades.entries.mapNotNull { (nombre, cantidad) ->
                productosDemo.find { it.nombre == nombre }?.let { producto ->
                    ItemTicket(producto.nombre, producto.detalle, cantidad, producto.precio)
                }
            },
            total = total,
            nombreNegocio = NOMBRE_NEGOCIO,
            metodoPago = metodoPago,
            montoRecibidoCentavos = montoRecibidoCentavos,
            pagoExacto = pagoExacto,
            onCerrar = { mostrarTicket = false }
        )
    }
}

@Composable
private fun CabeceraVenta(onCerrar: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.verticalGradient(listOf(AzulHeader, AzulBotones)))
            .statusBarsPadding()
            .padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Realizar venta",
            color = Color.White,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            modifier = Modifier.weight(1f)
        )
        Surface(
            color = Color.White.copy(alpha = 0.18f),
            shape = RoundedCornerShape(50),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.45f))
        ) {
            Text(
                "Escanear",
                color = Color.White,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }
        IconButton(onClick = onCerrar) {
            Text("✕", color = Color.White, style = MaterialTheme.typography.titleLarge)
        }
    }
}

@Composable
private fun BotonLupa(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(AzulEtiqueta)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(22.dp)) {
            val trazo = size.minDimension * 0.12f
            val radio = size.minDimension * 0.30f
            val centro = Offset(size.width * 0.40f, size.height * 0.40f)
            drawCircle(
                color = AzulBotones,
                radius = radio,
                center = centro,
                style = Stroke(width = trazo)
            )
            drawLine(
                color = AzulBotones,
                start = Offset(centro.x + radio * 0.75f, centro.y + radio * 0.75f),
                end = Offset(size.width * 0.95f, size.height * 0.95f),
                strokeWidth = trazo,
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
private fun IconoInfo() {
    Box(
        modifier = Modifier
            .size(18.dp)
            .background(AzulBotones, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "i",
            color = Color.White,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun TarjetaProducto(
    producto: ProductoDemo,
    onAgregar: () -> Unit,
    onEliminar: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, BordeCampo),
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onAgregar,
                onLongClick = onEliminar
            )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Inicial(producto.nombre, 72.dp)
            Spacer(Modifier.height(12.dp))
            Text(
                producto.nombre,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = AzulTexto,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
            Text(
                producto.detalle,
                style = MaterialTheme.typography.bodySmall,
                color = TextoSecundario
            )
        }
    }
}

@Composable
private fun ChipSeleccionado(
    producto: ProductoDemo,
    cantidad: Int,
    onAgregar: () -> Unit,
    onRestar: () -> Unit,
    onEliminar: () -> Unit
) {
    Box {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            border = BorderStroke(1.dp, BordeCampo),
            modifier = Modifier.combinedClickable(
                onClick = onAgregar,
                onDoubleClick = onRestar,
                onLongClick = onEliminar
            )
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Inicial(producto.nombre, 40.dp)
                Spacer(Modifier.width(8.dp))
                Column {
                    Text(
                        producto.nombre,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = AzulTexto,
                        maxLines = 1
                    )
                    Text(
                        producto.detalle,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundario,
                        maxLines = 1
                    )
                }
                Spacer(Modifier.width(24.dp))
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 6.dp, end = 6.dp)
                .size(22.dp)
                .background(RojoTexto, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                cantidad.toString(),
                color = Color.White,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun TarjetaTotal(total: Int) {
    Surface(
        color = AzulEtiqueta,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
            Text(
                "Total",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = TextoPrincipal
            )
            Text(
                "$" + String.format(Locale.US, "%.2f", total.toDouble()),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = AzulBotones
            )
        }
    }
}

@Composable
internal fun Inicial(nombre: String, tamano: Dp) {
    Box(
        modifier = Modifier
            .size(tamano)
            .background(AzulEtiqueta, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            nombre.take(1),
            style = if (tamano > 50.dp) MaterialTheme.typography.headlineMedium
            else MaterialTheme.typography.titleMedium,
            color = AzulTexto,
            fontWeight = FontWeight.Bold
        )
    }
}
