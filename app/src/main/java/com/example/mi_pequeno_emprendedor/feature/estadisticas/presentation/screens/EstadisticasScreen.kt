package com.example.mi_pequeno_emprendedor.feature.estadisticas.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mi_pequeno_emprendedor.feature.estadisticas.presentation.components.BannerConsejoPerfil
import com.example.mi_pequeno_emprendedor.feature.estadisticas.presentation.components.EstadisticasHeader
import com.example.mi_pequeno_emprendedor.feature.estadisticas.presentation.components.FilaHorariosYClientes
import com.example.mi_pequeno_emprendedor.feature.estadisticas.presentation.components.FilaKpisSuperiores
import com.example.mi_pequeno_emprendedor.feature.estadisticas.presentation.components.FilaProductosYPagos
import com.example.mi_pequeno_emprendedor.feature.estadisticas.presentation.components.FondoPantalla
import com.example.mi_pequeno_emprendedor.feature.estadisticas.presentation.components.TarjetaDineroSemana
import com.example.mi_pequeno_emprendedor.ui.components.NavegacionInferior

@Composable
fun EstadisticasScreen(
    alCambiarSeccion: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoPantalla)
    ) {
        // Cabecera azul fija en la parte superior
        EstadisticasHeader()

        // Contenido desplazable: weight(1f) hace que ocupe todo el espacio disponible
        // empujando la NavegacionInferior hasta el borde inferior de la pantalla
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 84.dp // Margen inferior para que el botón flotante de Belu-guIA no tape el banner final
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { FilaKpisSuperiores() }
            item { TarjetaDineroSemana() }
            item { FilaProductosYPagos() }
            item { FilaHorariosYClientes() }
            item {
                BannerConsejoPerfil(
                    perfilTitulo = "Consejo Mojarra:",
                    mensaje = "Revisa lo que más vendes para surtirte mejor."
                )
            }
        }

        // Barra inferior
        NavegacionInferior(
            seccionSeleccionada = "Estadísticas",
            alCambiarSeccion = alCambiarSeccion
        )
    }
}