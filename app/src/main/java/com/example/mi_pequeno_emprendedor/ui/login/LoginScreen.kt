package com.example.mi_pequeno_emprendedor.ui.login

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mi_pequeno_emprendedor.R
import com.example.mi_pequeno_emprendedor.ui.components.Campo
import com.example.mi_pequeno_emprendedor.ui.theme.AzulBotones
import com.example.mi_pequeno_emprendedor.ui.theme.AzulTexto
import com.example.mi_pequeno_emprendedor.ui.theme.RojoTexto
import com.example.mi_pequeno_emprendedor.ui.theme.TextoSecundario

@Composable
fun LoginScreen(
    onIniciado: () -> Unit,
    onRegistrarse: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val estado by viewModel.uiState.collectAsState()

    LaunchedEffect(estado.exito) {
        if (estado.exito) onIniciado()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(48.dp))
        Image(
            painter = painterResource(R.drawable.logo_ballena),
            contentDescription = "Mi pequeño emprendedor",
            modifier = Modifier.size(140.dp)
        )
        Spacer(Modifier.height(20.dp))
        Text(
            "Iniciar sesión",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = AzulTexto
        )
        Text(
            "Bienvenido de nuevo",
            style = MaterialTheme.typography.bodyMedium,
            color = TextoSecundario,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(32.dp))

        Campo(
            etiqueta = "Nickname",
            valor = estado.nickname,
            error = null,
            onValorChange = viewModel::onNicknameChange,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        Campo(
            etiqueta = "Contraseña",
            valor = estado.password,
            error = null,
            onValorChange = viewModel::onPasswordChange,
            esPassword = !estado.mostrarPassword,
            teclado = KeyboardType.Password,
            trailing = {
                IconoOjo(
                    visible = estado.mostrarPassword,
                    onClick = viewModel::alternarPassword
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        estado.error?.let { mensaje ->
            Spacer(Modifier.height(10.dp))
            Text(
                mensaje,
                style = MaterialTheme.typography.bodyMedium,
                color = RojoTexto,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(Modifier.height(20.dp))
        Button(
            onClick = viewModel::iniciarSesion,
            enabled = !estado.cargando,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AzulBotones),
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
                Text("Iniciar sesión", fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(Modifier.height(16.dp))
        TextButton(onClick = onRegistrarse) {
            Text(
                "¿No tienes cuenta? Regístrate",
                color = AzulBotones,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun IconoOjo(visible: Boolean, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Canvas(modifier = Modifier.size(24.dp)) {
            val trazo = size.minDimension * 0.09f
            drawOval(
                color = TextoSecundario,
                topLeft = Offset(size.width * 0.06f, size.height * 0.28f),
                size = Size(size.width * 0.88f, size.height * 0.44f),
                style = Stroke(width = trazo)
            )
            if (visible) {
                drawCircle(
                    color = TextoSecundario,
                    radius = size.minDimension * 0.15f,
                    center = Offset(size.width / 2f, size.height / 2f)
                )
            } else {
                drawLine(
                    color = TextoSecundario,
                    start = Offset(size.width * 0.15f, size.height * 0.20f),
                    end = Offset(size.width * 0.85f, size.height * 0.80f),
                    strokeWidth = trazo * 1.2f,
                    cap = StrokeCap.Round
                )
            }
        }
    }
}
