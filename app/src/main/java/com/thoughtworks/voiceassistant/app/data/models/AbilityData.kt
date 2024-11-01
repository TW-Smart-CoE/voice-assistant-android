package com.thoughtworks.voiceassistant.app.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AbilityData(
    val type: String,
    val provider: String,
) : Parcelable

@Parcelize
data class AbilityDataCollection(
    val tts: AbilityData,
    val asr: AbilityData,
    val wakeUp: AbilityData,
    val chat: AbilityData,
) : Parcelable