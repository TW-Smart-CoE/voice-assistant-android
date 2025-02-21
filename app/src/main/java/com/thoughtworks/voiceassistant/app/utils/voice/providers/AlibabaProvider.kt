package com.thoughtworks.voiceassistant.app.utils.voice.providers

import android.content.Context
import com.thoughtworks.voiceassistant.alibabakit.abilities.asr.AlibabaAsr
import com.thoughtworks.voiceassistant.alibabakit.abilities.asr.AsrParams
import com.thoughtworks.voiceassistant.alibabakit.abilities.tts.AlibabaTts
import com.thoughtworks.voiceassistant.alibabakit.abilities.tts.TtsParams
import com.thoughtworks.voiceassistant.app.BuildConfig
import com.thoughtworks.voiceassistant.core.abilities.Asr
import com.thoughtworks.voiceassistant.core.abilities.Tts

object AlibabaProvider {
    fun createAsr(context: Context): Asr {
        return AlibabaAsr.create(
            context,
            mapOf(
                AsrParams.AccessKey.KEY to BuildConfig.ALI_IVS_ACCESS_KEY,
                AsrParams.AccessKeySecret.KEY to BuildConfig.ALI_IVS_ACCESS_KEY_SECRET,
                AsrParams.AppKey.KEY to BuildConfig.ALI_IVS_APP_KEY,
                AsrParams.AudioSource.KEY to AsrParams.AudioSource.VALUES.COMMUNICATION,
                AsrParams.RecognitionType.KEY to AsrParams.RecognitionType.VALUES.SINGLE_SENTENCE,
                AsrParams.EnableAcousticEchoCanceler.KEY to true,
                AsrParams.EnableNoiseSuppression.KEY to true,
                AsrParams.EnableVoiceDetection.KEY to false,
            )
        )
    }

    fun createTts(context: Context): Tts {
        val encodeType = TtsParams.EncodeType.VALUES.WAV

        return AlibabaTts.create(
            context,
            mapOf(
                TtsParams.AccessKey.KEY to BuildConfig.ALI_IVS_ACCESS_KEY,
                TtsParams.AccessKeySecret.KEY to BuildConfig.ALI_IVS_ACCESS_KEY_SECRET,
                TtsParams.AppKey.KEY to BuildConfig.ALI_IVS_APP_KEY,
                TtsParams.EncodeType.KEY to encodeType,
                TtsParams.TtsFilePath.KEY to "${context.externalCacheDir?.absolutePath}/tts.${encodeType}",
                TtsParams.PlayMode.KEY to TtsParams.PlayMode.VALUES.MEDIA,
                TtsParams.RemoveWavHeader.KEY to true,
                TtsParams.FontName.KEY to "aitong",
            )
        )
    }
}