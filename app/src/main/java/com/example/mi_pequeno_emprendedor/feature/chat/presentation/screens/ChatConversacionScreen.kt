
package com.example.mi_pequeno_emprendedor.feature.chat.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

private val ChatAzul = Color(0xFF0057CE)
private val ChatAzulOscuro = Color(0xFF0808AE)
private val ChatAzulClaro = Color(0xFFE5F0FF)
private val ChatTexto = Color(0xFF111B5E)
private val ChatGris = Color(0xFF667092)
private val ChatFondo = Color(0xFFF7F9FD)
private val ChatRojo = Color(0xFFFD0B33)
private val ChatRojoClaro = Color(0xFFFDEBEA)
private val ChatVerde = Color(0xFF0AA14F)
private val ChatVerdeClaro = Color(0xFFE5FBEA)

// Datos temporales de los mensajes.
// No se conectan a Supabase.
private data class MensajeVisual(
    val id: Int,
    val contenido: String,
    val remitente: String,
    val hora: String,
    val esMio: Boolean
)

@Composable
fun ChatConversacionScreen(
    chatId: Long,
    nombreChat: String,
    alVolver: () -> Unit = {},
    alSalirGrupo: () -> Unit = {},
    fijado: Boolean = false,
    alCambiarAnclaje: (Boolean) -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val listaEstado = rememberLazyListState()

    var mensajes by rememberSaveable(chatId) {
        mutableStateOf(
            listOf(
                MensajeVisual(
                    1,
                    "¡Hola! ¿Cómo están todos?",
                    "Ana",
                    "9:25 AM",
                    false
                ),
                MensajeVisual(
                    2,
                    "Muy bien, gracias. ¿Ya revisaron el combo de bebidas?",
                    "Carlos",
                    "9:27 AM",
                    false
                ),
                MensajeVisual(
                    3,
                    "Sí, me parece una buena propuesta.",
                    "Tú",
                    "9:28 AM",
                    true
                ),
                MensajeVisual(
                    4,
                    "¡Perfecto! Entonces podemos continuar.",
                    "Ana",
                    "9:30 AM",
                    false
                )
            )
        )
    }

    var textoMensaje by rememberSaveable(chatId) {
        mutableStateOf("")
    }

    var nombreActual by rememberSaveable(chatId) {
        mutableStateOf(nombreChat)
    }

    var mostrarMenu by remember { mutableStateOf(false) }
    var mostrarRenombrar by remember { mutableStateOf(false) }
    var mostrarSalir by remember { mutableStateOf(false) }

    var mostrarOpcionesMas by remember { mutableStateOf(false) }
    var mostrarCrearCombo by remember { mutableStateOf(false) }
    var mostrarActualizarInventario by remember { mutableStateOf(false) }

    var nuevoNombre by rememberSaveable(chatId) {
        mutableStateOf(nombreChat)
    }

    // Campos visuales para crear un combo.
    var nombreCombo by rememberSaveable { mutableStateOf("") }
    var descripcionCombo by rememberSaveable { mutableStateOf("") }
    var precioVentaCombo by rememberSaveable { mutableStateOf("") }
    var precioCompraCombo by rememberSaveable { mutableStateOf("") }
    var cantidadCombo by rememberSaveable { mutableStateOf("") }
    var stockCombo by rememberSaveable { mutableStateOf("") }

    // Campos visuales para actualizar inventario.
    var nombreProductoInventario by rememberSaveable { mutableStateOf("") }
    var stockActual by rememberSaveable { mutableStateOf("") }
    var nuevoStock by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(mensajes.size) {
        if (mensajes.isNotEmpty()) {
            listaEstado.animateScrollToItem(mensajes.size)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ChatFondo)
            .imePadding()
    ) {

        // ENCABEZADO
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ChatAzul)
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = alVolver) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White
                )
            }

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Groups,
                    contentDescription = null,
                    tint = ChatAzul,
                    modifier = Modifier.size(27.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    nombreActual,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    "Grupo de colaboración",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 13.sp
                )
            }

            // MENÚ DE LOS TRES PUNTITOS
            Box {
                IconButton(onClick = { mostrarMenu = true }) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "Más opciones",
                        tint = Color.White
                    )
                }

                DropdownMenu(
                    expanded = mostrarMenu,
                    onDismissRequest = { mostrarMenu = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                if (fijado) "Desanclar chat" else "Anclar chat",
                                color = ChatAzulOscuro
                            )
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.PushPin,
                                contentDescription = null,
                                tint = ChatAzul
                            )
                        },
                        onClick = {
                            mostrarMenu = false
                            alCambiarAnclaje(!fijado)
                        }
                    )

                    DropdownMenuItem(
                        text = {
                            Text("Cambiar nombre", color = ChatTexto)
                        },
                        onClick = {
                            mostrarMenu = false
                            nuevoNombre = nombreActual
                            mostrarRenombrar = true
                        }
                    )

                    DropdownMenuItem(
                        text = {
                            Text("Salir del grupo", color = ChatRojo)
                        },
                        onClick = {
                            mostrarMenu = false
                            mostrarSalir = true
                        }
                    )
                }
            }
        }

        // MENSAJES
        LazyColumn(
            state = listaEstado,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 18.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text(
                    "MENSAJES",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    textAlign = TextAlign.Center,
                    color = ChatGris,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (mensajes.isEmpty()) {
                item {
                    Text(
                        "Todavía no hay mensajes. ¡Inicia la conversación!",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        textAlign = TextAlign.Center,
                        color = ChatGris
                    )
                }
            }

            items(mensajes, key = { it.id }) { mensaje ->
                BurbujaMensajeVisual(mensaje)
            }
        }

        // ENTRADA DE MENSAJES
        EntradaMensajeReal(
            texto = textoMensaje,
            alCambiarTexto = { textoMensaje = it },
            enviando = false,
            alAbrirOpciones = { mostrarOpcionesMas = true },
            alEnviar = {
                val contenido = textoMensaje.trim()

                if (contenido.isNotEmpty()) {
                    mensajes = mensajes + MensajeVisual(
                        id = (mensajes.maxOfOrNull { it.id } ?: 0) + 1,
                        contenido = contenido,
                        remitente = "Tú",
                        hora = "Ahora",
                        esMio = true
                    )

                    textoMensaje = ""
                }
            }
        )
    }

    // MENÚ DEL BOTÓN +
    if (mostrarOpcionesMas) {
        AlertDialog(
            onDismissRequest = { mostrarOpcionesMas = false },
            containerColor = Color.White,
            title = {
                Text(
                    "Opciones del grupo",
                    color = ChatTexto,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OpcionAccionChat(
                        titulo = "Crear combo",
                        descripcion = "Diseña un combo para compartir con el grupo.",
                        color = ChatAzulClaro,
                        alClick = {
                            mostrarOpcionesMas = false
                            mostrarCrearCombo = true
                        }
                    )

                    OpcionAccionChat(
                        titulo = "Actualizar inventario de grupo",
                        descripcion = "Registra una actualización de existencias.",
                        color = ChatVerdeClaro,
                        alClick = {
                            mostrarOpcionesMas = false
                            mostrarActualizarInventario = true
                        }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { mostrarOpcionesMas = false }) {
                    Text("Cerrar", color = ChatAzul)
                }
            }
        )
    }

    // FORMULARIO VISUAL: CREAR COMBO
    if (mostrarCrearCombo) {
        AlertDialog(
            onDismissRequest = { mostrarCrearCombo = false },
            containerColor = Color.White,
            title = {
                Text(
                    "Crear combo",
                    color = ChatTexto,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 430.dp),
                    verticalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    Text(
                        "Completa los datos del combo.",
                        color = ChatGris,
                        fontSize = 13.sp
                    )

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(9.dp)
                    ) {
                        item {
                            CampoChat(
                                nombreCombo,
                                { nombreCombo = it },
                                "Nombre del combo"
                            )
                        }
                        item {
                            CampoChat(
                                descripcionCombo,
                                { descripcionCombo = it },
                                "Descripción",
                                false
                            )
                        }
                        item {
                            CampoChat(
                                precioVentaCombo,
                                { precioVentaCombo = it },
                                "Precio de venta"
                            )
                        }
                        item {
                            CampoChat(
                                precioCompraCombo,
                                { precioCompraCombo = it },
                                "Precio de compra"
                            )
                        }
                        item {
                            CampoChat(
                                cantidadCombo,
                                { cantidadCombo = it },
                                "Cantidad"
                            )
                        }
                        item {
                            CampoChat(
                                stockCombo,
                                { stockCombo = it },
                                "Stock"
                            )
                        }
                    }

                    Text(
                        "Este formulario es una vista previa. Todavía no guarda datos.",
                        color = ChatGris,
                        fontSize = 11.sp
                    )
                }
            },
            confirmButton = {
                TextButton(
                    enabled = nombreCombo.isNotBlank(),
                    onClick = { mostrarCrearCombo = false }
                ) {
                    Text("Vista previa", color = ChatAzul)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarCrearCombo = false }) {
                    Text("Cancelar", color = ChatGris)
                }
            }
        )
    }

    // FORMULARIO VISUAL: ACTUALIZAR INVENTARIO
    if (mostrarActualizarInventario) {
        AlertDialog(
            onDismissRequest = { mostrarActualizarInventario = false },
            containerColor = Color.White,
            title = {
                Text(
                    "Actualizar inventario",
                    color = ChatTexto,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "Registra el cambio de existencias del grupo.",
                        color = ChatGris,
                        fontSize = 13.sp
                    )

                    CampoChat(
                        nombreProductoInventario,
                        { nombreProductoInventario = it },
                        "Nombre del producto"
                    )

                    CampoChat(
                        stockActual,
                        { stockActual = it },
                        "Stock actual"
                    )

                    CampoChat(
                        nuevoStock,
                        { nuevoStock = it },
                        "Nuevo stock"
                    )

                    Text(
                        "Esta función todavía es visual y no modifica el inventario.",
                        color = ChatGris,
                        fontSize = 11.sp
                    )
                }
            },
            confirmButton = {
                TextButton(
                    enabled = nombreProductoInventario.isNotBlank() &&
                            nuevoStock.isNotBlank(),
                    onClick = { mostrarActualizarInventario = false }
                ) {
                    Text("Guardar vista previa", color = ChatVerde)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarActualizarInventario = false }) {
                    Text("Cancelar", color = ChatGris)
                }
            }
        )
    }

    // CAMBIAR NOMBRE: SOLO LOCAL
    if (mostrarRenombrar) {
        AlertDialog(
            onDismissRequest = { mostrarRenombrar = false },
            containerColor = Color.White,
            title = {
                Text(
                    "Cambiar nombre del grupo",
                    color = ChatTexto,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                CampoChat(
                    nuevoNombre,
                    { nuevoNombre = it },
                    "Nuevo nombre"
                )
            },
            confirmButton = {
                TextButton(
                    enabled = nuevoNombre.isNotBlank(),
                    onClick = {
                        nombreActual = nuevoNombre.trim()
                        mostrarRenombrar = false
                    }
                ) {
                    Text("Guardar", color = ChatAzul)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarRenombrar = false }) {
                    Text("Cancelar", color = ChatGris)
                }
            }
        )
    }

    // SALIR DEL GRUPO: CONFIRMACIÓN VISUAL
    if (mostrarSalir) {
        AlertDialog(
            onDismissRequest = { mostrarSalir = false },
            containerColor = Color.White,
            title = {
                Text(
                    "Salir del grupo",
                    color = ChatTexto,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "¿Estás seguro de que deseas salir de \"$nombreActual\"? " +
                            "Ya no aparecerá en tus grupos.",
                    color = ChatGris
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarSalir = false
                        alSalirGrupo()
                    }
                ) {
                    Text("Salir", color = ChatRojo)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarSalir = false }) {
                    Text("Cancelar", color = ChatGris)
                }
            }
        )
    }
}

