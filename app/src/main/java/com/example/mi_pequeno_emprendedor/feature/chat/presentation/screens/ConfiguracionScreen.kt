
package com.example.mi_pequeno_emprendedor.feature.configuracion.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.mi_pequeno_emprendedor.ui.components.NavegacionInferior

private val AzulPrincipal = Color(0xFF0057CE)
private val FondoPantalla = Color(0xFFFFFFFF)
private val TextoPrincipal = Color(0xFF0808AE)
private val TextoSecundario = Color(0xFF667092)
private val FondoIcono = Color(0xFFE5F0FF)
private val RojoCerrar = Color(0xFFEF5B45)
private val FondoRojo = Color(0xFFFDEBEA)

@Composable
fun ConfiguracionScreen(
    modifier: Modifier = Modifier,
    alCambiarSeccion: (String) -> Unit = {},
    alAbrirMenu: () -> Unit = {},
    alAbrirNotificaciones: () -> Unit = {},
    alAbrirOpcion: (String) -> Unit = {}
) {
    var mostrarDialogoCerrarSesion by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FondoPantalla)
    ) {
        EncabezadoConfiguracion(
            alAbrirNotificaciones = alAbrirNotificaciones
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 18.dp, bottom = 18.dp)
        ) {
            TituloSeccionConfiguracion("Mi perfil")

            Spacer(Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color.White)
            ) {
                FilaConfiguracion(
                    icono = Icons.Default.Person,
                    titulo = "Datos personales",
                    alHacerClick = {
                        alAbrirOpcion("Datos personales")
                    }
                )

                FilaConfiguracion(
                    icono = Icons.Default.Store,
                    titulo = "Información del negocio",
                    alHacerClick = {
                        alAbrirOpcion("Información del negocio")
                    }
                )

                FilaConfiguracion(
                    icono = Icons.Default.CreditCard,
                    titulo = "Métodos de cobro",
                    alHacerClick = {
                        alAbrirOpcion("Métodos de cobro")
                    }
                )

                FilaConfiguracion(
                    icono = Icons.Default.Lock,
                    titulo = "Seguridad",
                    alHacerClick = {
                        alAbrirOpcion("Seguridad")
                    },
                    mostrarSeparador = false
                )
            }

            Spacer(Modifier.height(20.dp))

            TituloSeccionConfiguracion("Configuración")

            Spacer(Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color.White)
            ) {
                FilaConfiguracion(
                    icono = Icons.Default.Notifications,
                    titulo = "Notificaciones",
                    alHacerClick = {
                        alAbrirOpcion("Notificaciones")
                    }
                )

                FilaConfiguracion(
                    icono = Icons.Default.HelpOutline,
                    titulo = "Ayuda y soporte",
                    alHacerClick = {
                        alAbrirOpcion("Ayuda y soporte")
                    }
                )

                FilaConfiguracion(
                    icono = Icons.Default.Description,
                    titulo = "Términos y privacidad",
                    alHacerClick = {
                        alAbrirOpcion("Términos y privacidad")
                    }
                )

                FilaConfiguracion(
                    icono = Icons.Default.Logout,
                    titulo = "Cerrar sesión",
                    colorIcono = RojoCerrar,
                    colorFondoIcono = FondoRojo,
                    colorTexto = RojoCerrar,
                    alHacerClick = {
                        mostrarDialogoCerrarSesion = true
                    },
                    mostrarSeparador = false
                )
            }
        }

        NavegacionInferior(
            seccionSeleccionada = "Perfil",
            alCambiarSeccion = alCambiarSeccion
        )
    }

    if (mostrarDialogoCerrarSesion) {
        DialogoCerrarSesion(
            alCancelar = {
                mostrarDialogoCerrarSesion = false
            },
            alConfirmar = {
                mostrarDialogoCerrarSesion = false
                alAbrirOpcion("Cerrar sesión confirmada")
            }
        )
    }
}

@Composable
private fun EncabezadoConfiguracion(
    alAbrirNotificaciones: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AzulPrincipal)
            .statusBarsPadding()
            .padding(top = 8.dp, bottom = 18.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Configuración",
                color = Color.White,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = alAbrirNotificaciones) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notificaciones",
                    tint = Color.White,
                    modifier = Modifier.size(27.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 28.dp, end = 20.dp, top = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF3989EC)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "M",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(Modifier.width(13.dp))

            Text(
                text = "Hola, Marely",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            Surface(
                color = Color.White,
                shape = RoundedCornerShape(30.dp),
                shadowElevation = 3.dp
            ) {
                Row(
                    modifier = Modifier.padding(
                        horizontal = 11.dp,
                        vertical = 8.dp
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🐋", fontSize = 19.sp)

                    Spacer(Modifier.width(6.dp))

                    Text(
                        text = "Nivel",
                        color = AzulPrincipal,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun TituloSeccionConfiguracion(
    titulo: String
) {
    Text(
        text = titulo,
        color = TextoPrincipal,
        fontSize = 19.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun FilaConfiguracion(
    icono: ImageVector,
    titulo: String,
    alHacerClick: () -> Unit,
    colorIcono: Color = AzulPrincipal,
    colorFondoIcono: Color = FondoIcono,
    colorTexto: Color = TextoPrincipal,
    mostrarSeparador: Boolean = true
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = alHacerClick)
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(13.dp))
                    .background(colorFondoIcono),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = colorIcono,
                    modifier = Modifier.size(23.dp)
                )
            }

            Spacer(Modifier.width(13.dp))

            Text(
                text = titulo,
                color = colorTexto,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextoSecundario,
                modifier = Modifier.size(23.dp)
            )
        }

        if (mostrarSeparador) {
            HorizontalDivider(
                color = Color(0xFFECEEF2),
                thickness = 1.dp
            )
        }
    }
}

@Composable
private fun DialogoCerrarSesion(
    alCancelar: () -> Unit,
    alConfirmar: () -> Unit
) {
    Dialog(onDismissRequest = alCancelar) {
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = Color.White,
            tonalElevation = 5.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .clip(CircleShape)
                        .background(FondoRojo),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = null,
                        tint = RojoCerrar,
                        modifier = Modifier.size(29.dp)
                    )
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "¿Cerrar sesión?",
                    color = TextoPrincipal,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "¿Estás seguro de que deseas cerrar tu sesión?",
                    color = TextoSecundario,
                    fontSize = 14.sp
                )

                Spacer(Modifier.height(22.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = alCancelar,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = AzulPrincipal
                        )
                    ) {
                        Text("Cancelar")
                    }

                    Button(
                        onClick = alConfirmar,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RojoCerrar
                        )
                    ) {
                        Text("Cerrar sesión")
                    }
                }
            }
        }
    }
}
