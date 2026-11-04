
package com.example.mi_pequeno_emprendedor.feature.chat.presentation.screens

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
import androidx.compose.material.icons.filled.MoreVert
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
import com.example.mi_pequeno_emprendedor.ui.components.NavegacionInferior

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

private val chatsDeEjemplo = listOf(
    ChatVisual(
        chatId = 1L,
        nombre = "Equipo Belugas",
        mensaje = "Ana: ¿Ya revisaron el combo de bebidas?",
        hora = "9:30 AM",
        avatar = "🐬",
        esGrupo = true,
        noLeidos = 3
    ),
    ChatVisual(
        chatId = 2L,
        nombre = "Tiburones Unidos",
        mensaje = "Carlos: Subí las nuevas fotos de los productos",
        hora = "Ayer",
        avatar = "🦈",
        esGrupo = true,
        noLeidos = 1
    ),
    ChatVisual(
        chatId = 3L,
        nombre = "Juan López",
        mensaje = "Perfecto, mañana lo vemos",
        hora = "Ayer",
        avatar = "JL",
        esGrupo = false,
        noLeidos = 2,
        colorAvatar = Color(0xFFEDE7FF)
    ),
    ChatVisual(
        chatId = 4L,
        nombre = "Ana Flores",
        mensaje = "Gracias por la información",
        hora = "Lun",
        avatar = "AF",
        esGrupo = false,
        colorAvatar = Color(0xFFE5FBEA)
    ),
    ChatVisual(
        chatId = 5L,
        nombre = "Combo Verano",
        mensaje = "Tú: Propuse un nuevo combo",
        hora = "Dom",
        avatar = "🐬",
        esGrupo = true,
        colorAvatar = Color(0xFFE5F0FF)
    ),
    ChatVisual(
        chatId = 6L,
        nombre = "Miguel Rodríguez",
        mensaje = "¿Puedes enviar el catálogo actualizado?",
        hora = "Dom",
        avatar = "MR",
        esGrupo = false,
        colorAvatar = Color(0xFFFFE9D9)
    ),
    ChatVisual(
        chatId = 7L,
        nombre = "Equipo Tortugas",
        mensaje = "Sofía: Listo, ya quedó aprobado",
        hora = "Sáb",
        avatar = "🐢",
        esGrupo = true,
        colorAvatar = Color(0xFFE5FBEA)
    ),
    ChatVisual(
        chatId = 8L,
        nombre = "Paola Sánchez",
        mensaje = "Avísame cuando tengas un momento",
        hora = "Vie",
        avatar = "PS",
        esGrupo = false,
        colorAvatar = Color(0xFFFCE4EC)
    )
)

