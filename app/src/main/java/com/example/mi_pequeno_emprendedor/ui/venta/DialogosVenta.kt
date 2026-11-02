package com.example.mi_pequeno_emprendedor.ui.venta

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.mi_pequeno_emprendedor.ui.theme.AzulBotones
import com.example.mi_pequeno_emprendedor.ui.theme.AzulEtiqueta
import com.example.mi_pequeno_emprendedor.ui.theme.AzulTexto
import com.example.mi_pequeno_emprendedor.ui.theme.BordeCampo
import com.example.mi_pequeno_emprendedor.ui.theme.RojoBotones
import com.example.mi_pequeno_emprendedor.ui.theme.RojoEtiqueta
import com.example.mi_pequeno_emprendedor.ui.theme.RojoTexto
import com.example.mi_pequeno_emprendedor.ui.theme.TextoPrincipal
import com.example.mi_pequeno_emprendedor.ui.theme.TextoSecundario
import com.example.mi_pequeno_emprendedor.ui.theme.VerdeEtiqueta
import com.example.mi_pequeno_emprendedor.ui.theme.VerdeTexto
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal data class ItemTicket(
    val nombre: String,
    val detalle: String,
    val cantidad: Int,
    val precioUnitario: Int
)

@Composable
internal fun DialogoEliminarVenta(onDismiss: () -> Unit, onConfirmar: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(24.dp), color = Color.White) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(RojoEtiqueta, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    IconoBasura(color = RojoTexto, modifier = Modifier.size(34.dp))
                }
                Spacer(Modifier.height(16.dp))
                Text(
                    "¿Eliminar venta?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = AzulTexto
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    "Se eliminarán todos los productos seleccionados de esta venta.\nEsta acción no se puede deshacer.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoSecundario,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, BordeCampo),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = AzulTexto),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text("Cancelar", fontWeight = FontWeight.SemiBold)
                    }
                    Button(
                        onClick = onConfirmar,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RojoBotones),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text("Sí, eliminar", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
internal fun TicketDialog(
    items: List<ItemTicket>,
    total: Int,
    nombreNegocio: String,
    metodoPago: String,
    montoRecibido: Int?,
    pagoExacto: Boolean,
    onCerrar: () -> Unit
) {
    val ahora = remember { LocalDateTime.now() }
    val locale = Locale.forLanguageTag("es-MX")
    val fecha = ahora.format(DateTimeFormatter.ofPattern("d MMM yyyy", locale))
    val hora = ahora.format(DateTimeFormatter.ofPattern("h:mm a", locale))
    val folio = String.format(Locale.US, "#%06d", ahora.toLocalTime().toSecondOfDay())

    val contexto = LocalContext.current
    val alcance = rememberCoroutineScope()
    val datosPdf = DatosTicketPdf(
        items = items,
        total = total,
        nombreNegocio = nombreNegocio,
        metodoPago = metodoPago,
        montoRecibido = montoRecibido,
        pagoExacto = pagoExacto,
        fecha = fecha,
        hora = hora,
        folio = folio
    )

    Dialog(
        onDismissRequest = onCerrar,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.9f)
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconoTicket(color = AzulBotones, modifier = Modifier.size(26.dp))
                    Spacer(Modifier.width(10.dp))
                    Text(
                        "Ticket generado",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = AzulTexto
                    )
                }
                Spacer(Modifier.height(12.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, BordeCampo)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                nombreNegocio,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = AzulTexto,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                "Gracias por tu compra",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextoSecundario,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(10.dp))
                            HorizontalDivider(color = BordeCampo)
                            Spacer(Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    "Fecha: $fecha",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextoSecundario,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    "Hora: $hora",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextoSecundario
                                )
                            }
                            Text(
                                "Folio: $folio",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextoSecundario
                            )
                            Spacer(Modifier.height(8.dp))
                            HorizontalDivider(color = BordeCampo)
                            Spacer(Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    "Producto",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextoSecundario,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    "Cant.",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextoSecundario,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.width(40.dp)
                                )
                                Text(
                                    "Precio",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextoSecundario,
                                    textAlign = TextAlign.End,
                                    modifier = Modifier.width(64.dp)
                                )
                            }
                            Spacer(Modifier.height(6.dp))
                            items.forEach { item ->
                                FilaItemTicket(item)
                                Spacer(Modifier.height(6.dp))
                            }
                            HorizontalDivider(color = BordeCampo)
                            Spacer(Modifier.height(8.dp))
                            FilaMonto("Subtotal", total)
                            FilaMonto("Descuento", 0)
                            Spacer(Modifier.height(6.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(AzulEtiqueta, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Total",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = AzulTexto,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    precio(total),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = AzulBotones
                                )
                            }
                            Spacer(Modifier.height(8.dp))
                            HorizontalDivider(color = BordeCampo)
                            Spacer(Modifier.height(8.dp))
                            FilaTexto("Método de pago", metodoPago)
                            if (pagoExacto) {
                                FilaTexto("Monto recibido", "Pago exacto")
                                FilaTexto("Cambio", precio(0))
                            } else {
                                FilaTexto("Monto recibido", montoRecibido?.let { precio(it) } ?: "—")
                                FilaTexto("Cambio", montoRecibido?.let { precio(it - total) } ?: "—")
                            }
                            Spacer(Modifier.height(10.dp))
                            Text(
                                "¡Gracias por tu compra!",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextoSecundario,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                "Vuelve pronto",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextoSecundario,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                }

                Spacer(Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            alcance.launch {
                                val archivo = withContext(Dispatchers.IO) {
                                    runCatching { TicketPdf.generar(contexto, datosPdf) }.getOrNull()
                                }
                                if (archivo == null) {
                                    Toast.makeText(
                                        contexto,
                                        "No se pudo generar el PDF",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    runCatching { TicketPdf.compartir(contexto, archivo) }
                                        .onFailure {
                                            Toast.makeText(
                                                contexto,
                                                "No se pudo compartir el ticket",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                }
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AzulBotones),
                        contentPadding = PaddingValues(horizontal = 8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text("Compartir", fontWeight = FontWeight.SemiBold)
                    }
                    OutlinedButton(
                        onClick = {
                            alcance.launch {
                                val resultado = withContext(Dispatchers.IO) {
                                    runCatching {
                                        val archivo = TicketPdf.generar(contexto, datosPdf)
                                        TicketPdf.guardarEnDescargas(contexto, archivo)
                                    }
                                }
                                Toast.makeText(
                                    contexto,
                                    if (resultado.isSuccess) "Ticket guardado en Descargas"
                                    else "No se pudo guardar el PDF",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, AzulBotones),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = AzulBotones),
                        contentPadding = PaddingValues(horizontal = 8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text("Descargar", fontWeight = FontWeight.SemiBold)
                    }
                    Button(
                        onClick = onCerrar,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AzulEtiqueta,
                            contentColor = AzulTexto
                        ),
                        contentPadding = PaddingValues(horizontal = 8.dp),
                        modifier = Modifier
                            .weight(0.7f)
                            .height(48.dp)
                    ) {
                        Text("Listo", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
private fun FilaItemTicket(item: ItemTicket) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Inicial(item.nombre, 36.dp)
        Spacer(Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                item.nombre,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = AzulTexto,
                maxLines = 1
            )
            Text(
                item.detalle,
                style = MaterialTheme.typography.bodySmall,
                color = TextoSecundario,
                maxLines = 1
            )
        }
        Text(
            item.cantidad.toString(),
            style = MaterialTheme.typography.labelLarge,
            color = TextoPrincipal,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(40.dp)
        )
        Text(
            precio(item.precioUnitario * item.cantidad),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = TextoPrincipal,
            textAlign = TextAlign.End,
            modifier = Modifier.width(64.dp)
        )
    }
}

@Composable
private fun FilaMonto(etiqueta: String, monto: Int) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
        Text(
            etiqueta,
            style = MaterialTheme.typography.bodyMedium,
            color = TextoSecundario,
            modifier = Modifier.weight(1f)
        )
        Text(
            precio(monto),
            style = MaterialTheme.typography.bodyMedium,
            color = TextoPrincipal
        )
    }
}

@Composable
private fun FilaTexto(etiqueta: String, valor: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
        Text(
            etiqueta,
            style = MaterialTheme.typography.bodyMedium,
            color = TextoSecundario,
            modifier = Modifier.weight(1f)
        )
        Text(
            valor,
            style = MaterialTheme.typography.bodyMedium,
            color = TextoPrincipal
        )
    }
}

private fun precio(valor: Int): String {
    val signo = if (valor < 0) "-" else ""
    return signo + "$" + String.format(Locale.US, "%.2f", kotlin.math.abs(valor).toDouble())
}

@Composable
internal fun IconoBasura(
    color: Color = LocalContentColor.current,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val trazo = size.minDimension * 0.10f
        val ancho = size.width
        val alto = size.height

        drawLine(color, Offset(ancho * 0.15f, alto * 0.24f), Offset(ancho * 0.85f, alto * 0.24f), trazo, StrokeCap.Round)
        drawLine(color, Offset(ancho * 0.38f, alto * 0.24f), Offset(ancho * 0.43f, alto * 0.12f), trazo, StrokeCap.Round)
        drawLine(color, Offset(ancho * 0.62f, alto * 0.24f), Offset(ancho * 0.57f, alto * 0.12f), trazo, StrokeCap.Round)
        drawLine(color, Offset(ancho * 0.24f, alto * 0.30f), Offset(ancho * 0.30f, alto * 0.88f), trazo, StrokeCap.Round)
        drawLine(color, Offset(ancho * 0.76f, alto * 0.30f), Offset(ancho * 0.70f, alto * 0.88f), trazo, StrokeCap.Round)
        drawLine(color, Offset(ancho * 0.30f, alto * 0.88f), Offset(ancho * 0.70f, alto * 0.88f), trazo, StrokeCap.Round)
        drawLine(color, Offset(ancho * 0.43f, alto * 0.40f), Offset(ancho * 0.45f, alto * 0.74f), trazo * 0.8f, StrokeCap.Round)
        drawLine(color, Offset(ancho * 0.57f, alto * 0.40f), Offset(ancho * 0.55f, alto * 0.74f), trazo * 0.8f, StrokeCap.Round)
    }
}

@Composable
internal fun IconoTicket(
    color: Color = LocalContentColor.current,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val trazo = size.minDimension * 0.10f
        drawRoundRect(
            color = color,
            topLeft = Offset(size.width * 0.14f, size.height * 0.16f),
            size = Size(size.width * 0.72f, size.height * 0.68f),
            cornerRadius = CornerRadius(size.width * 0.08f),
            style = Stroke(width = trazo)
        )
        listOf(0.36f, 0.50f, 0.64f).forEach { y ->
            drawLine(
                color,
                Offset(size.width * 0.30f, size.height * y),
                Offset(size.width * 0.70f, size.height * y),
                trazo * 0.8f,
                StrokeCap.Round
            )
        }
    }
}

