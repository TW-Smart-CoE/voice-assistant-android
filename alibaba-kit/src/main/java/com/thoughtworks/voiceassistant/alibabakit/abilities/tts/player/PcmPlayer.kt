package com.thoughtworks.voiceassistant.alibabakit.abilities.tts.player

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import com.thoughtworks.voiceassistant.alibabakit.abilities.tts.TtsConfig
import com.thoughtworks.voiceassistant.core.logger.Logger
import com.thoughtworks.voiceassistant.core.logger.debug

class PcmPlayer(
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
                AudioAttributes.Builder()
                    .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
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
        private const val TAG = "PcmPlayer"
    }
}