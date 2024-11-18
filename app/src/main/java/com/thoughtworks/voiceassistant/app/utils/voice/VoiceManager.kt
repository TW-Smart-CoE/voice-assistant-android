package com.thoughtworks.voiceassistant.app.utils.voice

import android.content.Context
import com.thoughtworks.voiceassistant.app.data.models.AbilityDataCollection
import com.thoughtworks.voiceassistant.app.definitions.ServiceProvider
import com.thoughtworks.voiceassistant.app.utils.voice.providers.AlibabaProvider
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
        createAsr()
        createTts()
    }

    suspend fun initialize() {
        asr.initialize()
        tts.initialize()
    }

    private fun createAsr() {
        when (abilityCollection.asr.provider.lowercase()) {
            ServiceProvider.ALIBABA.name.lowercase() -> asr = AlibabaProvider.createAsr(context)
        }
    }

    private fun createTts() {
        when (abilityCollection.tts.provider.lowercase()) {
            ServiceProvider.ALIBABA.name.lowercase() -> tts = AlibabaProvider.createTts(context)
        }
    }
}