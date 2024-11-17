package com.thoughtworks.voiceassistant.alibabakit.abilities.tts.player

import android.media.MediaPlayer
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

        stop()

        try {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(ttsConfig.ttsFilePath)
                prepare() // or prepareAsync() for streaming
                setOnCompletionListener { onPlayEnd() }
                setOnErrorListener { _, _, _ -> onErrorEnd() }
                start()
            }
            logger.debug(TAG, "Mp3Player started")
        } catch (e: IOException) {
            logger.debug(TAG, "Error playing MP3: ${e.message}")
        }
    }

    fun stop() {
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null
        logger.debug(TAG, "Mp3Player stopped")
        onErrorEnd()
    }

    private fun onErrorEnd(): Boolean {
        onPlayEnd()
        return true
    }

    companion object {
        private const val TAG = "Mp3Player"
    }
}