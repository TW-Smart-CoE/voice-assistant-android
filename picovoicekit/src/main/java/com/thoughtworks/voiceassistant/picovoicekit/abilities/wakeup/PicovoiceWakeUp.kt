package com.thoughtworks.voiceassistant.picovoicekit.abilities.wakeup

import ai.picovoice.porcupine.PorcupineManager
import ai.picovoice.porcupine.PorcupineManagerCallback
import android.content.Context
import com.thoughtworks.voiceassistant.core.abilities.WakeUp
import com.thoughtworks.voiceassistant.core.logger.DefaultLogger
import com.thoughtworks.voiceassistant.core.logger.Logger
import com.thoughtworks.voiceassistant.core.logger.debug
import com.thoughtworks.voiceassistant.core.logger.error
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PicovoiceWakeUp(
    private val context: Context,
    private val logger: Logger,
    private val wakeUpConfig: WakeUpConfig,
) : WakeUp {
    private var isInit = false
    private var isListening = false
    private var wakeUpListener: WakeUpListener? = null

    interface WakeUpListener {
        fun onSuccess(keywordIndex: Int)
        fun onError(errorCode: Int, errorMessage: String)
        fun onStop()
    }

    val wakeWordCallback = object : PorcupineManagerCallback {
        override fun invoke(keywordIndex: Int) {
            wakeUpListener?.onSuccess(keywordIndex)
        }
    }

    private var porcupineManager: PorcupineManager? = null

    override suspend fun initialize(): Boolean {
        if (isInit) {
            logger.debug(TAG, "WakeUpManager is already initialized")
            return true
        }

        try {
            PorcupineManager.Builder().apply {
                setAccessKey(wakeUpConfig.accessKey)
                setKeywordPaths(wakeUpConfig.keywordPaths)
                if (wakeUpConfig.modelPath.isNotEmpty()) {
                    setModelPath(wakeUpConfig.modelPath)
                }
                porcupineManager = build(context, wakeWordCallback)
            }
        } catch (e: Exception) {
            logger.error(TAG, "Failed to initialize WakeUpManager: ${e.message}")
            return false
        }

        isInit = true
        logger.debug(TAG, "WakeUpManager initialized successfully")

        return true
    }

    private fun listen(wakeUpListener: WakeUpListener) {
        if (!isInit) {
            logger.error(TAG, "WakeUpManager is not initialized")
            return
        }

        porcupineManager?.start()
        this.wakeUpListener = wakeUpListener
    }

    override suspend fun listen(onWakeUp: ((Int) -> Unit)?): WakeUp.Result {
        isListening = true
        return suspendCoroutine { continuation ->
            CoroutineScope(Dispatchers.IO).launch {
                listen(object : WakeUpListener {
                    private fun resumeWithoutComplain(result: WakeUp.Result) {
                        try {
                            continuation.resume(
                                result
                            )
                        } catch (_: Exception) {
                        } finally {
                            isListening = false
                            wakeUpListener = null
                        }
                    }

                    override fun onSuccess(keywordIndex: Int) {
                        if (keywordIndex < wakeUpConfig.keywordPaths.size) {
                            onWakeUp?.invoke(keywordIndex)
                            resumeWithoutComplain(
                                WakeUp.Result(
                                    isSuccess = true,
                                    keywordIndex = keywordIndex
                                )
                            )
                        } else {
                            resumeWithoutComplain(
                                WakeUp.Result(
                                    isSuccess = false,
                                    errorMessage = "keywordIndex out of range"
                                )
                            )
                        }
                    }

                    override fun onError(errorCode: Int, errorMessage: String) {
                        resumeWithoutComplain(
                            WakeUp.Result(
                                isSuccess = false,
                                errorMessage = "errorCode: $errorCode, errorMessage: $errorMessage"
                            )
                        )
                    }

                    override fun onStop() {
                        resumeWithoutComplain(
                            WakeUp.Result(
                                isSuccess = false,
                                errorMessage = "stopped"
                            )
                        )
                    }
                })
            }
        }
    }

    override fun isListening(): Boolean {
        return isListening
    }

    override fun stop() {
        if (!isInit) {
            logger.error(TAG, "WakeUpManager is not initialized")
            return
        }

        porcupineManager?.stop()
        wakeUpListener?.onStop()
    }

    override fun release() {
        if (isInit) {
            wakeUpListener = null
            porcupineManager?.delete()
            porcupineManager = null
            isInit = false
        }
    }

    companion object {
        private const val TAG = "PicovoiceWakeup"

        fun create(
            context: Context,
            params: Map<String, Any> = emptyMap(),
            logger: Logger = DefaultLogger(),
        ): WakeUp {
            val wakeUpConfig = WakeUpConfig.create(context, params)
            return PicovoiceWakeUp(context, logger, wakeUpConfig)
        }
    }
}