package com.thoughtworks.voiceassistant.core.abilities

interface Tts {
    interface Listener {
        fun onPlayStart() {}
        fun onPlayEnd() {}
        fun onPlayCancel() {}
        fun onError(errorMessage: String) {}
        fun onTTSFileSaved(ttsFilePath: String) {}
    }

    suspend fun initialize()
    fun release()
    suspend fun play(text: String, params: Map<String, Any> = emptyMap(), listener: Listener? = null)
    fun stopPlay()
}