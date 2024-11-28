package com.thoughtworks.voiceassistant.app.utils.voice.providers

import android.content.Context
import com.thoughtworks.voiceassistant.app.BuildConfig
import com.thoughtworks.voiceassistant.core.abilities.Asr
import com.thoughtworks.voiceassistant.volcenginekit.abilities.asr.AsrParams
import com.thoughtworks.voiceassistant.volcenginekit.abilities.asr.VolcengineAsr

object VolcengineProvider {
    fun createAsr(context: Context): Asr {
        return VolcengineAsr.create(
            context,
            mapOf(
                AsrParams.AppId.KEY to BuildConfig.VOLCENGINE_APP_ID,
                AsrParams.AppToken.KEY to BuildConfig.VOLCENGINE_ACCESS_TOKEN,
                AsrParams.AsrCluster.KEY to BuildConfig.VOLCENGINE_ONE_SENTENCE_RECOGNITION_CLUSTER_ID,
                AsrParams.RecognitionType.KEY to AsrParams.RecognitionType.VALUES.LONG,
                AsrParams.VadMaxSpeechDuration.KEY to AsrParams.VadMaxSpeechDuration.VALUES.INFINITE,
            )
        )
    }
}