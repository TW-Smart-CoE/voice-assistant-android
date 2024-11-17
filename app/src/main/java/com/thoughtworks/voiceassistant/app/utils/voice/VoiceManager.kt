package com.thoughtworks.voiceassistant.app.utils.voice

import android.content.Context
import com.thoughtworks.voiceassistant.alibabakit.abilities.tts.AlibabaTts
import com.thoughtworks.voiceassistant.alibabakit.abilities.tts.AlibabaTtsParams
import com.thoughtworks.voiceassistant.app.BuildConfig
import com.thoughtworks.voiceassistant.app.data.models.AbilityDataCollection
import com.thoughtworks.voiceassistant.app.definitions.ServiceProvider
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
        createTts()
    }

    suspend fun initialize() {
        tts.initialize()
    }

    private fun createTts() {
        when (abilityCollection.tts.provider.lowercase()) {
            ServiceProvider.ALIBABA.name.lowercase() -> tts = createTtsAlibaba()
        }
    }

    private fun createTtsAlibaba(): Tts {
        val encodeType = AlibabaTtsParams.EncodeType.VALUES.MP3

        return AlibabaTts.create(
            context,
            mapOf(
                AlibabaTtsParams.AccessKey.KEY to BuildConfig.ALI_IVS_ACCESS_KEY,
                AlibabaTtsParams.AccessKeySecret.KEY to BuildConfig.ALI_IVS_ACCESS_KEY_SECRET,
                AlibabaTtsParams.AppKey.KEY to BuildConfig.ALI_IVS_APP_KEY,
                AlibabaTtsParams.EncodeType.KEY to encodeType,
                AlibabaTtsParams.TtsFilePath.KEY to "${context.externalCacheDir?.absolutePath}/tts.${encodeType}",
            )
        )
    }
}