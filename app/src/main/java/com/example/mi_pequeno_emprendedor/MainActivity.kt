
package com.example.mi_pequeno_emprendedor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.mi_pequeno_emprendedor.feature.chat.presentation.screens.ChatConversacionScreen
import com.example.mi_pequeno_emprendedor.feature.chat.presentation.screens.ColaboracionesScreen
import com.example.mi_pequeno_emprendedor.feature.configuracion.presentation.screens.ConfiguracionScreen
import com.example.mi_pequeno_emprendedor.ui.theme.Mi_pequeno_emprendedorTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Mi_pequeno_emprendedorTheme {

                var seccionActual by remember {
                    mutableStateOf("Colaboraciones")
                }

                var chatSeleccionado by remember {
                    mutableStateOf<Pair<Long, String>?>(null)
                }

                // Estado compartido de los chats fijados.
                var chatsFijados by remember {
                    mutableStateOf(setOf(1L, 2L))
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {

                    val chatActual = chatSeleccionado

                    if (chatActual != null) {

                        ChatConversacionScreen(
                            chatId = chatActual.first,
                            nombreChat = chatActual.second,

                            alVolver = {
                                chatSeleccionado = null
                            },

                            alSalirGrupo = {
                                chatSeleccionado = null
                            },

                            fijado = chatActual.first in chatsFijados,

                            alCambiarAnclaje = { nuevoEstado ->

                                chatsFijados =
                                    if (nuevoEstado) {
                                        chatsFijados + chatActual.first
                                    } else {
                                        chatsFijados - chatActual.first
                                    }
                            }
                        )

                    } else {

                        when (seccionActual) {

                            "Configuracion" -> {
                                ConfiguracionScreen(
                                    alCambiarSeccion = { seccion ->
                                        seccionActual = seccion
                                    }
                                )
                            }

                            "Colaboraciones" -> {
                                ColaboracionesScreen(

                                    alAbrirChat = { id, nombre ->
                                        chatSeleccionado = id to nombre
                                    },

                                    chatsFijadosIniciales = chatsFijados,

                                    alCambiarAnclaje = { id, fijado ->

                                        chatsFijados =
                                            if (fijado) {
                                                chatsFijados + id
                                            } else {
                                                chatsFijados - id
                                            }
                                    },

                                    alCambiarSeccion = { seccion ->
                                        seccionActual = seccion
                                    }
                                )
                            }

                            "Inicio" -> {
                                PantallaProvisional(
                                    titulo = "Inicio"
                                )
                            }

                            "Estadísticas" -> {
                                PantallaProvisional(
                                    titulo = "Estadísticas avanzadas"
                                )
                            }

                            "Perfil" -> {
                                ConfiguracionScreen(
                                    alCambiarSeccion = { seccion ->
                                        seccionActual = seccion
                                    }
                                )
                            }

                            else -> {
                                ConfiguracionScreen(
                                    alCambiarSeccion = { seccion ->
                                        seccionActual = seccion
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PantallaProvisional(
    titulo: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = titulo,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0057CE)
        )
    }
}
