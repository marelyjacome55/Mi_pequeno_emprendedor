package com.example.mi_pequeno_emprendedor.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Usuario(

    @SerialName("Usuario_id")
    val usuarioId: Long? = null,

    @SerialName("auth_user_id")
    val authUserId: String? = null,

    @SerialName("Foto_perfil")
    val fotoPerfil: String? = null,

    @SerialName("Nombre")
    val nombre: String,

    @SerialName("Apellidos")
    val apellidos: String? = null,

    @SerialName("NIckName")
    val nickname: String? = null,

    @SerialName("Clasificacion")
    val clasificacion: Int? = null,

    @SerialName("Fecha nacimiento")
    val fechaNacimiento: String? = null
)