@Composable
internal fun SelectorMetodoPago(
    onEfectivo: () -> Unit,
    onTarjeta: () -> Unit,
    onTransferencia: () -> Unit,
    onCancelar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, BordeCampo),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Método de pago",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AzulTexto,
                    modifier = Modifier.weight(1f)
                )
                TextButton(onClick = onCancelar) {
                    Text(
                        "Cancelar",
                        color = AzulBotones,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Spacer(Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MetodoPagoCard(
                    etiqueta = "Efectivo",
                    icono = { IconoBillete(TextoSecundario, Modifier.size(22.dp)) },
                    onClick = onEfectivo,
                    modifier = Modifier.weight(1f)
                )
                MetodoPagoCard(
                    etiqueta = "Tarjeta",
                    icono = { IconoTarjeta(TextoSecundario, Modifier.size(22.dp)) },
                    onClick = onTarjeta,
                    modifier = Modifier.weight(1f)
                )
                MetodoPagoCard(
                    etiqueta = "Transferencia",
                    icono = { IconoTransferencia(TextoSecundario, Modifier.size(22.dp)) },
                    onClick = onTransferencia,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun MetodoPagoCard(
    etiqueta: String,
    icono: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(1.dp, BordeCampo),
        modifier = modifier
            .height(92.dp)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            icono()
            Spacer(Modifier.height(8.dp))
            Text(
                etiqueta,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = TextoSecundario,
                maxLines = 1
            )
        }
    }
}

@Composable
internal fun DialogoEfectivo(
    total: Int,
    onCerrar: () -> Unit,
    onConfirmar: (Int) -> Unit,
    onVerTicket: (Int) -> Unit
) {
    var monto by rememberSaveable { mutableStateOf(0) }
    val cambio = monto - total
    val suficiente = monto >= total

    Dialog(
        onDismissRequest = onCerrar,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth(0.92f)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BotonTicketCirculo { onVerTicket(monto) }
                    Text(
                        "Ingresar efectivo",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = AzulTexto,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onCerrar) {
                        Text("✕", color = TextoSecundario, style = MaterialTheme.typography.titleLarge)
                    }
                }
                Text(
                    "¿Cuánto dinero te entregó el cliente?",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextoSecundario,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))

                Surface(
                    color = AzulEtiqueta,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Total a cobrar:",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextoSecundario
                        )
                        Text(
                            precio(total),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = AzulBotones
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    border = BorderStroke(2.dp, AzulBotones)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "$",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextoSecundario
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            String.format(Locale.US, "%.2f", monto.toDouble()),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextoPrincipal,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                Spacer(Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(50, 100, 200, 500).forEach { valor ->
                        val activo = monto == valor
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (activo) AzulEtiqueta else Color.White,
                            border = BorderStroke(1.dp, if (activo) AzulBotones else BordeCampo),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .clickable { monto = valor }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    "$" + valor,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (activo) AzulBotones else TextoSecundario
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.height(10.dp))

                val filas = listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9")
                )
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    filas.forEach { fila ->
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            fila.forEach { digito ->
                                Tecla(
                                    onClick = {
                                        monto = (monto * 10 + digito.toInt()).coerceAtMost(9_999_999)
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        digito,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = AzulTexto
                                    )
                                }
                            }
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Spacer(Modifier.weight(1f))
                        Tecla(
                            onClick = { monto = (monto * 10).coerceAtMost(9_999_999) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                "0",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = AzulTexto
                            )
                        }
                        Tecla(
                            onClick = { monto /= 10 },
                            modifier = Modifier.weight(1f)
                        ) {
                            IconoBorrar(color = AzulTexto, modifier = Modifier.size(22.dp))
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))

                Surface(
                    color = if (suficiente) VerdeEtiqueta else RojoEtiqueta,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Cambio:",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextoPrincipal
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            precio(cambio),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = if (suficiente) VerdeTexto else RojoTexto
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedButton(
                        onClick = onCerrar,
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, AzulBotones),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = AzulBotones),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text("Cancelar", fontWeight = FontWeight.SemiBold)
                    }
                    Button(
                        onClick = { onConfirmar(monto) },
                        enabled = suficiente,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AzulBotones),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text("Confirmar", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
private fun BotonTicketCirculo(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(AzulEtiqueta)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        IconoTicket(color = AzulBotones, modifier = Modifier.size(20.dp))
    }
}

@Composable
private fun Tecla(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color.White,
        border = BorderStroke(1.dp, BordeCampo),
        modifier = modifier
            .height(44.dp)
            .clickable(onClick = onClick)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            content()
        }
    }
}

