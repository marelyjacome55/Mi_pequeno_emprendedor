
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val AzulPrincipal = Color(0xFF0057CE)
private val FondoPantalla = Color(0xFFFFFFFF)
private val TextoPrincipal = Color(0xFF0808AE)
private val TextoSecundario = Color(0xFF777F91)
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
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FondoPantalla)
    ) {

        EncabezadoConfiguracion(
            alAbrirMenu = alAbrirMenu,
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
                    .clip(RoundedCornerShape(20.dp))
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

            Spacer(Modifier.height(22.dp))

            TituloSeccionConfiguracion("Configuración")

            Spacer(Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
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
                        alAbrirOpcion("Cerrar sesión")
                    },
                    mostrarSeparador = false
                )
            }
        }

        NavegacionInferiorConfiguracion(
            alCambiarSeccion = alCambiarSeccion
        )
    }
}

@Composable
private fun EncabezadoConfiguracion(
    alAbrirMenu: () -> Unit,
    alAbrirNotificaciones: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AzulPrincipal)
            .statusBarsPadding()
            .padding(top = 8.dp, bottom = 24.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = alAbrirMenu) {
                Icon(
                    Icons.Default.Menu,
                    contentDescription = "Abrir menú",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }

            Text(
                text = "Mi Pequeño Emprendedor",
                color = Color.White,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            )

            IconButton(onClick = alAbrirNotificaciones) {
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = "Notificaciones",
                    tint = Color.White,
                    modifier = Modifier.size(27.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 28.dp, end = 24.dp, top = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF3989EC)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "M",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Hola, Marely",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Administra tu cuenta",
                    color = Color.White,
                    fontSize = 15.sp
                )
            }

            Surface(
                color = Color.White,
                shape = RoundedCornerShape(30.dp),
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier.padding(
                        horizontal = 12.dp,
                        vertical = 11.dp
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🐋", fontSize = 22.sp)

                    Spacer(Modifier.width(7.dp))

                    Text(
                        text = "Nivel Beluga",
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
        fontSize = 20.sp,
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
                .padding(horizontal = 16.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(43.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(colorFondoIcono),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = colorIcono,
                    modifier = Modifier.size(25.dp)
                )
            }

            Spacer(Modifier.width(15.dp))

            Text(
                text = titulo,
                color = colorTexto,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextoSecundario,
                modifier = Modifier.size(25.dp)
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
private fun NavegacionInferiorConfiguracion(
    alCambiarSeccion: (String) -> Unit
) {
    Surface(
        color = Color.White,
        shadowElevation = 8.dp,
        shape = RoundedCornerShape(
            topStart = 22.dp,
            topEnd = 22.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ElementoNavegacionConfiguracion(
                nombre = "Inicio",
                icono = Icons.Default.Home,
                modifier = Modifier.weight(1f),
                alHacerClick = {
                    alCambiarSeccion("Inicio")
                }
            )

            ElementoNavegacionConfiguracion(
                nombre = "Estadísticas\navanzadas",
                icono = Icons.Default.BarChart,
                modifier = Modifier.weight(1.3f),
                alHacerClick = {
                    alCambiarSeccion("Estadísticas")
                }
            )

            ElementoNavegacionConfiguracion(
                nombre = "Colaboraciones",
                icono = Icons.Default.Groups,
                modifier = Modifier.weight(1.4f),
                alHacerClick = {
                    alCambiarSeccion("Colaboraciones")
                }
            )

            ElementoNavegacionConfiguracion(
                nombre = "Perfil",
                icono = Icons.Default.Person,
                seleccionado = true,
                modifier = Modifier.weight(1f),
                alHacerClick = {
                    alCambiarSeccion("Perfil")
                }
            )
        }
    }
}

@Composable
private fun ElementoNavegacionConfiguracion(
    nombre: String,
    icono: ImageVector,
    seleccionado: Boolean = false,
    modifier: Modifier = Modifier,
    alHacerClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = alHacerClick)
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icono,
            contentDescription = nombre,
            tint = if (seleccionado) AzulPrincipal else TextoSecundario,
            modifier = Modifier.size(30.dp)
        )

        Spacer(Modifier.height(5.dp))

        Text(
            text = nombre,
            color = if (seleccionado) AzulPrincipal else TextoSecundario,
            fontSize = 11.sp,
            fontWeight = if (seleccionado) {
                FontWeight.SemiBold
            } else {
                FontWeight.Normal
            },
            lineHeight = 14.sp
        )

        if (seleccionado) {
            Spacer(Modifier.height(5.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .height(4.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(AzulPrincipal)
            )
        }
    }
}
