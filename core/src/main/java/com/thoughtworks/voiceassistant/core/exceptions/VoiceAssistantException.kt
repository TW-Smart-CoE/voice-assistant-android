package com.thoughtworks.voiceassistant.core.exceptions

class VoiceAssistantException(
    val code: String = "",
    override val message: String = ""
) : Exception(message)
