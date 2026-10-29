package com.example.mi_pequeno_emprendedor.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ColoresApp = lightColorScheme(
    primary = AzulBotones,
    onPrimary = Color.White,
    primaryContainer = AzulEtiqueta,
    onPrimaryContainer = AzulTexto,
    secondary = VerdeBotones,
    onSecondary = Color.White,
    secondaryContainer = VerdeEtiqueta,
    onSecondaryContainer = VerdeTexto,
    tertiary = RojoBotones,
    onTertiary = Color.White,
    tertiaryContainer = RojoEtiqueta,
    onTertiaryContainer = RojoTexto,
    error = RojoTexto,
    errorContainer = RojoEtiqueta,
    onErrorContainer = RojoTexto,
    background = Color.White,
    onBackground = TextoPrincipal,
    surface = Color.White,
    onSurface = TextoPrincipal,
    surfaceVariant = AzulEtiqueta,
    onSurfaceVariant = AzulTexto,
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFF7FAFF),
    surfaceContainer = Color(0xFFF1F6FE),
    surfaceContainerHigh = Color.White,
    surfaceContainerHighest = Color(0xFFEAF1FB),
    outline = BordeCampo,
    outlineVariant = BordeCampo
)

@Composable
fun Mi_pequeno_emprendedorTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ColoresApp,
        typography = Typography,
        content = content
    )
}
