package com.thoughtworks.voiceassistant.app.utils.voice

import android.content.Context
import com.thoughtworks.voiceassistant.app.data.models.AbilityDataCollection
import com.thoughtworks.voiceassistant.app.definitions.ServiceProvider
import com.thoughtworks.voiceassistant.app.utils.voice.providers.AlibabaProvider
import com.thoughtworks.voiceassistant.app.utils.voice.providers.OpenAIProvider
import com.thoughtworks.voiceassistant.app.utils.voice.providers.PicovoiceProvider
import com.thoughtworks.voiceassistant.app.utils.voice.providers.VolcengineProvider
import com.thoughtworks.voiceassistant.core.abilities.Asr
import com.thoughtworks.voiceassistant.core.abilities.Chat
import com.thoughtworks.voiceassistant.core.abilities.Tts
import com.thoughtworks.voiceassistant.core.abilities.WakeUp

class VoiceManager(
    private val context: Context,
    private val abilityCollection: AbilityDataCollection,
) {
    lateinit var wakeUp: WakeUp
    lateinit var asr: Asr
    lateinit var tts: Tts
    lateinit var chat: Chat

    init {
        createWakeUp()
        createAsr()
        createTts()
        createChat()
    }

    suspend fun initialize() {
        wakeUp.initialize()
        asr.initialize()
        tts.initialize()
        chat.initialize()
    }

    fun release() {
        wakeUp.release()
        asr.release()
        tts.release()
        chat.release()
    }

    private fun createWakeUp() {
        when (abilityCollection.wakeUp.provider.lowercase()) {
            ServiceProvider.PICOVOICE.name.lowercase() -> wakeUp =
                PicovoiceProvider.createWakeUp(context)
        }
    }

    private fun createAsr() {
        when (abilityCollection.asr.provider.lowercase()) {
            ServiceProvider.ALIBABA.name.lowercase() -> asr = AlibabaProvider.createAsr(context)
            ServiceProvider.VOLCENGINE.name.lowercase() -> asr =
                VolcengineProvider.createAsr(context)
        }
    }

    private fun createTts() {
        when (abilityCollection.tts.provider.lowercase()) {
            ServiceProvider.ALIBABA.name.lowercase() -> tts = AlibabaProvider.createTts(context)
            ServiceProvider.VOLCENGINE.name.lowercase() -> tts =
                VolcengineProvider.createTts(context)
        }
    }

    private fun createChat() {
        when (abilityCollection.chat.provider.lowercase()) {
            ServiceProvider.OPENAI.name.lowercase() -> chat = OpenAIProvider.createChat(context)
        }
    }
}