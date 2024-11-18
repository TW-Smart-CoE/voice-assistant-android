package com.thoughtworks.voiceassistant.core.abilities

interface Asr {
    interface Listener {
        fun onResult(text: String) {}
        fun onError(errorMessage: String) {}
        fun onVolumeChanged(volume: Float) {}
    }

    data class Result(
        val success: Boolean = false,
        val heardContent: String = "",
        val errorMessage: String = "",
    )

    suspend fun initialize()
    fun release()
    suspend fun listen(listener: Listener)
    suspend fun listen(): Result
    fun stop()
}

