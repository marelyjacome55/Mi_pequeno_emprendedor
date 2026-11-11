
package com.example.mi_pequeno_emprendedor.data.auth

import com.example.mi_pequeno_emprendedor.core.supabase.SupabaseConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@Serializable
private data class CorreoRow(val correo: String)

class AuthRepository(
    private val cliente: SupabaseClient = SupabaseConfig.client
) {

    /** Registra una cuenta y devuelve true si se creó una sesión inmediatamente. */
    suspend fun registrar(
        correo: String,
        contrasena: String,
        nombre: String,
        apellido: String,
        nickname: String,
        fechaNacimiento: String
    ): Boolean {
        cliente.auth.signUpWith(Email) {
            email = correo
            password = contrasena
            data = buildJsonObject {
                put("nombre", nombre)
                put("apellido", apellido)
                put("nickname", nickname)
                put("fecha_nacimiento", fechaNacimiento)
            }
        }

        return cliente.auth.currentSessionOrNull() != null
    }

    suspend fun nicknameDisponible(nickname: String): Boolean =
        emailPorNickname(nickname) == null

    suspend fun iniciarSesion(nickname: String, contrasena: String) {
        val correo = emailPorNickname(nickname)
            ?: throw IllegalArgumentException(
                "Nickname o contraseña incorrectos"
            )

        cliente.auth.signInWith(Email) {
            email = correo
            password = contrasena
        }
    }

    /** Comprueba si Supabase tiene una sesión activa. */
    fun haySesionActiva(): Boolean {
        return cliente.auth.currentSessionOrNull() != null
    }

    /** Cierra la sesión actual. */
    suspend fun cerrarSesion() {
        cliente.auth.signOut()
    }

    private suspend fun emailPorNickname(nickname: String): String? =
        cliente.postgrest.rpc(
            "email_por_nickname",
            buildJsonObject {
                put("nick", nickname)
            }
        ).decodeSingleOrNull<CorreoRow>()?.correo
}
