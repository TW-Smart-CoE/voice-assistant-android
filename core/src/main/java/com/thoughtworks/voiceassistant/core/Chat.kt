package com.thoughtworks.voiceassistant.core

interface ChatCallback {
    fun onResult(text: String) {}
    fun onError(errorMessage: String) {}
}

interface Chat {
    fun initialize()
    suspend fun chat(content: String, chatCallback: ChatCallback? = null): String
    fun clearConversationHistory()
    fun release()
}