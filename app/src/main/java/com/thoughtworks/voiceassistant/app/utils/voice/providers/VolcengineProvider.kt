package com.thoughtworks.voiceassistant.app.utils.voice.providers

import android.content.Context
import com.thoughtworks.voiceassistant.app.BuildConfig
import com.thoughtworks.voiceassistant.core.abilities.Asr
import com.thoughtworks.voiceassistant.core.abilities.Tts
import com.thoughtworks.voiceassistant.volcenginekit.abilities.asr.AsrParams
import com.thoughtworks.voiceassistant.volcenginekit.abilities.asr.VolcengineAsr
import com.thoughtworks.voiceassistant.volcenginekit.abilities.tts.TtsParams
import com.thoughtworks.voiceassistant.volcenginekit.abilities.tts.VolcengineTts

object VolcengineProvider {
    fun createAsr(context: Context): Asr {
        return VolcengineAsr.create(
            context,
            mapOf(
                AsrParams.AppId.KEY to BuildConfig.VOLCENGINE_APP_ID,
                AsrParams.AppToken.KEY to BuildConfig.VOLCENGINE_ACCESS_TOKEN,
                AsrParams.Cluster.KEY to BuildConfig.VOLCENGINE_ONE_SENTENCE_RECOGNITION_CLUSTER_ID,
                AsrParams.AudioSource.KEY to AsrParams.AudioSource.VALUES.COMMUNICATION,
                AsrParams.RecognitionType.KEY to AsrParams.RecognitionType.VALUES.SINGLE_SENTENCE,
                AsrParams.AutoStop.KEY to false,
                AsrParams.VadMaxSpeechDuration.KEY to AsrParams.VadMaxSpeechDuration.VALUES.INFINITE,
            )
        )
    }

    fun createTts(context: Context): Tts {
        return VolcengineTts.create(
            context,
            mapOf(
                TtsParams.AppId.KEY to BuildConfig.VOLCENGINE_APP_ID,
                TtsParams.AppToken.KEY to BuildConfig.VOLCENGINE_ACCESS_TOKEN,
                TtsParams.Cluster.KEY to BuildConfig.VOLCENGINE_STREAM_SPEECH_SYNTHESIS_CLUSTER_ID,
                TtsParams.VoiceName.KEY to "灿灿 2.0",
                TtsParams.VoiceType.KEY to "BV700_V2_streaming",
                TtsParams.VoicePitchRatio.KEY to 1.0f,
                TtsParams.VoiceSpeedRatio.KEY to 1.0f,
                TtsParams.VoiceVolumeRatio.KEY to 1.0f,
                TtsParams.TtsFilePath.KEY to "${context.externalCacheDir?.absolutePath}/tts.pcm",
                TtsParams.PlayMode.KEY to TtsParams.PlayMode.VALUES.MEDIA,
            )
        )
    }
}