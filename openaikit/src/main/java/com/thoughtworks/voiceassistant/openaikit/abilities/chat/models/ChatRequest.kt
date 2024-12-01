package com.thoughtworks.voiceassistant.openaikit.abilities.chat.models

import com.google.gson.annotations.SerializedName
import com.thoughtworks.voiceassistant.core.abilities.Chat

class ChatRequest(
    val model: String,

    val messages: List<Chat.Message>,

    val temperature: Float,

    @SerializedName("max_tokens")
    val maxTokens: Int,

    val stream: Boolean = false,
)