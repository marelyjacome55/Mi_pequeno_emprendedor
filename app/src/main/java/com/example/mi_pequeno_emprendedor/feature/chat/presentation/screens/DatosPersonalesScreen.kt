
package com.example.mi_pequeno_emprendedor.feature.configuracion.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mi_pequeno_emprendedor.data.repository.UsuarioRepository
import com.example.mi_pequeno_emprendedor.ui.components.NavegacionInferior
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private val AzulDatos = Color(0xFF0057CE)
private val TextoDatos = Color(0xFF0808AE)
private val GrisDatos = Color(0xFF667092)
private val FondoDatos = Color.White
private val FondoAvatar = Color(0xFFE5F0FF)
private val BordeDatos = Color(0xFFDCE3EF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatosPersonalesScreen(
    modifier: Modifier = Modifier,
    alVolver: () -> Unit = {},
    alCambiarSeccion: (String) -> Unit = {},
    alAbrirNotificaciones: () -> Unit = {}
) {
    val repositorio = remember { UsuarioRepository() }
    val scope = rememberCoroutineScope()

    // Información que se muestra en pantalla.
    var nombre by rememberSaveable { mutableStateOf("") }
    var apellidos by rememberSaveable { mutableStateOf("") }
    var nickname by rememberSaveable { mutableStateOf("") }
    var fechaNacimiento by rememberSaveable { mutableStateOf<Long?>(null) }

    // Información temporal para editar.
    var nombreEdicion by rememberSaveable { mutableStateOf("") }
    var apellidosEdicion by rememberSaveable { mutableStateOf("") }
    var nicknameEdicion by rememberSaveable { mutableStateOf("") }
    var fechaEdicion by rememberSaveable { mutableStateOf<Long?>(null) }

    var modoEdicion by rememberSaveable { mutableStateOf(false) }
    var mostrarSelectorFecha by remember { mutableStateOf(false) }
    var mostrarConfirmacion by remember { mutableStateOf(false) }
    var mostrarErrorNombre by remember { mutableStateOf(false) }

    var cargando by remember { mutableStateOf(true) }
    var guardando by remember { mutableStateOf(false) }

    var tituloDialogo by remember { mutableStateOf("") }
    var mensajeDialogo by remember { mutableStateOf("") }

    // Convierte la fecha de milisegundos a texto.
    fun formatearFecha(fecha: Long?): String {
        return fecha?.let {
            Instant.ofEpochMilli(it)
                .atZone(ZoneOffset.UTC)
                .toLocalDate()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        } ?: "Sin registrar"
    }

    // Convierte la fecha de Supabase a milisegundos.
    fun convertirFechaAMillis(fecha: String?): Long? {
        if (fecha.isNullOrBlank()) return null

        return try {
            LocalDate.parse(fecha)
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant()
                .toEpochMilli()
        } catch (e: Exception) {
            null
        }
    }

    fun iniciarEdicion() {
        nombreEdicion = nombre
        apellidosEdicion = apellidos
        nicknameEdicion = nickname
        fechaEdicion = fechaNacimiento
        mostrarErrorNombre = false
        modoEdicion = true
    }

    fun cancelarEdicion() {
        modoEdicion = false
        mostrarErrorNombre = false
    }

    // Cargar información del usuario desde Supabase.
    LaunchedEffect(Unit) {
        try {
            val usuario = repositorio.obtenerPerfilActual()

            if (usuario != null) {
                nombre = usuario.nombre
                apellidos = usuario.apellidos.orEmpty()
                nickname = usuario.nickname.orEmpty()
                fechaNacimiento = convertirFechaAMillis(
                    usuario.fechaNacimiento
                )
            }

        } catch (e: Exception) {
            tituloDialogo = "Error al cargar"
            mensajeDialogo = e.message
                ?: "No fue posible obtener tus datos personales."
            mostrarConfirmacion = true

        } finally {
            cargando = false
        }
    }

    // Guardar información en Supabase.
    fun guardarCambios() {
        if (nombreEdicion.isBlank()) {
            mostrarErrorNombre = true
            return
        }

        scope.launch {
            guardando = true

            try {
                val fechaTexto = fechaEdicion?.let {
                    Instant.ofEpochMilli(it)
                        .atZone(ZoneOffset.UTC)
                        .toLocalDate()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE)
                }

                repositorio.guardarDatosPersonales(
                    nombre = nombreEdicion,
                    apellidos = apellidosEdicion,
                    nickname = nicknameEdicion,
                    fechaNacimiento = fechaTexto
                )

                // Actualizar los datos que se muestran.
                nombre = nombreEdicion.trim()
                apellidos = apellidosEdicion.trim()
                nickname = nicknameEdicion.trim()
                fechaNacimiento = fechaEdicion

                modoEdicion = false
                tituloDialogo = "¡Listo!"
                mensajeDialogo = "Tus datos personales se guardaron correctamente."
                mostrarConfirmacion = true

            } catch (e: Exception) {
                tituloDialogo = "No se pudieron guardar los cambios"
                mensajeDialogo = e.message
                    ?: "Ocurrió un error al actualizar tus datos."

                mostrarConfirmacion = true

            } finally {
                guardando = false
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FondoDatos)
    ) {
        EncabezadoDatosPersonales(
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
                text = "Mi información",
                color = TextoDatos,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Mantén tus datos personales actualizados.",
                color = GrisDatos,
                fontSize = 14.sp
            )

            Spacer(Modifier.height(24.dp))

            // Foto de perfil
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box {
                    Box(
                        modifier = Modifier
                            .size(104.dp)
                            .clip(CircleShape)
                            .background(FondoAvatar),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = nombre.firstOrNull()?.uppercase() ?: "?",
                            color = AzulDatos,
                            fontSize = 38.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (modoEdicion) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(AzulDatos),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Cambiar foto",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Foto de perfil",
                color = GrisDatos,
                fontSize = 13.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(28.dp))

            Text(
                text = "Información básica",
                color = TextoDatos,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(16.dp))

            if (cargando) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 35.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AzulDatos)
                }

            } else if (modoEdicion) {

                CampoDatosPersonales(
                    etiqueta = "Nombre *",
                    valor = nombreEdicion,
                    alCambiar = {
                        nombreEdicion = it
                        mostrarErrorNombre = false
                    },
                    placeholder = "Ingresa tu nombre",
                    error = mostrarErrorNombre,
                    mensajeError = "El nombre es obligatorio"
                )

                Spacer(Modifier.height(15.dp))

                CampoDatosPersonales(
                    etiqueta = "Apellidos",
                    valor = apellidosEdicion,
                    alCambiar = { apellidosEdicion = it },
                    placeholder = "Ingresa tus apellidos"
                )

                Spacer(Modifier.height(15.dp))

                CampoDatosPersonales(
                    etiqueta = "Nickname",
                    valor = nicknameEdicion,
                    alCambiar = { nicknameEdicion = it },
                    placeholder = "Elige un nickname"
                )

                Spacer(Modifier.height(15.dp))

                Text(
                    text = "Fecha de nacimiento",
                    color = TextoDatos,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(Modifier.height(7.dp))

                OutlinedTextField(
                    value = fechaEdicion?.let {
                        formatearFecha(it)
                    }.orEmpty(),
                    onValueChange = {},
                    readOnly = true,
                    placeholder = {
                        Text("Selecciona tu fecha de nacimiento")
                    },
                    trailingIcon = {
                        TextButton(
                            onClick = { mostrarSelectorFecha = true }
                        ) {
                            Text("Elegir", color = AzulDatos)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(13.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AzulDatos,
                        unfocusedBorderColor = BordeDatos,
                        focusedTextColor = TextoDatos,
                        unfocusedTextColor = TextoDatos
                    ),
                    singleLine = true
                )

            } else {

                CampoLecturaDatos(
                    etiqueta = "Nombre",
                    valor = nombre
                )

                CampoLecturaDatos(
                    etiqueta = "Apellidos",
                    valor = apellidos
                )

                CampoLecturaDatos(
                    etiqueta = "Nickname",
                    valor = nickname
                )

                CampoLecturaDatos(
                    etiqueta = "Fecha de nacimiento",
                    valor = formatearFecha(fechaNacimiento)
                )
            }

            Spacer(Modifier.height(28.dp))

            if (!cargando && modoEdicion) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { cancelarEdicion() },
                        enabled = !guardando,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(13.dp)
                    ) {
                        Text(
                            "Cancelar",
                            color = GrisDatos
                        )
                    }

                    Button(
                        onClick = { guardarCambios() },
                        enabled = !guardando,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(13.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AzulDatos
                        )
                    ) {
                        if (guardando) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(21.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                "Guardar cambios",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "* Campos obligatorios",
                    color = GrisDatos,
                    fontSize = 12.sp
                )

            } else if (!cargando) {

                Button(
                    onClick = { iniciarEdicion() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(13.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AzulDatos
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

    // Selector de fecha
    if (mostrarSelectorFecha) {
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
                    Text("Aceptar", color = AzulDatos)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarSelectorFecha = false
                    }
                ) {
                    Text("Cancelar", color = GrisDatos)
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = FondoDatos
            )
        ) {
            DatePicker(
                state = estadoFecha,
                title = {
                    Text(
                        "Fecha de nacimiento",
                        modifier = Modifier.padding(
                            start = 24.dp,
                            top = 16.dp
                        )
                    )
                }
            )
        }
    }

    // Mensajes de éxito y error
    if (mostrarConfirmacion) {
        AlertDialog(
            onDismissRequest = {
                mostrarConfirmacion = false
            },
            containerColor = FondoDatos,
            title = {
                Text(
                    tituloDialogo,
                    color = TextoDatos,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    mensajeDialogo,
                    color = GrisDatos
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarConfirmacion = false
                    }
                ) {
                    Text("Aceptar", color = AzulDatos)
                }
            }
        )
    }
}

@Composable
private fun EncabezadoDatosPersonales(
    alVolver: () -> Unit,
    alAbrirNotificaciones: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AzulDatos)
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
                text = "Datos personales",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun CampoLecturaDatos(
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
            color = TextoDatos,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(Modifier.height(7.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(13.dp))
                .background(Color(0xFFF8FAFD))
                .padding(horizontal = 16.dp, vertical = 17.dp)
        ) {
            Text(
                text = valor.ifBlank { "Sin registrar" },
                color = if (valor.isBlank()) GrisDatos else TextoDatos,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
private fun CampoDatosPersonales(
    etiqueta: String,
    valor: String,
    alCambiar: (String) -> Unit,
    placeholder: String,
    error: Boolean = false,
    mensajeError: String = ""
) {
    Column {
        Text(
            text = etiqueta,
            color = TextoDatos,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(Modifier.height(7.dp))

        OutlinedTextField(
            value = valor,
            onValueChange = alCambiar,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(placeholder, color = GrisDatos)
            },
            singleLine = true,
            isError = error,
            shape = RoundedCornerShape(13.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AzulDatos,
                unfocusedBorderColor = BordeDatos,
                focusedTextColor = TextoDatos,
                unfocusedTextColor = TextoDatos,
                cursorColor = AzulDatos,
                errorBorderColor = Color(0xFFFD0B33)
            )
        )

        if (error) {
            Text(
                text = mensajeError,
                color = Color(0xFFFD0B33),
                fontSize = 12.sp,
                modifier = Modifier.padding(
                    start = 8.dp,
                    top = 4.dp
                )
            )
        }
    }
}
