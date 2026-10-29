package com.example.mi_pequeno_emprendedor.ui.registro

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.ResolverStyle

data class ErroresRegistro(
    val nombre: String? = null,
    val apellido: String? = null,
    val nickname: String? = null,
    val fechaNacimiento: String? = null,
    val email: String? = null,
    val password: String? = null,
    val confirmacionPassword: String? = null
) {
    val hayErrores: Boolean
        get() = listOf(nombre, apellido, nickname, fechaNacimiento, email, password, confirmacionPassword)
            .any { it != null }
}

private val FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT)
private val REGEX_EMAIL = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")

/** Convierte dd/MM/aaaa a ISO (aaaa-MM-dd). Devuelve null si la fecha no es válida o es futura. */
fun fechaAIso(fecha: String): String? = runCatching {
    LocalDate.parse(fecha.trim(), FORMATO_FECHA)
}.getOrNull()
    ?.takeIf { !it.isAfter(LocalDate.now()) }
    ?.toString()

/** Error de confirmación mientras se escribe: vacío no marca error, distinto sí. */
fun errorConfirmacion(password: String, confirmacion: String): String? =
    if (confirmacion.isBlank() || confirmacion == password) null else "Las contraseñas no coinciden"

fun validarRegistro(
    nombre: String,
    apellido: String,
    nickname: String,
    fechaNacimiento: String,
    email: String,
    password: String,
    confirmacionPassword: String
) = ErroresRegistro(
    nombre = if (nombre.isBlank()) "Ingresa tu nombre" else null,
    apellido = if (apellido.isBlank()) "Ingresa tu apellido" else null,
    nickname = if (nickname.isBlank()) "Ingresa un nickname" else null,
    fechaNacimiento = when {
        fechaNacimiento.isBlank() -> "Ingresa tu fecha de nacimiento"
        fechaAIso(fechaNacimiento) == null -> "Usa una fecha válida dd/mm/aaaa"
        else -> null
    },
    email = if (REGEX_EMAIL.matches(email.trim())) null else "Ingresa un correo válido",
    password = if (password.length < 6) "Mínimo 6 caracteres" else null,
    confirmacionPassword = when {
        confirmacionPassword.isBlank() -> "Confirma tu contraseña"
        else -> errorConfirmacion(password, confirmacionPassword)
    }
)
