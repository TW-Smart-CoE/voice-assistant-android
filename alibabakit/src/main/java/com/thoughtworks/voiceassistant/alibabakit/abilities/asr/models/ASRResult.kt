package com.thoughtworks.voiceassistant.alibabakit.abilities.asr.models

import com.google.gson.annotations.SerializedName

data class ASRResult(
    @SerializedName("header")
    var header: Header? = null,
    @SerializedName("payload")
    var payload: Payload? = null,
)