@Composable
private fun IconoBillete(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val trazo = size.minDimension * 0.09f
        drawRoundRect(
            color = color,
            topLeft = Offset(size.width * 0.05f, size.height * 0.22f),
            size = Size(size.width * 0.90f, size.height * 0.56f),
            cornerRadius = CornerRadius(size.width * 0.08f),
            style = Stroke(width = trazo)
        )
        drawCircle(
            color = color,
            radius = size.minDimension * 0.13f,
            center = Offset(size.width * 0.5f, size.height * 0.5f),
            style = Stroke(width = trazo)
        )
    }
}

@Composable
private fun IconoTarjeta(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val trazo = size.minDimension * 0.09f
        drawRoundRect(
            color = color,
            topLeft = Offset(size.width * 0.08f, size.height * 0.22f),
            size = Size(size.width * 0.84f, size.height * 0.56f),
            cornerRadius = CornerRadius(size.width * 0.08f),
            style = Stroke(width = trazo)
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.10f, size.height * 0.40f),
            end = Offset(size.width * 0.90f, size.height * 0.40f),
            strokeWidth = size.height * 0.10f
        )
    }
}

@Composable
private fun IconoTransferencia(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val trazo = size.minDimension * 0.09f
        drawLine(
            color,
            Offset(size.width * 0.18f, size.height * 0.35f),
            Offset(size.width * 0.82f, size.height * 0.35f),
            trazo,
            StrokeCap.Round
        )
        drawLine(
            color,
            Offset(size.width * 0.66f, size.height * 0.20f),
            Offset(size.width * 0.82f, size.height * 0.35f),
            trazo,
            StrokeCap.Round
        )
        drawLine(
            color,
            Offset(size.width * 0.66f, size.height * 0.50f),
            Offset(size.width * 0.82f, size.height * 0.35f),
            trazo,
            StrokeCap.Round
        )
        drawLine(
            color,
            Offset(size.width * 0.82f, size.height * 0.65f),
            Offset(size.width * 0.18f, size.height * 0.65f),
            trazo,
            StrokeCap.Round
        )
        drawLine(
            color,
            Offset(size.width * 0.34f, size.height * 0.50f),
            Offset(size.width * 0.18f, size.height * 0.65f),
            trazo,
            StrokeCap.Round
        )
        drawLine(
            color,
            Offset(size.width * 0.34f, size.height * 0.80f),
            Offset(size.width * 0.18f, size.height * 0.65f),
            trazo,
            StrokeCap.Round
        )
    }
}

