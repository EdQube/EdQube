package com.module.edqube.models

data class ChatMessage(
    val text: String,
    val time: String,
    val isSentByMe: Boolean
)
