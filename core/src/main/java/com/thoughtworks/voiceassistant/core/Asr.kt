package com.thoughtworks.voiceassistant.core

interface AsrCallback {
    fun onResult(text: String) {}
    fun onError(errorMessage: String) {}
    fun onVolumeChanged(volume: Float) {}
}

interface Asr {
    fun initialize()
    suspend fun startListening(asrCallback: AsrCallback? = null): String
    fun stopListening()
    fun release()
}