
package com.example.mi_pequeno_emprendedor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.mi_pequeno_emprendedor.feature.chat.presentation.screens.ChatConversacionScreen
import com.example.mi_pequeno_emprendedor.feature.chat.presentation.screens.ColaboracionesScreen
import com.example.mi_pequeno_emprendedor.ui.theme.Mi_pequeno_emprendedorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Mi_pequeno_emprendedorTheme {
                var chatSeleccionado by remember {
                    mutableStateOf<String?>(null)
                }

                Surface(modifier = Modifier.fillMaxSize()) {
                    if (chatSeleccionado == null) {
                        ColaboracionesScreen(
                            alAbrirChat = { nombre ->
                                chatSeleccionado = nombre
                            }
                        )
                    } else {
                        ChatConversacionScreen(
                            nombreChat = chatSeleccionado!!,
                            alVolver = {
                                chatSeleccionado = null
                            }
                        )
                    }
                }
            }
        }
    }
}
