package com.thoughtworks.voiceassistant.app.ui.views.voice

import com.thoughtworks.voiceassistant.app.foundation.mvi.model.Action
import com.thoughtworks.voiceassistant.app.foundation.mvi.model.Event
import com.thoughtworks.voiceassistant.app.foundation.mvi.model.State

data class VoiceInteractionState(
    val ttsPrompt: String = "",
    val chatInput: String = "",
    val wakeUpStarted: Boolean = false,
    val asrStarted: Boolean = false,
    val ttsPlaying: Boolean = false,
    val chatStarted: Boolean = false,
) : State

sealed class VoiceInteractionEvent : Event

sealed interface VoiceInteractionAction : Action {
    data class ChangeTtsPrompt(val text: String) : VoiceInteractionAction
    data class ChangeChatInput(val text: String) : VoiceInteractionAction
    data object NavigateBack : VoiceInteractionAction
    data object WakeUpStart : VoiceInteractionAction
    data object WakeUpStop : VoiceInteractionAction
    data object AsrStart : VoiceInteractionAction
    data object AsrStop : VoiceInteractionAction
    data object TtsPlay : VoiceInteractionAction
    data object TtsStop : VoiceInteractionAction
    data object ChatStart : VoiceInteractionAction
    data object ChatClearHistory : VoiceInteractionAction
}