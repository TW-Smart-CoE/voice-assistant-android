package com.thoughtworks.voiceassistant.openaikit.abilities.chat

import android.content.Context
import com.thoughtworks.voiceassistant.core.utils.ParamUtils.requireKey

class ChatConfig(
    val apiKey: String,
    val baseUrl: String = ChatParams.BaseUrl.VALUES.OPEN_AI,
    val apiVersion: String = ChatParams.ApiVersion.VALUES.V1,
    val model: String,
    val maxHistoryLen: Int = ChatParams.MaxHistoryLen.VALUES.DEFAULT,
    val maxTokens: Int = ChatParams.MaxTokens.VALUES.DEFAULT,
    val temperature: Float = ChatParams.Temperature.VALUES.DEFAULT,
    val readTimeout: Long = ChatParams.ReadTimeout.VALUES.DEFAULT,
    val writeTimeout: Long = ChatParams.WriteTimeout.VALUES.DEFAULT,
) {
    companion object {
        fun create(
            context: Context,
            params: Map<String, Any>,
        ): ChatConfig {
            params.requireKey(ChatParams.ApiKey.KEY)
            params.requireKey(ChatParams.Model.KEY)

            return ChatConfig(
                apiKey = params[ChatParams.ApiKey.KEY].toString(),
                baseUrl = params[ChatParams.BaseUrl.KEY]?.toString()
                    ?: ChatParams.BaseUrl.VALUES.OPEN_AI,
                apiVersion = params[ChatParams.ApiVersion.KEY]?.toString()
                    ?: ChatParams.ApiVersion.VALUES.V1,
                model = params[ChatParams.Model.KEY].toString(),
                maxHistoryLen = params[ChatParams.MaxHistoryLen.KEY]?.toString()?.toInt()
                    ?: ChatParams.MaxHistoryLen.VALUES.DEFAULT,
                maxTokens = params[ChatParams.MaxTokens.KEY]?.toString()?.toInt()
                    ?: ChatParams.MaxTokens.VALUES.DEFAULT,
                temperature = params[ChatParams.Temperature.KEY]?.toString()?.toFloat()
                    ?: ChatParams.Temperature.VALUES.DEFAULT,
                readTimeout = params[ChatParams.ReadTimeout.KEY]?.toString()?.toLong()
                    ?: ChatParams.ReadTimeout.VALUES.DEFAULT,
                writeTimeout = params[ChatParams.WriteTimeout.KEY]?.toString()?.toLong()
                    ?: ChatParams.WriteTimeout.VALUES.DEFAULT,
            )
        }
    }
}