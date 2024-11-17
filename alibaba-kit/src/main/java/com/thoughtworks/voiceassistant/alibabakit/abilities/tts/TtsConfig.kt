package com.thoughtworks.voiceassistant.alibabakit.abilities.tts

import android.content.Context
import com.alibaba.idst.nui.CommonUtils
import com.thoughtworks.voiceassistant.alibabakit.utils.AuthUtils
import com.thoughtworks.voiceassistant.core.logger.Logger
import com.thoughtworks.voiceassistant.core.utils.DeviceUtils
import org.json.JSONObject

class TtsConfig(
    val accessKey: String = "",
    val accessKeySecret: String = "",
    val appKey: String = "",
    val deviceId: String = "",
    val workspace: String = "",
    val token: String = "",
    val encodeType: String = AlibabaTtsParams.EncodeType.VALUES.WAV,
    val ttsFilePath: String = "",
    val sampleRate: Int = AlibabaTtsParams.SampleRate.DEFAULT_VALUE,
    val fontName: String = AlibabaTtsParams.FontName.VALUES.ZHIMIAO_EMO,
    val enableSubtitle: String = AlibabaTtsParams.EnableSubtitle.VALUES.ENABLE,
    val playSound: Boolean = true,
    val stopAndStartDelay: Int = 50,
    private val modeType: Int = DEFAULT_MODE,
    private val url: String = DEFAULT_URL,
) {
    suspend fun generateTicket(context: Context, logger: Logger): String {
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
        private const val DEFAULT_URL = "wss://nls-gateway.cn-shanghai.aliyuncs.com:443/ws/v1"
        private const val DEFAULT_MODE = 2 // online synthesis

        fun create(
            context: Context,
            ttsParams: Map<String, Any>,
        ): TtsConfig {
            return TtsConfig(
                accessKey = ttsParams[AlibabaTtsParams.AccessKey.KEY]?.toString() ?: "",
                accessKeySecret = ttsParams[AlibabaTtsParams.AccessKeySecret.KEY]?.toString() ?: "",
                appKey = ttsParams[AlibabaTtsParams.AppKey.KEY]?.toString() ?: "",
                token = ttsParams[AlibabaTtsParams.Token.KEY]?.toString() ?: "",
                deviceId = DeviceUtils.getDeviceId(context),
                workspace = CommonUtils.getModelPath(context),
                encodeType = ttsParams[AlibabaTtsParams.EncodeType.KEY]?.toString()
                    ?: AlibabaTtsParams.EncodeType.VALUES.WAV,
                ttsFilePath = ttsParams[AlibabaTtsParams.TtsFilePath.KEY]?.toString() ?: "",
                sampleRate = ttsParams[AlibabaTtsParams.SampleRate.KEY]?.toString()?.toInt()
                    ?: AlibabaTtsParams.SampleRate.DEFAULT_VALUE,
                fontName = ttsParams[AlibabaTtsParams.FontName.KEY]?.toString()
                    ?: AlibabaTtsParams.FontName.VALUES.ZHIMIAO_EMO,
                enableSubtitle = ttsParams[AlibabaTtsParams.EnableSubtitle.KEY]?.toString()
                    ?: AlibabaTtsParams.EnableSubtitle.VALUES.ENABLE,
                playSound = ttsParams[AlibabaTtsParams.PlaySound.KEY]?.toString()?.toBoolean()
                    ?: true,
                stopAndStartDelay = ttsParams[AlibabaTtsParams.StopAndStartDelay.KEY]?.toString()
                    ?.toInt()
                    ?: 50
            )
        }
    }
}