@Composable
fun ColaboracionesScreen(
    modifier: Modifier = Modifier,
    alAbrirChat: (Long, String) -> Unit = { _, _ -> },
    alCrearChat: () -> Unit = {},
    alAbrirMenu: () -> Unit = {},
    alAbrirNotificaciones: () -> Unit = {},
    alCambiarSeccion: (String) -> Unit = {},
    chatsFijadosIniciales: Set<Long> = setOf(1L, 2L),
    alCambiarAnclaje: (Long, Boolean) -> Unit = { _, _ -> }
) {
    var pestanaSeleccionada by rememberSaveable {
        mutableStateOf("Chats")
    }

    var busqueda by rememberSaveable {
        mutableStateOf("")
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

    var mostrarAgregarAmigo by remember {
        mutableStateOf(false)
    }

    var mostrarCrearGrupo by remember {
        mutableStateOf(false)
    }

    var nombreGrupo by rememberSaveable {
        mutableStateOf("")
    }

    var nombreAmigo by rememberSaveable {
        mutableStateOf("")
    }

    var mensajeTemporal by remember {
        mutableStateOf<String?>(null)
    }

    val conversacionesDePestana = chatsDeEjemplo.filter { chat ->
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

            // PESTAÑAS
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

            // CONTENIDO
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
                    if (chatsFiltrados.isEmpty()) {
                        item {
                            Text(
                                text = if (busqueda.isNotBlank()) {
                                    "No se encontraron conversaciones."
                                } else if (pestanaSeleccionada == "Equipos") {
                                    "Todavía no hay equipos."
                                } else {
                                    "Todavía no tienes conversaciones."
                                },
                                color = TextoSecundario,
                                modifier = Modifier.padding(24.dp)
                            )
                        }
                    } else {
                        // SECCIÓN DE CHATS FIJADOS
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
                                            contentDescription = if (mostrarFijados) {
                                                "Contraer chats fijados"
                                            } else {
                                                "Expandir chats fijados"
                                            },
                                            tint = TextoSecundario
                                        )
                                    }

                                    if (mostrarFijados) {
                                        fijadosFiltrados.forEach { chat ->
                                            ChatFijadoFila(
                                                chat = chat,
                                                alHacerClick = {
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

                        // TODOS LOS CHATS
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

    // DIÁLOGO AÑADIR AMIGO: VISTA TEMPORAL
    if (mostrarAgregarAmigo) {
        AlertDialog(
            onDismissRequest = {
                mostrarAgregarAmigo = false
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
                Column {
                    Text(
                        "Esta opción estará disponible cuando conectemos nuevamente la lógica.",
                        color = TextoSecundario
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = nombreAmigo,
                        onValueChange = { nombreAmigo = it },
                        label = { Text("Nickname o ID") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AzulPrincipal,
                            unfocusedBorderColor = Separador,
                            focusedLabelColor = AzulPrincipal,
                            unfocusedLabelColor = TextoSecundario,
                            focusedTextColor = TextoPrincipal,
                            unfocusedTextColor = TextoPrincipal,
                            cursorColor = AzulPrincipal
                        )
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarAgregarAmigo = false
                        mensajeTemporal = "La búsqueda de amigos estará disponible próximamente."
                    }
                ) {
                    Text("Aceptar", color = AzulPrincipal)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { mostrarAgregarAmigo = false }
                ) {
                    Text("Cancelar", color = TextoSecundario)
                }
            }
        )
    }

    // DIÁLOGO CREAR GRUPO: VISTA TEMPORAL
    if (mostrarCrearGrupo) {
        AlertDialog(
            onDismissRequest = {
                mostrarCrearGrupo = false
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
                Column {
                    Text(
                        "La creación de grupos se conectará más adelante.",
                        color = TextoSecundario
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = nombreGrupo,
                        onValueChange = { nombreGrupo = it },
                        label = { Text("Nombre del grupo") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AzulPrincipal,
                            unfocusedBorderColor = Separador,
                            focusedLabelColor = AzulPrincipal,
                            unfocusedLabelColor = TextoSecundario,
                            focusedTextColor = TextoPrincipal,
                            unfocusedTextColor = TextoPrincipal,
                            cursorColor = AzulPrincipal
                        )
                    )
                }
            },
            confirmButton = {
                TextButton(
                    enabled = nombreGrupo.isNotBlank(),
                    onClick = {
                        mostrarCrearGrupo = false
                        mensajeTemporal = "La creación de grupos estará disponible próximamente."
                    }
                ) {
                    Text("Vista previa", color = AzulPrincipal)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { mostrarCrearGrupo = false }
                ) {
                    Text("Cancelar", color = TextoSecundario)
                }
            }
        )
    }

    // MENSAJE INFORMATIVO
    mensajeTemporal?.let { mensaje ->
        AlertDialog(
            onDismissRequest = { mensajeTemporal = null },
            containerColor = Color.White,
            text = {
                Text(mensaje, color = TextoPrincipal)
            },
            confirmButton = {
                TextButton(
                    onClick = { mensajeTemporal = null }
                ) {
                    Text("Entendido", color = AzulPrincipal)
                }
            }
        )
    }
}


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

            IconButton(onClick = alAbrirNotificaciones) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notificaciones",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}


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
                color = if (seleccionada) AzulPrincipal else TextoSecundario,
                fontWeight = if (seleccionada) FontWeight.Bold else FontWeight.Medium,
                fontSize = 16.sp
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(if (seleccionada) AzulPrincipal else Fondo)
        )
    }
}

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

@Composable
private fun ChatFijadoFila(
    chat: ChatVisual,
    alHacerClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 9.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(FondoFilaFijada)
            .clickable(onClick = alHacerClick)
            .padding(horizontal = 11.dp, vertical = 10.dp),
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

@Composable
private fun ChatListaFila(
    chat: ChatVisual,
    alHacerClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = alHacerClick)
            .padding(horizontal = 18.dp, vertical = 11.dp),
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
        modifier = Modifier.padding(start = 86.dp, end = 18.dp),
        color = Separador
    )
}

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
            fontSize = if (chat.avatar.length > 2) 27.sp else 16.sp,
            color = TextoPrincipal,
            fontWeight = FontWeight.Bold
        )
    }
}
