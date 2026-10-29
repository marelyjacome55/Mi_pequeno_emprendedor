package com.example.mi_pequeno_emprendedor.ui.registro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mi_pequeno_emprendedor.data.auth.AuthRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegistroUiState(
    val nombre: String = "",
    val apellido: String = "",
    val nickname: String = "",
    val fechaNacimiento: String = "",
    val email: String = "",
    val password: String = "",
    val confirmacionPassword: String = "",
    val errores: ErroresRegistro = ErroresRegistro(),
    val cargando: Boolean = false,
    val errorGeneral: String? = null,
    val exito: Boolean = false,
    val requiereConfirmacion: Boolean = false
)

class RegistroViewModel(
    private val repositorio: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistroUiState())
    val uiState: StateFlow<RegistroUiState> = _uiState.asStateFlow()

    fun onNombreChange(valor: String) = _uiState.update { it.copy(nombre = valor) }
    fun onApellidoChange(valor: String) = _uiState.update { it.copy(apellido = valor) }
    fun onNicknameChange(valor: String) = _uiState.update { it.copy(nickname = valor) }
    fun onFechaNacimientoChange(valor: String) = _uiState.update { it.copy(fechaNacimiento = valor) }
    fun onEmailChange(valor: String) = _uiState.update { it.copy(email = valor) }
    fun onPasswordChange(valor: String) = _uiState.update {
        it.copy(
            password = valor,
            errores = it.errores.copy(
                confirmacionPassword = errorConfirmacion(valor, it.confirmacionPassword)
            )
        )
    }
    fun onConfirmacionPasswordChange(valor: String) = _uiState.update {
        it.copy(
            confirmacionPassword = valor,
            errores = it.errores.copy(
                confirmacionPassword = errorConfirmacion(it.password, valor)
            )
        )
    }

    fun registrar() {
        val estado = _uiState.value
        val errores = validarRegistro(
            nombre = estado.nombre,
            apellido = estado.apellido,
            nickname = estado.nickname,
            fechaNacimiento = estado.fechaNacimiento,
            email = estado.email,
            password = estado.password,
            confirmacionPassword = estado.confirmacionPassword
        )
        if (errores.hayErrores) {
            _uiState.update { it.copy(errores = errores) }
            return
        }
        val fechaIso = fechaAIso(estado.fechaNacimiento) ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true, errores = ErroresRegistro(), errorGeneral = null) }
            try {
                val conSesion = repositorio.registrar(
                    correo = estado.email.trim(),
                    contrasena = estado.password,
                    nombre = estado.nombre.trim(),
                    apellido = estado.apellido.trim(),
                    nickname = estado.nickname.trim(),
                    fechaNacimiento = fechaIso
                )
                _uiState.update {
                    it.copy(cargando = false, exito = conSesion, requiereConfirmacion = !conSesion)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update { it.copy(cargando = false, errorGeneral = mensajeError(e)) }
            }
        }
    }

    private fun mensajeError(e: Exception): String =
        if (e.message?.contains("already registered", ignoreCase = true) == true) {
            "Ese correo ya está registrado"
        } else {
            e.message ?: "No se pudo crear la cuenta"
        }
}
