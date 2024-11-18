package com.thoughtworks.voiceassistant.alibabakit.abilities.tts.player

import com.thoughtworks.voiceassistant.alibabakit.abilities.tts.TtsParams
import com.thoughtworks.voiceassistant.alibabakit.abilities.tts.TtsConfig
import com.thoughtworks.voiceassistant.core.logger.Logger
import com.thoughtworks.voiceassistant.core.logger.debug
import com.thoughtworks.voiceassistant.core.logger.error

class PlayerManager(
    private val logger: Logger,
    private val ttsConfig: TtsConfig,
    onMp3PlayEnd: () -> Unit,
) {
    private val pcmPlayer = PcmPlayer(logger, ttsConfig)
    private val mp3Player = Mp3Player(logger, ttsConfig, onMp3PlayEnd)

    fun playSoundBegin() {
        if (!ttsConfig.playSound) {
            logger.debug(TAG, "playSound is false, skip playSoundBegin")
            return
        }

        when (ttsConfig.encodeType) {
            TtsParams.EncodeType.VALUES.WAV -> {
                try {
                    pcmPlayer.stop()
                    pcmPlayer.start()
                } catch (t: Throwable) {
                    logger.error(TAG, "Failed to create PcmPlayer: ${t.message}")
                    return
                }
            }
        }
    }

    fun playSoundEnd() {
        if (!ttsConfig.playSound) {
            logger.debug(TAG, "playSound is false, skip playSoundEnd")
            return
        }

        when (ttsConfig.encodeType) {
            TtsParams.EncodeType.VALUES.MP3 -> {
                try {
                    mp3Player.play()
                } catch (t: Throwable) {
                    logger.error(TAG, "Failed to create Mp3Player: ${t.message}")
                    return
                }
            }
        }
    }

    fun writeSoundData(data: ByteArray) {
        if (!ttsConfig.playSound) {
            logger.debug(TAG, "playSound is false, skip writeSoundData")
            return
        }

        when (ttsConfig.encodeType) {
            TtsParams.EncodeType.VALUES.WAV -> pcmPlayer.writeData(data)
        }
    }

    fun stopPlaySound() {
        if (!ttsConfig.playSound) {
            logger.debug(TAG, "playSound is false, skip stopPlaySound")
            return
        }

        when (ttsConfig.encodeType) {
            TtsParams.EncodeType.VALUES.WAV -> pcmPlayer.stop()
            TtsParams.EncodeType.VALUES.MP3 -> mp3Player.stop()
        }
    }

    companion object {
        private const val TAG = "PlayerManager"
    }
}