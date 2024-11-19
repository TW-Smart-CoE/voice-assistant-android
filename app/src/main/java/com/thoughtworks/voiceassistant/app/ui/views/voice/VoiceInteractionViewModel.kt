package com.thoughtworks.voiceassistant.app.ui.views.voice

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.thoughtworks.voiceassistant.app.data.models.AbilityDataCollection
import com.thoughtworks.voiceassistant.app.definitions.Ability
import com.thoughtworks.voiceassistant.app.di.Dependency
import com.thoughtworks.voiceassistant.app.foundation.mvi.DefaultStore
import com.thoughtworks.voiceassistant.app.foundation.mvi.MVIViewModel
import com.thoughtworks.voiceassistant.app.foundation.mvi.Store
import com.thoughtworks.voiceassistant.app.utils.voice.VoiceManager
import kotlinx.coroutines.launch

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
    private lateinit var voiceManager: VoiceManager

    init {
        initAbilities(dependency.context)
        viewModelScope.launch {
            voiceManager.initialize()
        }
    }

    private fun initAbilities(context: Context) {
        abilityCollection = dataSource.loadAbilityDataCollection()!!
        voiceManager = VoiceManager(context, abilityCollection)
        sendAction(VoiceInteractionAction.UpdateAbilityDataCollection(abilityCollection))
    }

    override fun reduce(
        currentState: VoiceInteractionState,
        action: VoiceInteractionAction,
    ): VoiceInteractionState {
        return when (action) {
            is VoiceInteractionAction.UpdateAbilityDataCollection -> {
                currentState.copy(
                    wakeUp = WakeUpState(
                        title = "${Ability.WAKE_UP.displayName}: ${abilityCollection.wakeUp.provider}",
                        listening = false
                    ),
                    asr = AsrState(
                        title = "${Ability.ASR.displayName}: ${abilityCollection.asr.provider}",
                        listening = false
                    ),
                    tts = currentState.tts.copy(
                        title = "${Ability.TTS.displayName}: ${abilityCollection.tts.provider}",
                        speaking = false,
                        input = currentState.tts.input

                    ),
                    chat = ChatState(
                        title = "${Ability.CHAT.displayName}: ${abilityCollection.chat.provider}",
                        started = false
                    )
                )
            }

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

            is VoiceInteractionAction.WakeUpListen -> {
                currentState.copy(
                    wakeUp = currentState.wakeUp.copy(
                        listening = true
                    )
                )
            }

            is VoiceInteractionAction.WakeUpStop -> {
                currentState.copy(
                    wakeUp = currentState.wakeUp.copy(
                        listening = false
                    )
                )
            }

            is VoiceInteractionAction.AsrListen -> {
                currentState.copy(
                    asr = currentState.asr.copy(
                        listening = true
                    )
                )
            }

            is VoiceInteractionAction.AsrStop -> {
                currentState.copy(
                    asr = currentState.asr.copy(
                        listening = false
                    )
                )
            }

            is VoiceInteractionAction.TtsSpeak -> {
                currentState.copy(
                    tts = currentState.tts.copy(
                        speaking = true
                    )
                )
            }

            is VoiceInteractionAction.TtsStop -> {
                currentState.copy(
                    tts = currentState.tts.copy(
                        speaking = false
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

            is VoiceInteractionAction.AsrListen -> {
                asrListen()
            }

            is VoiceInteractionAction.AsrStop -> {
                voiceManager.asr.stop()
            }

            is VoiceInteractionAction.TtsSpeak -> {
                dataSource.saveTtsInput(currentState.tts.input)
                ttsSpeak(currentState.tts.input)
            }

            is VoiceInteractionAction.TtsStop -> {
                voiceManager.tts.stop()
            }

            else -> {
            }
        }
    }

    private fun asrListen() {
        viewModelScope.launch {
            val result = voiceManager.asr.listen {
                Log.d(TAG, "onHeard: $it")
                sendEvent(VoiceInteractionEvent.ShowToast(it))
            }
            Log.d(TAG, result.toString())
            sendAction(VoiceInteractionAction.AsrStop)
        }
    }

    private fun ttsSpeak(text: String) {
        viewModelScope.launch {
            val result = voiceManager.tts.speak(text, emptyMap())
            Log.d(TAG, result.toString())
            sendAction(VoiceInteractionAction.TtsStop)
        }
    }

    companion object {
        private const val TAG = "VoiceInteractionViewModel"
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
