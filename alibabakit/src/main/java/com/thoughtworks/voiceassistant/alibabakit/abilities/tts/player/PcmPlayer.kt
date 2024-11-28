package com.thoughtworks.voiceassistant.alibabakit.abilities.tts.player

import android.content.Context
import android.media.AudioFormat
import android.media.AudioTrack
import com.thoughtworks.voiceassistant.alibabakit.abilities.tts.AlibabaTts
import com.thoughtworks.voiceassistant.alibabakit.abilities.tts.TtsConfig
import com.thoughtworks.voiceassistant.alibabakit.abilities.tts.TtsParams
import com.thoughtworks.voiceassistant.core.logger.Logger
import com.thoughtworks.voiceassistant.core.logger.debug
import com.thoughtworks.voiceassistant.core.utils.AudioUtils

class PcmPlayer(
    private val context: Context,
    private val logger: Logger,
    ttsConfig: TtsConfig,
) {
    private val encode = AudioFormat.ENCODING_PCM_16BIT
    private val audioTrack: AudioTrack
    private val sampleRate = ttsConfig.sampleRate
    private val minBufferSize: Int = AudioTrack.getMinBufferSize(
        ttsConfig.sampleRate,
        AudioFormat.CHANNEL_OUT_MONO,
        encode
    ) * 2

    init {
        audioTrack = AudioTrack.Builder()
            .setAudioAttributes(
                AudioUtils(context).buildAudioAttributes(ttsConfig.playMode == TtsParams.PlayMode.VALUES.COMMUNICATION)
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .setEncoding(encode)
                    .setSampleRate(sampleRate)
                    .build()
            )
            .setBufferSizeInBytes(minBufferSize)
            .setTransferMode(AudioTrack.MODE_STREAM)
            .build()
    }

    fun start() {
        audioTrack.play()
        logger.debug(TAG, "PcmPlayer start")
    }

    fun writeData(data: ByteArray) {
        audioTrack.write(data, 0, data.size)
    }

    fun stop() {
        audioTrack.apply {
            flush()
            pause()
            stop()
            logger.debug(TAG, "PcmPlayer stop")
        }
    }

    companion object {
        private const val TAG = "${AlibabaTts.TAG}.PcmPlayer"
    }
}