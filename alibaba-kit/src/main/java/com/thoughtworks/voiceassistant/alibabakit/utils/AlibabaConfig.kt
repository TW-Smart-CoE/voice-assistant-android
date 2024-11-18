package com.thoughtworks.voiceassistant.alibabakit.utils

import android.content.Context
import com.thoughtworks.voiceassistant.core.logger.Logger
import org.json.JSONObject

open class AlibabaConfig(
    val accessKey: String = "",
    val accessKeySecret: String = "",
    val appKey: String = "",
    val deviceId: String = "",
    val workspace: String = "",
    val token: String = "",
    private val url: String = DEFAULT_URL,
    private val modeType: Int = DEFAULT_MODE,
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
    }
}