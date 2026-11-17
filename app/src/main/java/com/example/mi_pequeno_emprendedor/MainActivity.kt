
package com.example.mi_pequeno_emprendedor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider

import com.example.mi_pequeno_emprendedor.feature.inicio.presentation.screens.InicioScreen
import com.example.mi_pequeno_emprendedor.feature.inventario.presentation.screens.InventarioScreen
import com.example.mi_pequeno_emprendedor.feature.inventario.presentation.screens.CarpetaScreen
import com.example.mi_pequeno_emprendedor.feature.inventario.presentation.screens.DetalleProductoScreen
import com.example.mi_pequeno_emprendedor.feature.inventario.presentation.viewmodel.InventarioViewModel
import com.example.mi_pequeno_emprendedor.feature.historial.presentation.screens.HistorialScreen
import com.example.mi_pequeno_emprendedor.ui.theme.Mi_pequeno_emprendedorTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // El ViewModel se comparte entre todas las pantallas.
        val inventarioViewModel = ViewModelProvider(this)[
            InventarioViewModel::class.java
        ]

        setContent {
            Mi_pequeno_emprendedorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    NavegacionTemporal(
                        inventarioViewModel = inventarioViewModel
                    )
                }
            }
        }
    }
}

@Composable
private fun NavegacionTemporal(
    inventarioViewModel: InventarioViewModel
) {
    var pantallaActual by rememberSaveable {
        mutableStateOf("inicio")
    }

    var carpetaSeleccionada by rememberSaveable {
        mutableStateOf("")
    }

    var productoSeleccionadoId by rememberSaveable {
        mutableIntStateOf(0)
    }

    // Para saber a dónde regresar desde el detalle.
    var origenDetalle by rememberSaveable {
        mutableStateOf("carpeta")
    }

    BackHandler(enabled = pantallaActual != "inicio") {
        pantallaActual = when (pantallaActual) {
            "detalleProducto" -> origenDetalle
            "carpeta" -> "inventario"
            "inventario" -> "inicio"
            "historial" -> "inicio"
            else -> "inicio"
        }
    }

    when (pantallaActual) {

        // =====================================
        // INICIO
        // =====================================

        "inicio" -> {
            InicioScreen(
                alAbrirInventario = {
                    pantallaActual = "inventario"
                },

                alAbrirHistorial = {
                    pantallaActual = "historial"
                },

                alVerTodasActividades = {
                    pantallaActual = "historial"
                },

                alCambiarSeccion = { seccion ->
                    if (seccion == "Inicio") {
                        pantallaActual = "inicio"
                    }
                }
            )
        }

        // =====================================
        // CARPETAS DIGITALES
        // =====================================

        "inventario" -> {
            InventarioScreen(
                inventarioViewModel = inventarioViewModel,

                alAbrirCarpeta = { nombre ->
                    carpetaSeleccionada = nombre
                    pantallaActual = "carpeta"
                },

                alCambiarSeccion = { seccion ->
                    if (seccion == "Inicio") {
                        pantallaActual = "inicio"
                    }
                }
            )
        }

        // =====================================
        // PRODUCTOS DE UNA CARPETA
        // =====================================

        "carpeta" -> {
            CarpetaScreen(
                nombreCarpeta = carpetaSeleccionada,

                inventarioViewModel = inventarioViewModel,

                alVolver = {
                    pantallaActual = "inventario"
                },

                alAbrirProducto = { idProducto ->
                    productoSeleccionadoId = idProducto
                    origenDetalle = "carpeta"
                    pantallaActual = "detalleProducto"
                },

                alCambiarSeccion = { seccion ->
                    if (seccion == "Inicio") {
                        pantallaActual = "inicio"
                    }
                }
            )
        }

        // =====================================
        // HISTORIAL GENERAL
        // =====================================

        "historial" -> {
            HistorialScreen(
                inventarioViewModel = inventarioViewModel,

                alVolver = {
                    pantallaActual = "inicio"
                },

                alAbrirProducto = { idProducto ->
                    productoSeleccionadoId = idProducto

                    // Regresar a Historial al salir del detalle.
                    origenDetalle = "historial"

                    pantallaActual = "detalleProducto"
                },

                alCambiarSeccion = { seccion ->
                    if (seccion == "Inicio") {
                        pantallaActual = "inicio"
                    }
                }
            )
        }

        // =====================================
        // DETALLE DE PRODUCTO
        // =====================================

        "detalleProducto" -> {
            DetalleProductoScreen(
                productoId = productoSeleccionadoId,

                inventarioViewModel = inventarioViewModel,

                alVolver = {
                    pantallaActual = origenDetalle
                },

                alCambiarSeccion = { seccion ->
                    if (seccion == "Inicio") {
                        pantallaActual = "inicio"
                    }
                }
            )
        }

        // =====================================
        // PANTALLA POR DEFECTO
        // =====================================

        else -> {
            InicioScreen(
                alAbrirInventario = {
                    pantallaActual = "inventario"
                },

                alAbrirHistorial = {
                    pantallaActual = "historial"
                }
            )
        }
    }
}
