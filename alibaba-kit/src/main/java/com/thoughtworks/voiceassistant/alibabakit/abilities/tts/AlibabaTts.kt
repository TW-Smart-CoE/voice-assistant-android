package com.thoughtworks.voiceassistant.alibabakit.abilities.tts

import android.content.Context
import android.os.Build
import android.text.TextUtils
import com.alibaba.idst.nui.CommonUtils
import com.alibaba.idst.nui.Constants
import com.alibaba.idst.nui.INativeTtsCallback
import com.alibaba.idst.nui.NativeNui
import com.thoughtworks.voiceassistant.alibabakit.abilities.tts.player.PlayerManager
import com.thoughtworks.voiceassistant.alibabakit.abilities.tts.player.TtsFileWriter
import com.thoughtworks.voiceassistant.alibabakit.abilities.tts.player.TtsStreamData
import com.thoughtworks.voiceassistant.core.abilities.Tts
import com.thoughtworks.voiceassistant.core.logger.DefaultLogger
import com.thoughtworks.voiceassistant.core.logger.Logger
import com.thoughtworks.voiceassistant.core.logger.debug
import com.thoughtworks.voiceassistant.core.logger.error
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class AlibabaTts private constructor(
    private val context: Context,
    private val logger: Logger,
    private val config: TtsConfig,
) : Tts {
    private val ttsInstance = NativeNui(Constants.ModeType.MODE_TTS)
    private var isInit = false
    private val taskIdManager = TaskIdManager()
    private val ttsFileWriter = TtsFileWriter(logger, config)
    private val playerManager = PlayerManager(logger, config) {
        logger.debug(TAG, "MP3 player end")
        listener?.onPlayEnd()
        listener = null
    }
    private var listener: Tts.Listener? = null
    private var wavHeaderToBeRemove: Boolean = false

    private val ttsInitCallback = object : INativeTtsCallback {
        override fun onTtsEventCallback(
            event: INativeTtsCallback.TtsEvent,
            taskId: String,
            resultCode: Int,
        ) {
            logger.debug(
                TAG,
                "onTtsEventCallback event:$event taskId:$taskId resultCode:$resultCode"
            )

            when (event) {
                INativeTtsCallback.TtsEvent.TTS_EVENT_START -> {
                    onTtsStart()
                }

                INativeTtsCallback.TtsEvent.TTS_EVENT_END -> {
                    onTtsEnd()
                }

                INativeTtsCallback.TtsEvent.TTS_EVENT_CANCEL -> {
                    onTtsCancel()
                }

                INativeTtsCallback.TtsEvent.TTS_EVENT_ERROR -> {
                    onTtsError(resultCode)
                }

                else -> {}
            }
        }

        private fun onTtsError(resultCode: Int) {
            val errorMsg = ttsInstance.getparamTts("error_msg")
            logger.error(TAG, "error_code: $resultCode err_msg: $errorMsg")
            listener?.onError(errorMsg)
            listener = null
        }

        private fun onTtsCancel() {
            finishFileWrite()
            wavHeaderToBeRemove = false
            listener?.onPlayCancel()
            listener = null
        }

        private fun onTtsEnd() {
            finishFileWrite()
            wavHeaderToBeRemove = false
            playerManager.playSoundEnd()

            if (!config.playSound ||
                config.encodeType == TtsParams.EncodeType.VALUES.WAV
            ) {
                listener?.onPlayEnd()
                listener = null
            }
        }

        private fun onTtsStart() {
            wavHeaderToBeRemove = Build.VERSION.SDK_INT > Build.VERSION_CODES.Q
            if (config.ttsFilePath.isNotEmpty()) {
                ttsFileWriter.createFile()
            }
            listener?.onPlayStart()
        }

        private fun finishFileWrite() {
            ttsFileWriter.closeFile()
            if (config.ttsFilePath.isNotEmpty()) {
                logger.debug(TAG, "TTS file saved at: ${config.ttsFilePath}")
                listener?.onTTSFileSaved(config.ttsFilePath)
            }
        }

        override fun onTtsDataCallback(info: String, infoLen: Int, data: ByteArray) {
            if (data.isNotEmpty()) {
                val ttsData = TtsStreamData(info, infoLen, data)

                val newAudioData: ByteArray =
                    if (config.encodeType == TtsParams.EncodeType.VALUES.WAV && wavHeaderToBeRemove) {
                        ttsData.data.copyOfRange(
                            44,
                            ttsData.data.size
                        )
                    } else ttsData.data
                wavHeaderToBeRemove = false
                playerManager.writeSoundData(newAudioData)

                if (config.ttsFilePath.isNotEmpty()) {
                    ttsFileWriter.writeData(ttsData.data)
                }
            }
        }

        override fun onTtsVolCallback(vol: Int) {
        }
    }

    override suspend fun initialize() {
        if (isInit) {
            logger.debug(TAG, "TTS instance has been initialized")
            return
        }

        if (CommonUtils.copyAssetsData(context)) {
            logger.debug(TAG, "copy assets data done")
        } else {
            logger.error(TAG, "copy assets failed")
            return
        }

        val ticket = config.generateTicket(context, logger)
        val initResult = ttsInstance.tts_initialize(
            ttsInitCallback,
            ticket,
            Constants.LogLevel.LOG_LEVEL_DEBUG,
            false
        )

        if (initResult == Constants.NuiResultCode.SUCCESS) {
            ttsInstance.setparamTts("font_name", config.fontName)
            ttsInstance.setparamTts("sample_rate", config.sampleRate.toString())
            ttsInstance.setparamTts("enable_subtitle", config.enableSubtitle)
            ttsInstance.setparamTts("encode_type", config.encodeType)
            logger.debug(TAG, "TTS instance initialized successfully")
            isInit = true
        }
    }

    override fun release() {
        if (isInit) {
            ttsInstance.tts_release()
            isInit = false
            logger.debug(TAG, "TTS instance has been released")
        }
    }

    override suspend fun speak(text: String, params: Map<String, Any>, listener: Tts.Listener) {
        if (!isInit) {
            logger.error(TAG, "TTS is not initialized!")
            return
        }

        // Wait for the previous tts play clear
        delay(config.stopAndStartDelay.toLong())

        if (TextUtils.isEmpty(text)) {
            logger.debug(TAG, "Text is empty")
            listener.onPlayEnd()
            return
        }

        playerManager.playSoundBegin()

        val charNum = ttsInstance.getUtf8CharsNum(text)

        val ttsVersion = if (charNum > MAX_TEXT_NUM) 1 else 0
        ttsInstance.setparamTts("tts_version", ttsVersion.toString())
        if (params.isNotEmpty()) {
            val fontName = params["font_name"]
            fontName?.let {
                if (it.toString().endsWith("_emo")) {
                    val emotion = params["emotion"]?.toString() ?: SSMLEmotions.NEUTRAL
                    val intensity = params["intensity"]?.toString()?.toFloat() ?: 1.0f
                    val ssml = formatSSML(it.toString(), emotion, intensity, text)
                    ttsInstance.startTts("1", taskIdManager.generateTaskId(), ssml)
                }
            }
        } else {
            ttsInstance.startTts("1", taskIdManager.generateTaskId(), text)
        }

        this.listener = listener
    }

    override suspend fun speak(
        text: String,
        params: Map<String, Any>,
    ): Tts.Result {
        return suspendCoroutine { continuation ->
            CoroutineScope(Dispatchers.IO).launch {
                speak(text, params, object : Tts.Listener {
                    private fun resumeWithoutComplain(result: Tts.Result) {
                        try {
                            continuation.resume(
                                result
                            )
                        } catch (_: Exception) {
                        }
                    }

                    override fun onPlayStart() {
                    }

                    override fun onPlayEnd() {
                        resumeWithoutComplain(
                            Tts.Result(
                                success = true,
                                ttsFilePath = config.ttsFilePath
                            )
                        )
                    }

                    override fun onPlayCancel() {
                        resumeWithoutComplain(
                            Tts.Result(
                                success = false,
                                errorMessage = "cancel"
                            )
                        )
                    }

                    override fun onError(errorMessage: String) {
                        resumeWithoutComplain(
                            Tts.Result(
                                success = false,
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

    override fun stop() {
        if (!isInit) {
            logger.error(TAG, "TTS is not initialized!")
            return
        }

        taskIdManager.getTaskId().let {
            if (it.isNotEmpty()) {
                ttsInstance.cancelTts(it)
            }
        }
        ttsInstance.stopDialog()
        taskIdManager.clearTaskId()
        playerManager.stopPlaySound()
        this.listener = null
    }

    private fun formatSSML(
        fontName: String,
        emotion: String,
        intensity: Float,
        text: String,
    ): String {
        return "<speak voice=\"$fontName\">\n" +
                "    <emotion category=\"$emotion\" intensity=\"$intensity\">$text</emotion>\n" +
                "</speak>"
    }

    companion object {
        private const val TAG = "AlibabaTts"
        private const val MAX_TEXT_NUM = 300

        fun create(
            context: Context,
            params: Map<String, Any> = emptyMap(),
            logger: Logger = DefaultLogger(),
        ): Tts {
            val ttsConfig = TtsConfig.create(context, params)
            return AlibabaTts(context, logger, ttsConfig)
        }
    }
}