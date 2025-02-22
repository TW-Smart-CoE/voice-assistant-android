package com.thoughtworks.voiceassistant.volcenginekit.abilities.tts

import android.content.Context
import com.thoughtworks.voiceassistant.core.utils.ParamUtils.requireKey
import com.thoughtworks.voiceassistant.volcenginekit.abilities.asr.AsrParams

class TtsConfig(
    val appId: String,
    val appToken: String,
    val cluster: String,
    val voiceName: String,
    val voiceType: String,
    val voicePitch: Float,
    val voiceSpeed: Float,
    val ttsFilePath: String,
    val playMode: String = TtsParams.PlayMode.VALUES.MEDIA,
    val userId: String = TtsParams.UserId.VALUES.DEFAULT,
) {
    companion object {
        fun create(
            context: Context,
            params: Map<String, Any>,
        ): TtsConfig {
            params.requireKey(TtsParams.AppId.KEY)
            params.requireKey(TtsParams.AppToken.KEY)
            params.requireKey(AsrParams.Cluster.KEY)
            params.requireKey(TtsParams.VoiceName.KEY)
            params.requireKey(TtsParams.VoiceType.KEY)

            return TtsConfig(
                appId = params[TtsParams.AppId.KEY].toString(),
                appToken = params[TtsParams.AppToken.KEY].toString(),
                cluster = params[AsrParams.Cluster.KEY].toString(),
                voiceName = params[TtsParams.VoiceName.KEY].toString(),
                voiceType = params[TtsParams.VoiceType.KEY].toString(),
                voicePitch = params[TtsParams.VoicePitch.KEY]?.toString()?.toFloat()
                    ?: TtsParams.VoicePitch.VALUES.DEFAULT,
                voiceSpeed = params[TtsParams.VoiceSpeed.KEY]?.toString()?.toFloat()
                    ?: TtsParams.VoiceSpeed.VALUES.DEFAULT,
                ttsFilePath = params[TtsParams.TtsFilePath.KEY]?.toString() ?: "",
                playMode = params[TtsParams.PlayMode.KEY]?.toString()
                    ?: TtsParams.PlayMode.VALUES.MEDIA,
                userId = params[TtsParams.UserId.KEY]?.toString()
                    ?: TtsParams.UserId.VALUES.DEFAULT,
            )
        }
    }
}
