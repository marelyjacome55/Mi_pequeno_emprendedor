package com.example.mi_pequeno_emprendedor.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

import com.example.mi_pequeno_emprendedor.feature.beluguia.presentation.screens.BeluguiaScreen
import com.example.mi_pequeno_emprendedor.ui.components.NavegacionInferior
import com.example.mi_pequeno_emprendedor.feature.chat.presentation.screens.ColaboracionesScreen

import com.example.mi_pequeno_emprendedor.feature.beluguia.presentation.components.BeluguiaFloatingButton

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Obtenemos la ruta actual para iluminar el ícono correcto en la barra inferior
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "beluguia"

    // Mapeamos la ruta interna de Compose a la sección String que espera NavegacionInferior
    val seccionSeleccionada = when (currentRoute) {
        "inicio" -> "Inicio"
        "estadisticas" -> "Estadísticas"
        "colaboraciones" -> "Colaboraciones"
        "perfil" -> "Perfil"
        else -> "Inicio"
    }

    Scaffold(
        bottomBar = {
            // Solo mostramos la barra en las pantallas principales
            if (currentRoute in listOf("inicio", "estadisticas", "colaboraciones", "perfil", "beluguia")) {
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
        NavHost(
            navController = navController,
            startDestination = "inicio", // <--- Pantalla donde arranca la app
            modifier = Modifier.padding(innerPadding)
        ) {
            // Pantalla de Belu-guIA
            composable("beluguia") {
                BeluguiaScreen(onNavigateBack = { navController.popBackStack() })
            }

            // Rutas base de las pestañas principales
            composable("inicio") {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Pantalla de Inicio")

                    // Botón flotante arrastrable
                    BeluguiaFloatingButton(
                        onClick = { navController.navigate("beluguia") },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                    )
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
    }
}