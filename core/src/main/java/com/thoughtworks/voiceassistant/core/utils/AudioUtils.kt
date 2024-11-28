package com.thoughtworks.voiceassistant.core.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager

class AudioUtils(context: Context) {
    private val audioManager: AudioManager =
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    /**
     * Sets the volume for the specified stream type.
     *
     * @param streamType The audio stream type (e.g., AudioManager.STREAM_MUSIC).
     * @param volumeLevel The desired volume level.
     * @param showUI Whether to show the system volume UI.
     */
    fun setVolume(streamType: Int, volumeLevel: Int, showUI: Boolean = false) {
        val maxVolume = audioManager.getStreamMaxVolume(streamType)
        val safeVolumeLevel = volumeLevel.coerceIn(0, maxVolume)

        audioManager.setStreamVolume(
            streamType,
            safeVolumeLevel,
            if (showUI) AudioManager.FLAG_SHOW_UI else 0
        )
    }

    /**
     * Gets the current volume level for the specified stream type.
     *
     * @param streamType The audio stream type (e.g., AudioManager.STREAM_MUSIC).
     * @return The current volume level.
     */
    fun getVolume(streamType: Int): Int {
        return audioManager.getStreamVolume(streamType)
    }

    /**
     * Gets the maximum volume level for the specified stream type.
     *
     * @param streamType The audio stream type (e.g., AudioManager.STREAM_MUSIC).
     * @return The maximum volume level.
     */
    fun getMaxVolume(streamType: Int): Int {
        return audioManager.getStreamMaxVolume(streamType)
    }

    fun buildAudioAttributes(isCommunication: Boolean): AudioAttributes {
        return if (isCommunication) {
            AudioAttributes.Builder()
                .setLegacyStreamType(AudioManager.STREAM_VOICE_CALL)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
                .build()
        } else {
            AudioAttributes.Builder()
                .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        }
    }
}