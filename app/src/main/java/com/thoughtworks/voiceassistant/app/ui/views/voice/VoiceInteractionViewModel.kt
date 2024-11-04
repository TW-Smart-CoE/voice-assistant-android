package com.thoughtworks.voiceassistant.app.ui.views.voice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thoughtworks.voiceassistant.app.di.Dependency
import com.thoughtworks.voiceassistant.app.foundation.mvi.DefaultStore
import com.thoughtworks.voiceassistant.app.foundation.mvi.MVIViewModel
import com.thoughtworks.voiceassistant.app.foundation.mvi.Store

class VoiceInteractionViewModel(
    dependency: Dependency,
    store: Store<VoiceInteractionState, VoiceInteractionEvent, VoiceInteractionAction> = DefaultStore(
        initialState = VoiceInteractionState(
            ttsPrompt = dependency.dataSource.loadTtsPrompt(),
        )
    ),
) : MVIViewModel<VoiceInteractionState, VoiceInteractionEvent, VoiceInteractionAction>(store) {

    private val navigator = dependency.navigator
    private val dataSource = dependency.dataSource

    override fun reduce(
        currentState: VoiceInteractionState,
        action: VoiceInteractionAction,
    ): VoiceInteractionState {
        return when (action) {
            is VoiceInteractionAction.ChangeTtsPrompt -> {
                currentState.copy(
                    ttsPrompt = action.text
                )
            }

            is VoiceInteractionAction.ChangeChatInput -> {
                currentState.copy(
                    chatInput = action.text
                )
            }

            is VoiceInteractionAction.WakeUpStart -> {
                currentState.copy(
                    wakeUpStarted = true
                )
            }

            is VoiceInteractionAction.WakeUpStop -> {
                currentState.copy(
                    wakeUpStarted = false
                )
            }

            is VoiceInteractionAction.AsrStart -> {
                currentState.copy(
                    asrStarted = true
                )
            }

            is VoiceInteractionAction.AsrStop -> {
                currentState.copy(
                    asrStarted = false
                )
            }

            is VoiceInteractionAction.TtsPlay -> {
                currentState.copy(
                    ttsPlaying = true
                )
            }

            is VoiceInteractionAction.TtsStop -> {
                currentState.copy(
                    ttsPlaying = false
                )
            }

            is VoiceInteractionAction.ChatStart -> {
                currentState.copy(
                    chatStarted = true
                )
            }

            is VoiceInteractionAction.ChatClearHistory -> {
                currentState.copy(
                    chatStarted = false
                )
            }

            else -> {
                currentState
            }
        }
    }

    override fun runSideEffect(
        action: VoiceInteractionAction,
        currentState: VoiceInteractionState,
    ) {
        when (action) {
            is VoiceInteractionAction.NavigateBack -> {
                dataSource.saveTtsPrompt(currentState.ttsPrompt)
                navigator.navigateBack()
            }

            is VoiceInteractionAction.TtsPlay -> {
                dataSource.saveTtsPrompt(currentState.ttsPrompt)
            }

            else -> {
            }
        }
    }
}

class VoiceInteractionViewModelFactory(
    private val dependency: Dependency,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VoiceInteractionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VoiceInteractionViewModel(dependency) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
