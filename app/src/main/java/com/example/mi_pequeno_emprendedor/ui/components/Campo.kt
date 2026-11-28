package com.example.mi_pequeno_emprendedor.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.mi_pequeno_emprendedor.ui.theme.AzulBotones
import com.example.mi_pequeno_emprendedor.ui.theme.BordeCampo
import com.example.mi_pequeno_emprendedor.ui.theme.RojoTexto
import com.example.mi_pequeno_emprendedor.ui.theme.TextoPrincipal
import com.example.mi_pequeno_emprendedor.ui.theme.TextoSecundario

@Composable
internal fun Campo(
    etiqueta: String,
    valor: String,
    error: String?,
    onValorChange: (String) -> Unit,
    modifier: Modifier = Modifier,
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

    Box(modifier = modifier.fillMaxWidth()) {
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