@Composable
private fun IconoBorrar(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val trazo = size.minDimension * 0.09f
        val forma = Path().apply {
            moveTo(size.width * 0.40f, size.height * 0.22f)
            lineTo(size.width * 0.92f, size.height * 0.22f)
            lineTo(size.width * 0.92f, size.height * 0.78f)
            lineTo(size.width * 0.40f, size.height * 0.78f)
            lineTo(size.width * 0.10f, size.height * 0.50f)
            close()
        }
        drawPath(forma, color, style = Stroke(width = trazo))
        drawLine(
            color,
            Offset(size.width * 0.53f, size.height * 0.38f),
            Offset(size.width * 0.77f, size.height * 0.62f),
            trazo,
            StrokeCap.Round
        )
        drawLine(
            color,
            Offset(size.width * 0.77f, size.height * 0.38f),
            Offset(size.width * 0.53f, size.height * 0.62f),
            trazo,
            StrokeCap.Round
        )
    }
}

@Composable
internal fun DialogoConfirmarVenta(
    metodo: String,
    onCerrar: () -> Unit,
    onConfirmar: () -> Unit,
    onVerTicket: () -> Unit
) {
    Dialog(onDismissRequest = onCerrar) {
        Surface(shape = RoundedCornerShape(24.dp), color = Color.White) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BotonTicketCirculo(onClick = onVerTicket)
                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = onCerrar) {
                        Text("✕", color = TextoSecundario, style = MaterialTheme.typography.titleLarge)
                    }
                }
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(AzulEtiqueta, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (metodo == "Tarjeta") {
                        IconoTarjeta(AzulBotones, Modifier.size(34.dp))
                    } else {
                        IconoTransferencia(AzulBotones, Modifier.size(34.dp))
                    }
                }
                Spacer(Modifier.height(14.dp))
                Text(
                    "Confirmar venta",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = AzulTexto
                )
                Spacer(Modifier.height(20.dp))
                DeslizaParaConfirmar(onConfirmar)
            }
        }
    }
}

