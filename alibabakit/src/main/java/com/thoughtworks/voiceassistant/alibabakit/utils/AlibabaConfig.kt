package com.thoughtworks.voiceassistant.alibabakit.utils

import android.content.Context
import com.thoughtworks.voiceassistant.core.logger.Logger

abstract class AlibabaConfig(
    val accessKey: String = "",
    val accessKeySecret: String = "",
    val appKey: String = "",
    val deviceId: String = "",
    val workspace: String = "",
    val token: String = "",
    val url: String = DEFAULT_URL,
) {
    abstract suspend fun generateTicket(context: Context, logger: Logger): String

    companion object {
        private const val DEFAULT_URL = "wss://nls-gateway.cn-shanghai.aliyuncs.com:443/ws/v1"

        fun Map<String, Any>.requireKey(key: String) {
            if (!this.containsKey(key)) {
                throw IllegalArgumentException("The key '$key' is required but was not provided.")
            }
        }

        fun Map<String, Any>.requireAtLeastOneKeyGroup(vararg keyGroups: List<String>) {
            val isAnyGroupSatisfied = keyGroups.any { group ->
                group.all { key -> this.containsKey(key) }
            }

            if (!isAnyGroupSatisfied) {
                throw IllegalArgumentException("At least one of the key groups must be fully provided: ${keyGroups.joinToString { it.toString() }}")
            }
        }
    }
}