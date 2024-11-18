package com.thoughtworks.voiceassistant.core.abilities

interface Asr {
    data class Result(
        val success: Boolean = false,
        val heardContent: String = "",
        val errorMessage: String = "",
    )

    suspend fun initialize()
    fun release()
    suspend fun listen(): Result
    fun stop()
}