@Composable
private fun DeslizaParaConfirmar(onConfirmar: () -> Unit) {
    val densidad = LocalDensity.current
    val tamanoThumb = with(densidad) { 48.dp.toPx() }
    val margen = with(densidad) { 4.dp.toPx() }
    var ancho by remember { mutableStateOf(0) }
    var desplazamiento by remember { mutableStateOf(0f) }
    val maximo = (ancho - tamanoThumb - margen * 2).coerceAtLeast(0f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(AzulEtiqueta)
            .onSizeChanged { ancho = it.width },
        contentAlignment = Alignment.Center
    ) {
        Text(
            "Desliza para confirmar",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = AzulTexto
        )
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset { IntOffset((margen + desplazamiento).roundToInt(), 0) }
                .size(48.dp)
                .background(AzulBotones, CircleShape)
                .pointerInput(maximo) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            if (desplazamiento >= maximo * 0.9f) onConfirmar()
                            desplazamiento = 0f
                        },
                        onHorizontalDrag = { change, arrastre ->
                            change.consume()
                            desplazamiento = (desplazamiento + arrastre).coerceIn(0f, maximo)
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(20.dp)) {
                val trazo = size.minDimension * 0.13f
                drawLine(
                    Color.White,
                    Offset(size.width * 0.35f, size.height * 0.25f),
                    Offset(size.width * 0.65f, size.height * 0.5f),
                    trazo,
                    StrokeCap.Round
                )
                drawLine(
                    Color.White,
                    Offset(size.width * 0.65f, size.height * 0.5f),
                    Offset(size.width * 0.35f, size.height * 0.75f),
                    trazo,
                    StrokeCap.Round
                )
            }
        }
    }
}
