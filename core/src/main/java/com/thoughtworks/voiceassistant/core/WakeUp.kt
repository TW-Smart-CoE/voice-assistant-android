package com.thoughtworks.voiceassistant.core

interface WakeUpCallback {
    fun onSuccess(keywordIndex: Int) {}
    fun onError(errorCode: Int, errorMessage: String) {}
    fun onStop() {}
}

interface WakeUp {
    fun initialize()
    suspend fun start(wakeUpCallback: WakeUpCallback? = null): Int
    fun stop()
    fun release()
}
