package com.thoughtworks.voiceassistant.volcenginekit.abilities.asr.models

data class Hotword(
    val word: String,
    val scale: Double
)

data class HotwordsData(
    val hotwords: List<Hotword>
)