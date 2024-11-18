package com.thoughtworks.voiceassistant.core.abilities

interface Tts {
    interface Listener {
        fun onPlayStart() {}
        fun onPlayEnd() {}
        fun onPlayCancel() {}
        fun onError(errorMessage: String) {}
        fun onTTSFileSaved(ttsFilePath: String) {}
    }

    data class Result(
        val success: Boolean = false,
        val errorMessage: String = "",
        val ttsFilePath: String = "",
    )

    suspend fun initialize()

    fun release()

    suspend fun speak(
        text: String,
        params: Map<String, Any>,
        listener: Listener,
    )

    suspend fun speak(
        text: String,
        params: Map<String, Any>,
    ): Result

    fun stop()
}