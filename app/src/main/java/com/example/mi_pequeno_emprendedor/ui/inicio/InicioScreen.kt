package com.example.mi_pequeno_emprendedor.ui.inicio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mi_pequeno_emprendedor.ui.components.NavegacionInferior
import com.example.mi_pequeno_emprendedor.ui.theme.AzulBotones
import com.example.mi_pequeno_emprendedor.ui.theme.AzulHeader
import com.example.mi_pequeno_emprendedor.ui.theme.AzulTexto

@Composable
fun InicioScreen(
    alRealizarVenta: () -> Unit,
    alCambiarSeccion: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(AzulHeader, AzulBotones)))
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                "Mi pequeño emprendedor",
                color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                "Inicio",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Punto de venta",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AzulTexto
                )
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = alRealizarVenta,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AzulBotones),
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(52.dp)
                ) {
                    Text("Realizar venta", fontWeight = FontWeight.SemiBold)
                }
            }
        }

        NavegacionInferior(
            seccionSeleccionada = "Inicio",
            alCambiarSeccion = alCambiarSeccion
        )
    }
}
