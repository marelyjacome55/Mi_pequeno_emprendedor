package com.example.mi_pequeno_emprendedor.ui.registro

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mi_pequeno_emprendedor.ui.theme.AzulBotones
import com.example.mi_pequeno_emprendedor.ui.theme.AzulEtiqueta
import com.example.mi_pequeno_emprendedor.ui.theme.AzulHeader
import com.example.mi_pequeno_emprendedor.ui.theme.AzulTexto
import com.example.mi_pequeno_emprendedor.ui.theme.BordeCampo
import com.example.mi_pequeno_emprendedor.ui.theme.RojoEtiqueta
import com.example.mi_pequeno_emprendedor.ui.theme.RojoTexto
import com.example.mi_pequeno_emprendedor.ui.theme.TextoPrincipal
import com.example.mi_pequeno_emprendedor.ui.theme.TextoSecundario
import com.example.mi_pequeno_emprendedor.ui.theme.VerdeEtiqueta
import com.example.mi_pequeno_emprendedor.ui.theme.VerdeTexto
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private val FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/uuuu")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    onRegistrado: () -> Unit,
    viewModel: RegistroViewModel = viewModel()
) {
    val estado by viewModel.uiState.collectAsState()
    var mostrarCalendario by remember { mutableStateOf(false) }

    LaunchedEffect(estado.exito) {
        if (estado.exito) onRegistrado()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .imePadding()
    ) {
        Cabecera()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Campo("Nombre", estado.nombre, estado.errores.nombre, viewModel::onNombreChange)
            Campo("Apellido", estado.apellido, estado.errores.apellido, viewModel::onApellidoChange)
            Campo("Nickname", estado.nickname, estado.errores.nickname, viewModel::onNicknameChange)
            Campo(
                etiqueta = "Fecha de nacimiento",
                valor = estado.fechaNacimiento,
                error = estado.errores.fechaNacimiento,
                onValorChange = {},
                readOnly = true,
                trailing = {
                    Text(
                        "Elegir",
                        color = AzulBotones,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                onClick = { mostrarCalendario = true }
            )
            Campo(
                etiqueta = "Correo",
                valor = estado.email,
                error = estado.errores.email,
                onValorChange = viewModel::onEmailChange,
                teclado = KeyboardType.Email
            )
            Campo(
                etiqueta = "Contraseña",
                valor = estado.password,
                error = estado.errores.password,
                onValorChange = viewModel::onPasswordChange,
                esPassword = true,
                teclado = KeyboardType.Password
            )
            Campo(
                etiqueta = "Confirmar contraseña",
                valor = estado.confirmacionPassword,
                error = estado.errores.confirmacionPassword,
                onValorChange = viewModel::onConfirmacionPasswordChange,
                esPassword = true,
                teclado = KeyboardType.Password
            )

            if (estado.requiereConfirmacion) {
                Aviso("Cuenta creada. Revisa tu correo para confirmarla.", VerdeEtiqueta, VerdeTexto)
            }
            estado.errorGeneral?.let { Aviso(it, RojoEtiqueta, RojoTexto) }

            Spacer(Modifier.height(2.dp))

            Button(
                onClick = viewModel::registrar,
                enabled = !estado.cargando,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulBotones,
                    contentColor = Color.White,
                    disabledContainerColor = AzulEtiqueta,
                    disabledContentColor = AzulTexto
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                if (estado.cargando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                } else {
                    Text("Crear mi cuenta", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }

    if (mostrarCalendario) {
        val inicial = fechaAIso(estado.fechaNacimiento)
            ?.let { LocalDate.parse(it).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli() }
        val selector = rememberDatePickerState(initialSelectedDateMillis = inicial)
        DatePickerDialog(
            onDismissRequest = { mostrarCalendario = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        selector.selectedDateMillis?.let { millis ->
                            val fecha = Instant.ofEpochMilli(millis)
                                .atZone(ZoneOffset.UTC)
                                .toLocalDate()
                            viewModel.onFechaNacimientoChange(fecha.format(FORMATO_FECHA))
                        }
                        mostrarCalendario = false
                    }
                ) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarCalendario = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = selector, showModeToggle = false)
        }
    }
}

@Composable
private fun Cabecera() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.verticalGradient(listOf(AzulHeader, AzulBotones)))
            .statusBarsPadding()
            .padding(start = 22.dp, end = 22.dp, top = 14.dp, bottom = 15.dp)
    ) {
        Text(
            "Crear cuenta",
            color = Color.White,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(2.dp))
        Text(
            "Registra tus datos para empezar",
            color = Color.White.copy(alpha = 0.8f),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun Aviso(mensaje: String, fondo: Color, texto: Color) {
    Surface(color = fondo, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
        Text(
            mensaje,
            color = texto,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Composable
private fun Campo(
    etiqueta: String,
    valor: String,
    error: String?,
    onValorChange: (String) -> Unit,
    teclado: KeyboardType = KeyboardType.Text,
    esPassword: Boolean = false,
    readOnly: Boolean = false,
    trailing: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    val colores = OutlinedTextFieldDefaults.colors(
        focusedTextColor = TextoPrincipal,
        unfocusedTextColor = TextoPrincipal,
        cursorColor = AzulBotones,
        focusedBorderColor = AzulBotones,
        unfocusedBorderColor = BordeCampo,
        focusedLabelColor = AzulBotones,
        unfocusedLabelColor = TextoSecundario,
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        errorContainerColor = Color.White,
        errorBorderColor = RojoTexto,
        errorLabelColor = RojoTexto,
        errorSupportingTextColor = RojoTexto
    )

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = valor,
            onValueChange = onValorChange,
            label = { Text(etiqueta) },
            isError = error != null,
            supportingText = error?.let { { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = teclado),
            visualTransformation = if (esPassword) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = trailing,
            readOnly = readOnly,
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = colores,
            modifier = Modifier.fillMaxWidth()
        )
        if (onClick != null) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable(onClick = onClick)
            )
        }
    }
}
