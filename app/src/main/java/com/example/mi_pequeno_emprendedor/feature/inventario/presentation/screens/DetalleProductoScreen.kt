
package com.example.mi_pequeno_emprendedor.feature.inventario.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.mi_pequeno_emprendedor.feature.inventario.domain.model.Producto
import com.example.mi_pequeno_emprendedor.feature.inventario.domain.model.TipoMovimiento
import com.example.mi_pequeno_emprendedor.feature.inventario.presentation.viewmodel.InventarioViewModel
import com.example.mi_pequeno_emprendedor.ui.components.NavegacionInferior

import java.util.Locale

// ==========================================
// COLORES
// ==========================================

private val AzulDetalle = Color(0xFF0066E8)
private val AzulOscuroDetalle = Color(0xFF003BA6)
private val TextoDetalle = Color(0xFF101560)
private val GrisDetalle = Color(0xFF667092)
private val VerdeDetalle = Color(0xFF008E57)
private val RojoDetalle = Color(0xFFFF564B)
private val BordeDetalle = Color(0xFFE4EBF6)

// ==========================================
// FORMATO DE PRECIO
// ==========================================

private fun formatoPrecio(valor: Double): String {
    return String.format(Locale.US, "$%,.2f", valor)
}

// ==========================================
// PANTALLA DETALLE DEL PRODUCTO
// ==========================================

