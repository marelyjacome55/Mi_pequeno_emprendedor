package com.example.mi_pequeno_emprendedor.feature.beluguia.presentation.viewmodel

import java.util.UUID

enum class MessageSender {
    USER, ASSISTANT
}

data class GroupSummaryPayload(
    val topCombo: String,
    val topMember: String,
    val opportunity: String,
    val note: String
)

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val sender: MessageSender,
    val text: String,
    val timestamp: String,
    val summaryPayload: GroupSummaryPayload? = null
)

data class BeluguiaUiState(
    val messages: List<ChatMessage> = emptyList(),
    val inputText: String = "",
    val isTyping: Boolean = false,
    val quickSuggestions: List<String> = listOf(
        "Revisar ganancias",
        "Analizar combos",
        "Mejorar inventario",
        "Consejos para vender más"
    )
)