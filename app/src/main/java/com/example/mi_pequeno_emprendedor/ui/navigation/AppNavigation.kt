package com.example.mi_pequeno_emprendedor.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mi_pequeno_emprendedor.ui.login.LoginScreen
import com.example.mi_pequeno_emprendedor.ui.registro.RegistroScreen
import com.example.mi_pequeno_emprendedor.ui.venta.VentaScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onIniciado = {
                    navController.navigate("inicio") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onRegistrarse = { navController.navigate("registro") }
            )
        }
        composable("registro") {
            RegistroScreen(
                onRegistrado = {
                    navController.navigate("inicio") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("inicio") {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Button(onClick = { navController.navigate("venta") }) {
                    Text("Realizar venta")
                }
            }
        }
        composable("venta") {
            VentaScreen(onCerrar = { navController.popBackStack() })
        }
    }
}
