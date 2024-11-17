package com.thoughtworks.voiceassistant.alibabakit.abilities.tts

import android.content.Context
import android.os.Build
import android.os.SystemClock
import android.text.TextUtils
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


class AlibabaTts private constructor(
    private val context: Context,
    private val logger: Logger,
    private val ttsConfig: TtsConfig,
) : Tts {
    private val ttsInstance = NativeNui(Constants.ModeType.MODE_TTS)
    private var isInit = false
    private val taskIdManager = TaskIdManager()
    private val ttsFileWriter = TtsFileWriter(logger, ttsConfig)
    private val playerManager = PlayerManager(logger, ttsConfig) {
        logger.debug(TAG, "MP3 player end")
        listener?.onPlayEnd()
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
                    logger.debug(TAG, "TTS_EVENT_START")
                    listener?.onPlayStart()
                    wavHeaderToBeRemove = Build.VERSION.SDK_INT > Build.VERSION_CODES.Q
                    if (ttsConfig.ttsFilePath.isNotEmpty()) {
                        ttsFileWriter.createFile()
                    }
                }

                INativeTtsCallback.TtsEvent.TTS_EVENT_END -> {
                    logger.debug(TAG, "TTS_EVENT_END")
                    finishFileWrite()
                    wavHeaderToBeRemove = false
                    playerManager.playSoundEnd()

                    if (!ttsConfig.playSound ||
                        ttsConfig.encodeType == AlibabaTtsParams.EncodeType.VALUES.WAV
                    ) {
                        listener?.onPlayEnd()
                    }
                }

                INativeTtsCallback.TtsEvent.TTS_EVENT_CANCEL -> {
                    logger.debug(TAG, "TTS_EVENT_CANCEL")
                    finishFileWrite()
                    wavHeaderToBeRemove = false
                    listener?.onPlayCancel()
                }

                INativeTtsCallback.TtsEvent.TTS_EVENT_ERROR -> {
                    val errorMsg = ttsInstance.getparamTts("error_msg")
                    logger.error(TAG, "TTS_EVENT_ERROR error_code:$resultCode err_msg:$errorMsg")
                    listener?.onError(errorMsg)
                }

                else -> {}
            }
        }

        private fun finishFileWrite() {
            ttsFileWriter.closeFile()
            if (ttsConfig.ttsFilePath.isNotEmpty()) {
                logger.debug(TAG, "TTS file saved at: ${ttsConfig.ttsFilePath}")
                listener?.onTTSFileSaved(ttsConfig.ttsFilePath)
            }
        }

        override fun onTtsDataCallback(info: String, infoLen: Int, data: ByteArray) {
            if (data.isNotEmpty()) {
                val ttsData = TtsStreamData(info, infoLen, data)

                val newAudioData: ByteArray =
                    if (ttsConfig.encodeType == AlibabaTtsParams.EncodeType.VALUES.WAV && wavHeaderToBeRemove) {
                        ttsData.data.copyOfRange(
                            44,
                            ttsData.data.size
                        )
                    } else ttsData.data
                wavHeaderToBeRemove = false
                playerManager.writeSoundData(newAudioData)

                if (ttsConfig.ttsFilePath.isNotEmpty()) {
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

        val ticket = ttsConfig.generateTicket(context, logger)
        val initResult = ttsInstance.tts_initialize(
            ttsInitCallback,
            ticket,
            Constants.LogLevel.LOG_LEVEL_DEBUG,
            false
        )

        if (initResult == Constants.NuiResultCode.SUCCESS) {
            ttsInstance.setparamTts("font_name", ttsConfig.fontName)
            ttsInstance.setparamTts("sample_rate", ttsConfig.sampleRate.toString())
            ttsInstance.setparamTts("enable_subtitle", ttsConfig.enableSubtitle)
            ttsInstance.setparamTts("encode_type", ttsConfig.encodeType)
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

    override suspend fun play(text: String, params: Map<String, Any>, listener: Tts.Listener?) {
        if (!isInit) {
            logger.error(TAG, "TTS is not initialized!")
            return
        }

        // Wait for the previous tts play clear
        SystemClock.sleep(ttsConfig.stopAndStartDelay.toLong())

        if (TextUtils.isEmpty(text)) {
            logger.debug(TAG, "Text is empty")
            listener?.onPlayEnd()
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

    override fun stopPlay() {
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