package com.thoughtworks.voiceassistant.app.utils.voice.providers

import android.content.Context
import com.thoughtworks.voiceassistant.app.BuildConfig
import com.thoughtworks.voiceassistant.core.abilities.Chat
import com.thoughtworks.voiceassistant.openaikit.abilities.chat.ChatParams


object OpenAIProvider {
    fun createChat(context: Context): Chat {
        return OpenAIChat.create(
            context,
            mapOf(
                ChatParams.ApiKey.KEY to BuildConfig.OPENAI_API_KEY,
                ChatParams.BaseUrl.KEY to ChatParams.BaseUrl.VALUES.KIMI,
                ChatParams.ApiVersion.KEY to ChatParams.ApiVersion.VALUES.V1,
                ChatParams.Model.KEY to "moonshot-v1-8k",
            ),
        )
    }
}