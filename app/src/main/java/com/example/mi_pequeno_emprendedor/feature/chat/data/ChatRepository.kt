
package com.example.mi_pequeno_emprendedor.feature.chat.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.example.mi_pequeno_emprendedor.core.supabase.SupabaseConfig
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc

@Serializable
data class ChatDto(
    @SerialName("Chat_id")
    val chatId: Long,
    @SerialName("Nombre")
    val nombre: String? = null,
    @SerialName("Tipo")
    val tipo: String? = null
)


@Serializable
private data class ParametrosCrearChat(
    @SerialName("p_nombre")
    val nombre: String,

    @SerialName("p_tipo")
    val tipo: String,

    @SerialName("p_integrantes")
    val integrantes: List<Long>
)

@Serializable
data class UsuarioChatDto(
    @SerialName("Usuario_id")
    val usuarioId: Long,
    @SerialName("Nombre")
    val nombre: String,
    @SerialName("Apellidos")
    val apellidos: String? = null,
    @SerialName("NIckName")
    val nickName: String? = null
)

@Serializable
data class ChatUsuarioDto(
    @SerialName("Chat_id")
    val chatId: Long,
    @SerialName("Usuario_id")
    val usuarioId: Long,
    @SerialName("Es_administrador")
    val esAdministrador: Boolean = false,
    @SerialName("Fijado")
    val fijado: Boolean = false
)

@Serializable
data class MensajeDto(
    @SerialName("Mensaje_id")
    val mensajeId: Long,
    @SerialName("Contenido")
    val contenido: String,
    @SerialName("Fecha")
    val fecha: String? = null,
    @SerialName("Chat_id")
    val chatId: Long? = null,
    @SerialName("Usuario_id")
    val usuarioId: Long? = null
)

@Serializable
private data class NuevoChatDto(
    @SerialName("Nombre")
    val nombre: String,
    @SerialName("Tipo")
    val tipo: String
)

@Serializable
private data class NuevoIntegranteDto(
    @SerialName("Chat_id")
    val chatId: Long,
    @SerialName("Usuario_id")
    val usuarioId: Long,
    @SerialName("Es_administrador")
    val esAdministrador: Boolean
)

@Serializable
private data class NuevoMensajeDto(
    @SerialName("Contenido")
    val contenido: String,
    @SerialName("Chat_id")
    val chatId: Long,
    @SerialName("Usuario_id")
    val usuarioId: Long
)

@Serializable
private data class ActualizarNombreChatDto(
    @SerialName("Nombre")
    val nombre: String
)

@Serializable
private data class ActualizarFijadoDto(
    @SerialName("Fijado")
    val fijado: Boolean
)

class ChatRepository {

    private val supabase = SupabaseConfig.client

    // Obtener el ID del usuario que inició sesión.
    suspend fun obtenerUsuarioActualId(): Long {
        val usuarioAuthId = supabase.auth.currentUserOrNull()?.id
            ?: throw IllegalStateException("No hay una sesión iniciada.")

        val registro = supabase
            .from("Usuario")
            .select {
                filter {
                    eq("auth_user_id", usuarioAuthId)
                }
            }
            .decodeSingle<UsuarioActualDto>()

        return registro.usuarioId
    }

    suspend fun obtenerMisGrupos(): List<ChatDto> {
        val usuarioId = obtenerUsuarioActualId()

        val integrantes = supabase
            .from("Chat_Usuario")
            .select {
                filter {
                    eq("Usuario_id", usuarioId)
                }
            }
            .decodeList<ChatUsuarioDto>()

        if (integrantes.isEmpty()) return emptyList()

        val idsChats = integrantes.map { it.chatId }.distinct()

        val chats = supabase
            .from("Chat")
            .select()
            .decodeList<ChatDto>()

        return chats.filter {
            it.chatId in idsChats && it.tipo == "grupo"
        }
    }

    suspend fun obtenerMisChats(): List<ChatDto> {
        val usuarioId = obtenerUsuarioActualId()

        val integrantes = supabase
            .from("Chat_Usuario")
            .select {
                filter {
                    eq("Usuario_id", usuarioId)
                }
            }
            .decodeList<ChatUsuarioDto>()

        if (integrantes.isEmpty()) return emptyList()

        val idsChats = integrantes.map { it.chatId }.distinct()

        val chats = supabase
            .from("Chat")
            .select()
            .decodeList<ChatDto>()

        return chats.filter {
            it.chatId in idsChats && it.tipo == "chat"
        }
    }

    suspend fun obtenerMisMembresias(): List<ChatUsuarioDto> {
        val usuarioId = obtenerUsuarioActualId()

        return supabase
            .from("Chat_Usuario")
            .select {
                filter {
                    eq("Usuario_id", usuarioId)
                }
            }
            .decodeList<ChatUsuarioDto>()
    }

