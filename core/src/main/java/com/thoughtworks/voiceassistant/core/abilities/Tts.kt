package com.thoughtworks.voiceassistant.core.abilities

interface Tts {
    data class Result(
        val success: Boolean = false,
        val errorMessage: String = "",
        val ttsFilePath: String = "",
    )

    suspend fun initialize()
    fun release()
    suspend fun speak(text: String, params: Map<String, Any>): Result
    fun stop()
}