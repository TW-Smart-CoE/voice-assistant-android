package com.thoughtworks.voiceassistant.app.ui.views.voice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thoughtworks.voiceassistant.app.data.models.AbilityDataCollection
import com.thoughtworks.voiceassistant.app.di.Dependency
import com.thoughtworks.voiceassistant.app.foundation.mvi.DefaultStore
import com.thoughtworks.voiceassistant.app.foundation.mvi.MVIViewModel
import com.thoughtworks.voiceassistant.app.foundation.mvi.Store
import com.thoughtworks.voiceassistant.core.Asr
import com.thoughtworks.voiceassistant.core.Chat
import com.thoughtworks.voiceassistant.core.Tts
import com.thoughtworks.voiceassistant.core.WakeUp

class VoiceInteractionViewModel(
    dependency: Dependency,
    store: Store<VoiceInteractionState, VoiceInteractionEvent, VoiceInteractionAction> = DefaultStore(
        initialState = VoiceInteractionState(
            tts = TtsState(
                input = dependency.dataSource.loadTtsInput(),
            ),
        )
    ),
) : MVIViewModel<VoiceInteractionState, VoiceInteractionEvent, VoiceInteractionAction>(store) {

    private val navigator = dependency.navigator
    private val dataSource = dependency.dataSource

    private lateinit var abilityCollection: AbilityDataCollection
    private lateinit var wakeUp: WakeUp
    private lateinit var asr: Asr
    private lateinit var tts: Tts
    private lateinit var chat: Chat

    init {
        initAbilities()
    }

    private fun initAbilities() {
        abilityCollection = dataSource.loadAbilityDataCollection()!!
        sendAction(VoiceInteractionAction.UpdateAbilityDataCollection(abilityCollection))
    }

    override fun reduce(
        currentState: VoiceInteractionState,
        action: VoiceInteractionAction,
    ): VoiceInteractionState {
        return when (action) {
            is VoiceInteractionAction.ChangeTtsPrompt -> {
                currentState.copy(
                    tts = currentState.tts.copy(
                        input = action.text
                    )
                )
            }

            is VoiceInteractionAction.ChangeChatInput -> {
                currentState.copy(
                    chat = currentState.chat.copy(
                        input = action.text
                    )
                )
            }

            is VoiceInteractionAction.WakeUpStart -> {
                currentState.copy(
                    wakeUp = currentState.wakeUp.copy(
                        started = true
                    )
                )
            }

            is VoiceInteractionAction.WakeUpStop -> {
                currentState.copy(
                    wakeUp = currentState.wakeUp.copy(
                        started = false
                    )
                )
            }

            is VoiceInteractionAction.AsrStart -> {
                currentState.copy(
                    asr = currentState.asr.copy(
                        started = true
                    )
                )
            }

            is VoiceInteractionAction.AsrStop -> {
                currentState.copy(
                    asr = currentState.asr.copy(
                        started = false
                    )
                )
            }

            is VoiceInteractionAction.TtsPlay -> {
                currentState.copy(
                    tts = currentState.tts.copy(
                        playing = true
                    )
                )
            }

            is VoiceInteractionAction.TtsStop -> {
                currentState.copy(
                    tts = currentState.tts.copy(
                        playing = false
                    )
                )
            }

            is VoiceInteractionAction.ChatStart -> {
                currentState.copy(
                    chat = currentState.chat.copy(
                        started = true
                    )
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
                dataSource.saveTtsInput(currentState.tts.input)
                navigator.navigateBack()
            }

            is VoiceInteractionAction.TtsPlay -> {
                dataSource.saveTtsInput(currentState.tts.input)
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