    suspend fun actualizarFijado(
        chatId: Long,
        fijado: Boolean
    ) {
        val usuarioId = obtenerUsuarioActualId()

        supabase
            .from("Chat_Usuario")
            .update(ActualizarFijadoDto(fijado)) {
                filter {
                    eq("Chat_id", chatId)
                    eq("Usuario_id", usuarioId)
                }
            }
    }


    suspend fun buscarUsuarios(texto: String): List<UsuarioChatDto> {
        val usuarioActualId = obtenerUsuarioActualId()

        android.util.Log.d("ChatRepository", "Usuario actual ID: $usuarioActualId")
        android.util.Log.d("ChatRepository", "Texto de búsqueda: $texto")

        val usuarios = supabase
            .from("Usuario")
            .select()
            .decodeList<UsuarioChatDto>()

        android.util.Log.d(
            "ChatRepository",
            "Usuarios recibidos desde Supabase: ${usuarios.size}"
        )

        usuarios.forEach { usuario ->
            android.util.Log.d(
                "ChatRepository",
                "Usuario: ID=${usuario.usuarioId}, " +
                        "Nombre=${usuario.nombre}, " +
                        "NickName=${usuario.nickName}, " +
                        "Apellidos=${usuario.apellidos}"
            )
        }

        val resultados = usuarios.filter { usuario ->
            usuario.usuarioId != usuarioActualId &&
                    (
                            usuario.nickName.orEmpty().contains(texto.trim(), ignoreCase = true) ||
                                    usuario.usuarioId.toString().contains(texto.trim()) ||
                                    usuario.nombre.contains(texto.trim(), ignoreCase = true) ||
                                    usuario.apellidos.orEmpty().contains(texto.trim(), ignoreCase = true)
                            )
        }

        android.util.Log.d(
            "ChatRepository",
            "Resultados encontrados: ${resultados.size}"
        )

        return resultados
    }




    suspend fun crearChat(
        nombre: String,
        tipo: String,
        integrantesSeleccionados: List<Long>
    ): Long {
        require(tipo == "chat" || tipo == "grupo") {
            "El tipo de conversación no es válido."
        }

        require(nombre.isNotBlank()) {
            "El nombre de la conversación no puede estar vacío."
        }

        val usuarioActualId = obtenerUsuarioActualId()

        val usuarios = (integrantesSeleccionados + usuarioActualId)
            .distinct()

        require(tipo != "chat" || usuarios.size == 2) {
            "Un chat debe tener exactamente dos integrantes."
        }

        require(tipo != "grupo" || usuarios.size >= 2) {
            "Un grupo necesita al menos otro integrante."
        }

        val otrosIntegrantes = usuarios.filter {
            it != usuarioActualId
        }

        val parametros = ParametrosCrearChat(
            nombre = nombre.trim(),
            tipo = tipo,
            integrantes = otrosIntegrantes
        )

        return supabase.postgrest
            .rpc("crear_chat", parametros)
            .decodeAs<Long>()
    }

    suspend fun obtenerMensajes(chatId: Long): List<MensajeDto> {
        return supabase
            .from("Mensaje")
            .select {
                filter {
                    eq("Chat_id", chatId)
                }
            }
            .decodeList<MensajeDto>()
            .sortedBy { it.fecha.orEmpty() }
    }

    suspend fun enviarMensaje(chatId: Long, contenido: String) {
        val usuarioId = obtenerUsuarioActualId()

        supabase
            .from("Mensaje")
            .insert(
                NuevoMensajeDto(
                    contenido = contenido,
                    chatId = chatId,
                    usuarioId = usuarioId
                )
            )
    }

    suspend fun obtenerUsuariosDelGrupo(chatId: Long): List<UsuarioChatDto> {
        val integrantes = supabase
            .from("Chat_Usuario")
            .select {
                filter {
                    eq("Chat_id", chatId)
                }
            }
            .decodeList<ChatUsuarioDto>()

        if (integrantes.isEmpty()) return emptyList()

        val ids = integrantes.map { it.usuarioId }

        val usuarios = supabase
            .from("Usuario")
            .select()
            .decodeList<UsuarioChatDto>()

        return usuarios.filter { it.usuarioId in ids }
    }

    suspend fun cambiarNombreGrupo(chatId: Long, nuevoNombre: String) {
        supabase
            .from("Chat")
            .update(ActualizarNombreChatDto(nuevoNombre)) {
                filter {
                    eq("Chat_id", chatId)
                }
            }
    }

    suspend fun salirDelGrupo(chatId: Long) {
        val usuarioId = obtenerUsuarioActualId()

        supabase
            .from("Chat_Usuario")
            .delete {
                filter {
                    eq("Chat_id", chatId)
                    eq("Usuario_id", usuarioId)
                }
            }
    }
}

@Serializable
private data class UsuarioActualDto(
    @SerialName("Usuario_id")
    val usuarioId: Long,
    @SerialName("auth_user_id")
    val authUserId: String
)
