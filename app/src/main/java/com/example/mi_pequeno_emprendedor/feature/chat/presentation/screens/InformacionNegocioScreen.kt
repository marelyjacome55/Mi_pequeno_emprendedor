
package com.example.mi_pequeno_emprendedor.feature.configuracion.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mi_pequeno_emprendedor.ui.components.NavegacionInferior
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private val AzulNegocio = Color(0xFF0057CE)
private val TextoNegocio = Color(0xFF0808AE)
private val GrisNegocio = Color(0xFF667092)
private val FondoNegocio = Color(0xFFFFFFFF)
private val BordeNegocio = Color(0xFFDCE3EF)
private val FondoAzulNegocio = Color(0xFFE5F0FF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformacionNegocioScreen(
    modifier: Modifier = Modifier,
    alVolver: () -> Unit = {},
    alCambiarSeccion: (String) -> Unit = {},
    alAbrirNotificaciones: () -> Unit = {}
) {
    // Información principal del negocio.
    var nombre by rememberSaveable { mutableStateOf("") }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var direccion by rememberSaveable { mutableStateOf("") }
    var fechaCreacion by rememberSaveable { mutableStateOf<Long?>(null) }
    var tienePuntoVenta by rememberSaveable { mutableStateOf(false) }

    // Copias temporales para editar.
    var nombreEdicion by rememberSaveable { mutableStateOf("") }
    var descripcionEdicion by rememberSaveable { mutableStateOf("") }
    var direccionEdicion by rememberSaveable { mutableStateOf("") }
    var fechaEdicion by rememberSaveable { mutableStateOf<Long?>(null) }
    var puntoVentaEdicion by rememberSaveable { mutableStateOf(false) }

    var modoEdicion by rememberSaveable { mutableStateOf(false) }
    var mostrarSelectorFecha by remember { mutableStateOf(false) }
    var mostrarConfirmacion by remember { mutableStateOf(false) }

    fun formatearFecha(fecha: Long?): String {
        return fecha?.let {
            Instant.ofEpochMilli(it)
                .atZone(ZoneOffset.UTC)
                .toLocalDate()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        } ?: "Sin registrar"
    }

    fun iniciarEdicion() {
        nombreEdicion = nombre
        descripcionEdicion = descripcion
        direccionEdicion = direccion
        fechaEdicion = fechaCreacion
        puntoVentaEdicion = tienePuntoVenta
        modoEdicion = true
    }

    fun cancelarEdicion() {
        modoEdicion = false
    }

    fun guardarCambios() {
        nombre = nombreEdicion.trim()
        descripcion = descripcionEdicion.trim()
        direccion = direccionEdicion.trim()
        fechaCreacion = fechaEdicion
        tienePuntoVenta = puntoVentaEdicion

        modoEdicion = false
        mostrarConfirmacion = true
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FondoNegocio)
    ) {
        EncabezadoInformacionNegocio(
            alVolver = alVolver,
            alAbrirNotificaciones = alAbrirNotificaciones
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Text(
                text = "Mi negocio",
                color = TextoNegocio,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Administra la información de tu emprendimiento.",
                color = GrisNegocio,
                fontSize = 14.sp
            )

            Spacer(Modifier.height(25.dp))

            Text(
                text = "Información general",
                color = TextoNegocio,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(16.dp))

            if (modoEdicion) {
                // MODO EDICIÓN

                CampoInformacionNegocio(
                    etiqueta = "Nombre del negocio",
                    valor = nombreEdicion,
                    alCambiar = { nombreEdicion = it },
                    placeholder = "Ej. Mi pequeño negocio"
                )

                Spacer(Modifier.height(16.dp))

                CampoInformacionNegocio(
                    etiqueta = "Descripción de tu negocio",
                    valor = descripcionEdicion,
                    alCambiar = { descripcionEdicion = it },
                    placeholder = "Cuéntanos sobre tu emprendimiento...",
                    esMultilinea = true
                )

                Spacer(Modifier.height(16.dp))

                CampoInformacionNegocio(
                    etiqueta = "Dirección",
                    valor = direccionEdicion,
                    alCambiar = { direccionEdicion = it },
                    placeholder = "Ingresa la dirección de tu negocio",
                    esMultilinea = true
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Fecha de creación del negocio",
                    color = TextoNegocio,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(Modifier.height(7.dp))

                OutlinedTextField(
                    value = fechaEdicion?.let {
                        formatearFecha(it)
                    } ?: "",
                    onValueChange = {},
                    readOnly = true,
                    placeholder = {
                        Text("Selecciona la fecha de creación")
                    },
                    trailingIcon = {
                        TextButton(
                            onClick = {
                                mostrarSelectorFecha = true
                            }
                        ) {
                            Text(
                                "Elegir",
                                color = AzulNegocio,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(13.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AzulNegocio,
                        unfocusedBorderColor = BordeNegocio,
                        focusedTextColor = TextoNegocio,
                        unfocusedTextColor = TextoNegocio,
                        cursorColor = AzulNegocio
                    ),
                    singleLine = true
                )

                Spacer(Modifier.height(25.dp))

                // Interruptor editable.
                PuntoVentaCard(
                    tienePuntoVenta = puntoVentaEdicion,
                    editable = true,
                    alCambiar = {
                        puntoVentaEdicion = it
                    }
                )

            } else {
                // MODO CONSULTA

                CampoLecturaNegocio(
                    etiqueta = "Nombre del negocio",
                    valor = nombre
                )

                CampoLecturaNegocio(
                    etiqueta = "Descripción de tu negocio",
                    valor = descripcion
                )

                CampoLecturaNegocio(
                    etiqueta = "Dirección",
                    valor = direccion
                )

                CampoLecturaNegocio(
                    etiqueta = "Fecha de creación del negocio",
                    valor = fechaCreacion?.let {
                        formatearFecha(it)
                    } ?: ""
                )

                Spacer(Modifier.height(10.dp))

                PuntoVentaCard(
                    tienePuntoVenta = tienePuntoVenta,
                    editable = false
                )
            }

            Spacer(Modifier.height(28.dp))

            if (modoEdicion) {
                // Botones de edición.
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            cancelarEdicion()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(13.dp)
                    ) {
                        Text(
                            "Cancelar",
                            color = GrisNegocio
                        )
                    }

                    Button(
                        onClick = {
                            guardarCambios()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(13.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AzulNegocio
                        )
                    ) {
                        Text(
                            "Guardar cambios",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

            } else {
                // Botón para habilitar edición.
                Button(
                    onClick = {
                        iniciarEdicion()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(13.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AzulNegocio
                    )
                ) {
                    Text(
                        "Editar información",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
        }

        NavegacionInferior(
            seccionSeleccionada = "Perfil",
            alCambiarSeccion = alCambiarSeccion
        )
    }

    // Selector de fecha, disponible solamente durante la edición.
    if (mostrarSelectorFecha && modoEdicion) {
        val estadoFecha = rememberDatePickerState(
            initialSelectedDateMillis = fechaEdicion
        )

        DatePickerDialog(
            onDismissRequest = {
                mostrarSelectorFecha = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        fechaEdicion = estadoFecha.selectedDateMillis
                        mostrarSelectorFecha = false
                    }
                ) {
                    Text("Aceptar", color = AzulNegocio)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarSelectorFecha = false
                    }
                ) {
                    Text("Cancelar", color = GrisNegocio)
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = FondoNegocio
            )
        ) {
            DatePicker(
                state = estadoFecha,
                title = {
                    Text(
                        "Fecha de creación",
                        modifier = Modifier.padding(
                            start = 24.dp,
                            top = 16.dp
                        )
                    )
                }
            )
        }
    }

    // Confirmación visual.
    if (mostrarConfirmacion) {
        AlertDialog(
            onDismissRequest = {
                mostrarConfirmacion = false
            },
            containerColor = FondoNegocio,
            title = {
                Text(
                    "¡Listo!",
                    color = TextoNegocio,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "La información de tu negocio se actualizó en esta vista de prueba. Todavía no se ha guardado en la base de datos.",
                    color = GrisNegocio
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarConfirmacion = false
                    }
                ) {
                    Text("Aceptar", color = AzulNegocio)
                }
            }
        )
    }
}

@Composable
private fun EncabezadoInformacionNegocio(
    alVolver: () -> Unit,
    alAbrirNotificaciones: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AzulNegocio)
            .statusBarsPadding()
            .height(64.dp)
            .padding(horizontal = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = alVolver) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Regresar",
                    tint = Color.White
                )
            }

            Text(
                text = "Información del negocio",
                color = Color.White,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            /*IconButton(onClick = alAbrirNotificaciones) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notificaciones",
                    tint = Color.White
                )
            }*/
        }
    }
}

@Composable
private fun CampoLecturaNegocio(
    etiqueta: String,
    valor: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Text(
            text = etiqueta,
            color = TextoNegocio,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(Modifier.height(7.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color(0xFFF8FAFD),
                    RoundedCornerShape(13.dp)
                )
                .padding(
                    horizontal = 16.dp,
                    vertical = 17.dp
                )
        ) {
            Text(
                text = valor.ifBlank { "Sin registrar" },
                color = if (valor.isBlank()) GrisNegocio else TextoNegocio,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
private fun CampoInformacionNegocio(
    etiqueta: String,
    valor: String,
    alCambiar: (String) -> Unit,
    placeholder: String,
    esMultilinea: Boolean = false
) {
    Column {
        Text(
            text = etiqueta,
            color = TextoNegocio,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(Modifier.height(7.dp))

        OutlinedTextField(
            value = valor,
            onValueChange = alCambiar,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    placeholder,
                    color = GrisNegocio
                )
            },
            singleLine = !esMultilinea,
            minLines = if (esMultilinea) 3 else 1,
            maxLines = if (esMultilinea) 4 else 1,
            shape = RoundedCornerShape(13.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AzulNegocio,
                unfocusedBorderColor = BordeNegocio,
                focusedTextColor = TextoNegocio,
                unfocusedTextColor = TextoNegocio,
                cursorColor = AzulNegocio
            )
        )
    }
}

@Composable
private fun PuntoVentaCard(
    tienePuntoVenta: Boolean,
    editable: Boolean,
    alCambiar: (Boolean) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                FondoAzulNegocio,
                RoundedCornerShape(15.dp)
            )
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Punto de venta",
                    color = TextoNegocio,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = if (tienePuntoVenta) {
                        "Tu negocio cuenta con un punto de venta."
                    } else {
                        "Tu negocio no cuenta con un punto de venta."
                    },
                    color = GrisNegocio,
                    fontSize = 12.sp
                )
            }

            if (editable) {
                Switch(
                    checked = tienePuntoVenta,
                    onCheckedChange = alCambiar,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = AzulNegocio,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = GrisNegocio
                    )
                )
            } else {
                Text(
                    text = if (tienePuntoVenta) "Sí" else "No",
                    color = TextoNegocio,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}
