package com.example.mi_pequeno_emprendedor.data.auth

import com.example.mi_pequeno_emprendedor.core.supabase.SupabaseConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class AuthRepository(
    private val cliente: SupabaseClient = SupabaseConfig.client
) {

    /** Devuelve true si Supabase creó la sesión de inmediato (sin confirmar correo). */
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
}
