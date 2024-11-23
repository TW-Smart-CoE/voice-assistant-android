package com.thoughtworks.voiceassistant.core.abilities

interface WakeUp {
    data class Result(
        val isSuccess: Boolean = false,
        val keywordIndex: Int = 0,
        val errorMessage: String = "",
    )

    suspend fun initialize(): Boolean
    suspend fun listen(onWakeUp: ((Int) -> Unit)?): Result
    fun stop()
    fun release()
}
