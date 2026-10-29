package com.example.mi_pequeno_emprendedor.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mi_pequeno_emprendedor.ui.registro.RegistroScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "registro") {
        composable("registro") {
            RegistroScreen(
                onRegistrado = {
                    navController.navigate("inicio") {
                        popUpTo("registro") { inclusive = true }
                    }
                }
            )
        }
        composable("inicio") {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Inicio", style = MaterialTheme.typography.headlineMedium)
            }
        }
    }
}
