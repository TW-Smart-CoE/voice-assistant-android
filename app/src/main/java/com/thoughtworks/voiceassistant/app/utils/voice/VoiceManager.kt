package com.thoughtworks.voiceassistant.app.utils.voice

import android.content.Context
import com.thoughtworks.voiceassistant.alibabakit.abilities.asr.AlibabaAsr
import com.thoughtworks.voiceassistant.alibabakit.abilities.asr.AsrParams
import com.thoughtworks.voiceassistant.alibabakit.abilities.tts.AlibabaTts
import com.thoughtworks.voiceassistant.alibabakit.abilities.tts.TtsParams
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
        createAsr()
        createTts()
    }

    suspend fun initialize() {
        asr.initialize()
        tts.initialize()
    }

    private fun createAsr() {
        when (abilityCollection.asr.provider.lowercase()) {
            ServiceProvider.ALIBABA.name.lowercase() -> asr = createAsrAlibaba()
        }
    }

    private fun createAsrAlibaba(): Asr {
        return AlibabaAsr.create(
            context,
            mapOf(
                AsrParams.AccessKey.KEY to BuildConfig.ALI_IVS_ACCESS_KEY,
                AsrParams.AccessKeySecret.KEY to BuildConfig.ALI_IVS_ACCESS_KEY_SECRET,
                AsrParams.AppKey.KEY to BuildConfig.ALI_IVS_APP_KEY,
                AsrParams.AudioSource.KEY to AsrParams.AudioSource.VALUES.COMMUNICATION,
                AsrParams.VadMode.KEY to AsrParams.VadMode.VALUES.P2T,
            )
        )
    }

    private fun createTts() {
        when (abilityCollection.tts.provider.lowercase()) {
            ServiceProvider.ALIBABA.name.lowercase() -> tts = createTtsAlibaba()
        }
    }

    private fun createTtsAlibaba(): Tts {
        val encodeType = TtsParams.EncodeType.VALUES.MP3

        return AlibabaTts.create(
            context,
            mapOf(
                TtsParams.AccessKey.KEY to BuildConfig.ALI_IVS_ACCESS_KEY,
                TtsParams.AccessKeySecret.KEY to BuildConfig.ALI_IVS_ACCESS_KEY_SECRET,
                TtsParams.AppKey.KEY to BuildConfig.ALI_IVS_APP_KEY,
                TtsParams.EncodeType.KEY to encodeType,
                TtsParams.TtsFilePath.KEY to "${context.externalCacheDir?.absolutePath}/tts.${encodeType}",
            )
        )
    }
}