@Composable
private fun OpcionAccionChat(
    titulo: String,
    descripcion: String,
    color: Color,
    alClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(color)
            .clickable(onClick = alClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                titulo,
                color = ChatTexto,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(3.dp))
            Text(
                descripcion,
                color = ChatGris,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun CampoChat(
    valor: String,
    alCambiar: (String) -> Unit,
    etiqueta: String,
    unaLinea: Boolean = true
) {
    OutlinedTextField(
        value = valor,
        onValueChange = alCambiar,
        label = {
            Text(etiqueta, color = ChatGris)
        },
        singleLine = unaLinea,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = ChatTexto,
            unfocusedTextColor = ChatTexto,
            focusedBorderColor = ChatAzul,
            unfocusedBorderColor = Color(0xFFD5DCEB),
            focusedLabelColor = ChatAzul,
            unfocusedLabelColor = ChatGris,
            cursorColor = ChatAzul,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}

@Composable
private fun BurbujaMensajeVisual(
    mensaje: MensajeVisual
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (mensaje.esMio) Alignment.End else Alignment.Start
    ) {
        if (!mensaje.esMio) {
            Text(
                mensaje.remitente,
                color = ChatAzul,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(0.86f)
                .clip(
                    RoundedCornerShape(
                        topStart = 18.dp,
                        topEnd = 18.dp,
                        bottomStart = if (mensaje.esMio) 18.dp else 4.dp,
                        bottomEnd = if (mensaje.esMio) 4.dp else 18.dp
                    )
                )
                .background(if (mensaje.esMio) ChatAzul else Color.White)
                .padding(horizontal = 14.dp, vertical = 11.dp)
        ) {
            Text(
                mensaje.contenido,
                color = if (mensaje.esMio) Color.White else ChatTexto,
                fontSize = 15.sp,
                lineHeight = 21.sp
            )

            Spacer(Modifier.height(5.dp))

            Text(
                mensaje.hora,
                modifier = Modifier.align(Alignment.End),
                color = if (mensaje.esMio) Color.White.copy(alpha = 0.8f) else ChatGris,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun EntradaMensajeReal(
    texto: String,
    alCambiarTexto: (String) -> Unit,
    enviando: Boolean,
    alAbrirOpciones: () -> Unit,
    alEnviar: () -> Unit
) {
    Surface(
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = alAbrirOpciones,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(ChatAzulClaro)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Crear combo o actualizar inventario",
                    tint = ChatAzul,
                    modifier = Modifier.size(27.dp)
                )
            }

            Spacer(Modifier.width(7.dp))

            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(24.dp))
                    .background(ChatFondo)
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = texto,
                    onValueChange = alCambiarTexto,
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    textStyle = androidx.compose.ui.text.TextStyle(
                        color = ChatTexto,
                        fontSize = 15.sp
                    ),
                    decorationBox = { contenido ->
                        Box {
                            if (texto.isEmpty()) {
                                Text(
                                    "Escribe un mensaje...",
                                    color = ChatGris,
                                    fontSize = 14.sp
                                )
                            }
                            contenido()
                        }
                    }
                )
            }

            Spacer(Modifier.width(8.dp))

            IconButton(
                onClick = alEnviar,
                enabled = texto.isNotBlank() && !enviando,
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(ChatAzul)
            ) {
                if (enviando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        Icons.Default.Send,
                        contentDescription = "Enviar mensaje",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

