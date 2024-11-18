package com.thoughtworks.voiceassistant.alibabakit.abilities.tts

import android.content.Context
import com.alibaba.idst.nui.CommonUtils
import com.thoughtworks.voiceassistant.alibabakit.utils.AlibabaConfig
import com.thoughtworks.voiceassistant.core.utils.DeviceUtils

class TtsConfig(
    accessKey: String = "",
    accessKeySecret: String = "",
    appKey: String = "",
    deviceId: String = "",
    workspace: String = "",
    token: String = "",
    val encodeType: String = TtsParams.EncodeType.VALUES.WAV,
    val ttsFilePath: String = "",
    val sampleRate: Int = TtsParams.SampleRate.DEFAULT_VALUE,
    val fontName: String = TtsParams.FontName.VALUES.ZHIMIAO_EMO,
    val enableSubtitle: String = TtsParams.EnableSubtitle.VALUES.ENABLE,
    val playSound: Boolean = true,
    val stopAndStartDelay: Int = 50,
) : AlibabaConfig(accessKey, accessKeySecret, appKey, deviceId, workspace, token) {
    companion object {
        fun create(
            context: Context,
            params: Map<String, Any>,
        ): TtsConfig {
            return TtsConfig(
                accessKey = params[TtsParams.AccessKey.KEY]?.toString() ?: "",
                accessKeySecret = params[TtsParams.AccessKeySecret.KEY]?.toString() ?: "",
                appKey = params[TtsParams.AppKey.KEY]?.toString() ?: "",
                token = params[TtsParams.Token.KEY]?.toString() ?: "",
                deviceId = DeviceUtils.getDeviceId(context),
                workspace = CommonUtils.getModelPath(context),
                encodeType = params[TtsParams.EncodeType.KEY]?.toString()
                    ?: TtsParams.EncodeType.VALUES.WAV,
                ttsFilePath = params[TtsParams.TtsFilePath.KEY]?.toString() ?: "",
                sampleRate = params[TtsParams.SampleRate.KEY]?.toString()?.toInt()
                    ?: TtsParams.SampleRate.DEFAULT_VALUE,
                fontName = params[TtsParams.FontName.KEY]?.toString()
                    ?: TtsParams.FontName.VALUES.ZHIMIAO_EMO,
                enableSubtitle = params[TtsParams.EnableSubtitle.KEY]?.toString()
                    ?: TtsParams.EnableSubtitle.VALUES.ENABLE,
                playSound = params[TtsParams.PlaySound.KEY]?.toString()?.toBoolean() != false,
                stopAndStartDelay = params[TtsParams.StopAndStartDelay.KEY]?.toString()
                    ?.toInt()
                    ?: 50
            )
        }
    }
}