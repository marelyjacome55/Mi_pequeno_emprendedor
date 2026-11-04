package com.example.mi_pequeno_emprendedor.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val AzulNav = Color(0xFF0057CE)
private val GrisNav = Color(0xFF667092)

@Composable
fun NavegacionInferior(
    seccionSeleccionada: String,
    alCambiarSeccion: (String) -> Unit
) {
    Surface(
        color = Color.White,
        shadowElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp),
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ElementoNav(
                nombre = "Inicio",
                seccion = "Inicio",
                seleccionado = seccionSeleccionada == "Inicio",
                modifier = Modifier.weight(1f),
                alHacerClick = alCambiarSeccion
            ) {
                Icon(Icons.Default.Home, contentDescription = null)
            }

            ElementoNav(
                nombre = "Estadísticas\navanzadas",
                seccion = "Estadísticas",
                seleccionado = seccionSeleccionada == "Estadísticas",
                modifier = Modifier.weight(1.3f),
                alHacerClick = alCambiarSeccion
            ) {
                Icon(Icons.Default.BarChart, contentDescription = null)
            }

            ElementoNav(
                nombre = "Colaboraciones",
                seccion = "Colaboraciones",
                seleccionado = seccionSeleccionada == "Colaboraciones",
                modifier = Modifier.weight(1.4f),
                alHacerClick = alCambiarSeccion
            ) {
                Icon(Icons.Default.Groups, contentDescription = null)
            }

            ElementoNav(
                nombre = "Perfil",
                seccion = "Perfil",
                seleccionado = seccionSeleccionada == "Perfil",
                modifier = Modifier.weight(1f),
                alHacerClick = alCambiarSeccion
            ) {
                Icon(Icons.Default.Person, contentDescription = null)
            }
        }
    }
}

@Composable
private fun ElementoNav(
    nombre: String,
    seccion: String,
    seleccionado: Boolean,
    modifier: Modifier = Modifier,
    alHacerClick: (String) -> Unit,
    icono: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { alHacerClick(seccion) }
            .padding(vertical = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(32.dp),
            contentAlignment = Alignment.Center
        ) {
            CompositionLocalProvider(
                androidx.compose.material3.LocalContentColor provides
                        if (seleccionado) AzulNav else GrisNav
            ) {
                icono()
            }
        }

        Spacer(Modifier.height(3.dp))

        Text(
            text = nombre,
            color = if (seleccionado) AzulNav else GrisNav,
            fontSize = 11.sp,
            fontWeight = if (seleccionado) FontWeight.SemiBold else FontWeight.Normal,
            lineHeight = 14.sp
        )

        if (seleccionado) {
            Spacer(Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(3.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(AzulNav)
            )
        }
    }
}