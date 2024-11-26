package com.thoughtworks.voiceassistant.volcenginekit.abilities.asr

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.bytedance.speech.speechengine.SpeechEngine
import com.bytedance.speech.speechengine.SpeechEngineDefines
import com.bytedance.speech.speechengine.SpeechEngineGenerator
import com.google.gson.Gson
import com.thoughtworks.voiceassistant.core.abilities.Asr
import com.thoughtworks.voiceassistant.core.logger.DefaultLogger
import com.thoughtworks.voiceassistant.core.logger.Logger
import com.thoughtworks.voiceassistant.core.logger.debug
import com.thoughtworks.voiceassistant.core.logger.error
import com.thoughtworks.voiceassistant.core.utils.DeviceUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class VolcengineAsr(
    val context: Context,
    val logger: Logger,
    val config: AsrConfig,
) : Asr {
    private val gson = Gson()
    private var engine: SpeechEngine? = null
    private var isListening: Boolean = false
    private var asrListener: AsrListener? = null
    private var onHeard: ((String) -> Unit)? = null

    interface AsrListener {
        fun onResult(text: String)
        fun onError(errorMessage: String)
    }

    override suspend fun initialize(): Boolean {
        if (engine != null) {
            logger.debug(TAG, "ASR instance is already initialized")
            return true
        }

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            logger.error(TAG, "Manifest.permission.RECORD_AUDIO does not granted")
            return false
        }

        SpeechEngineGenerator.PrepareEnvironment(context, context.applicationContext as Application)
        engine = SpeechEngineGenerator.getInstance().apply {
            createEngine()
            setContext(context)
            configParams()
            val ret = initEngine()
            if (ret != SpeechEngineDefines.ERR_NO_ERROR) {
                val errMessage = "Init Engine Failed: $ret"
                logger.error(TAG, errMessage)
                return false
            }

            setListener(object : SpeechEngine.SpeechListener {
                override fun onSpeechMessage(
                    type: Int,
                    data: ByteArray,
                    len: Int,
                ) {
                    handleSpeechMessage(type, data, len)
                }
            })

            logger.debug(TAG, "ASR instance initialized successfully")
        }

        return true
    }

    private fun SpeechEngine.configParams() {
        setOptionString(
            SpeechEngineDefines.PARAMS_KEY_ENGINE_NAME_STRING,
            SpeechEngineDefines.ASR_ENGINE
        )
        setOptionString(SpeechEngineDefines.PARAMS_KEY_UID_STRING, config.userId)
        setOptionString(
            SpeechEngineDefines.PARAMS_KEY_DEVICE_ID_STRING,
            DeviceUtils.getDeviceId(context)
        )
        setOptionString(SpeechEngineDefines.PARAMS_KEY_APP_ID_STRING, config.appId)
        setOptionString(
            SpeechEngineDefines.PARAMS_KEY_APP_TOKEN_STRING,
            "Bearer;${config.appToken}"
        )
        setOptionString(
            SpeechEngineDefines.PARAMS_KEY_ASR_ADDRESS_STRING,
            "wss://openspeech.bytedance.com"
        )
        setOptionString(SpeechEngineDefines.PARAMS_KEY_ASR_URI_STRING, "/api/v2/asr")
        setOptionString(SpeechEngineDefines.PARAMS_KEY_ASR_CLUSTER_STRING, config.asrCluster)
        setOptionInt(SpeechEngineDefines.PARAMS_KEY_ASR_CONN_TIMEOUT_INT, 12000)
        setOptionInt(SpeechEngineDefines.PARAMS_KEY_ASR_RECV_TIMEOUT_INT, 8000)
        setOptionInt(SpeechEngineDefines.PARAMS_KEY_ASR_MAX_RETRY_TIMES_INT, 0)
        setOptionString(
            SpeechEngineDefines.PARAMS_KEY_RECORDER_TYPE_STRING,
            SpeechEngineDefines.RECORDER_TYPE_RECORDER
        )
        setOptionInt(
            SpeechEngineDefines.PARAMS_KEY_VAD_MAX_SPEECH_DURATION_INT,
            config.vadMaxSpeechDuration
        )
        setOptionBoolean(SpeechEngineDefines.PARAMS_KEY_ASR_ENABLE_DDC_BOOL, true)
        setOptionBoolean(SpeechEngineDefines.PARAMS_KEY_ASR_SHOW_NLU_PUNC_BOOL, true)
        setOptionBoolean(SpeechEngineDefines.PARAMS_KEY_ASR_DISABLE_END_PUNC_BOOL, true)
        setOptionBoolean(SpeechEngineDefines.PARAMS_KEY_ASR_AUTO_STOP_BOOL, config.autoStop)
        setOptionString(
            SpeechEngineDefines.PARAMS_KEY_ASR_RESULT_TYPE_STRING,
            if (config.recognitionType == AsrParams.RecognitionType.VALUES.LONG) {
                SpeechEngineDefines.ASR_RESULT_TYPE_SINGLE
            } else {
                SpeechEngineDefines.ASR_RESULT_TYPE_FULL
            }
        )

        if (config.hotwords.hotwords.isNotEmpty()) {
            sendDirective(
                SpeechEngineDefines.DIRECTIVE_UPDATE_ASR_HOTWORDS,
                gson.toJson(config.hotwords)
            )
        }
    }

    private fun handleSpeechMessage(
        type: Int,
        data: ByteArray,
        len: Int,
    ) {
        val stdData = String(data)
        when (type) {
            SpeechEngineDefines.MESSAGE_TYPE_ENGINE_START -> {
                logger.debug(TAG, "engine start success. data: $stdData")
            }

            SpeechEngineDefines.MESSAGE_TYPE_ENGINE_STOP -> {
                logger.debug(TAG, "engine stop success. data: $stdData")
            }

            SpeechEngineDefines.MESSAGE_TYPE_ENGINE_ERROR -> {
                logger.error(TAG, "engine error. data: $stdData")
            }

            SpeechEngineDefines.MESSAGE_TYPE_CONNECTION_CONNECTED -> {
                logger.debug(TAG, "connection connected. data: $stdData")
            }

            SpeechEngineDefines.MESSAGE_TYPE_PARTIAL_RESULT -> {
                logger.debug(TAG, "ASR partial result. data: $stdData")
                speechAsrResult(stdData, false)
            }

            SpeechEngineDefines.MESSAGE_TYPE_FINAL_RESULT -> {
                logger.debug(TAG, "ASR final result. data: $stdData")
                speechAsrResult(stdData, true)
            }

            SpeechEngineDefines.MESSAGE_TYPE_VOLUME_LEVEL -> {
                logger.debug(TAG, "Callback: volume level. data: $stdData")
            }
        }
    }

    private fun speechAsrResult(data: String, isFinal: Boolean) {
        logger.debug(TAG, "speechAsrResult: $data, isFinal: $isFinal")
    }

    override fun release() {
        engine?.destroyEngine()
        engine = null
    }

    private fun listen(listener: AsrListener) {
        if (engine == null) {
            val errorMessage = "ASR instance not initialized"
            logger.error(TAG, errorMessage)
        }

        engine?.apply {
            sendDirective(SpeechEngineDefines.DIRECTIVE_SYNC_STOP_ENGINE, "")
            sendDirective(SpeechEngineDefines.DIRECTIVE_START_ENGINE, "")
            this@VolcengineAsr.asrListener = listener
        }
    }

    override suspend fun listen(onHeard: ((String) -> Unit)?): Asr.Result {
        this.onHeard = onHeard
        isListening = true

        return suspendCoroutine { continuation ->
            CoroutineScope(Dispatchers.IO).launch {
                listen(object :
                    AsrListener {
                    private fun resumeWithoutComplain(result: Asr.Result) {
                        try {
                            continuation.resume(
                                result
                            )
                        } catch (_: Exception) {
                        } finally {
                            isListening = false
                            asrListener = null
                            this@VolcengineAsr.onHeard = null
                        }
                    }

                    override fun onResult(result: String) {
                        resumeWithoutComplain(
                            Asr.Result(
                                isSuccess = true,
                                heardContent = result
                            )
                        )
                    }

                    override fun onError(error: String) {
                        resumeWithoutComplain(
                            Asr.Result(
                                isSuccess = false,
                                errorMessage = error
                            )
                        )
                    }
                })
            }
        }

        return Asr.Result(false, "")
    }

    override fun isListening(): Boolean {
        return isListening
    }

    override fun stop() {
        if (engine == null) {
            logger.error(TAG, "ASR instance not initialized")
            return
        }

        engine?.apply {
            sendDirective(SpeechEngineDefines.DIRECTIVE_FINISH_TALKING, "")
            sendDirective(SpeechEngineDefines.DIRECTIVE_STOP_ENGINE, "")
        }
    }

    companion object {
        private const val TAG = "VolcengineAsr"

        fun create(
            context: Context,
            params: Map<String, Any> = emptyMap(),
            logger: Logger = DefaultLogger(),
        ): Asr {
            val asrConfig = AsrConfig.create(context, params)
            return VolcengineAsr(context, logger, asrConfig)
        }
    }
}