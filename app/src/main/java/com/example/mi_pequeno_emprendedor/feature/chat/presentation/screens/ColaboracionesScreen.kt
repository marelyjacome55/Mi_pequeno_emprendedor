package com.example.mi_pequeno_emprendedor.feature.chat.presentation.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mi_pequeno_emprendedor.feature.chat.data.ChatRepository
import com.example.mi_pequeno_emprendedor.feature.chat.data.UsuarioChatDto
import com.example.mi_pequeno_emprendedor.ui.components.NavegacionInferior
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay

private const val TAG = "ColaboracionesScreen"

private val AzulPrincipal = Color(0xFF0057CE)
private val AzulOscuro = Color(0xFF0808AE)
private val AzulClaro = Color(0xFFE5F0FF)
private val TextoPrincipal = Color(0xFF111B5E)
private val TextoSecundario = Color(0xFF667092)
private val Fondo = Color.White
private val Separador = Color(0xFFE9EDF5)
private val FondoFijados = Color(0xFFF0F6FF)
private val FondoFilaFijada = Color(0xFFF5F8FF)

private data class ChatVisual(
    val chatId: Long,
    val nombre: String,
    val mensaje: String,
    val hora: String,
    val avatar: String,
    val esGrupo: Boolean,
    val noLeidos: Int = 0,
    val colorAvatar: Color = AzulClaro
)

private data class SolicitudGrupo(
    val nombre: String,
    val integrantes: List<Long>
)

