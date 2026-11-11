package com.example.mi_pequeno_emprendedor.data.repository

import com.example.mi_pequeno_emprendedor.core.supabase.SupabaseConfig
import com.example.mi_pequeno_emprendedor.data.model.Usuario
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class UsuarioRepository(
    private val cliente: SupabaseClient = SupabaseConfig.client
) {

    /**
     * Obtiene el ID del usuario autenticado en Supabase Auth.
     */
    private fun obtenerAuthUserId(): String {
        return cliente.auth.currentUserOrNull()?.id
            ?: throw IllegalStateException(
                "No hay una sesión activa. Inicia sesión nuevamente."
            )
    }

    /**
     * Busca el registro de Usuario relacionado con la sesión actual.
     * La relación se resuelve mediante auth_user_id.
     */
    suspend fun obtenerPerfilActual(): Usuario? {
        val authId = obtenerAuthUserId()

        return cliente
            .from("Usuario")
            .select {
                filter {
                    eq("auth_user_id", authId)
                }
            }
            .decodeSingleOrNull<Usuario>()
    }

    /**
     * Comprueba si un nickname está disponible.
     * No considera como ocupado el nickname del propio usuario.
     */
    suspend fun nicknameDisponible(
        nickname: String
    ): Boolean {
        val nicknameLimpio = nickname.trim()

        if (nicknameLimpio.isBlank()) {
            return true
        }

        val authId = obtenerAuthUserId()

        val usuarios = cliente
            .from("Usuario")
            .select {
                filter {
                    eq("NIckName", nicknameLimpio)
                }
            }
            .decodeList<Usuario>()

        return usuarios.none {
            it.authUserId != authId
        }
    }

    /**
     * Guarda los datos personales del usuario actual.
     * Si todavía no existe su registro, lo crea.
     */
    suspend fun guardarDatosPersonales(
        nombre: String,
        apellidos: String,
        nickname: String,
        fechaNacimiento: String?
    ) {
        val authId = obtenerAuthUserId()

        val nombreLimpio = nombre.trim()
        val apellidosLimpios = apellidos.trim().ifBlank { null }
        val nicknameLimpio = nickname.trim().ifBlank { null }

        require(nombreLimpio.isNotBlank()) {
            "El nombre es obligatorio."
        }

        if (!nicknameLimpio.isNullOrBlank()) {
            val disponible = nicknameDisponible(nicknameLimpio)

            require(disponible) {
                "Este nickname ya está en uso."
            }
        }

        val perfilExistente = obtenerPerfilActual()

        if (perfilExistente != null) {

            cliente
                .from("Usuario")
                .update({
                    set("Nombre", nombreLimpio)
                    set("Apellidos", apellidosLimpios)
                    set("NIckName", nicknameLimpio)
                    set("Fecha nacimiento", fechaNacimiento)
                }) {
                    filter {
                        eq("auth_user_id", authId)
                    }
                }

        } else {

            val nuevoUsuario = UsuarioInsertDto(
                authUserId = authId,
                nombre = nombreLimpio,
                apellidos = apellidosLimpios,
                nickname = nicknameLimpio,
                fechaNacimiento = fechaNacimiento
            )

            cliente
                .from("Usuario")
                .insert(nuevoUsuario)
        }
    }

    @Serializable
    private data class UsuarioInsertDto(

        @SerialName("auth_user_id")
        val authUserId: String,

        @SerialName("Nombre")
        val nombre: String,

        @SerialName("Apellidos")
        val apellidos: String?,

        @SerialName("NIckName")
        val nickname: String?,

        @SerialName("Fecha nacimiento")
        val fechaNacimiento: String?
    )
}