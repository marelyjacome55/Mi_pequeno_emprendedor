package com.example.mi_pequeno_emprendedor.feature.beluguia.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BeluguiaViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(BeluguiaUiState())
    val uiState: StateFlow<BeluguiaUiState> = _uiState.asStateFlow()

    init {
        // Carga los mensajes de la maqueta
        _uiState.update { currentState ->
            currentState.copy(
                messages = listOf(
                    ChatMessage(
                        sender = MessageSender.ASSISTANT,
                        text = "¡Hola, Denisse! Veo que el combo 'Outfit completo' fue tu mejor venta hoy. ¿Quieres que te dé recomendaciones para mejorar tus ganancias?",
                        timestamp = "9:41 AM"
                    ),
                    ChatMessage(
                        sender = MessageSender.USER,
                        text = "Sí, recomiéndame cómo repartir mejor las ganancias.",
                        timestamp = "9:42 AM"
                    ),
                    ChatMessage(
                        sender = MessageSender.ASSISTANT,
                        text = "Te sugiero revisar el aporte de cada integrante y comparar los costos de cada producto del combo. También puedes impulsar el combo desayuno, que tiene buen potencial.",
                        timestamp = "9:42 AM"
                    ),
                    ChatMessage(
                        sender = MessageSender.ASSISTANT,
                        text = "",
                        timestamp = "9:43 AM",
                        summaryPayload = GroupSummaryPayload(
                            topCombo = "Outfit completo",
                            topMember = "Ana",
                            opportunity = "Promocionar combo desayuno",
                            note = "¡Van muy bien!"
                        )
                    )
                )
            )
        }
    }

    fun onInputTextChanged(newText: String) {
        _uiState.update { it.copy(inputText = newText) }
    }

    fun sendMessage(text: String = _uiState.value.inputText) {
        if (text.isBlank()) return

        val userMsg = ChatMessage(
            sender = MessageSender.USER,
            text = text,
            timestamp = "9:44 AM"
        )

        _uiState.update { currentState ->
            currentState.copy(
                messages = currentState.messages + userMsg,
                inputText = "",
                isTyping = true
            )
        }

        viewModelScope.launch {
            delay(1500)
            val botMsg = ChatMessage(
                sender = MessageSender.ASSISTANT,
                text = "Belu-guIA ha procesado tu consulta sobre: '$text'. Espera la próxima actualización con recomendaciones personalizadas.",
                timestamp = "9:44 AM"
            )
            _uiState.update { currentState ->
                currentState.copy(
                    messages = currentState.messages + botMsg,
                    isTyping = false
                )
            }
        }
    }
}