
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

private val AzulNotificaciones = Color(0xFF0057CE)
private val TextoNotificaciones = Color(0xFF0808AE)
private val GrisNotificaciones = Color(0xFF667092)
private val FondoNotificaciones = Color.White
private val FondoAzulNotificaciones = Color(0xFFE5F0FF)

@Composable
fun NotificacionesScreen(
    modifier: Modifier = Modifier,
    alVolver: () -> Unit = {},
    alCambiarSeccion: (String) -> Unit = {},
    alAbrirNotificaciones: () -> Unit = {}
) {
    // Preferencias temporales de notificaciones.
    var mensajes by rememberSaveable { mutableStateOf(true) }
    var actividadNegocio by rememberSaveable { mutableStateOf(true) }
    var recordatorios by rememberSaveable { mutableStateOf(true) }
    var novedades by rememberSaveable { mutableStateOf(false) }
    var seguridad by rememberSaveable { mutableStateOf(true) }

    var mostrarConfirmacion by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FondoNotificaciones)
    ) {
        EncabezadoNotificaciones(
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
                text = "Notificaciones",
                color = TextoNotificaciones,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Personaliza los avisos que deseas recibir de la aplicación.",
                color = GrisNotificaciones,
                fontSize = 14.sp
            )

            Spacer(Modifier.height(25.dp))

            Text(
                text = "Administrar notificaciones",
                color = TextoNotificaciones,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(15.dp))

            OpcionNotificacion(
                titulo = "Mensajes y colaboraciones",
                descripcion = "Recibe avisos cuando tengas mensajes o actividad en tus colaboraciones.",
                activada = mensajes,
                alCambiar = { mensajes = it }
            )

            OpcionNotificacion(
                titulo = "Actividad del negocio",
                descripcion = "Recibe notificaciones relacionadas con los movimientos y novedades de tu negocio.",
                activada = actividadNegocio,
                alCambiar = { actividadNegocio = it }
            )

            OpcionNotificacion(
                titulo = "Recordatorios",
                descripcion = "Recibe avisos para recordar actividades importantes.",
                activada = recordatorios,
                alCambiar = { recordatorios = it }
            )

            OpcionNotificacion(
                titulo = "Novedades y actualizaciones",
                descripcion = "Entérate de nuevas funciones y mejoras de la aplicación.",
                activada = novedades,
                alCambiar = { novedades = it }
            )

            OpcionNotificacion(
                titulo = "Avisos de seguridad",
                descripcion = "Recibe avisos importantes relacionados con la seguridad de tu cuenta.",
                activada = seguridad,
                alCambiar = { seguridad = it }
            )

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    mostrarConfirmacion = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(13.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulNotificaciones
                )
            ) {
                Text(
                    "Guardar preferencias",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.height(16.dp))
        }

        NavegacionInferior(
            seccionSeleccionada = "Perfil",
            alCambiarSeccion = alCambiarSeccion
        )
    }

    if (mostrarConfirmacion) {
        AlertDialog(
            onDismissRequest = {
                mostrarConfirmacion = false
            },
            containerColor = FondoNotificaciones,
            title = {
                Text(
                    "¡Preferencias guardadas!",
                    color = TextoNotificaciones,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "Tus preferencias se actualizaron en esta vista de prueba. Todavía no se han guardado permanentemente.",
                    color = GrisNotificaciones
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarConfirmacion = false
                    }
                ) {
                    Text("Aceptar", color = AzulNotificaciones)
                }
            }
        )
    }
}

@Composable
private fun EncabezadoNotificaciones(
    alVolver: () -> Unit,
    alAbrirNotificaciones: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AzulNotificaciones)
            .statusBarsPadding()
            .height(64.dp)
            .padding(horizontal = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = alVolver) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Regresar",
                    tint = Color.White
                )
            }

            Text(
                "Notificaciones",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            /*IconButton(onClick = alAbrirNotificaciones) {
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = "Notificaciones",
                    tint = Color.White
                )
            }*/
        }
    }
}

@Composable
private fun OpcionNotificacion(
    titulo: String,
    descripcion: String,
    activada: Boolean,
    alCambiar: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 13.dp)
            .background(
                FondoAzulNotificaciones,
                RoundedCornerShape(15.dp)
            )
            .padding(15.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    titulo,
                    color = TextoNotificaciones,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(5.dp))

                Text(
                    descripcion,
                    color = GrisNotificaciones,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
            }

            Spacer(Modifier.width(8.dp))

            Switch(
                checked = activada,
                onCheckedChange = alCambiar,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = AzulNotificaciones,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = GrisNotificaciones
                )
            )
        }
    }
}
