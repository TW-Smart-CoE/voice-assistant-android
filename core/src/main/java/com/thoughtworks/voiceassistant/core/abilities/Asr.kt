package com.thoughtworks.voiceassistant.core.abilities

import com.thoughtworks.voiceassistant.core.exceptions.VoiceAssistantException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

interface AsrCallback {
    fun onResult(text: String) {}
    fun onError(errorMessage: String) {}
    fun onVolumeChanged(volume: Float) {}
}

interface Asr {
    fun initialize()
    fun startListening(asrCallback: AsrCallback? = null)
    fun stopListening()
    fun release()
}

suspend fun Asr.startListeningSuspend(
    asrCallback: AsrCallback? = null,
    onVolumeChanged: ((Float) -> Unit)? = null
): String {
    return suspendCancellableCoroutine { continuation ->
        startListening(object : AsrCallback {
            override fun onResult(text: String) {
                asrCallback?.onResult(text)
                if (continuation.isActive) {
                    continuation.resume(text)
                }
            }

            override fun onError(errorMessage: String) {
                asrCallback?.onError(errorMessage)
                if (continuation.isActive) {
                    continuation.resumeWithException(
                        VoiceAssistantException(code = "", message = errorMessage)
                    )
                }
            }

            override fun onVolumeChanged(volume: Float) {
                asrCallback?.onVolumeChanged(volume)
                onVolumeChanged?.invoke(volume)
            }
        })
    }
}
