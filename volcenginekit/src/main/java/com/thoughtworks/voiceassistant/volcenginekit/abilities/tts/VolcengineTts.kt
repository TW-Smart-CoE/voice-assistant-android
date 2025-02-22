package com.thoughtworks.voiceassistant.volcenginekit.abilities.tts

import android.app.Application
import android.content.Context
import android.text.TextUtils
import com.bytedance.speech.speechengine.SpeechEngine
import com.bytedance.speech.speechengine.SpeechEngineDefines
import com.bytedance.speech.speechengine.SpeechEngineGenerator
import com.thoughtworks.voiceassistant.core.abilities.Tts
import com.thoughtworks.voiceassistant.core.logger.DefaultLogger
import com.thoughtworks.voiceassistant.core.logger.Logger
import com.thoughtworks.voiceassistant.core.logger.debug
import com.thoughtworks.voiceassistant.core.logger.error
import com.thoughtworks.voiceassistant.core.logger.warn
import com.thoughtworks.voiceassistant.core.utils.DeviceUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class VolcengineTts(
    private val context: Context,
    private val logger: Logger,
    private val config: TtsConfig,
) : Tts {
    interface TtsListener {
        fun onPlayStart()
        fun onPlayEnd()
        fun onPlayCancel()
        fun onError(errorMessage: String)
        fun onTTSFileSaved(ttsFilePath: String)
    }

    private var engine: SpeechEngine? = null
    private var ttsListener: TtsListener? = null
    private var isSpeaking = false

    override suspend fun initialize(): Boolean {
        if (engine != null) {
            logger.debug(TAG, "TTS instance is already initialized")
            return true
        }

        SpeechEngineGenerator.PrepareEnvironment(context, context.applicationContext as Application)
        engine = SpeechEngineGenerator.getInstance().apply {
            createEngine()
            setContext(context)
            configureParams()
            val ret = initEngine()
            if (ret != SpeechEngineDefines.ERR_NO_ERROR) {
                val errMessage = "Init Engine Failed: $ret"
                logger.error(TAG, errMessage)
                release()
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

            logger.debug(TAG, "TTS instance initialized successfully")
        }

        return true
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

            SpeechEngineDefines.MESSAGE_TYPE_TTS_SYNTHESIS_BEGIN -> {
                logger.debug(TAG, "tts synthesis begin.")
            }

            SpeechEngineDefines.MESSAGE_TYPE_TTS_SYNTHESIS_END -> {
                logger.debug(TAG, "tts synthesis end.")
            }

            SpeechEngineDefines.MESSAGE_TYPE_TTS_START_PLAYING -> {
                logger.debug(TAG, "tts playing start.")
                isSpeaking = true
                ttsListener?.onPlayStart()
            }

            SpeechEngineDefines.MESSAGE_TYPE_TTS_FINISH_PLAYING -> {
                logger.debug(TAG, "tts playing finish.")
                isSpeaking = false
                ttsListener?.onPlayEnd()
                ttsListener = null
            }
        }
    }

    private fun SpeechEngine.configureParams() {
        setOptionString(
            SpeechEngineDefines.PARAMS_KEY_ENGINE_NAME_STRING,
            SpeechEngineDefines.TTS_ENGINE
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
            SpeechEngineDefines.PARAMS_KEY_TTS_SCENARIO_STRING,
            SpeechEngineDefines.TTS_SCENARIO_TYPE_NORMAL
        )
        setOptionInt(
            SpeechEngineDefines.PARAMS_KEY_TTS_WORK_MODE_INT,
            SpeechEngineDefines.TTS_WORK_MODE_ONLINE
        )
        setOptionInt(SpeechEngineDefines.PARAMS_KEY_TTS_CONN_TIMEOUT_INT, 12000)
        setOptionInt(SpeechEngineDefines.PARAMS_KEY_TTS_RECV_TIMEOUT_INT, 8000)
        setOptionString(
            SpeechEngineDefines.PARAMS_KEY_TTS_ADDRESS_STRING,
            "wss://openspeech.bytedance.com"
        )
        setOptionString(SpeechEngineDefines.PARAMS_KEY_TTS_URI_STRING, "/api/v1/tts/ws_binary")
        setOptionString(SpeechEngineDefines.PARAMS_KEY_TTS_CLUSTER_STRING, config.cluster)
        setOptionString(SpeechEngineDefines.PARAMS_KEY_TTS_VOICE_ONLINE_STRING, config.voiceName)
        setOptionString(
            SpeechEngineDefines.PARAMS_KEY_TTS_VOICE_TYPE_ONLINE_STRING,
            config.voiceType
        )
        setOptionDouble(
            SpeechEngineDefines.PARAMS_KEY_TTS_PITCH_RATIO_DOUBLE,
            config.voicePitch.toDouble()
        )
        setOptionDouble(SpeechEngineDefines.PARAMS_KEY_TTS_VOLUME_RATIO_DOUBLE, 1.0)
        setOptionDouble(
            SpeechEngineDefines.PARAMS_KEY_TTS_SPEED_RATIO_DOUBLE,
            config.voiceSpeed.toDouble()
        )
        setOptionInt(
            SpeechEngineDefines.PARAMS_KEY_AUDIO_STREAM_TYPE_INT,
            SpeechEngineDefines.AUDIO_STREAM_TYPE_MEDIA
        )
    }

    override fun release() {
        if (isSpeaking()) {
            stop()
        }

        engine?.destroyEngine()
        engine = null
        logger.debug(TAG, "TTS instance has been released")
    }

    private fun speak(
        text: String,
        params: Map<String, Any>,
        listener: TtsListener,
    ) {
        if (engine == null) {
            val errorMessage = "TTS is not initialized!"
            logger.error(
                TAG,
                errorMessage
            )
            listener.onError(errorMessage)
            return
        }

        if (TextUtils.isEmpty(text)) {
            val errorMessage = "Text is empty"
            logger.warn(TAG, errorMessage)
            listener.onPlayEnd()
            return
        }

        ttsListener = listener
        engine?.apply {
            setOptionString(SpeechEngineDefines.PARAMS_KEY_TTS_TEXT_STRING, text)
            params[SpeakParams.Emotion.KEY]?.toString()?.let {
                if (it != SpeakParams.Emotion.VALUES.NEUTRAL) {
                    setOptionString(SpeechEngineDefines.PARAMS_KEY_TTS_EMOTION_STRING, it)
                }
            }
            sendDirective(SpeechEngineDefines.DIRECTIVE_START_ENGINE, "")
        }
    }

    override suspend fun speak(
        text: String,
        params: Map<String, Any>,
    ): Tts.Result {
        isSpeaking = true
        return suspendCoroutine { continuation ->
            CoroutineScope(Dispatchers.IO).launch {
                speak(text, params, object :
                    TtsListener {
                    private fun resumeWithoutComplain(result: Tts.Result) {
                        try {
                            continuation.resume(
                                result
                            )
                        } catch (_: Exception) {
                        } finally {
                            isSpeaking = false
                        }
                    }

                    override fun onPlayStart() {
                    }

                    override fun onPlayEnd() {
                        resumeWithoutComplain(
                            Tts.Result(
                                isSuccess = true,
                                ttsFilePath = "",//config.ttsFilePath
                            )
                        )
                    }

                    override fun onPlayCancel() {
                        resumeWithoutComplain(
                            Tts.Result(
                                isSuccess = false,
                                errorMessage = "cancel"
                            )
                        )
                    }

                    override fun onError(errorMessage: String) {
                        resumeWithoutComplain(
                            Tts.Result(
                                isSuccess = false,
                                errorMessage = errorMessage,
                            )
                        )
                    }

                    override fun onTTSFileSaved(ttsFilePath: String) {
                    }
                })
            }
        }
    }

    override fun isSpeaking(): Boolean {
        return isSpeaking
    }

    override fun stop() {
        if (engine == null) {
            logger.warn(TAG, "TTS is not initialized!")
            return
        }

        if (!isSpeaking) {
            logger.warn(TAG, "TTS is not speaking!")
            return
        }

        engine?.apply {
            sendDirective(SpeechEngineDefines.DIRECTIVE_SYNC_STOP_ENGINE, "")
        }
        ttsListener?.onPlayCancel()
        ttsListener = null
    }

    companion object {
        private const val TAG = "VolcengineTts"

        fun create(
            context: Context,
            params: Map<String, Any> = emptyMap(),
            logger: Logger = DefaultLogger(),
        ): Tts {
            val ttsConfig = TtsConfig.create(context, params)
            return VolcengineTts(context, logger, ttsConfig)
        }
    }
}