@Composable
fun DetalleProductoScreen(
    productoId: Int,
    inventarioViewModel: InventarioViewModel,
    alVolver: () -> Unit,
    alCambiarSeccion: (String) -> Unit = {}
) {

    // Consultar el producto directamente desde el ViewModel.
    val producto = inventarioViewModel.obtenerProducto(productoId)

    // Controlar la ventana de edición.
    var mostrarEdicion by remember {
        mutableStateOf(false)
    }

    // Mensaje para informar el resultado de cada operación.
    var mensaje by remember {
        mutableStateOf<String?>(null)
    }

    // Movimientos registrados para el producto actual.
    val movimientos = inventarioViewModel.movimientos.filter {
        it.productoId == productoId
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

            // ==================================
            // ENCABEZADO
            // ==================================

            EncabezadoDetalle(
                nombreProducto = producto?.nombre ?: "Producto"
            )

            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {

                // ==================================
                // BOTÓN REGRESAR
                // ==================================

                Surface(
                    onClick = alVolver,
                    color = Color(0xFFF0F5FF),
                    shape = RoundedCornerShape(13.dp)
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
                            tint = AzulDetalle
                        )

                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )

                        Text(
                            text = "Volver a la carpeta",
                            color = AzulDetalle,
                            fontSize = 14.sp
                        )
                    }
                }

                if (producto == null) {

                    Text(
                        text = "No se encontró el producto.",
                        color = RojoDetalle
                    )

                } else {

                    // ==================================
                    // INFORMACIÓN GENERAL
                    // ==================================

                    InformacionProducto(
                        producto = producto
                    )

                    // ==================================
                    // EXISTENCIAS Y AJUSTES
                    // ==================================

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(17.dp),
                        color = Color(0xFFEAF3FF)
                    ) {

                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {

                            Text(
                                text = "Existencias disponibles",
                                fontSize = 17.sp,
                                color = TextoDetalle,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "${producto.existencias}",
                                fontSize = 44.sp,
                                fontWeight = FontWeight.Bold,
                                color = AzulDetalle
                            )

                            Text(
                                text = "unidades",
                                fontSize = 14.sp,
                                color = GrisDetalle
                            )

                            // Estado del inventario.
                            val estado = when {
                                producto.existencias == 0 -> "Agotado"
                                producto.existencias <= 3 -> "Stock bajo"
                                else -> "Disponible"
                            }

                            val colorEstado = if (
                                producto.existencias <= 3
                            ) {
                                RojoDetalle
                            } else {
                                VerdeDetalle
                            }

                            Surface(
                                color = colorEstado.copy(alpha = 0.12f),
                                shape = CircleShape
                            ) {
                                Text(
                                    text = estado,
                                    modifier = Modifier.padding(
                                        horizontal = 15.dp,
                                        vertical = 6.dp
                                    ),
                                    color = colorEstado,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            HorizontalDivider(
                                color = Color(0xFFD0E3FF)
                            )

                            Text(
                                text = "Ajuste rápido de inventario",
                                fontSize = 14.sp,
                                color = TextoDetalle
                            )

                            Row(
                                horizontalArrangement =
                                    Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                // RESTAR UNA UNIDAD
                                OutlinedButton(
                                    onClick = {

                                        val realizado =
                                            inventarioViewModel
                                                .disminuirUnaUnidad(
                                                    productoId
                                                )

                                        mensaje = if (realizado) {
                                            "Se descontó 1 unidad."
                                        } else {
                                            "No hay existencias para descontar."
                                        }
                                    },
                                    enabled = producto.existencias > 0,
                                    shape = RoundedCornerShape(13.dp)
                                ) {

                                    Icon(
                                        imageVector = Icons.Default.Remove,
                                        contentDescription = null
                                    )

                                    Spacer(
                                        modifier = Modifier.width(5.dp)
                                    )

                                    Text("-1")
                                }

                                // SUMAR UNA UNIDAD
                                Button(
                                    onClick = {

                                        val realizado =
                                            inventarioViewModel
                                                .aumentarUnaUnidad(
                                                    productoId
                                                )

                                        mensaje = if (realizado) {
                                            "Se agregó 1 unidad."
                                        } else {
                                            "No se pudo realizar el ajuste."
                                        }
                                    },
                                    shape = RoundedCornerShape(13.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = AzulDetalle
                                    )
                                ) {

                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null
                                    )

                                    Spacer(
                                        modifier = Modifier.width(5.dp)
                                    )

                                    Text("+1")
                                }
                            }
                        }
                    }

                    // ==================================
                    // BOTÓN EDITAR PRODUCTO
                    // ==================================

                    Button(
                        onClick = {
                            mostrarEdicion = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(13.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AzulDetalle
                        )
                    ) {

                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null
                        )

                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )

                        Text("Editar producto")
                    }

                    // ==================================
                    // HISTORIAL DE MOVIMIENTOS
                    // ==================================

                    Text(
                        text = "Historial de movimientos",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoDetalle
                    )

                    if (movimientos.isEmpty()) {

                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFFF6F9FF),
                            border = BorderStroke(
                                1.dp,
                                BordeDetalle
                            )
                        ) {

                            Text(
                                text = "Este producto todavía no tiene movimientos registrados.",
                                modifier = Modifier.padding(18.dp),
                                fontSize = 13.sp,
                                lineHeight = 19.sp,
                                color = GrisDetalle
                            )
                        }

                    } else {

                        movimientos.forEach { movimiento ->

                            val esEntrada = movimiento.cantidad > 0

                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                color = Color.White,
                                border = BorderStroke(
                                    1.dp,
                                    BordeDetalle
                                )
                            ) {

                                Row(
                                    modifier = Modifier.padding(13.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement =
                                        Arrangement.spacedBy(12.dp)
                                ) {

                                    Box(
                                        modifier = Modifier
                                            .size(43.dp)
                                            .background(
                                                if (esEntrada) {
                                                    Color(0xFFE5FAF0)
                                                } else {
                                                    Color(0xFFFFECEA)
                                                },
                                                CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {

                                        Icon(
                                            imageVector = if (esEntrada) {
                                                Icons.Default.TrendingUp
                                            } else {
                                                Icons.Default.TrendingDown
                                            },
                                            contentDescription = null,
                                            tint = if (esEntrada) {
                                                VerdeDetalle
                                            } else {
                                                RojoDetalle
                                            }
                                        )
                                    }

                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {

                                        Text(
                                            text = when (movimiento.tipo) {

                                                TipoMovimiento.ENTRADA ->
                                                    "Entrada de mercancía"

                                                TipoMovimiento.BAJA ->
                                                    "Baja de mercancía"

                                                TipoMovimiento.AJUSTE ->
                                                    "Ajuste de inventario"
                                            },
                                            color = TextoDetalle,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )

                                        Text(
                                            text = movimiento.fecha,
                                            color = GrisDetalle,
                                            fontSize = 11.sp
                                        )

                                        Text(
                                            text = "${movimiento.existenciasAnteriores} → ${movimiento.existenciasNuevas} unidades",
                                            color = GrisDetalle,
                                            fontSize = 11.sp
                                        )
                                    }

                                    Text(
                                        text = if (esEntrada) {
                                            "+${movimiento.cantidad}"
                                        } else {
                                            "${movimiento.cantidad}"
                                        },
                                        color = if (esEntrada) {
                                            VerdeDetalle
                                        } else {
                                            RojoDetalle
                                        },
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // ==========================================
    // DIÁLOGO PARA EDITAR PRODUCTO
    // ==========================================

    if (mostrarEdicion && producto != null) {

        DialogoEditarProducto(
            producto = producto,

            alCancelar = {
                mostrarEdicion = false
            },

            alGuardar = { nombre, presentacion, compra, venta ->

                val actualizado = inventarioViewModel.editarProducto(
                    id = productoId,
                    nombre = nombre,
                    presentacion = presentacion,
                    precioCompra = compra,
                    precioVenta = venta
                )

                if (actualizado) {
                    mostrarEdicion = false
                    mensaje = "Producto actualizado correctamente."
                } else {
                    mensaje = "No se pudo actualizar el producto."
                }
            }
        )
    }

    // ==========================================
    // MENSAJE DE CONFIRMACIÓN
    // ==========================================

    if (mensaje != null) {

        AlertDialog(
            onDismissRequest = {
                mensaje = null
            },

            title = {
                Text("Inventario")
            },

            text = {
                Text(mensaje.orEmpty())
            },

            confirmButton = {

                TextButton(
                    onClick = {
                        mensaje = null
                    }
                ) {
                    Text("Aceptar")
                }
            }
        )
    }
}

// ==========================================
// ENCABEZADO AZUL
// ==========================================

@Composable
private fun EncabezadoDetalle(
    nombreProducto: String
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
            .background(
                Brush.linearGradient(
                    listOf(
                        AzulDetalle,
                        AzulOscuroDetalle
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

                moveTo(
                    0f,
                    alto * 0.8f
                )

                cubicTo(
                    ancho * 0.3f,
                    alto * 0.6f,
                    ancho * 0.45f,
                    alto * 1.05f,
                    ancho * 0.68f,
                    alto * 0.8f
                )

                cubicTo(
                    ancho * 0.84f,
                    alto * 0.68f,
                    ancho * 0.9f,
                    alto * 0.8f,
                    ancho,
                    alto * 0.68f
                )

                lineTo(ancho, alto)
                lineTo(0f, alto)
                close()
            }

            drawPath(
                path = onda,
                color = Color(0xFF49A2FF).copy(
                    alpha = 0.45f
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(18.dp),
            verticalArrangement = Arrangement.Bottom
        ) {

            Text(
                text = "Inventario",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Detalle de $nombreProducto",
                color = Color(0xFFE0EDFF),
                fontSize = 15.sp
            )
        }
    }
}

// ==========================================
// INFORMACIÓN GENERAL DEL PRODUCTO
// ==========================================

@Composable
private fun InformacionProducto(
    producto: Producto
) {

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(17.dp),
        color = Color.White,
        border = BorderStroke(
            1.dp,
            BordeDetalle
        ),
        shadowElevation = 3.dp
    ) {

        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(13.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                Box(
                    modifier = Modifier
                        .size(78.dp)
                        .background(
                            Color(0xFFEAF3FF),
                            RoundedCornerShape(14.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.Inventory2,
                        contentDescription = null,
                        tint = AzulDetalle,
                        modifier = Modifier.size(44.dp)
                    )
                }

                Column {

                    Text(
                        text = producto.nombre,
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoDetalle
                    )

                    Text(
                        text = producto.presentacion,
                        fontSize = 15.sp,
                        color = GrisDetalle
                    )
                }
            }

            HorizontalDivider(
                color = BordeDetalle
            )

            FilaInformacion(
                etiqueta = "Nombre",
                valor = producto.nombre
            )

            FilaInformacion(
                etiqueta = "Presentación",
                valor = producto.presentacion
            )

            FilaInformacion(
                etiqueta = "Carpeta",
                valor = producto.carpeta
            )

            FilaInformacion(
                etiqueta = "Precio de compra",
                valor = formatoPrecio(producto.precioCompra)
            )

            FilaInformacion(
                etiqueta = "Precio de venta",
                valor = formatoPrecio(producto.precioVenta)
            )

            FilaInformacion(
                etiqueta = "Existencia actual",
                valor = "${producto.existencias} unidades"
            )

            FilaInformacion(
                etiqueta = "Última entrada",
                valor = producto.ultimaEntrada
            )
        }
    }
}

// ==========================================
// FILA DE INFORMACIÓN
// ==========================================

@Composable
private fun FilaInformacion(
    etiqueta: String,
    valor: String
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = etiqueta,
            color = GrisDetalle,
            fontSize = 13.sp,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = valor,
            color = TextoDetalle,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}

// ==========================================
// DIÁLOGO PARA EDITAR PRODUCTO
// ==========================================

@Composable
private fun DialogoEditarProducto(
    producto: Producto,
    alCancelar: () -> Unit,
    alGuardar: (
        String,
        String,
        Double,
        Double
    ) -> Unit
) {

    var nombre by remember(producto.id) {
        mutableStateOf(producto.nombre)
    }

    var presentacion by remember(producto.id) {
        mutableStateOf(producto.presentacion)
    }

    var compraTexto by remember(producto.id) {
        mutableStateOf(producto.precioCompra.toString())
    }

    var ventaTexto by remember(producto.id) {
        mutableStateOf(producto.precioVenta.toString())
    }

    val compra = compraTexto.toDoubleOrNull()
    val venta = ventaTexto.toDoubleOrNull()

    val datosValidos =
        nombre.isNotBlank() &&
                presentacion.isNotBlank() &&
                compra != null &&
                compra.isFinite() &&
                compra >= 0 &&
                venta != null &&
                venta.isFinite() &&
                venta >= 0

    AlertDialog(
        onDismissRequest = alCancelar,

        title = {
            Text("Editar producto")
        },

        text = {

            Column(
                modifier = Modifier.verticalScroll(
                    rememberScrollState()
                ),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                OutlinedTextField(
                    value = nombre,
                    onValueChange = {
                        nombre = it
                    },
                    label = {
                        Text("Nombre")
                    },
                    singleLine = true
                )

                OutlinedTextField(
                    value = presentacion,
                    onValueChange = {
                        presentacion = it
                    },
                    label = {
                        Text("Presentación")
                    },
                    singleLine = true
                )

                OutlinedTextField(
                    value = compraTexto,
                    onValueChange = {
                        compraTexto = it
                    },
                    label = {
                        Text("Precio de compra")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    singleLine = true
                )

                OutlinedTextField(
                    value = ventaTexto,
                    onValueChange = {
                        ventaTexto = it
                    },
                    label = {
                        Text("Precio de venta")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    singleLine = true
                )
            }
        },

        confirmButton = {

            TextButton(
                enabled = datosValidos,
                onClick = {

                    val precioCompra =
                        compra ?: return@TextButton

                    val precioVenta =
                        venta ?: return@TextButton

                    alGuardar(
                        nombre.trim(),
                        presentacion.trim(),
                        precioCompra,
                        precioVenta
                    )
                }
            ) {
                Text("Guardar cambios")
            }
        },

        dismissButton = {

            TextButton(
                onClick = alCancelar
            ) {
                Text("Cancelar")
            }
        }
    )
}
