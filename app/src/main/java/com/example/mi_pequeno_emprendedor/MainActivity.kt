package com.example.mi_pequeno_emprendedor

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mi_pequeno_emprendedor.ui.navigation.AppNavigation
import com.example.mi_pequeno_emprendedor.ui.theme.Mi_pequeno_emprendedorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )

        setContent {
            Mi_pequeno_emprendedorTheme {
                AppNavigation()
            }
        }
    }
}
