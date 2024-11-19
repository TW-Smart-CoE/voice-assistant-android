package com.thoughtworks.voiceassistant.alibabakit.abilities.tts.player

import android.media.MediaPlayer
import com.thoughtworks.voiceassistant.alibabakit.abilities.tts.AlibabaTts
import com.thoughtworks.voiceassistant.alibabakit.abilities.tts.TtsConfig
import com.thoughtworks.voiceassistant.core.logger.Logger
import com.thoughtworks.voiceassistant.core.logger.debug
import java.io.IOException

class Mp3Player(
    private val logger: Logger,
    private val ttsConfig: TtsConfig,
    private val onPlayEnd: () -> Unit,
) {
    private var mediaPlayer: MediaPlayer? = null

    fun play() {
        if (ttsConfig.ttsFilePath.isEmpty()) {
            logger.debug(TAG, "MP3 file path is empty")
            return
        }

        stopPlay()

        try {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(ttsConfig.ttsFilePath)
                prepare() // or prepareAsync() for streaming
                setOnCompletionListener { onPlayEndHandler(false) }
                setOnErrorListener { _, _, _ -> onPlayEndHandler(false) }
                start()
            }
            logger.debug(TAG, "Mp3Player started")
        } catch (e: IOException) {
            logger.debug(TAG, "Error playing MP3: ${e.message}")
        }
    }

    fun stop() {
        stopPlay()
        onPlayEndHandler(true)
    }

    private fun stopPlay() {
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null
    }

    private fun onPlayEndHandler(stopByForce: Boolean): Boolean {
        logger.debug(TAG, "Mp3Player stopped by force: $stopByForce")
        onPlayEnd()
        return true
    }

    companion object {
        private const val TAG = "${AlibabaTts.TAG}.Mp3Player"
    }
}