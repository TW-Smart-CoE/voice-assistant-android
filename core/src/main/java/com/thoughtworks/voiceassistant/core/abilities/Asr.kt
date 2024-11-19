package com.thoughtworks.voiceassistant.core.abilities

interface Asr {
    data class Result(
        val isSuccess: Boolean = false,
        val heardContent: String = "",
        val errorMessage: String = "",
    )

    suspend fun initialize()
    fun release()
    suspend fun listen(onHeard: ((String) -> Unit)? = null): Result
    fun stop()
}

