package com.thoughtworks.voiceassistant.app.utils.voice.providers

import android.content.Context
import com.thoughtworks.voiceassistant.app.BuildConfig
import com.thoughtworks.voiceassistant.core.abilities.WakeUp
import com.thoughtworks.voiceassistant.core.utils.LanguageUtils
import com.thoughtworks.voiceassistant.picovoicekit.abilities.wakeup.PicovoiceWakeUp
import com.thoughtworks.voiceassistant.picovoicekit.abilities.wakeup.WakeUpParams

object PicovoiceProvider {
    fun createWakeUp(context: Context): WakeUp {
        return PicovoiceWakeUp.create(
            context,
            mapOf(
                WakeUpParams.AccessKey.KEY to BuildConfig.PICOVOICE_ACCESS_KEY,
                WakeUpParams.ModelPath.KEY to if (LanguageUtils.isChinese()) "wakeup/picovoice/models/porcupine_params_zh.pv" else "",
                WakeUpParams.KeywordPaths.KEY to if (LanguageUtils.isChinese()) listOf(
                    "wakeup/picovoice/小智_zh_android_v3_0_0.ppn",
                ) else listOf(
                    "wakeup/picovoice/Hi-Joey_en_android_v3_0_0.ppn",
                )
            )
        )
    }
}