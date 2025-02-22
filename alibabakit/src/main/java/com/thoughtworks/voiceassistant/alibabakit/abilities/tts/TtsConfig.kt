package com.thoughtworks.voiceassistant.alibabakit.abilities.tts

import android.content.Context
import com.alibaba.idst.nui.CommonUtils
import com.thoughtworks.voiceassistant.alibabakit.abilities.asr.AsrParams
import com.thoughtworks.voiceassistant.alibabakit.utils.AlibabaConfig
import com.thoughtworks.voiceassistant.alibabakit.utils.AuthUtils
import com.thoughtworks.voiceassistant.core.logger.Logger
import com.thoughtworks.voiceassistant.core.utils.DeviceUtils
import com.thoughtworks.voiceassistant.core.utils.ParamUtils.requireAtLeastOneKeyGroup
import com.thoughtworks.voiceassistant.core.utils.ParamUtils.requireKey
import org.json.JSONObject

class TtsConfig(
    accessKey: String = "",
    accessKeySecret: String = "",
    appKey: String = "",
    deviceId: String = "",
    workspace: String = "",
    token: String = "",
    val encodeType: String = TtsParams.EncodeType.VALUES.WAV,
    val ttsFilePath: String = "",
    val sampleRate: Int = TtsParams.SampleRate.VALUES.RATE_16K,
    val fontName: String = TtsParams.FontName.VALUES.ZHIMIAO_EMO,
    val enableSubtitle: String = TtsParams.EnableSubtitle.VALUES.ENABLE,
    val playSound: Boolean = true,
    val stopAndStartDelay: Int = 50,
    val modeType: Int = DEFAULT_MODE,
    val playMode: String = TtsParams.PlayMode.VALUES.MEDIA,
    val removeWavHeader: Boolean = true,
    val voicePitchRatio: Float,
    val voiceSpeedRatio: Float,
    val voiceVolumeRatio: Float,
) : AlibabaConfig(accessKey, accessKeySecret, appKey, deviceId, workspace, token) {
    override suspend fun generateTicket(context: Context, logger: Logger): String {
        val tokenValue = when (token.isNotEmpty()) {
            true -> token
            false -> AuthUtils(
                context,
                logger
            ).getToken(
                accessKey,
                accessKeySecret
            )
        }

        val jsonObj = JSONObject().apply {
            put("app_key", appKey)
            put("token", tokenValue)
            put("device_id", deviceId)
            put("url", url)
            put("workspace", workspace)
            put("mode_type", modeType.toString())
        }
        return jsonObj.toString()
    }

    companion object {
        private const val DEFAULT_MODE = 2 // online synthesis

        fun create(
            context: Context,
            params: Map<String, Any>,
        ): TtsConfig {
            params.requireKey(AsrParams.AppKey.KEY)
            params.requireAtLeastOneKeyGroup(
                listOf(AsrParams.Token.KEY),
                listOf(
                    AsrParams.AccessKey.KEY,
                    AsrParams.AccessKeySecret.KEY
                )
            )

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
                    ?: TtsParams.SampleRate.VALUES.RATE_16K,
                fontName = params[TtsParams.FontName.KEY]?.toString()
                    ?: TtsParams.FontName.VALUES.ZHIMIAO_EMO,
                enableSubtitle = params[TtsParams.EnableSubtitle.KEY]?.toString()
                    ?: TtsParams.EnableSubtitle.VALUES.ENABLE,
                playSound = params[TtsParams.PlaySound.KEY]?.toString()?.toBoolean() != false,
                stopAndStartDelay = params[TtsParams.StopAndStartDelay.KEY]?.toString()
                    ?.toInt()
                    ?: 50,
                playMode = params[TtsParams.PlayMode.KEY]?.toString()
                    ?: TtsParams.PlayMode.VALUES.MEDIA,
                removeWavHeader = params[TtsParams.RemoveWavHeader.KEY]?.toString()?.toBoolean() != false,
                voicePitchRatio = params[TtsParams.VoicePitchRatio.KEY]?.toString()?.toFloat()
                    ?: TtsParams.VoicePitchRatio.VALUES.DEFAULT,
                voiceSpeedRatio = params[TtsParams.VoiceSpeedRatio.KEY]?.toString()?.toFloat()
                    ?: TtsParams.VoiceSpeedRatio.VALUES.DEFAULT,
                voiceVolumeRatio = params[TtsParams.VoiceVolumeRatio.KEY]?.toString()?.toFloat()
                    ?: TtsParams.VoiceVolumeRatio.VALUES.DEFAULT,
            )
        }
    }
}