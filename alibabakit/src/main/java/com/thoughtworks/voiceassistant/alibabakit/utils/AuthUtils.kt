package com.thoughtworks.voiceassistant.alibabakit.utils

import android.content.Context
import com.thoughtworks.voiceassistant.core.logger.Logger
import com.thoughtworks.voiceassistant.core.logger.error
import java.io.IOException

class AuthUtils(
    private val context: Context,
    private val logger: Logger,
) {
    suspend fun getToken(accessKey: String, accessKeySecret: String): String {
        try {
            val spUtils = SpUtils(context)
            val savedExpireTime = spUtils.getLong(SpUtils.SP_ALI_EXPIRE_TIME_KEY)
            val savedToken = spUtils.getStr(SpUtils.SP_ALI_ACCESS_TOKEN_KEY)
            if (savedToken.isEmpty() || savedExpireTime == 0L || savedExpireTime * 1000 <= System.currentTimeMillis()) {
                val tokenResult = CreateToken(logger).getToken(accessKey, accessKeySecret)
                val expireTime = tokenResult.expireTime
                val token = tokenResult.token

                if (token.isEmpty()) {
                    logger.error(TAG, "Get access token failed!")
                }

                spUtils.saveLong(SpUtils.SP_ALI_EXPIRE_TIME_KEY, expireTime)
                spUtils.saveStr(SpUtils.SP_ALI_ACCESS_TOKEN_KEY, token)

                return token
            } else {
                return spUtils.getStr(SpUtils.SP_ALI_ACCESS_TOKEN_KEY)
            }
        } catch (e: IOException) {
            logger.error(TAG, "Get token failed: ${e.message}")
            return ""
        }
    }

    companion object {
        private const val TAG = "AuthUtils"
    }
}