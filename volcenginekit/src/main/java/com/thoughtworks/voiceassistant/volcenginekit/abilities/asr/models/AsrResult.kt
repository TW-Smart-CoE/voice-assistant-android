package com.thoughtworks.voiceassistant.volcenginekit.abilities.asr.models

data class AsrResult(
    val confidence: Float,
    val text: String,
)

data class AsrResponse(
    val result: List<AsrResult>?
)