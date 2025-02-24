package com.thoughtworks.voiceassistant.core.abilities

interface Tts {
    data class Result(
        val isSuccess: Boolean = false,
        val errorMessage: String = "",
        val ttsFilePath: String = "",
    )

    suspend fun initialize(): Boolean
    fun release()
    suspend fun speak(text: String, params: Map<String, Any> = emptyMap()): Result
    fun isSpeaking(): Boolean
    fun stop()
}