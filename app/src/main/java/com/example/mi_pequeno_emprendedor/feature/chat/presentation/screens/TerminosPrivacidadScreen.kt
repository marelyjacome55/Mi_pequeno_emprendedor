
package com.example.mi_pequeno_emprendedor.feature.configuracion.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val AzulTerminos = Color(0xFF0057CE)
private val TextoTerminos = Color(0xFF0808AE)
private val GrisTerminos = Color(0xFF667092)
private val FondoTerminos = Color(0xFFE5F0FF)

@Composable
fun TerminosPrivacidadScreen(
    alVolver: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        EncabezadoTerminos(
            alVolver = alVolver
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    tint = AzulTerminos,
                    modifier = Modifier.size(35.dp)
                )

                Spacer(Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Términos y privacidad",
                        color = TextoTerminos,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Mi pequeño emprendedor",
                        color = GrisTerminos,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(Modifier.height(18.dp))

            Card(
                shape = RoundedCornerShape(15.dp),
                colors = CardDefaults.cardColors(
                    containerColor = FondoTerminos
                )
            ) {
                Text(
                    text = "En este apartado encontrarás información sobre las condiciones de uso de la aplicación y el tratamiento de la información proporcionada por sus usuarios.",
                    color = TextoTerminos,
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Spacer(Modifier.height(22.dp))

            SeccionTerminos(
                titulo = "1. Aceptación de los términos",
                contenido = "Al utilizar Mi pequeño emprendedor, el usuario acepta las condiciones de uso establecidas para la aplicación. Si no está de acuerdo con ellas, deberá evitar utilizar sus funcionalidades."
            )

            SeccionTerminos(
                titulo = "2. Uso de la aplicación",
                contenido = "La aplicación está diseñada para apoyar a pequeños emprendedores en la organización de sus negocios, la administración de productos, el seguimiento de información y la colaboración con otros usuarios."
            )

            SeccionTerminos(
                titulo = "3. Cuenta de usuario",
                contenido = "Cada usuario es responsable de mantener la confidencialidad de sus credenciales de acceso y de la actividad realizada desde su cuenta. Se recomienda utilizar contraseñas seguras y no compartirlas con otras personas."
            )

            SeccionTerminos(
                titulo = "4. Información personal",
                contenido = "La aplicación puede solicitar información necesaria para identificar al usuario y permitir el funcionamiento de sus herramientas, como nombre, apellidos, correo electrónico y otros datos de perfil."
            )

            SeccionTerminos(
                titulo = "5. Privacidad y protección de datos",
                contenido = "La información proporcionada por los usuarios deberá utilizarse únicamente para los fines relacionados con las funcionalidades de la aplicación. El proyecto deberá implementar medidas de seguridad para protegerla contra accesos o usos no autorizados."
            )

            SeccionTerminos(
                titulo = "6. Información del negocio",
                contenido = "Los usuarios son responsables de la exactitud de la información que registren sobre sus negocios, productos, servicios, precios y existencias."
            )

            SeccionTerminos(
                titulo = "7. Colaboraciones y comunicación",
                contenido = "Los usuarios deberán mantener una comunicación respetuosa al utilizar los chats y las herramientas de colaboración. No deberán compartir información engañosa ni utilizar estas funciones para actividades indebidas."
            )

            SeccionTerminos(
                titulo = "8. Responsabilidades del usuario",
                contenido = "El usuario se compromete a utilizar la aplicación de manera responsable, proporcionar información verídica y evitar acciones que afecten el funcionamiento de la plataforma o a otros usuarios."
            )

            SeccionTerminos(
                titulo = "9. Modificaciones",
                contenido = "Los términos y las políticas de privacidad podrán actualizarse cuando se incorporen nuevas funciones o se realicen cambios en el funcionamiento de la aplicación."
            )

            SeccionTerminos(
                titulo = "10. Contacto",
                contenido = "Para resolver dudas relacionadas con estos términos o con el tratamiento de la información, el usuario podrá consultar el apartado de Ayuda y soporte."
            )

            Spacer(Modifier.height(18.dp))
        }
    }
}

@Composable
private fun SeccionTerminos(
    titulo: String,
    contenido: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 19.dp)
    ) {
        Text(
            text = titulo,
            color = TextoTerminos,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(7.dp))

        Text(
            text = contenido,
            color = GrisTerminos,
            fontSize = 14.sp,
            lineHeight = 21.sp
        )
    }
}

@Composable
private fun EncabezadoTerminos(
    alVolver: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AzulTerminos)
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
            text = "Términos y privacidad",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
