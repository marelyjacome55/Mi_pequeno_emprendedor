
package com.example.mi_pequeno_emprendedor.feature.chat.presentation.screens

import com.example.mi_pequeno_emprendedor.ui.components.NavegacionInferior
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.foundation.clickable
private val AzulPrincipal = Color(0xFF0057CE)
private val AzulClaro = Color(0xFFE5F0FF)
private val TextoPrincipal = Color(0xFF111B5E)
private val TextoSecundario = Color(0xFF667092)
private val Fondo = Color(0xFFFFFFFF)
private val Separador = Color(0xFFE9EDF5)

private data class ChatItem(
    val nombre: String,
    val mensaje: String,
    val hora: String,
    val iniciales: String = "",
    val colorAvatar: Color = AzulClaro,
    val emoji: String = "",
    val noLeidos: Int = 0,
    val fijado: Boolean = false,
    val esGrupo: Boolean = false
)

private val chatsFijados = listOf(
    ChatItem(
        nombre = "Equipo Belugas",
        mensaje = "Ana: ¿Ya revisaron el combo de bebidas?",
        hora = "9:30 AM",
        emoji = "🐬",
        noLeidos = 3,
        fijado = true,
        esGrupo = true
    ),
    ChatItem(
        nombre = "Tiburones Unidos",
        mensaje = "Carlos: Subí las nuevas fotos de los productos",
        hora = "Ayer",
        emoji = "🦈",
        noLeidos = 1,
        fijado = true,
        esGrupo = true
    )
)

private val todosLosChats = listOf(
    ChatItem(
        nombre = "Juan López",
        mensaje = "Perfecto, mañana lo vemos",
        hora = "Ayer",
        iniciales = "JL",
        colorAvatar = Color(0xFFD5B4F5),
        noLeidos = 2
    ),
    ChatItem(
        nombre = "Ana Flores",
        mensaje = "Gracias por la información",
        hora = "Lun",
        iniciales = "AF",
        colorAvatar = Color(0xFFC8F0DF)
    ),
    ChatItem(
        nombre = "Combo Verano",
        mensaje = "Tú: Propuse un nuevo combo",
        hora = "Dom",
        emoji = "🐬" ,
        esGrupo = true
    ),
    ChatItem(
        nombre = "Miguel Rodríguez",
        mensaje = "¿Puedes enviar el catálogo actualizado?",
        hora = "Dom",
        iniciales = "MR",
        colorAvatar = Color(0xFFFFE0CC)
    ),
    ChatItem(
        nombre = "Equipo Tortugas",
        mensaje = "Sofía: Listo, ya quedó aprobado",
        hora = "Sáb",
        emoji = "🐢",
        esGrupo = true
    ),
    ChatItem(
        nombre = "Paola Sánchez",
        mensaje = "Avísame cuando tengas un momento",
        hora = "Vie",
        iniciales = "PS",
        colorAvatar = Color(0xFFFFD2E1)
    )
)


