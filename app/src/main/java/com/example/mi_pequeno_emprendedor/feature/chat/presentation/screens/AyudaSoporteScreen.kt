
package com.example.mi_pequeno_emprendedor.feature.configuracion.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val AzulAyuda = Color(0xFF0057CE)
private val TextoAyuda = Color(0xFF0808AE)
private val GrisAyuda = Color(0xFF667092)
private val FondoAyuda = Color(0xFFE5F0FF)

@Composable
fun AyudaSoporteScreen(
    alVolver: () -> Unit = {}
) {
    val preguntas = listOf(
        "¿Cómo puedo registrar un producto?" to
                "Puedes registrar tus productos desde la sección de inventario, donde podrás agregar su nombre, descripción, precio y existencias.",

        "¿Cómo puedo actualizar mi inventario?" to
                "Ingresa al apartado de inventario, selecciona el producto que deseas modificar y actualiza sus datos.",

        "¿Cómo funcionan las colaboraciones?" to
                "La sección de colaboraciones permite comunicarte con otros emprendedores mediante chats y equipos de trabajo.",

        "¿Cómo puedo crear un combo?" to
                "Desde una conversación puedes utilizar la opción de crear combo para compartir una propuesta con otros integrantes.",

        "¿Qué hago si olvidé mi contraseña?" to
                "Utiliza la opción de recuperación de contraseña desde el inicio de sesión y sigue las instrucciones que se indiquen."
    )

    var preguntaAbierta by remember {
        mutableIntStateOf(-1)
    }

    var mostrarContacto by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        EncabezadoAyuda(
            titulo = "Ayuda y soporte",
            alVolver = alVolver
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Text(
                text = "¿En qué podemos ayudarte?",
                color = TextoAyuda,
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Encuentra respuestas a las dudas más comunes sobre Mi pequeño emprendedor.",
                color = GrisAyuda,
                fontSize = 14.sp,
                lineHeight = 21.sp
            )

            Spacer(Modifier.height(22.dp))

            Text(
                text = "Preguntas frecuentes",
                color = TextoAyuda,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(12.dp))

            preguntas.forEachIndexed { index, pregunta ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    shape = RoundedCornerShape(15.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 1.dp
                    )
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    preguntaAbierta =
                                        if (preguntaAbierta == index) -1
                                        else index
                                }
                                .padding(15.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = pregunta.first,
                                color = TextoAyuda,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.weight(1f)
                            )

                            Icon(
                                imageVector = if (preguntaAbierta == index)
                                    Icons.Default.ExpandLess
                                else
                                    Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = AzulAyuda
                            )
                        }

                        if (preguntaAbierta == index) {
                            HorizontalDivider(
                                color = Color(0xFFECEEF2)
                            )

                            Text(
                                text = pregunta.second,
                                color = GrisAyuda,
                                fontSize = 14.sp,
                                lineHeight = 21.sp,
                                modifier = Modifier.padding(15.dp)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(15.dp))

            Text(
                text = "¿Necesitas más ayuda?",
                color = TextoAyuda,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = FondoAyuda
                )
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.SupportAgent,
                            contentDescription = null,
                            tint = AzulAyuda,
                            modifier = Modifier.size(30.dp)
                        )

                        Spacer(Modifier.width(10.dp))

                        Text(
                            text = "Contacta con soporte",
                            color = TextoAyuda,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Si no encontraste la respuesta que buscabas, puedes consultar los canales de atención del proyecto.",
                        color = GrisAyuda,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )

                    Spacer(Modifier.height(14.dp))

                    Button(
                        onClick = {
                            mostrarContacto = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AzulAyuda
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null
                        )

                        Spacer(Modifier.width(8.dp))

                        Text("Ver información de contacto")
                    }
                }
            }

            Spacer(Modifier.height(20.dp))
        }
    }

    if (mostrarContacto) {
        AlertDialog(
            onDismissRequest = {
                mostrarContacto = false
            },
            title = {
                Text(
                    "Contacto de soporte",
                    color = TextoAyuda,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "Los datos de contacto del equipo de soporte se agregarán cuando se definan los canales oficiales del proyecto.",
                    color = GrisAyuda
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarContacto = false
                    }
                ) {
                    Text("Entendido")
                }
            },
            containerColor = Color.White
        )
    }
}

@Composable
private fun EncabezadoAyuda(
    titulo: String,
    alVolver: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AzulAyuda)
            .statusBarsPadding()
            .height(58.dp)
            .padding(horizontal = 12.dp),
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
}
