package com.thoughtworks.voiceassistant.core.abilities

interface WakeUpCallback {
    fun onSuccess(keywordIndex: Int) {}
    fun onError(errorCode: Int, errorMessage: String) {}
    fun onStop() {}
}

interface WakeUp {
    fun initialize()
    fun listen(wakeUpCallback: WakeUpCallback? = null)
    fun stop()
    fun release()
}
