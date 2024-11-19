package com.thoughtworks.voiceassistant.app.ui.views.voice

import com.thoughtworks.voiceassistant.app.data.models.AbilityDataCollection
import com.thoughtworks.voiceassistant.app.definitions.Ability
import com.thoughtworks.voiceassistant.app.foundation.mvi.model.Action
import com.thoughtworks.voiceassistant.app.foundation.mvi.model.Event
import com.thoughtworks.voiceassistant.app.foundation.mvi.model.State

data class WakeUpState(
    val title: String = Ability.WAKE_UP.displayName,
    val listening: Boolean = false,
)

data class AsrState(
    val title: String = Ability.ASR.displayName,
    val listening: Boolean = false,
)

data class TtsState(
    val title: String = Ability.TTS.displayName,
    val speaking: Boolean = false,
    val input: String = "",
)

data class ChatState(
    val title: String = Ability.CHAT.displayName,
    val input: String = "",
    val started: Boolean = false,
)

data class VoiceInteractionState(
    val wakeUp: WakeUpState = WakeUpState(),
    val asr: AsrState = AsrState(),
    val tts: TtsState = TtsState(),
    val chat: ChatState = ChatState(),
) : State

sealed interface VoiceInteractionEvent : Event {
    data class ShowToast(val text: String) : VoiceInteractionEvent
}

sealed interface VoiceInteractionAction : Action {
    data class ChangeTtsPrompt(val text: String) : VoiceInteractionAction
    data class ChangeChatInput(val text: String) : VoiceInteractionAction
    data class UpdateAbilityDataCollection(val abilityDataCollection: AbilityDataCollection) :
        VoiceInteractionAction

    data object NavigateBack : VoiceInteractionAction
    data object WakeUpListen : VoiceInteractionAction
    data object WakeUpStop : VoiceInteractionAction
    data object AsrListen : VoiceInteractionAction
    data object AsrStop : VoiceInteractionAction
    data object TtsSpeak : VoiceInteractionAction
    data object TtsStop : VoiceInteractionAction
    data object ChatStart : VoiceInteractionAction
    data object ChatClearHistory : VoiceInteractionAction
}