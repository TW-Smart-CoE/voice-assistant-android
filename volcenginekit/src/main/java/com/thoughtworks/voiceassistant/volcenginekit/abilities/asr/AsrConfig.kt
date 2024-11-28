package com.thoughtworks.voiceassistant.volcenginekit.abilities.asr

import android.content.Context
import com.thoughtworks.voiceassistant.core.utils.ParamUtils.requireKey
import com.thoughtworks.voiceassistant.volcenginekit.abilities.asr.models.HotwordsData

class AsrConfig(
    val appId: String,
    val appToken: String,
    val asrCluster: String,
    val vadMaxSpeechDuration: Int,
    val autoStop: Boolean = false,
    val audioSource: String = AsrParams.AudioSource.VALUES.DEFAULT,
    val recognitionType: String = AsrParams.RecognitionType.VALUES.SINGLE_SENTENCE,
    val hotwords: HotwordsData = AsrParams.Hotwords.VALUES.DEFAULT,
    val userId: String = AsrParams.UserId.VALUES.DEFAULT,
) {
    companion object {
        fun create(
            context: Context,
            params: Map<String, Any>,
        ): AsrConfig {
            params.requireKey(AsrParams.AppId.KEY)
            params.requireKey(AsrParams.AppToken.KEY)
            params.requireKey(AsrParams.AsrCluster.KEY)

            val hotwords = params[AsrParams.Hotwords.KEY]?.let {
                it as HotwordsData
            } ?: AsrParams.Hotwords.VALUES.DEFAULT

            return AsrConfig(
                appId = params[AsrParams.AppId.KEY].toString(),
                appToken = params[AsrParams.AppToken.KEY].toString(),
                asrCluster = params[AsrParams.AsrCluster.KEY].toString(),
                vadMaxSpeechDuration = params[AsrParams.VadMaxSpeechDuration.KEY]?.toString()
                    ?.toInt() ?: AsrParams.VadMaxSpeechDuration.VALUES.DEFAULT,
                autoStop = params[AsrParams.AutoStop.KEY]?.toString()?.toBoolean() == true,
                audioSource = params[AsrParams.AudioSource.KEY]?.toString()
                    ?: AsrParams.AudioSource.VALUES.DEFAULT,
                recognitionType = params[AsrParams.RecognitionType.KEY]?.toString()
                    ?: AsrParams.RecognitionType.VALUES.SINGLE_SENTENCE,
                hotwords = hotwords,
                userId = params[AsrParams.UserId.KEY]?.toString()
                    ?: AsrParams.UserId.VALUES.DEFAULT,
            )
        }
    }
}
