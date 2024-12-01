package com.thoughtworks.voiceassistant.core.abilities

interface Chat {
    data class Message(
        val role: String,
        val content: String,
    )

    data class Result(
        val isSuccess: Boolean = false,
        val errorMessage: String = "",
        val message: Message = Message("assistant", ""),
    )

    suspend fun initialize()
    suspend fun chat(userMessage: Message): Result
    fun isChatting(): Boolean
    fun stop()
    fun release()
    fun getSystemContext(): List<Message>
    fun setSystemContext(contextMessages: List<Message>)
    fun getConversationHistory(): List<Message>
    fun clearConversationHistory()
}