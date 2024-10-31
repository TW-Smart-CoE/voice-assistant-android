package com.thoughtworks.voiceassistant.app.definitions

enum class Ability(val displayName: String) {
    TTS("Tts"),
    ASR("Asr"),
    WAKE_UP("WakeUp"),
    CHAT("Chat"),
}