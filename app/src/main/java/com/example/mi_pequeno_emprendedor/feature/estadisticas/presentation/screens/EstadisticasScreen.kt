package com.example.mi_pequeno_emprendedor.feature.estadisticas.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mi_pequeno_emprendedor.feature.estadisticas.presentation.components.*

@Composable
fun EstadisticasScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoPantalla)
    ) {
        // Cabecera azul fija en la parte superior
        EstadisticasHeader()

        // Contenido desplazable con todas las tarjetas de métricas
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 84.dp // Espacio extra para que el botón flotante de Belu-guIA no tape el banner final
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                FilaKpisSuperiores()
            }
            item {
                TarjetaDineroSemana()
            }
            item {
                FilaProductosYPagos()
            }
            item {
                FilaHorariosYClientes()
            }
            item {
                BannerConsejoPerfil(
                    perfilTitulo = "Consejo Mojarra:",
                    mensaje = "Revisa lo que más vendes para surtirte mejor."
                )
            }
        }
    }
}