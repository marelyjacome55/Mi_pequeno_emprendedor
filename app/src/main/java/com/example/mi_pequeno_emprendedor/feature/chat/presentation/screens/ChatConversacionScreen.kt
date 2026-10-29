
package com.example.mi_pequeno_emprendedor.feature.chat.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachFile

private val ChatAzul = Color(0xFF0057CE)
private val ChatAzulClaro = Color(0xFFE5F0FF)
private val ChatTexto = Color(0xFF111B5E)
private val ChatGris = Color(0xFF667092)
private val ChatFondo = Color(0xFFF7F9FD)
private val ChatVerde = Color(0xFF0AA14F)
private val ChatRojo = Color(0xFFFD0B33)

private data class MensajeDemo(
    val id: Int,
    val remitente: String,
    val texto: String,
    val hora: String,
    val mio: Boolean
)

@Composable
fun ChatConversacionScreen(
    nombreChat: String = "Equipo Belugas",
    alVolver: () -> Unit = {}
) {
    val mensajes = androidx.compose.runtime.remember {
        mutableStateListOf(
            MensajeDemo(
                1, "Ana", "¡Hola equipo! 🐬 ¿Cómo vamos con los productos?",
                "9:25 AM", false
            ),
            MensajeDemo(
                2, "Tú", "Ya tengo actualizados los precios de mis productos.",
                "9:26 AM", true
            ),
            MensajeDemo(
                3, "Carlos", "¡Excelente! Podemos preparar un combo para esta semana.",
                "9:27 AM", false
            ),
            MensajeDemo(
                4, "Ana", "Les comparto una propuesta de combo 👇",
                "9:28 AM", false
            )
        )
    }

    var textoMensaje by rememberSaveable { mutableStateOf("") }
    var estadoCombo by rememberSaveable { mutableStateOf("Pendiente") }
    val listaEstado = rememberLazyListState()

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
        EncabezadoChat(
            nombre = nombreChat,
            alVolver = alVolver
        )

        LazyColumn(
            state = listaEstado,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                horizontal = 16.dp,
                vertical = 18.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text(
                    text = "HOY",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    color = ChatGris,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            items(mensajes, key = { it.id }) { mensaje ->
                BurbujaMensaje(mensaje)
            }

            item {
                PropuestaCombo(
                    estado = estadoCombo,
                    alAceptar = {
                        estadoCombo = "Aceptado"
                        mensajes.add(
                            MensajeDemo(
                                id = (mensajes.maxOfOrNull { it.id } ?: 0) + 1,
                                remitente = "Tú",
                                texto = "¡Acepto la propuesta del combo! ✅",
                                hora = "Ahora",
                                mio = true
                            )
                        )
                    },
                    alRechazar = {
                        estadoCombo = "Rechazado"
                        mensajes.add(
                            MensajeDemo(
                                id = (mensajes.maxOfOrNull { it.id } ?: 0) + 1,
                                remitente = "Tú",
                                texto = "Por ahora no puedo participar en este combo.",
                                hora = "Ahora",
                                mio = true
                            )
                        )
                    }
                )
            }
        }

        EntradaMensaje(
            texto = textoMensaje,
            alCambiarTexto = { textoMensaje = it },
            alEnviar = {
                val texto = textoMensaje.trim()
                if (texto.isNotEmpty()) {
                    mensajes.add(
                        MensajeDemo(
                            id = (mensajes.maxOfOrNull { it.id } ?: 0) + 1,
                            remitente = "Tú",
                            texto = texto,
                            hora = "Ahora",
                            mio = true
                        )
                    )
                    textoMensaje = ""
                }
            }
        )
    }
}

@Composable
private fun EncabezadoChat(
    nombre: String,
    alVolver: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(ChatAzul)
            .padding(horizontal = 8.dp, vertical = 12.dp),
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
                .size(46.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text("🐬", fontSize = 27.sp)
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                nombre,
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

        IconButton(onClick = {}) {
            Icon(
                Icons.Default.MoreVert,
                contentDescription = "Más opciones",
                tint = Color.White
            )
        }
    }
}

@Composable
private fun BurbujaMensaje(mensaje: MensajeDemo) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (mensaje.mio) Alignment.End else Alignment.Start
    ) {
        if (!mensaje.mio) {
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
                        bottomStart = if (mensaje.mio) 18.dp else 4.dp,
                        bottomEnd = if (mensaje.mio) 4.dp else 18.dp
                    )
                )
                .background(if (mensaje.mio) ChatAzul else Color.White)
                .padding(horizontal = 14.dp, vertical = 11.dp)
        ) {
            Text(
                mensaje.texto,
                color = if (mensaje.mio) Color.White else ChatTexto,
                fontSize = 15.sp,
                lineHeight = 21.sp
            )

            Spacer(Modifier.height(5.dp))

            Text(
                mensaje.hora,
                modifier = Modifier.align(Alignment.End),
                color = if (mensaje.mio) Color.White.copy(alpha = 0.8f) else ChatGris,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun PropuestaCombo(
    estado: String,
    alAceptar: () -> Unit,
    alRechazar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(ChatAzulClaro),
                contentAlignment = Alignment.Center
            ) {
                Text("🎁", fontSize = 23.sp)
            }

            Spacer(Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Propuesta de combo",
                    color = ChatTexto,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
                Text(
                    "Ana Flores · 9:30 AM",
                    color = ChatGris,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        Text(
            "Combo de bebidas",
            color = ChatTexto,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(8.dp))

        Text("• 2 aguas frescas", color = ChatTexto, fontSize = 14.sp)
        Text("• 1 paquete de galletas", color = ChatTexto, fontSize = 14.sp)
        Text("• Precio propuesto: \$85.00", color = ChatTexto, fontSize = 14.sp)

        Spacer(Modifier.height(14.dp))

        if (estado == "Pendiente") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = alAceptar,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ChatVerde
                    )
                ) {
                    Text("Aceptar")
                }

                OutlinedButton(
                    onClick = alRechazar,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = ChatRojo
                    )
                ) {
                    Text("Rechazar")
                }
            }
        } else {
            Text(
                "Propuesta $estado",
                color = if (estado == "Aceptado") ChatVerde else ChatRojo,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun EntradaMensaje(
    texto: String,
    alCambiarTexto: (String) -> Unit,
    alEnviar: () -> Unit
) {
    Surface(
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {},
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Adjuntar",
                    tint = ChatAzul
                )
            }

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

                Spacer(Modifier.width(8.dp))

                Icon(
                    Icons.Default.AttachFile,
                    contentDescription = "Adjuntar archivo",
                    tint = ChatGris,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(Modifier.width(8.dp))

            IconButton(
                onClick = alEnviar,
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(ChatAzul)
            ) {
                Icon(
                    Icons.Default.Send,
                    contentDescription = "Enviar mensaje",
                    tint = Color.White
                )
            }
        }
    }
}

