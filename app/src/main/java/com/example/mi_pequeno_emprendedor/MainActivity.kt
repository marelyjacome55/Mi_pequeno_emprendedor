
package com.example.mi_pequeno_emprendedor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.mi_pequeno_emprendedor.feature.inicio.presentation.screens.InicioScreen
import com.example.mi_pequeno_emprendedor.ui.theme.Mi_pequeno_emprendedorTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Mi_pequeno_emprendedorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    androidx.compose.foundation.layout.Box(
                        modifier = Modifier.safeDrawingPadding()
                    ) {
                        InicioScreen()
                    }
                }
            }
        }
    }
}
