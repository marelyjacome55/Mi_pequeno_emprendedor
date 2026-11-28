package com.example.mi_pequeno_emprendedor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mi_pequeno_emprendedor.data.auth.AuthRepository
import com.example.mi_pequeno_emprendedor.feature.chat.presentation.screens.ChatConversacionScreen
import com.example.mi_pequeno_emprendedor.feature.chat.presentation.screens.ColaboracionesScreen
import com.example.mi_pequeno_emprendedor.feature.configuracion.presentation.screens.*
import com.example.mi_pequeno_emprendedor.ui.components.NavegacionInferior
import com.example.mi_pequeno_emprendedor.ui.inicio.InicioScreen
import com.example.mi_pequeno_emprendedor.ui.login.LoginScreen
import com.example.mi_pequeno_emprendedor.ui.registro.RegistroScreen
import com.example.mi_pequeno_emprendedor.ui.venta.VentaScreen
import com.example.mi_pequeno_emprendedor.ui.theme.Mi_pequeno_emprendedorTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Mi_pequeno_emprendedorTheme {

                val repositorioAuth = remember {
                    AuthRepository()
                }

                val scope = rememberCoroutineScope()

                // =====================================
                // NAVEGACIÓN DE AUTENTICACIÓN
                // =====================================

                var pantallaActual by remember {
                    mutableStateOf("cargando")
                }

                var cargandoSesion by remember {
                    mutableStateOf(true)
                }

                // =====================================
                // ESTADOS DE LA APLICACIÓN
                // =====================================

                var seccionActual by remember {
                    mutableStateOf("Inicio")
                }

                var mostrandoVenta by remember {
                    mutableStateOf(false)
                }

                var chatSeleccionado by remember {
                    mutableStateOf<Pair<Long, String>?>(null)
                }

                var chatsFijados by remember {
                    mutableStateOf(setOf(1L, 2L))
                }

                var opcionConfiguracion by remember {
                    mutableStateOf<String?>(null)
                }

                // =====================================
                // COMPROBAR SESIÓN AL ABRIR LA APP
                // =====================================

                LaunchedEffect(Unit) {
                    pantallaActual = try {
                        if (repositorioAuth.haySesionActiva()) {
                            "app"
                        } else {
                            "login"
                        }
                    } catch (e: Exception) {
                        "login"
                    }

                    cargandoSesion = false
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {

                    when {

                        // =====================================
                        // CARGANDO SESIÓN
                        // =====================================

                        cargandoSesion -> {

                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = Color(0xFF0057CE)
                                )
                            }
                        }

                        // =====================================
                        // LOGIN
                        // =====================================

                        pantallaActual == "login" -> {

                            LoginScreen(
                                onIniciado = {
                                    pantallaActual = "app"
                                    seccionActual = "Inicio"
                                    mostrandoVenta = false
                                },

                                onRegistrarse = {
                                    pantallaActual = "registro"
                                }
                            )
                        }

                        // =====================================
                        // REGISTRO
                        // =====================================

                        pantallaActual == "registro" -> {

                            RegistroScreen(
                                onRegistrado = {
                                    pantallaActual = "app"
                                    seccionActual = "Inicio"
                                    mostrandoVenta = false
                                }
                            )
                        }

                        // =====================================
                        // APLICACIÓN PRINCIPAL
                        // =====================================

                        pantallaActual == "app" -> {

                            val chatActual = chatSeleccionado
                            val opcionActual = opcionConfiguracion

                            when {

                                // =====================================
                                // CALCULADORA DE VENTAS
                                // =====================================

                                mostrandoVenta -> {

                                    VentaScreen(
                                        onCerrar = {
                                            mostrandoVenta = false
                                        }
                                    )
                                }

                                // =====================================
                                // CONVERSACIÓN
                                // =====================================

                                chatActual != null -> {

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
                                }

                                // =====================================
                                // PANTALLAS SECUNDARIAS DE CONFIGURACIÓN
                                // =====================================

                                opcionActual != null -> {

                                    when (opcionActual) {

                                        "Datos personales" -> {

                                            DatosPersonalesScreen(
                                                alVolver = {
                                                    opcionConfiguracion = null
                                                },
                                                alCambiarSeccion = { seccion ->
                                                    opcionConfiguracion = null
                                                    seccionActual = seccion
                                                }
                                            )
                                        }

                                        "Información del negocio" -> {

                                            InformacionNegocioScreen(
                                                alVolver = {
                                                    opcionConfiguracion = null
                                                },
                                                alCambiarSeccion = { seccion ->
                                                    opcionConfiguracion = null
                                                    seccionActual = seccion
                                                }
                                            )
                                        }

                                        "Seguridad" -> {

                                            SeguridadScreen(
                                                alVolver = {
                                                    opcionConfiguracion = null
                                                },
                                                alCambiarSeccion = { seccion ->
                                                    opcionConfiguracion = null
                                                    seccionActual = seccion
                                                }
                                            )
                                        }

                                        "Notificaciones" -> {

                                            NotificacionesScreen(
                                                alVolver = {
                                                    opcionConfiguracion = null
                                                },
                                                alCambiarSeccion = { seccion ->
                                                    opcionConfiguracion = null
                                                    seccionActual = seccion
                                                }
                                            )
                                        }

                                        "Ayuda y soporte" -> {

                                            AyudaSoporteScreen(
                                                alVolver = {
                                                    opcionConfiguracion = null
                                                }
                                            )
                                        }

                                        "Términos y privacidad" -> {

                                            TerminosPrivacidadScreen(
                                                alVolver = {
                                                    opcionConfiguracion = null
                                                }
                                            )
                                        }

                                        else -> {

                                            PantallaOpcionConfiguracion(
                                                titulo = opcionActual,
                                                alVolver = {
                                                    opcionConfiguracion = null
                                                }
                                            )
                                        }
                                    }
                                }

                                // =====================================
                                // SECCIONES PRINCIPALES
                                // =====================================

                                else -> {

                                    when (seccionActual) {

                                        "Configuracion", "Perfil" -> {

                                            ConfiguracionScreen(
                                                alCambiarSeccion = { seccion ->
                                                    seccionActual = seccion
                                                },

                                                alAbrirOpcion = { opcion ->

                                                    if (opcion == "Cerrar sesión confirmada") {

                                                        scope.launch {
                                                            try {
                                                                repositorioAuth.cerrarSesion()

                                                                chatSeleccionado = null
                                                                opcionConfiguracion = null
                                                                mostrandoVenta = false
                                                                seccionActual = "Inicio"

                                                                pantallaActual = "login"

                                                            } catch (e: Exception) {
                                                                // Si falla el cierre de sesión,
                                                                // permanecemos en la aplicación.
                                                            }
                                                        }

                                                    } else {
                                                        opcionConfiguracion = opcion
                                                    }
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

                                            InicioScreen(
                                                alRealizarVenta = {
                                                    mostrandoVenta = true
                                                },
                                                alCambiarSeccion = { seccion ->
                                                    seccionActual = seccion
                                                }
                                            )
                                        }

                                        "Estadísticas" -> {

                                            PantallaProvisional(
                                                titulo = "Estadísticas avanzadas",
                                                seccion = "Estadísticas",
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
        }
    }
}

// =====================================
// PANTALLA PROVISIONAL
// =====================================

@Composable
private fun PantallaProvisional(
    titulo: String,
    seccion: String,
    alCambiarSeccion: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = titulo,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0057CE)
            )
        }
        NavegacionInferior(
            seccionSeleccionada = seccion,
            alCambiarSeccion = alCambiarSeccion
        )
    }
}

// =====================================
// PANTALLAS SECUNDARIAS DE CONFIGURACIÓN
// =====================================

@Composable
private fun PantallaOpcionConfiguracion(
    titulo: String,
    alVolver: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0057CE))
                .statusBarsPadding()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = alVolver) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Regresar",
                    tint = Color.White
                )
            }

            Text(
                text = titulo,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Pantalla de $titulo",
                color = Color(0xFF0808AE),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}