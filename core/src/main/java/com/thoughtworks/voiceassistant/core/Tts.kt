package com.thoughtworks.voiceassistant.core

interface TtsCallback {
    fun onPlayEnd() {}
    fun onPlayCancel() {}
    fun onPlayError(errorMessage: String) {}
    fun onTTSFileSaved(ttsFilePath: String) {}
}

interface Tts {
    fun initialize()
    fun release()
    suspend fun play(text: String, params: Map<String, Any> = emptyMap(), ttsCallback: TtsCallback? = null)
    fun stopPlay()
}