package com.example.mi_pequeno_emprendedor.ui.venta

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.util.Locale

internal data class DatosTicketPdf(
    val items: List<ItemTicket>,
    val total: Int,
    val nombreNegocio: String,
    val metodoPago: String,
    val montoRecibidoCentavos: Int?,
    val pagoExacto: Boolean,
    val fecha: String,
    val hora: String,
    val folio: String
)

internal object TicketPdf {

    private const val ANCHO = 595
    private const val ALTO = 842
    private const val MARGEN = 48f

    fun generar(context: Context, ticket: DatosTicketPdf): File {
        val pdf = PdfDocument()
        renderizar(pdf, ticket)

        val carpeta = File(context.cacheDir, "tickets").apply { mkdirs() }
        val archivo = File(carpeta, "ticket_${ticket.folio.filter { it.isLetterOrDigit() }}.pdf")
        FileOutputStream(archivo).use { pdf.writeTo(it) }
        pdf.close()
        return archivo
    }

    fun guardarEnDescargas(context: Context, archivo: File): Uri {
        val valores = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, archivo.name)
            put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
            put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            put(MediaStore.Downloads.IS_PENDING, 1)
        }
        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, valores)
            ?: error("No se pudo crear el archivo en Descargas")
        resolver.openOutputStream(uri).use { salida ->
            requireNotNull(salida) { "No se pudo abrir el archivo de Descargas" }
            archivo.inputStream().use { it.copyTo(salida) }
        }
        resolver.update(
            uri,
            ContentValues().apply { put(MediaStore.Downloads.IS_PENDING, 0) },
            null,
            null
        )
        return uri
    }

    fun compartir(context: Context, archivo: File) {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            archivo
        )
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(
            Intent.createChooser(intent, "Compartir ticket").apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        )
    }

    private fun renderizar(pdf: PdfDocument, ticket: DatosTicketPdf) {
        val lienzo = LienzoPdf(pdf)
        val derecha = ANCHO - MARGEN

        fun escribir(valor: String, x: Float, paint: Paint) {
            lienzo.canvas.drawText(valor, x, lienzo.y, paint)
        }

        val separador = Paint().apply {
            color = BORDE
            strokeWidth = 1f
        }
        val izq = pintura(11f, TEXTO)
        val izqGris = pintura(10f, GRIS)
        val izqNegrita = pintura(11f, TEXTO, negrita = true)
        val der = pintura(11f, TEXTO, derecha = true)
        val derGris = pintura(10f, GRIS, derecha = true)
        val derNegrita = pintura(11f, TEXTO, negrita = true, derecha = true)
        val centroGris = pintura(10f, GRIS, centro = true)
        val centro = pintura(11f, TEXTO, centro = true)
        val centroNegrita = pintura(11f, TEXTO, negrita = true, centro = true)

        lienzo.asegurar(60f)
        lienzo.canvas.drawText(ticket.nombreNegocio, ANCHO / 2f, lienzo.y, pintura(22f, AZUL_TEXTO, negrita = true, centro = true))
        lienzo.y += 20f
        lienzo.canvas.drawText("Gracias por tu compra", ANCHO / 2f, lienzo.y, pintura(11f, GRIS, centro = true))
        lienzo.y += 24f
        lienzo.canvas.drawLine(MARGEN, lienzo.y, derecha, lienzo.y, separador)
        lienzo.y += 20f

        lienzo.asegurar(40f)
        escribir("Fecha: ${ticket.fecha}", MARGEN, izq)
        escribir("Hora: ${ticket.hora}", derecha, der)
        lienzo.y += 16f
        escribir("Folio: ${ticket.folio}", MARGEN, izq)
        lienzo.y += 20f
        lienzo.canvas.drawLine(MARGEN, lienzo.y, derecha, lienzo.y, separador)
        lienzo.y += 18f

        lienzo.asegurar(20f)
        escribir("Producto", MARGEN, izqGris)
        escribir("Cant.", ANCHO / 2f + 40f, centroGris)
        escribir("Precio", derecha, derGris)
        lienzo.y += 18f

        ticket.items.forEach { item ->
            lienzo.asegurar(34f)
            escribir(item.nombre, MARGEN, izqNegrita)
            lienzo.canvas.drawText(item.detalle, MARGEN, lienzo.y + 13f, izqGris)
            escribir(item.cantidad.toString(), ANCHO / 2f + 40f, centro)
            escribir(precio(item.precioUnitario * item.cantidad), derecha, der)
            lienzo.y += 34f
        }

        lienzo.asegurar(20f)
        lienzo.canvas.drawLine(MARGEN, lienzo.y, derecha, lienzo.y, separador)
        lienzo.y += 20f

        lienzo.asegurar(56f)
        escribir("Subtotal", derecha - 140f, izq)
        escribir(precio(ticket.total), derecha, der)
        lienzo.y += 16f
        escribir("Descuento", derecha - 140f, izq)
        escribir(precio(0), derecha, der)
        lienzo.y += 24f

        lienzo.canvas.drawRoundRect(
            RectF(MARGEN, lienzo.y - 6f, derecha, lienzo.y + 28f),
            8f,
            8f,
            Paint().apply { color = AZUL_ETIQUETA }
        )
        lienzo.canvas.drawText("Total", MARGEN + 12f, lienzo.y + 18f, pintura(12f, AZUL_TEXTO, negrita = true))
        lienzo.canvas.drawText(
            precio(ticket.total),
            derecha - 12f,
            lienzo.y + 18f,
            pintura(16f, AZUL_BOTONES, negrita = true, derecha = true)
        )
        lienzo.y += 48f

        lienzo.asegurar(32f)
        escribir("Método de pago", MARGEN, izq)
        escribir(ticket.metodoPago, derecha, derNegrita)
        lienzo.y += 16f
        if (ticket.pagoExacto) {
            escribir("Monto recibido", MARGEN, izq)
            escribir("Pago exacto", derecha, der)
            lienzo.y += 16f
            escribir("Cambio", MARGEN, izq)
            escribir(precio(0), derecha, der)
        } else {
            escribir("Monto recibido", MARGEN, izq)
            escribir(ticket.montoRecibidoCentavos?.let { precioCentavos(it) } ?: "—", derecha, der)
            lienzo.y += 16f
            escribir("Cambio", MARGEN, izq)
            escribir(ticket.montoRecibidoCentavos?.let { precioCentavos(it - ticket.total * 100) } ?: "—", derecha, der)
        }
        lienzo.y += 30f

        lienzo.asegurar(44f)
        lienzo.canvas.drawLine(MARGEN, lienzo.y, derecha, lienzo.y, separador)
        lienzo.y += 24f
        lienzo.canvas.drawText("¡Gracias por tu compra!", ANCHO / 2f, lienzo.y, centroNegrita)
        lienzo.y += 16f
        lienzo.canvas.drawText("Vuelve pronto", ANCHO / 2f, lienzo.y, centroGris)

        lienzo.terminar()
    }

    private class LienzoPdf(private val pdf: PdfDocument) {
        private var numeroPagina = 1
        private var pagina = pdf.startPage(
            PdfDocument.PageInfo.Builder(ANCHO, ALTO, numeroPagina).create()
        )
        var canvas: Canvas = pagina.canvas
            private set
        var y = 80f

        fun asegurar(alto: Float) {
            if (y + alto <= ALTO - MARGEN) return
            pdf.finishPage(pagina)
            numeroPagina += 1
            pagina = pdf.startPage(
                PdfDocument.PageInfo.Builder(ANCHO, ALTO, numeroPagina).create()
            )
            canvas = pagina.canvas
            y = MARGEN
        }

        fun terminar() {
            pdf.finishPage(pagina)
        }
    }

    private fun pintura(
        tamano: Float,
        color: Int,
        negrita: Boolean = false,
        centro: Boolean = false,
        derecha: Boolean = false
    ) = Paint().apply {
        isAntiAlias = true
        textSize = tamano
        this.color = color
        typeface = if (negrita) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
        textAlign = when {
            centro -> Paint.Align.CENTER
            derecha -> Paint.Align.RIGHT
            else -> Paint.Align.LEFT
        }
    }

    private fun precio(valor: Int): String {
        val signo = if (valor < 0) "-" else ""
        return signo + "$" + String.format(Locale.US, "%.2f", kotlin.math.abs(valor).toDouble())
    }

    private fun precioCentavos(centavos: Int): String {
        val signo = if (centavos < 0) "-" else ""
        val absoluto = kotlin.math.abs(centavos)
        return signo + "$" + String.format(Locale.US, "%d.%02d", absoluto / 100, absoluto % 100)
    }

    private const val AZUL_TEXTO = 0xFF0808AE.toInt()
    private const val AZUL_BOTONES = 0xFF0060FD.toInt()
    private const val AZUL_ETIQUETA = 0xFFE5F0FF.toInt()
    private const val TEXTO = 0xFF1B2430.toInt()
    private const val GRIS = 0xFF6B7A99.toInt()
    private const val BORDE = 0xFFD3DEEF.toInt()
}
