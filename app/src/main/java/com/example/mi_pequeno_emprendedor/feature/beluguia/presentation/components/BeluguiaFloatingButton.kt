package com.example.mi_pequeno_emprendedor.feature.beluguia.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun BeluguiaFloatingButton(
    onOpenChat: () -> Unit
) {
    // BoxWithConstraints nos da el ancho y alto exacto disponible de la pantalla
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp) // Margen de seguridad respecto a los bordes
    ) {
        val density = LocalDensity.current
        val buttonSize = 60.dp
        val buttonSizePx = with(density) { buttonSize.toPx() }
        
        // Límites máximos en píxeles para que el botón nunca salga de la pantalla
        val maxWidthPx = with(density) { maxWidth.toPx() } - buttonSizePx
        val maxHeightPx = with(density) { maxHeight.toPx() } - buttonSizePx

        // Posición inicial: esquina inferior derecha (encima de la barra de navegación)
        var offsetX by remember { mutableFloatStateOf(maxWidthPx.coerceAtLeast(0f)) }
        var offsetY by remember { mutableFloatStateOf((maxHeightPx - 70f).coerceAtLeast(0f)) }

        Surface(
            onClick = onOpenChat,
            shape = CircleShape,
            color = Color(0xFF005AC1),
            modifier = Modifier
                // 1. LAMBDA DE OFFSET: Se ejecuta en la fase de Layout/Draw.
                // Cero recomposiciones = Cero lag al arrastrar.
                .offset { 
                    IntOffset(
                        x = offsetX.roundToInt(), 
                        y = offsetY.roundToInt()
                    ) 
                }
                .size(buttonSize)
                .shadow(elevation = 6.dp, shape = CircleShape)
                // 2. DETECCIÓN DE GESTOS CON LÍMITES DE BORDE
                .pointerInput(maxWidthPx, maxHeightPx) {
                    detectDragGestures { change, dragAmount ->
                        change.consume() // Bloquea el scroll de fondo mientras arrastras
                        
                        // Actualiza la coordenada restringiéndola dentro de la pantalla
                        offsetX = (offsetX + dragAmount.x).coerceIn(0f, maxWidthPx.coerceAtLeast(0f))
                        offsetY = (offsetY + dragAmount.y).coerceIn(0f, maxHeightPx.coerceAtLeast(0f))
                    }
                }
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFD0E4FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "B",
                        fontSize = 22.sp,
                        color = Color(0xFF005AC1)
                    )
                }
            }
        }
    }
}