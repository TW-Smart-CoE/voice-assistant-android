package com.thoughtworks.voiceassistant.alibabakit.abilities.tts

import com.thoughtworks.voiceassistant.core.Tts
import com.thoughtworks.voiceassistant.core.TtsCallback

class AlibabaTts : Tts{
    override fun initialize() {
        TODO("Not yet implemented")
    }

    override fun release() {
        TODO("Not yet implemented")
    }

    override suspend fun play(text: String, params: Map<String, Any>, ttsCallback: TtsCallback?) {
        TODO("Not yet implemented")
    }

    override fun stopPlay() {
        TODO("Not yet implemented")
    }
}