@Composable
fun ColaboracionesScreen(
    modifier: Modifier = Modifier,
    alAbrirChat: (String) -> Unit = {},
    alCrearChat: () -> Unit = {},
    alAbrirMenu: () -> Unit = {},
    alAbrirNotificaciones: () -> Unit = {},
    alCambiarSeccion: (String) -> Unit = {}
) {
    var pestanaSeleccionada by rememberSaveable { mutableStateOf("Chats") }
    var busqueda by rememberSaveable { mutableStateOf("") }
    var mostrarFijados by rememberSaveable { mutableStateOf(true) }

    val coincideBusqueda: (ChatItem) -> Boolean = { chat ->
        chat.nombre.contains(busqueda, ignoreCase = true) ||
                chat.mensaje.contains(busqueda, ignoreCase = true)
    }

    val fijadosFiltrados = chatsFijados.filter(coincideBusqueda)

    val todosLosChatsFiltrados = todosLosChats.filter(coincideBusqueda)

    val equiposFiltrados = (chatsFijados + todosLosChats).filter {
        it.esGrupo && coincideBusqueda(it)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Fondo)
    ) {
        EncabezadoColaboraciones(
            alAbrirMenu = alAbrirMenu,
            alAbrirNotificaciones = alAbrirNotificaciones
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Fondo)
                .padding(top = 10.dp),
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
                        tint = if (pestanaSeleccionada == "Chats")
                            AzulPrincipal else TextoSecundario,
                        modifier = Modifier.size(25.dp)
                    )
                },
                alSeleccionar = { pestanaSeleccionada = "Chats" }
            )

            PestanaColaboraciones(
                texto = "Equipos",
                seleccionada = pestanaSeleccionada == "Equipos",
                modifier = Modifier.weight(1f),
                icono = {
                    Icon(
                        Icons.Default.Groups,
                        contentDescription = null,
                        tint = if (pestanaSeleccionada == "Equipos")
                            AzulPrincipal else TextoSecundario,
                        modifier = Modifier.size(25.dp)
                    )
                },
                alSeleccionar = { pestanaSeleccionada = "Equipos" }
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(Fondo)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 24.dp,
                        end = 18.dp,
                        top = 20.dp,
                        bottom = 14.dp
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

                IconButton(
                    onClick = alCrearChat,
                    modifier = Modifier.size(46.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Crear chat",
                        tint = AzulPrincipal,
                        modifier = Modifier.size(38.dp)
                    )
                }
            }

            BuscadorChats(
                texto = busqueda,
                alCambiarTexto = { busqueda = it },
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(Modifier.height(18.dp))

            if (pestanaSeleccionada == "Chats") {

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                        bottom = 12.dp
                    )
                ) {
                    item {
                        if (fijadosFiltrados.isNotEmpty()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(Color(0xFFF0F6FF))
                                    .padding(horizontal = 12.dp, vertical = 10.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(38.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.PushPin,
                                        contentDescription = null,
                                        tint = AzulPrincipal,
                                        modifier = Modifier.size(23.dp)
                                    )

                                    Spacer(Modifier.width(10.dp))

                                    Text(
                                        "Chats fijados",
                                        fontSize = 19.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AzulPrincipal,
                                        modifier = Modifier.weight(1f)
                                    )

                                    IconButton(
                                        onClick = {
                                            mostrarFijados = !mostrarFijados
                                        },
                                        modifier = Modifier.size(40.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (mostrarFijados)
                                                Icons.Default.KeyboardArrowUp
                                            else
                                                Icons.Default.KeyboardArrowDown,
                                            contentDescription = if (mostrarFijados)
                                                "Ocultar chats fijados"
                                            else
                                                "Mostrar chats fijados",
                                            tint = AzulPrincipal,
                                            modifier = Modifier.size(30.dp)
                                        )
                                    }
                                }

                                if (mostrarFijados) {
                                    fijadosFiltrados.forEach { chat ->
                                        ChatFijadoFila(
                                            chat = chat,
                                            alHacerClick = {
                                                alAbrirChat(chat.nombre)
                                            }
                                        )

                                        Spacer(Modifier.height(8.dp))
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(18.dp))

                        Text(
                            text = "Todos los chats",
                            fontSize = 17.sp,
                            color = TextoSecundario,
                            modifier = Modifier.padding(
                                start = 20.dp,
                                bottom = 8.dp
                            )
                        )
                    }

                    if (todosLosChatsFiltrados.isEmpty()) {
                        item {
                            Text(
                                text = "No hay chats disponibles.",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                color = TextoSecundario
                            )
                        }
                    } else {
                        items(
                            todosLosChatsFiltrados,
                            key = { it.nombre }
                        ) { chat ->
                            ChatNormalFila(
                                chat = chat,
                                alHacerClick = {
                                    alAbrirChat(chat.nombre)
                                }
                            )
                        }
                    }
                }

            } else {

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                        bottom = 12.dp
                    )
                ) {
                    if (equiposFiltrados.isEmpty()) {
                        item {
                            Text(
                                text = "No hay equipos disponibles.",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                color = TextoSecundario
                            )
                        }
                    } else {
                        items(
                            equiposFiltrados,
                            key = { it.nombre }
                        ) { chat ->
                            ChatNormalFila(
                                chat = chat,
                                alHacerClick = {
                                    alAbrirChat(chat.nombre)
                                }
                            )
                        }
                    }
                }
            }
        }

        NavegacionInferior(
            seccionSeleccionada = "Colaboraciones",
            alCambiarSeccion = alCambiarSeccion
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
                        .padding(top = 8.dp, bottom = 16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = alAbrirMenu) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Abrir menú",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Text(
                            text = "Colaboraciones",
                            color = Color.White,
                            fontSize = 27.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 14.dp)
                        )

                        IconButton(onClick = alAbrirNotificaciones) {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = "Notificaciones",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 32.dp, end = 32.dp, top = 8.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = Color.White,
                            shape = RoundedCornerShape(30.dp)
                        ) {
                            Text(
                                text = "Nivel",
                                modifier = Modifier.padding(
                                    horizontal = 22.dp,
                                    vertical = 9.dp
                                ),
                                color = AzulPrincipal,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
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
                    .fillMaxWidth()
                    .height(58.dp)
                    .clickable(onClick = alSeleccionar)
                    .background(Fondo),
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
                        text = texto,
                        color = if (seleccionada) AzulPrincipal else TextoSecundario,
                        fontWeight = if (seleccionada) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 16.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(
                            if (seleccionada) AzulPrincipal else Fondo
                        )
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
                    modifier = Modifier.size(27.dp)
                )

                Spacer(Modifier.width(16.dp))

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
                                    color = TextoSecundario,
                                    fontSize = 15.sp
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
            chat: ChatItem,
            alHacerClick: () -> Unit
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = alHacerClick)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFFF5F8FF))
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                AvatarChat(chat = chat, tamanio = 58.dp)

                Spacer(Modifier.width(14.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        chat.nombre,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoPrincipal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(Modifier.height(7.dp))

                    Text(
                        chat.mensaje,
                        fontSize = 13.sp,
                        color = TextoSecundario,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 22.sp
                    )
                }

                Spacer(Modifier.width(6.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        chat.hora,
                        color = TextoSecundario,
                        fontSize = 12.sp
                    )

                    Spacer(Modifier.height(10.dp))

                    if (chat.noLeidos > 0) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(AzulPrincipal),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                chat.noLeidos.toString(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Spacer(Modifier.width(10.dp))

                Icon(
                    Icons.Default.PushPin,
                    contentDescription = "Chat fijado",
                    tint = AzulPrincipal,
                    modifier = Modifier.size(23.dp)
                )
            }
        }


        @Composable
        private fun ChatNormalFila(
            chat: ChatItem,
            alHacerClick: () -> Unit
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = alHacerClick)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Fondo)
                    .padding(horizontal = 18.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                AvatarChat(chat = chat, tamanio = 54.dp)

                Spacer(Modifier.width(16.dp))

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

                    Spacer(Modifier.height(7.dp))

                    Text(
                        chat.mensaje,
                        color = TextoSecundario,
                        fontSize = 13.sp,
                        lineHeight = 22.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(Modifier.width(8.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        chat.hora,
                        color = TextoSecundario,
                        fontSize = 13.sp
                    )

                    if (chat.noLeidos > 0) {
                        Spacer(Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .size(29.dp)
                                .clip(CircleShape)
                                .background(AzulPrincipal),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                chat.noLeidos.toString(),
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 112.dp, end = 24.dp)
                    .height(1.dp)
                    .background(Separador)
            )
        }

        @Composable
        private fun AvatarChat(
            chat: ChatItem,
            tamanio: androidx.compose.ui.unit.Dp
        ) {
            Box(
                modifier = Modifier
                    .size(tamanio)
                    .clip(RoundedCornerShape(if (chat.emoji.isNotEmpty()) 18.dp else 100.dp))
                    .background(chat.colorAvatar),
                contentAlignment = Alignment.Center
            ) {
                if (chat.emoji.isNotEmpty()) {
                    Text(chat.emoji, fontSize = (tamanio.value * 0.52f).sp)
                } else {
                    Text(
                        chat.iniciales,
                        color = if (chat.iniciales == "AF") Color(0xFF0AA14F)
                        else if (chat.iniciales == "MR") Color(0xFFE87518)
                        else if (chat.iniciales == "PS") Color(0xFFED2874)
                        else Color.White,
                        fontSize = (tamanio.value * 0.31f).sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
