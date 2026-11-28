package com.example.mi_pequeno_emprendedor.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mi_pequeno_emprendedor.data.auth.AuthRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val nickname: String = "",
    val password: String = "",
    val mostrarPassword: Boolean = false,
    val cargando: Boolean = false,
    val error: String? = null,
    val exito: Boolean = false
)

class LoginViewModel(
    private val repositorio: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onNicknameChange(valor: String) = _uiState.update { it.copy(nickname = valor, error = null) }
    fun onPasswordChange(valor: String) = _uiState.update { it.copy(password = valor, error = null) }
    fun alternarPassword() = _uiState.update { it.copy(mostrarPassword = !it.mostrarPassword) }

    fun iniciarSesion() {
        val estado = _uiState.value
        if (estado.nickname.isBlank() || estado.password.isBlank()) {
            _uiState.update { it.copy(error = "Ingresa tu nickname y contraseña") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true, error = null) }
            try {
                repositorio.iniciarSesion(estado.nickname.trim(), estado.password)
                _uiState.update { it.copy(cargando = false, exito = true) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        cargando = false,
                        error = if (e is IllegalArgumentException) {
                            e.message ?: "Nickname o contraseña incorrectos"
                        } else {
                            "Nickname o contraseña incorrectos"
                        }
                    )
                }
            }
        }
    }
}
