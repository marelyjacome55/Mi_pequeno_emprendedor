
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mi_pequeno_emprendedor.ui.components.NavegacionInferior

private val AzulSeguridad = Color(0xFF0057CE)
private val TextoSeguridad = Color(0xFF0808AE)
private val GrisSeguridad = Color(0xFF667092)
private val FondoSeguridad = Color.White
private val BordeSeguridad = Color(0xFFDCE3EF)
private val FondoAzulSeguridad = Color(0xFFE5F0FF)
private val RojoSeguridad = Color(0xFFFD0B33)

@Composable
fun SeguridadScreen(
    modifier: Modifier = Modifier,
    alVolver: () -> Unit = {},
    alCambiarSeccion: (String) -> Unit = {},
    alAbrirNotificaciones: () -> Unit = {}
) {
    var nuevoCorreo by rememberSaveable { mutableStateOf("") }
    var confirmarCorreo by rememberSaveable { mutableStateOf("") }

    var contrasenaActual by rememberSaveable { mutableStateOf("") }
    var nuevaContrasena by rememberSaveable { mutableStateOf("") }
    var confirmarContrasena by rememberSaveable { mutableStateOf("") }

    var errorCorreo by rememberSaveable { mutableStateOf("") }
    var errorContrasena by rememberSaveable { mutableStateOf("") }

    var mostrarConfirmacionCorreo by remember { mutableStateOf(false) }
    var mostrarConfirmacionContrasena by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FondoSeguridad)
    ) {
        EncabezadoSeguridad(
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
                text = "Seguridad",
                color = TextoSeguridad,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Administra el acceso y la seguridad de tu cuenta.",
                color = GrisSeguridad,
                fontSize = 14.sp
            )

            Spacer(Modifier.height(25.dp))

            // CAMBIO DE CORREO
            Text(
                text = "Correo electrónico",
                color = TextoSeguridad,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "El correo asociado a tu cuenta se mostrará aquí cuando esté disponible.",
                color = GrisSeguridad,
                fontSize = 13.sp
            )

            Spacer(Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        FondoAzulSeguridad,
                        RoundedCornerShape(13.dp)
                    )
                    .padding(15.dp)
            ) {
                Text(
                    "Correo actual",
                    color = TextoSeguridad,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(Modifier.height(5.dp))

                Text(
                    "No disponible en esta vista de prueba",
                    color = GrisSeguridad,
                    fontSize = 14.sp
                )
            }

            Spacer(Modifier.height(16.dp))

            CampoSeguridad(
                etiqueta = "Nuevo correo electrónico",
                valor = nuevoCorreo,
                alCambiar = {
                    nuevoCorreo = it
                    errorCorreo = ""
                },
                placeholder = "ejemplo@correo.com",
                tipo = KeyboardType.Email
            )

            Spacer(Modifier.height(14.dp))

            CampoSeguridad(
                etiqueta = "Confirmar nuevo correo",
                valor = confirmarCorreo,
                alCambiar = {
                    confirmarCorreo = it
                    errorCorreo = ""
                },
                placeholder = "Escribe nuevamente tu correo",
                tipo = KeyboardType.Email
            )

            if (errorCorreo.isNotEmpty()) {
                Text(
                    errorCorreo,
                    color = RojoSeguridad,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = {
                    errorCorreo = when {
                        nuevoCorreo.isBlank() || confirmarCorreo.isBlank() ->
                            "Completa ambos campos."

                        !nuevoCorreo.contains("@") || !nuevoCorreo.contains(".") ->
                            "Ingresa un correo válido."

                        nuevoCorreo.trim() != confirmarCorreo.trim() ->
                            "Los correos electrónicos no coinciden."

                        else -> ""
                    }

                    if (errorCorreo.isEmpty()) {
                        mostrarConfirmacionCorreo = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(13.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulSeguridad
                )
            ) {
                Text("Actualizar correo")
            }

            Spacer(Modifier.height(30.dp))

            HorizontalDivider(color = BordeSeguridad)

            Spacer(Modifier.height(25.dp))

            // CAMBIO DE CONTRASEÑA
            Text(
                text = "Contraseña",
                color = TextoSeguridad,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Utiliza una contraseña segura para proteger tu cuenta.",
                color = GrisSeguridad,
                fontSize = 13.sp
            )

            Spacer(Modifier.height(16.dp))

            CampoSeguridad(
                etiqueta = "Contraseña actual",
                valor = contrasenaActual,
                alCambiar = {
                    contrasenaActual = it
                    errorContrasena = ""
                },
                placeholder = "Ingresa tu contraseña actual",
                tipo = KeyboardType.Password,
                esContrasena = true
            )

            Spacer(Modifier.height(14.dp))

            CampoSeguridad(
                etiqueta = "Nueva contraseña",
                valor = nuevaContrasena,
                alCambiar = {
                    nuevaContrasena = it
                    errorContrasena = ""
                },
                placeholder = "Ingresa una nueva contraseña",
                tipo = KeyboardType.Password,
                esContrasena = true
            )

            Spacer(Modifier.height(14.dp))

            CampoSeguridad(
                etiqueta = "Confirmar nueva contraseña",
                valor = confirmarContrasena,
                alCambiar = {
                    confirmarContrasena = it
                    errorContrasena = ""
                },
                placeholder = "Confirma tu nueva contraseña",
                tipo = KeyboardType.Password,
                esContrasena = true
            )

            if (errorContrasena.isNotEmpty()) {
                Text(
                    errorContrasena,
                    color = RojoSeguridad,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = {
                    errorContrasena = when {
                        contrasenaActual.isBlank() ||
                                nuevaContrasena.isBlank() ||
                                confirmarContrasena.isBlank() ->
                            "Completa todos los campos."

                        nuevaContrasena.length < 8 ->
                            "La nueva contraseña debe tener al menos 8 caracteres."

                        nuevaContrasena != confirmarContrasena ->
                            "Las contraseñas nuevas no coinciden."

                        contrasenaActual == nuevaContrasena ->
                            "La nueva contraseña debe ser diferente."

                        else -> ""
                    }

                    if (errorContrasena.isEmpty()) {
                        mostrarConfirmacionContrasena = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(13.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulSeguridad
                )
            ) {
                Text("Actualizar contraseña")
            }

            Spacer(Modifier.height(20.dp))
        }

        NavegacionInferior(
            seccionSeleccionada = "Perfil",
            alCambiarSeccion = alCambiarSeccion
        )
    }

    if (mostrarConfirmacionCorreo) {
        AlertDialog(
            onDismissRequest = {
                mostrarConfirmacionCorreo = false
            },
            containerColor = FondoSeguridad,
            title = {
                Text(
                    "¡Correo actualizado!",
                    color = TextoSeguridad,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "La información se actualizó en esta vista de prueba. No se modificó el correo de tu cuenta.",
                    color = GrisSeguridad
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarConfirmacionCorreo = false
                        nuevoCorreo = ""
                        confirmarCorreo = ""
                    }
                ) {
                    Text("Aceptar", color = AzulSeguridad)
                }
            }
        )
    }

    if (mostrarConfirmacionContrasena) {
        AlertDialog(
            onDismissRequest = {
                mostrarConfirmacionContrasena = false
            },
            containerColor = FondoSeguridad,
            title = {
                Text(
                    "¡Contraseña actualizada!",
                    color = TextoSeguridad,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "La información se validó en esta vista de prueba. No se modificó la contraseña de tu cuenta.",
                    color = GrisSeguridad
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarConfirmacionContrasena = false
                        contrasenaActual = ""
                        nuevaContrasena = ""
                        confirmarContrasena = ""
                    }
                ) {
                    Text("Aceptar", color = AzulSeguridad)
                }
            }
        )
    }
}

@Composable
private fun EncabezadoSeguridad(
    alVolver: () -> Unit,
    alAbrirNotificaciones: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AzulSeguridad)
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
                "Seguridad",
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
private fun CampoSeguridad(
    etiqueta: String,
    valor: String,
    alCambiar: (String) -> Unit,
    placeholder: String,
    tipo: KeyboardType = KeyboardType.Text,
    esContrasena: Boolean = false
) {
    Column {
        Text(
            etiqueta,
            color = TextoSeguridad,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(Modifier.height(7.dp))

        OutlinedTextField(
            value = valor,
            onValueChange = alCambiar,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(placeholder, color = GrisSeguridad)
            },
            singleLine = true,
            visualTransformation = if (esContrasena) {
                PasswordVisualTransformation()
            } else {
                androidx.compose.ui.text.input.VisualTransformation.None
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = tipo
            ),
            shape = RoundedCornerShape(13.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AzulSeguridad,
                unfocusedBorderColor = BordeSeguridad,
                focusedTextColor = TextoSeguridad,
                unfocusedTextColor = TextoSeguridad,
                cursorColor = AzulSeguridad
            )
        )
    }
}
