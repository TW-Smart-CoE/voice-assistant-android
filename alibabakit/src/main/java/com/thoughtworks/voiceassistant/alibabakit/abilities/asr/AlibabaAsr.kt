package com.thoughtworks.voiceassistant.alibabakit.abilities.asr

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import androidx.core.app.ActivityCompat
import com.alibaba.idst.nui.AsrResult
import com.alibaba.idst.nui.CommonUtils
import com.alibaba.idst.nui.Constants
import com.alibaba.idst.nui.INativeNuiCallback
import com.alibaba.idst.nui.KwsResult
import com.alibaba.idst.nui.NativeNui
import com.google.gson.Gson
import com.thoughtworks.voiceassistant.alibabakit.abilities.asr.models.ASRResult
import com.thoughtworks.voiceassistant.core.abilities.Asr
import com.thoughtworks.voiceassistant.core.logger.DefaultLogger
import com.thoughtworks.voiceassistant.core.logger.Logger
import com.thoughtworks.voiceassistant.core.logger.debug
import com.thoughtworks.voiceassistant.core.logger.error
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AlibabaAsr(
    private val context: Context,
    private val logger: Logger,
    private val config: AsrConfig,
) : Asr {
    interface AsrListener {
        fun onResult(text: String) {}
        fun onError(errorMessage: String) {}
        fun onVolumeChanged(volume: Float) {}
    }

    private var isInit = false
    private val nuiInstance = NativeNui()
    private lateinit var audioRecorder: AudioRecord
    private val gson = Gson()
    var asrListener: AsrListener? = null
    var onHeard: ((String) -> Unit)? = null

    private val nuiCallback = object : INativeNuiCallback {
        private fun logEvent(event: Constants.NuiEvent, resultCode: Int) {
            logger.debug(
                TAG,
                "onNuiEventCallback event:$event resultCode:$resultCode"
            )
        }

        override fun onNuiEventCallback(
            event: Constants.NuiEvent,
            resultCode: Int,
            arg2: Int,
            kwsResult: KwsResult?,
            asrResult: AsrResult?,
        ) {
            when (event) {
                Constants.NuiEvent.EVENT_ASR_RESULT -> {
                    asrResult?.let {
                        val result = gson.fromJson(it.asrResult, ASRResult::class.java)
                        result.payload?.result?.let { payload ->
                            logger.debug(TAG, "RESULT: $payload")
                            asrListener?.onResult(payload)
                            onHeard?.invoke(payload)
                        }
                    }
                }

                Constants.NuiEvent.EVENT_ASR_PARTIAL_RESULT -> {
                }

                Constants.NuiEvent.EVENT_SENTENCE_END -> {
                    logEvent(event, resultCode)
                    asrResult?.let {
                        val result = gson.fromJson(it.asrResult, ASRResult::class.java)
                        result.payload?.result?.let { payload ->
                            logger.debug(TAG, "SENTENCE: $payload")
                            onHeard?.invoke(payload)
                        }
                    }
                }

                Constants.NuiEvent.EVENT_ASR_ERROR -> {
                    logger.error(TAG, "EVENT_ASR_ERROR: $resultCode")
                    asrListener?.onError("EVENT_ASR_ERROR: $resultCode")
                    clearListeners()
                }

                Constants.NuiEvent.EVENT_VAD_START -> {
                    logEvent(event, resultCode)
                }

                Constants.NuiEvent.EVENT_VAD_END -> {
                    logEvent(event, resultCode)
                }

                Constants.NuiEvent.EVENT_VAD_TIMEOUT -> {
                    logEvent(event, resultCode)
                }

                Constants.NuiEvent.EVENT_DIALOG_EX -> {
                    logger.debug(TAG, "onDialogEx: ${asrResult?.asrResult}")
                    asrListener?.onResult("")
                }

                else -> {
                    logEvent(event, resultCode)
                }
            }
        }

        override fun onNuiNeedAudioData(buffer: ByteArray, len: Int): Int {
            var ret = 0
            if (audioRecorder.state != AudioRecord.STATE_INITIALIZED) {
                logger.error(TAG, "audio recorder not init")
                return -1
            }
            ret = audioRecorder.read(buffer, 0, len)
            return ret
        }

        override fun onNuiAudioStateChanged(state: Constants.AudioState?) {
            when (state) {
                Constants.AudioState.STATE_OPEN -> {
                    audioRecorder.startRecording()
                }

                Constants.AudioState.STATE_CLOSE -> {
                    audioRecorder.release()
                }

                Constants.AudioState.STATE_PAUSE -> {
                    audioRecorder.stop()
                }

                else -> {}
            }
        }

        override fun onNuiAudioRMSChanged(p0: Float) {
            asrListener?.onVolumeChanged(p0)
        }

        override fun onNuiVprEventCallback(p0: Constants.NuiVprEvent?) {
            logger.debug(TAG, "onNuiVprEventCallback event $p0")
        }
    }

    @SuppressLint("MissingPermission")
    override suspend fun initialize() {
        if (isInit) {
            logger.debug(TAG, "ASR instance has been initialized")
            return
        }

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            logger.error(TAG, "Manifest.permission.RECORD_AUDIO does not granted")
            return
        }

        val ticket = config.generateTicket(context, logger)

        audioRecorder = AudioRecord(
            config.getMediaRecorderAudioSource(), SAMPLE_RATE,
            AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, WAVE_FRAME_SIZE * 4
        )

        if (CommonUtils.copyAssetsData(context)) {
            logger.debug(TAG, "copy assets data done")
        } else {
            logger.error(TAG, "copy assets failed")
            return
        }

        val ret: Int = nuiInstance.initialize(
            nuiCallback,
            ticket,
            Constants.LogLevel.LOG_LEVEL_DEBUG,
            false
        )

        if (ret != Constants.NuiResultCode.SUCCESS) {
            logger.error(TAG, "ASR instance initialize failed: result = $ret")
            return
        }

        nuiInstance.setParams(config.genParams())
        isInit = true

        logger.debug(TAG, "ASR instance initialized successfully")
    }

    override fun release() {
        if (isInit) {
            nuiInstance.release()
            isInit = false
        }
    }

    private fun listen(listener: AsrListener) {
        if (!isInit) {
            logger.error(TAG, "ASR instance not initialized")
            return
        }

        val ret: Int = nuiInstance.startDialog(
            config.getAliVadMode(),
            config.genDialogParams(),
        )

        logger.debug(TAG, "start done with $ret")
        if (ret != 0) {
            logger.error(TAG, "start dialog failed")
            return
        }

        this.asrListener = listener
    }

    override suspend fun listen(onHeard: ((String) -> Unit)?): Asr.Result {
        this.onHeard = onHeard

        return suspendCoroutine { continuation ->
            CoroutineScope(Dispatchers.IO).launch {
                listen(object : AsrListener {
                    private fun resumeWithoutComplain(result: Asr.Result) {
                        try {
                            continuation.resume(
                                result
                            )
                        } catch (_: Exception) {
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

                    override fun onVolumeChanged(volume: Float) {
                        // no-op
                    }
                })
            }
        }
    }

    override fun stop() {
        if (!isInit) {
            logger.error(TAG, "ASR instance not initialized")
            return
        }

        asrListener?.onResult("")
        clearListeners()
        nuiInstance.stopDialog()
    }

    private fun clearListeners() {
        asrListener = null
        onHeard = null
    }

    companion object {
        private const val TAG = "AlibabaAsr"
        private const val WAVE_FRAME_SIZE =
            20 * 2 * 1 * 16000 / 1000 //20ms audio for 16k/16bit/mono
        private const val SAMPLE_RATE = 16000

        fun create(
            context: Context,
            params: Map<String, Any> = emptyMap(),
            logger: Logger = DefaultLogger(),
        ): Asr {
            val asrConfig = AsrConfig.create(context, params)
            return AlibabaAsr(context, logger, asrConfig)
        }
    }
}