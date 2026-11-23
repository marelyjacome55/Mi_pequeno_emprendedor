package com.example.mi_pequeno_emprendedor.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

import com.example.mi_pequeno_emprendedor.feature.beluguia.presentation.components.BeluguiaFloatingButton
import com.example.mi_pequeno_emprendedor.feature.beluguia.presentation.screens.BeluguiaScreen
import com.example.mi_pequeno_emprendedor.feature.chat.presentation.screens.ColaboracionesScreen
import com.example.mi_pequeno_emprendedor.ui.components.NavegacionInferior

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "inicio"

    val seccionSeleccionada = when (currentRoute) {
        "inicio" -> "Inicio"
        "estadisticas" -> "Estadísticas"
        "colaboraciones" -> "Colaboraciones"
        "perfil" -> "Perfil"
        else -> "Inicio"
    }

    // Rutas principales donde sí queremos mostrar la barra inferior y el botón flotante
    val rutasPrincipales = listOf("inicio", "estadisticas", "colaboraciones", "perfil")

    Scaffold(
        bottomBar = {
            if (currentRoute in rutasPrincipales) {
                NavegacionInferior(
                    seccionSeleccionada = seccionSeleccionada,
                    alCambiarSeccion = { nuevaSeccion ->
                        val rutaDestino = when (nuevaSeccion) {
                            "Inicio" -> "inicio"
                            "Estadísticas" -> "estadisticas"
                            "Colaboraciones" -> "colaboraciones"
                            "Perfil" -> "perfil"
                            else -> "inicio"
                        }
                        navController.navigate(rutaDestino) {
                            popUpTo("inicio") { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        // Este Box permite apilar el botón flotante POR ENCIMA de cualquier pantalla activa
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = "inicio",
                modifier = Modifier.fillMaxSize()
            ) {
                composable("beluguia") {
                    BeluguiaScreen(onNavigateBack = { navController.popBackStack() })
                }

                composable("inicio") {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Pantalla de Inicio")
                    }
                }
                composable("estadisticas") {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Pantalla de Estadísticas Avanzadas")
                    }
                }
                composable("colaboraciones") {
                    ColaboracionesScreen()
                }
                composable("perfil") {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Pantalla de Perfil")
                    }
                }
            }

            // El botón flotante se renderiza encima de todo, EXCEPTO cuando ya estás dentro de Belu-guIA
            if (currentRoute in rutasPrincipales) {
                BeluguiaFloatingButton(
                    onOpenChat = {
                        navController.navigate("beluguia") {
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}