@Composable
fun ColaboracionesScreen(
    modifier: Modifier = Modifier,
    alAbrirChat: (Long, String) -> Unit = { _, _ -> },
    alCrearChat: () -> Unit = {},
    alAbrirMenu: () -> Unit = {},
    alAbrirNotificaciones: () -> Unit = {},
    alCambiarSeccion: (String) -> Unit = {},
    chatsFijadosIniciales: Set<Long> = emptySet(),
    alCambiarAnclaje: (Long, Boolean) -> Unit = { _, _ -> }
) {
    val repositorio = remember { ChatRepository() }

    var pestanaSeleccionada by rememberSaveable {
        mutableStateOf("Chats")
    }

    var busqueda by rememberSaveable {
        mutableStateOf("")
    }

    var chats by remember {
        mutableStateOf<List<ChatVisual>>(emptyList())
    }

    var chatsFijados by remember {
        mutableStateOf(chatsFijadosIniciales)
    }

    var mostrarFijados by rememberSaveable {
        mutableStateOf(true)
    }

    var mostrarOpcionesCrear by remember {
        mutableStateOf(false)
    }

    var mostrarAgregarAmigo by rememberSaveable {
        mutableStateOf(false)
    }

    var mostrarCrearGrupo by rememberSaveable {
        mutableStateOf(false)
    }

    var nombreAmigo by rememberSaveable {
        mutableStateOf("")
    }

    var nombreGrupo by rememberSaveable {
        mutableStateOf("")
    }

    var busquedaIntegrantes by rememberSaveable {
        mutableStateOf("")
    }

    var usuariosEncontrados by remember {
        mutableStateOf<List<UsuarioChatDto>>(emptyList())
    }

    var integrantesSeleccionados by remember {
        mutableStateOf<Set<Long>>(emptySet())
    }

    var amigoSeleccionado by remember {
        mutableStateOf<UsuarioChatDto?>(null)
    }

    var amigoPendiente by remember {
        mutableStateOf<UsuarioChatDto?>(null)
    }

    var solicitudGrupo by remember {
        mutableStateOf<SolicitudGrupo?>(null)
    }

    var cargando by remember {
        mutableStateOf(false)
    }

    var cargandoConversaciones by remember {
        mutableStateOf(true)
    }

    var mensajeTemporal by remember {
        mutableStateOf<String?>(null)
    }

    var recargar by remember {
        mutableIntStateOf(0)
    }

    // =========================================================
    // CARGAR CONVERSACIONES DESDE SUPABASE
    // =========================================================

    LaunchedEffect(recargar) {
        Log.d(TAG, "----------------------------------------")
        Log.d(TAG, "INICIANDO CARGA DE CONVERSACIONES")
        Log.d(TAG, "Número de recarga: $recargar")

        cargandoConversaciones = true

        try {
            Log.d(TAG, "Obteniendo ID del usuario actual...")

            val usuarioActualId = repositorio.obtenerUsuarioActualId()

            Log.d(TAG, "ID del usuario actual: $usuarioActualId")

            Log.d(TAG, "Obteniendo chats privados...")
            val chatsPrivados = repositorio.obtenerMisChats()

            Log.d(TAG, "Chats privados encontrados: ${chatsPrivados.size}")

            Log.d(TAG, "Obteniendo grupos...")
            val grupos = repositorio.obtenerMisGrupos()

            Log.d(TAG, "Grupos encontrados: ${grupos.size}")

            Log.d(TAG, "Obteniendo membresías...")
            val membresias = repositorio.obtenerMisMembresias()

            Log.d(TAG, "Membresías encontradas: ${membresias.size}")

            chatsFijados = membresias
                .filter { it.fijado }
                .map { it.chatId }
                .toSet()

            Log.d(TAG, "Chats fijados: ${chatsFijados.size}")

            val conversaciones = mutableListOf<ChatVisual>()

            (chatsPrivados + grupos).forEach { chat ->

                Log.d(TAG, "Procesando chat con ID: ${chat.chatId}")

                val esGrupo = chat.tipo == "grupo"

                val usuarios =
                    repositorio.obtenerUsuariosDelGrupo(chat.chatId)

                Log.d(
                    TAG,
                    "Usuarios encontrados en chat ${chat.chatId}: ${usuarios.size}"
                )

                val otroUsuario = usuarios.firstOrNull {
                    it.usuarioId != usuarioActualId
                }

                val nombre = if (esGrupo) {
                    chat.nombre?.takeIf { it.isNotBlank() }
                        ?: "Grupo sin nombre"
                } else {
                    otroUsuario?.let {
                        it.nickName?.takeIf { nickname ->
                            nickname.isNotBlank()
                        } ?: it.nombre
                    } ?: chat.nombre?.takeIf { it.isNotBlank() }
                    ?: "Chat privado"
                }

                val ultimoMensaje = repositorio
                    .obtenerMensajes(chat.chatId)
                    .lastOrNull()

                val textoMensaje = ultimoMensaje?.contenido
                    ?: "Todavía no hay mensajes."

                val fecha = ultimoMensaje?.fecha.orEmpty()

                val hora = if (fecha.isNotBlank()) {
                    fecha.substringAfter("T", fecha)
                        .substringAfter(" ")
                        .take(5)
                } else {
                    ""
                }

                val avatar = if (esGrupo) {
                    "👥"
                } else {
                    nombre.split(" ")
                        .filter { it.isNotBlank() }
                        .take(2)
                        .mapNotNull {
                            it.firstOrNull()?.uppercaseChar()
                        }
                        .joinToString("")
                        .ifBlank { "?" }
                }

                conversaciones.add(
                    ChatVisual(
                        chatId = chat.chatId,
                        nombre = nombre,
                        mensaje = textoMensaje,
                        hora = hora,
                        avatar = avatar,
                        esGrupo = esGrupo,
                        colorAvatar = if (esGrupo) {
                            AzulClaro
                        } else {
                            Color(0xFFEDE7FF)
                        }
                    )
                )
            }

            chats = conversaciones.sortedByDescending {
                it.chatId
            }

            Log.d(
                TAG,
                "Carga terminada. Conversaciones totales: ${chats.size}"
            )

        } catch (e: CancellationException) {
            Log.w(
                TAG,
                "CARGA DE CONVERSACIONES CANCELADA",
                e
            )
            throw e

        } catch (e: Exception) {
            Log.e(
                TAG,
                "ERROR AL CARGAR CONVERSACIONES",
                e
            )

            mensajeTemporal =
                "No se pudieron cargar tus conversaciones: ${e.message}"

        } finally {
            cargandoConversaciones = false
            Log.d(TAG, "Finalizó el proceso de carga de conversaciones.")
        }
    }

    // =========================================================
    // BUSCAR USUARIOS PARA AÑADIR AMIGOS
    // =========================================================

    LaunchedEffect(mostrarAgregarAmigo, nombreAmigo) {

        if (mostrarAgregarAmigo && nombreAmigo.isNotBlank()) {

            Log.d(TAG, "Iniciando búsqueda de amigo: $nombreAmigo")

            delay(350)

            try {
                usuariosEncontrados =
                    repositorio.buscarUsuarios(nombreAmigo.trim())

                Log.d(
                    TAG,
                    "Búsqueda de amigos terminada. Resultados: ${usuariosEncontrados.size}"
                )

            } catch (e: CancellationException) {
                Log.w(TAG, "BÚSQUEDA DE AMIGOS CANCELADA", e)
                throw e

            } catch (e: Exception) {
                Log.e(TAG, "ERROR AL BUSCAR AMIGOS", e)

                usuariosEncontrados = emptyList()

                mensajeTemporal =
                    "No se pudo realizar la búsqueda: ${e.message}"
            }

        } else if (mostrarAgregarAmigo) {
            usuariosEncontrados = emptyList()
        }
    }

    // =========================================================
    // CREAR CONVERSACIÓN CON EL AMIGO SELECCIONADO
    // =========================================================

    LaunchedEffect(amigoPendiente) {

        val amigo = amigoPendiente ?: return@LaunchedEffect

        Log.d(TAG, "========================================")
        Log.d(TAG, "INICIANDO CREACIÓN DE CONVERSACIÓN")
        Log.d(TAG, "Usuario seleccionado: ${amigo.usuarioId}")
        Log.d(TAG, "Nombre: ${amigo.nombre}")
        Log.d(TAG, "========================================")

        cargando = true

        try {
            val nombre = amigo.nickName
                ?.takeIf { it.isNotBlank() }
                ?: amigo.nombre

            Log.d(TAG, "Llamando a repositorio.crearChat()")
            Log.d(TAG, "Tipo de chat: chat")
            Log.d(TAG, "Integrante: ${amigo.usuarioId}")

            val chatId = repositorio.crearChat(
                nombre = nombre,
                tipo = "chat",
                integrantesSeleccionados = listOf(amigo.usuarioId)
            )

            Log.d(TAG, "Chat creado correctamente.")
            Log.d(TAG, "ID del chat creado: $chatId")

            cargando = false
            mostrarAgregarAmigo = false
            recargar++

            // Limpiamos la solicitud antes de navegar.
            amigoPendiente = null

            Log.d(TAG, "Ejecutando callback alCrearChat()")
            alCrearChat()

            Log.d(TAG, "Ejecutando callback alAbrirChat()")
            Log.d(TAG, "Chat destino: $chatId")

            alAbrirChat(chatId, nombre)

            Log.d(TAG, "Navegación solicitada correctamente.")

        } catch (e: CancellationException) {
            Log.w(
                TAG,
                "CORRUTINA DE CREACIÓN DE CHAT CANCELADA",
                e
            )
            throw e

        } catch (e: Exception) {
            Log.e(
                TAG,
                "ERROR AL CREAR LA CONVERSACIÓN",
                e
            )

            cargando = false
            amigoPendiente = null

            mensajeTemporal =
                "No se pudo iniciar la conversación: ${e.message}"
        }
    }

    // =========================================================
    // BUSCAR USUARIOS PARA CREAR GRUPOS
    // =========================================================

    LaunchedEffect(mostrarCrearGrupo, busquedaIntegrantes) {

        if (mostrarCrearGrupo && busquedaIntegrantes.isNotBlank()) {

            Log.d(
                TAG,
                "Buscando integrantes: $busquedaIntegrantes"
            )

            delay(350)

            try {
                usuariosEncontrados =
                    repositorio.buscarUsuarios(busquedaIntegrantes.trim())

                Log.d(
                    TAG,
                    "Búsqueda de integrantes terminada. Resultados: ${usuariosEncontrados.size}"
                )

            } catch (e: CancellationException) {
                Log.w(TAG, "BÚSQUEDA DE INTEGRANTES CANCELADA", e)
                throw e

            } catch (e: Exception) {
                Log.e(TAG, "ERROR AL BUSCAR INTEGRANTES", e)

                usuariosEncontrados = emptyList()

                mensajeTemporal =
                    "No se pudieron buscar integrantes: ${e.message}"
            }

        } else if (mostrarCrearGrupo) {
            usuariosEncontrados = emptyList()
        }
    }

    // =========================================================
    // CREAR GRUPO
    // =========================================================

    LaunchedEffect(solicitudGrupo) {

        val solicitud = solicitudGrupo ?: return@LaunchedEffect

        Log.d(TAG, "========================================")
        Log.d(TAG, "INICIANDO CREACIÓN DE GRUPO")
        Log.d(TAG, "Nombre: ${solicitud.nombre}")
        Log.d(TAG, "Integrantes: ${solicitud.integrantes}")
        Log.d(TAG, "========================================")

        cargando = true

        try {
            Log.d(TAG, "Llamando a repositorio.crearChat() para grupo...")

            val nuevoGrupoId = repositorio.crearChat(
                nombre = solicitud.nombre,
                tipo = "grupo",
                integrantesSeleccionados = solicitud.integrantes
            )

            Log.d(TAG, "Grupo creado correctamente.")
            Log.d(TAG, "ID del grupo: $nuevoGrupoId")

            cargando = false
            mostrarCrearGrupo = false
            recargar++

            // Limpiamos la solicitud antes de navegar.
            solicitudGrupo = null

            Log.d(TAG, "Ejecutando callback alCrearChat()")
            alCrearChat()

            Log.d(TAG, "Ejecutando callback alAbrirChat()")
            alAbrirChat(nuevoGrupoId, solicitud.nombre)

            Log.d(TAG, "Navegación al grupo solicitada correctamente.")

        } catch (e: CancellationException) {
            Log.w(
                TAG,
                "CORRUTINA DE CREACIÓN DE GRUPO CANCELADA",
                e
            )
            throw e

        } catch (e: Exception) {
            Log.e(
                TAG,
                "ERROR AL CREAR EL GRUPO",
                e
            )

            cargando = false
            solicitudGrupo = null

            mensajeTemporal =
                "No se pudo crear el grupo: ${e.message}"
        }
    }

    // =========================================================
    // FILTRAR CONVERSACIONES
    // =========================================================

    val conversacionesDePestana = chats.filter { chat ->
        if (pestanaSeleccionada == "Chats") {
            true
        } else {
            chat.esGrupo
        }
    }

    val chatsFiltrados = conversacionesDePestana.filter { chat ->
        chat.nombre.contains(busqueda, ignoreCase = true) ||
                chat.mensaje.contains(busqueda, ignoreCase = true)
    }

    val fijadosFiltrados = chatsFiltrados.filter {
        it.chatId in chatsFijados
    }

    val todosLosDemasChats = chatsFiltrados.filter {
        it.chatId !in chatsFijados
    }

    // =========================================================
    // INTERFAZ PRINCIPAL
    // =========================================================

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Fondo,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            NavegacionInferior(
                seccionSeleccionada = "Colaboraciones",
                alCambiarSeccion = alCambiarSeccion
            )
        }
    ) { paddingInterno ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Fondo)
                .padding(paddingInterno)
        ) {

            EncabezadoColaboraciones(
                alAbrirMenu = alAbrirMenu,
                alAbrirNotificaciones = alAbrirNotificaciones
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                PestanaColaboraciones(
                    texto = "Chats",
                    seleccionada = pestanaSeleccionada == "Chats",
                    modifier = Modifier.weight(1f),
                    icono = {
                        Icon(
                            Icons.Default.ChatBubble,
                            contentDescription = null,
                            tint = if (pestanaSeleccionada == "Chats") {
                                AzulPrincipal
                            } else {
                                TextoSecundario
                            }
                        )
                    },
                    alSeleccionar = {
                        pestanaSeleccionada = "Chats"
                        busqueda = ""
                    }
                )

                PestanaColaboraciones(
                    texto = "Equipos",
                    seleccionada = pestanaSeleccionada == "Equipos",
                    modifier = Modifier.weight(1f),
                    icono = {
                        Icon(
                            Icons.Default.Groups,
                            contentDescription = null,
                            tint = if (pestanaSeleccionada == "Equipos") {
                                AzulPrincipal
                            } else {
                                TextoSecundario
                            }
                        )
                    },
                    alSeleccionar = {
                        pestanaSeleccionada = "Equipos"
                        busqueda = ""
                    }
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 24.dp,
                            topEnd = 24.dp
                        )
                    )
                    .background(Fondo)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 24.dp,
                            end = 18.dp,
                            top = 14.dp,
                            bottom = 12.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = pestanaSeleccionada,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoPrincipal,
                        modifier = Modifier.weight(1f)
                    )

                    Box {

                        IconButton(
                            onClick = {
                                mostrarOpcionesCrear = true
                            },
                            modifier = Modifier.size(46.dp)
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Crear",
                                tint = AzulPrincipal,
                                modifier = Modifier.size(38.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = mostrarOpcionesCrear,
                            onDismissRequest = {
                                mostrarOpcionesCrear = false
                            },
                            modifier = Modifier.background(Color.White)
                        ) {

                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "Añadir amigo",
                                        color = TextoPrincipal
                                    )
                                },
                                onClick = {
                                    mostrarOpcionesCrear = false
                                    nombreAmigo = ""
                                    amigoSeleccionado = null
                                    mostrarAgregarAmigo = true
                                }
                            )

                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "Crear grupo",
                                        color = TextoPrincipal
                                    )
                                },
                                onClick = {
                                    mostrarOpcionesCrear = false
                                    nombreGrupo = ""
                                    busquedaIntegrantes = ""
                                    integrantesSeleccionados = emptySet()
                                    mostrarCrearGrupo = true
                                }
                            )
                        }
                    }
                }

                BuscadorChats(
                    texto = busqueda,
                    alCambiarTexto = {
                        busqueda = it
                    },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(Modifier.height(12.dp))

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {

                    if (cargandoConversaciones) {

                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(35.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = AzulPrincipal
                                )
                            }
                        }

                    } else if (chatsFiltrados.isEmpty()) {

                        item {
                            Text(
                                text = if (busqueda.isNotBlank()) {
                                    "No se encontraron conversaciones."
                                } else if (pestanaSeleccionada == "Equipos") {
                                    "Todavía no tienes equipos."
                                } else {
                                    "Todavía no tienes conversaciones."
                                },
                                color = TextoSecundario,
                                modifier = Modifier.padding(24.dp)
                            )
                        }

                    } else {

                        if (fijadosFiltrados.isNotEmpty()) {

                            item(key = "seccion_fijados") {

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp)
                                        .clip(RoundedCornerShape(18.dp))
                                        .background(FondoFijados)
                                ) {

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                mostrarFijados = !mostrarFijados
                                            }
                                            .padding(
                                                horizontal = 16.dp,
                                                vertical = 14.dp
                                            ),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        Icon(
                                            Icons.Default.PushPin,
                                            contentDescription = null,
                                            tint = AzulPrincipal,
                                            modifier = Modifier.size(20.dp)
                                        )

                                        Spacer(Modifier.width(10.dp))

                                        Text(
                                            "Chats fijados",
                                            color = TextoPrincipal,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            modifier = Modifier.weight(1f)
                                        )

                                        Text(
                                            fijadosFiltrados.size.toString(),
                                            color = TextoSecundario,
                                            fontSize = 13.sp
                                        )

                                        Spacer(Modifier.width(6.dp))

                                        Icon(
                                            if (mostrarFijados) {
                                                Icons.Default.KeyboardArrowUp
                                            } else {
                                                Icons.Default.KeyboardArrowDown
                                            },
                                            contentDescription = null,
                                            tint = TextoSecundario
                                        )
                                    }

                                    if (mostrarFijados) {

                                        fijadosFiltrados.forEach { chat ->

                                            ChatFijadoFila(
                                                chat = chat,
                                                alHacerClick = {
                                                    Log.d(
                                                        TAG,
                                                        "Abriendo chat fijado: ${chat.chatId}"
                                                    )

                                                    alAbrirChat(
                                                        chat.chatId,
                                                        chat.nombre
                                                    )
                                                }
                                            )
                                        }
                                    }

                                    Spacer(Modifier.height(5.dp))
                                }
                            }

                            item(key = "espacio_despues_fijados") {
                                Spacer(Modifier.height(14.dp))
                            }
                        }

                        item(key = "titulo_todos") {

                            Text(
                                "Todos los chats",
                                color = AzulPrincipal,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(
                                    start = 22.dp,
                                    top = 8.dp,
                                    bottom = 8.dp
                                )
                            )
                        }

                        if (todosLosDemasChats.isEmpty()) {

                            item(key = "sin_otros_chats") {

                                Text(
                                    "No hay más conversaciones.",
                                    color = TextoSecundario,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(
                                        horizontal = 22.dp,
                                        vertical = 12.dp
                                    )
                                )
                            }

                        } else {

                            items(
                                items = todosLosDemasChats,
                                key = { "chat_${it.chatId}" }
                            ) { chat ->

                                ChatListaFila(
                                    chat = chat,
                                    alHacerClick = {
                                        Log.d(
                                            TAG,
                                            "Abriendo chat: ${chat.chatId}"
                                        )

                                        alAbrirChat(
                                            chat.chatId,
                                            chat.nombre
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // =========================================================
    // DIALOGO: AÑADIR AMIGO
    // =========================================================

    if (mostrarAgregarAmigo) {

        AlertDialog(
            onDismissRequest = {
                if (!cargando) {
                    mostrarAgregarAmigo = false
                }
            },
            containerColor = Color.White,
            title = {
                Text(
                    "Añadir amigo",
                    color = TextoPrincipal,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {

                Column(modifier = Modifier.fillMaxWidth()) {

                    Text(
                        "Busca a una persona por su nickname, nombre o ID.",
                        color = TextoSecundario,
                        fontSize = 13.sp
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = nombreAmigo,
                        onValueChange = {
                            nombreAmigo = it
                            amigoSeleccionado = null
                        },
                        label = {
                            Text("Nickname, nombre o ID")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = coloresCampo()
                    )

                    Spacer(Modifier.height(10.dp))

                    if (nombreAmigo.isNotBlank()) {

                        if (usuariosEncontrados.isEmpty()) {

                            Text(
                                "No se encontraron usuarios.",
                                color = TextoSecundario,
                                fontSize = 13.sp
                            )

                        } else {

                            LazyColumn(
                                modifier = Modifier.heightIn(max = 220.dp)
                            ) {

                                items(
                                    usuariosEncontrados,
                                    key = { it.usuarioId }
                                ) { usuario ->

                                    UsuarioSeleccionFila(
                                        usuario = usuario,
                                        seleccionado =
                                            amigoSeleccionado?.usuarioId ==
                                                    usuario.usuarioId,
                                        alSeleccionar = {
                                            amigoSeleccionado = usuario
                                        }
                                    )
                                }
                            }
                        }
                    }

                    amigoSeleccionado?.let { amigo ->

                        Spacer(Modifier.height(8.dp))

                        Text(
                            "Seleccionado: ${amigo.nickName ?: amigo.nombre}",
                            color = AzulPrincipal,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            },
            confirmButton = {

                TextButton(
                    enabled = amigoSeleccionado != null && !cargando,
                    onClick = {

                        amigoSeleccionado?.let { amigo ->

                            Log.d(
                                TAG,
                                "Amigo seleccionado para iniciar chat: ${amigo.usuarioId}"
                            )

                            amigoPendiente = amigo
                        }
                    }
                ) {

                    if (cargando) {

                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = AzulPrincipal
                        )

                    } else {

                        Text(
                            "Iniciar chat",
                            color = AzulPrincipal
                        )
                    }
                }
            },
            dismissButton = {

                TextButton(
                    enabled = !cargando,
                    onClick = {
                        mostrarAgregarAmigo = false
                        amigoSeleccionado = null
                        nombreAmigo = ""
                    }
                ) {
                    Text(
                        "Cancelar",
                        color = TextoSecundario
                    )
                }
            }
        )
    }

    // =========================================================
    // DIALOGO: CREAR GRUPO
    // =========================================================

    if (mostrarCrearGrupo) {

        AlertDialog(
            onDismissRequest = {
                if (!cargando) {
                    mostrarCrearGrupo = false
                }
            },
            containerColor = Color.White,
            title = {
                Text(
                    "Crear grupo",
                    color = TextoPrincipal,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {

                Column(modifier = Modifier.fillMaxWidth()) {

                    OutlinedTextField(
                        value = nombreGrupo,
                        onValueChange = {
                            nombreGrupo = it
                        },
                        label = {
                            Text("Nombre del grupo")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = coloresCampo()
                    )

                    Spacer(Modifier.height(14.dp))

                    Text(
                        "Añadir integrantes",
                        color = TextoPrincipal,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = busquedaIntegrantes,
                        onValueChange = {
                            busquedaIntegrantes = it
                        },
                        label = {
                            Text("Buscar personas")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = coloresCampo()
                    )

                    Spacer(Modifier.height(8.dp))

                    if (busquedaIntegrantes.isNotBlank()) {

                        if (usuariosEncontrados.isEmpty()) {

                            Text(
                                "No se encontraron usuarios.",
                                color = TextoSecundario,
                                fontSize = 13.sp
                            )

                        } else {

                            LazyColumn(
                                modifier = Modifier.heightIn(max = 180.dp)
                            ) {

                                items(
                                    usuariosEncontrados,
                                    key = { it.usuarioId }
                                ) { usuario ->

                                    UsuarioSeleccionFila(
                                        usuario = usuario,
                                        seleccionado =
                                            usuario.usuarioId in integrantesSeleccionados,
                                        mostrarCheckbox = true,
                                        alSeleccionar = {

                                            integrantesSeleccionados =
                                                if (
                                                    usuario.usuarioId in integrantesSeleccionados
                                                ) {
                                                    integrantesSeleccionados -
                                                            usuario.usuarioId
                                                } else {
                                                    integrantesSeleccionados +
                                                            usuario.usuarioId
                                                }
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    Text(
                        "Integrantes seleccionados: ${integrantesSeleccionados.size}",
                        color = AzulPrincipal,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            },
            confirmButton = {

                TextButton(
                    enabled = nombreGrupo.isNotBlank() &&
                            integrantesSeleccionados.isNotEmpty() &&
                            !cargando,
                    onClick = {

                        val nombreCreado = nombreGrupo.trim()

                        val integrantes = integrantesSeleccionados.toList()

                        Log.d(TAG, "Solicitud de creación de grupo.")
                        Log.d(TAG, "Nombre: $nombreCreado")
                        Log.d(TAG, "Integrantes: $integrantes")

                        solicitudGrupo = SolicitudGrupo(
                            nombre = nombreCreado,
                            integrantes = integrantes
                        )
                    }
                ) {

                    if (cargando) {

                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = AzulPrincipal
                        )

                    } else {

                        Text(
                            "Crear grupo",
                            color = AzulPrincipal
                        )
                    }
                }
            },
            dismissButton = {

                TextButton(
                    enabled = !cargando,
                    onClick = {
                        mostrarCrearGrupo = false
                    }
                ) {
                    Text(
                        "Cancelar",
                        color = TextoSecundario
                    )
                }
            }
        )
    }

    // =========================================================
    // MENSAJES DE ERROR O INFORMACIÓN
    // =========================================================

    mensajeTemporal?.let { mensaje ->

        AlertDialog(
            onDismissRequest = {
                mensajeTemporal = null
            },
            containerColor = Color.White,
            text = {
                Text(
                    mensaje,
                    color = TextoPrincipal
                )
            },
            confirmButton = {

                TextButton(
                    onClick = {
                        mensajeTemporal = null
                    }
                ) {
                    Text(
                        "Entendido",
                        color = AzulPrincipal
                    )
                }
            }
        )
    }
}

// =============================================================
// FILA DE USUARIO PARA SELECCIONAR
// =============================================================

@Composable
private fun UsuarioSeleccionFila(
    usuario: UsuarioChatDto,
    seleccionado: Boolean,
    mostrarCheckbox: Boolean = false,
    alSeleccionar: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = alSeleccionar)
            .background(
                if (seleccionado) {
                    AzulClaro
                } else {
                    Color.Transparent
                }
            )
            .padding(
                horizontal = 8.dp,
                vertical = 9.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Color(0xFFEDE7FF)),
            contentAlignment = Alignment.Center
        ) {

            Text(
                usuario.nombre.firstOrNull()?.uppercase() ?: "?",
                color = TextoPrincipal,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.width(10.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                usuario.nickName ?: usuario.nombre,
                color = TextoPrincipal,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            Text(
                "ID: ${usuario.usuarioId}",
                color = TextoSecundario,
                fontSize = 11.sp
            )
        }

        if (mostrarCheckbox) {

            Checkbox(
                checked = seleccionado,
                onCheckedChange = {
                    alSeleccionar()
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = AzulPrincipal
                )
            )

        } else if (seleccionado) {

            Text(
                "Seleccionado",
                color = AzulPrincipal,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// =============================================================
// COLORES DE LOS CAMPOS
// =============================================================

@Composable
private fun coloresCampo() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = AzulPrincipal,
    unfocusedBorderColor = Separador,
    focusedLabelColor = AzulPrincipal,
    unfocusedLabelColor = TextoSecundario,
    focusedTextColor = TextoPrincipal,
    unfocusedTextColor = TextoPrincipal,
    cursorColor = AzulPrincipal
)

// =============================================================
// ENCABEZADO
// =============================================================

@Composable
private fun EncabezadoColaboraciones(
    alAbrirMenu: () -> Unit,
    alAbrirNotificaciones: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AzulPrincipal)
            .statusBarsPadding()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Colaboraciones",
                color = Color.White,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = alAbrirNotificaciones
            ) {

                Icon(
                    Icons.Default.Notifications,
                    contentDescription = "Notificaciones",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}

// =============================================================
// PESTAÑAS
// =============================================================

@Composable
private fun PestanaColaboraciones(
    texto: String,
    seleccionada: Boolean,
    modifier: Modifier = Modifier,
    icono: @Composable () -> Unit,
    alSeleccionar: () -> Unit
) {

    Column(
        modifier = modifier
            .height(58.dp)
            .clickable(onClick = alSeleccionar),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {

        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            icono()

            Spacer(Modifier.width(10.dp))

            Text(
                texto,
                color = if (seleccionada) {
                    AzulPrincipal
                } else {
                    TextoSecundario
                },
                fontWeight = if (seleccionada) {
                    FontWeight.Bold
                } else {
                    FontWeight.Medium
                },
                fontSize = 16.sp
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(
                    if (seleccionada) {
                        AzulPrincipal
                    } else {
                        Fondo
                    }
                )
        )
    }
}

// =============================================================
// BUSCADOR DE CHATS
// =============================================================

@Composable
private fun BuscadorChats(
    texto: String,
    alCambiarTexto: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF4F6FA))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            Icons.Default.Search,
            contentDescription = "Buscar",
            tint = TextoSecundario,
            modifier = Modifier.size(25.dp)
        )

        Spacer(Modifier.width(14.dp))

        BasicTextField(
            value = texto,
            onValueChange = alCambiarTexto,
            singleLine = true,
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = 15.sp,
                color = TextoPrincipal
            ),
            decorationBox = { contenido ->

                Box {

                    if (texto.isEmpty()) {

                        Text(
                            "Buscar chats...",
                            color = TextoSecundario
                        )
                    }

                    contenido()
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// =============================================================
// FILA DE CHAT FIJADO
// =============================================================

@Composable
private fun ChatFijadoFila(
    chat: ChatVisual,
    alHacerClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 9.dp,
                vertical = 4.dp
            )
            .clip(RoundedCornerShape(15.dp))
            .background(FondoFilaFijada)
            .clickable(onClick = alHacerClick)
            .padding(
                horizontal = 11.dp,
                vertical = 10.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AvatarChat(chat)

        Spacer(Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                chat.nombre,
                color = TextoPrincipal,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(4.dp))

            Text(
                chat.mensaje,
                color = TextoSecundario,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(Modifier.width(7.dp))

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {

            Text(
                chat.hora,
                color = TextoSecundario,
                fontSize = 11.sp
            )

            if (chat.noLeidos > 0) {

                Badge(
                    containerColor = AzulPrincipal,
                    contentColor = Color.White
                ) {

                    Text(
                        chat.noLeidos.toString(),
                        modifier = Modifier.padding(horizontal = 3.dp)
                    )
                }
            }
        }

        Spacer(Modifier.width(7.dp))

        Icon(
            Icons.Default.PushPin,
            contentDescription = "Chat fijado",
            tint = AzulPrincipal,
            modifier = Modifier.size(19.dp)
        )
    }
}

// =============================================================
// FILA DE CHAT NORMAL
// =============================================================

@Composable
private fun ChatListaFila(
    chat: ChatVisual,
    alHacerClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = alHacerClick)
            .padding(
                horizontal = 18.dp,
                vertical = 11.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AvatarChat(chat)

        Spacer(Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                chat.nombre,
                fontSize = 16.sp,
                color = TextoPrincipal,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(5.dp))

            Text(
                chat.mensaje,
                color = TextoSecundario,
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(Modifier.width(8.dp))

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {

            Text(
                chat.hora,
                color = TextoSecundario,
                fontSize = 12.sp
            )

            if (chat.noLeidos > 0) {

                Badge(
                    containerColor = AzulPrincipal,
                    contentColor = Color.White
                ) {

                    Text(
                        chat.noLeidos.toString(),
                        modifier = Modifier.padding(horizontal = 3.dp)
                    )
                }
            }
        }
    }

    HorizontalDivider(
        modifier = Modifier.padding(
            start = 86.dp,
            end = 18.dp
        ),
        color = Separador
    )
}

// =============================================================
// AVATAR DEL CHAT
// =============================================================

@Composable
private fun AvatarChat(chat: ChatVisual) {

    Box(
        modifier = Modifier
            .size(54.dp)
            .clip(CircleShape)
            .background(chat.colorAvatar),
        contentAlignment = Alignment.Center
    ) {

        Text(
            chat.avatar,
            fontSize = if (chat.avatar.length > 2) {
                27.sp
            } else {
                16.sp
            },
            color = TextoPrincipal,
            fontWeight = FontWeight.Bold
        )
    }
}