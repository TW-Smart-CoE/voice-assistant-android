package com.thoughtworks.voiceassistant.picovoicekit.abilities.wakeup

import android.content.Context
import com.thoughtworks.voiceassistant.core.utils.ParamUtils.requireKey

class WakeUpConfig(
    val accessKey: String = "",
    val modelPath: String = "",
    val keywordPaths: Array<String> = emptyArray(),
) {
    companion object {
        fun create(
            context: Context,
            params: Map<String, Any>,
        ): WakeUpConfig {
            params.requireKey(WakeUpParams.AccessKey.KEY)
            params.requireKey(WakeUpParams.KeywordPaths.KEY)

            val keywordPaths = params[WakeUpParams.KeywordPaths.KEY]
            if (keywordPaths !is List<*>) {
                throw IllegalArgumentException("The key '${WakeUpParams.KeywordPaths.KEY}' must be an array.")
            }

            return WakeUpConfig(
                accessKey = params[WakeUpParams.AccessKey.KEY]?.toString() ?: "",
                modelPath = params[WakeUpParams.ModelPath.KEY]?.toString() ?: "",
                keywordPaths = keywordPaths.map { it.toString() }.toTypedArray(),
            )
        }
    }
}
