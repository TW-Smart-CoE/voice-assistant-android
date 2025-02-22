package com.thoughtworks.voiceassistant.app.ui.views.voice

import android.content.Context
import android.media.AudioManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.thoughtworks.voiceassistant.alibabakit.abilities.tts.TtsParams
import com.thoughtworks.voiceassistant.app.data.models.AbilityDataCollection
import com.thoughtworks.voiceassistant.app.definitions.Ability
import com.thoughtworks.voiceassistant.app.di.Dependency
import com.thoughtworks.voiceassistant.app.foundation.mvi.DefaultStore
import com.thoughtworks.voiceassistant.app.foundation.mvi.MVIViewModel
import com.thoughtworks.voiceassistant.app.foundation.mvi.Store
import com.thoughtworks.voiceassistant.app.utils.voice.VoiceManager
import com.thoughtworks.voiceassistant.core.abilities.Chat
import com.thoughtworks.voiceassistant.core.utils.AudioUtils
import com.thoughtworks.voiceassistant.volcenginekit.abilities.tts.SpeakParams
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class VoiceInteractionViewModel(
    dependency: Dependency,
    store: Store<VoiceInteractionState, VoiceInteractionEvent, VoiceInteractionAction> = DefaultStore(
        initialState = VoiceInteractionState(
            tts = TtsState(
                input = dependency.dataSource.loadTtsInput(),
            ),
            chat = ChatState(
                input = dependency.dataSource.loadChatInput(),
            ),
        )
    ),
) : MVIViewModel<VoiceInteractionState, VoiceInteractionEvent, VoiceInteractionAction>(store) {
    private val navigator = dependency.navigator
    private val dataSource = dependency.dataSource
    private val ioDispatcher = dependency.coroutineDispatchers.ioDispatcher
    private val audioUtils = AudioUtils(dependency.context)

    private lateinit var abilityCollection: AbilityDataCollection
    private lateinit var voiceManager: VoiceManager

    init {
        initAbilities(dependency.context)
        audioUtils.setVolume(
            AudioManager.STREAM_VOICE_CALL,
            audioUtils.getMaxVolume(AudioManager.STREAM_VOICE_CALL)
        )
//        checkStatusAsync()
    }

    private fun checkStatusAsync() {
        viewModelScope.launch(ioDispatcher) {
            while (true) {
                Log.d(
                    TAG,
                    "isTtsSpeaking = ${voiceManager.tts.isSpeaking()}, isAsrListening = ${voiceManager.asr.isListening()}, isWakeUpListening = ${voiceManager.wakeUp.isListening()}"
                )
                delay(1000L)
            }
        }
    }

    private fun initAbilities(context: Context) {
        abilityCollection = dataSource.loadAbilityDataCollection()!!
        voiceManager = VoiceManager(context, abilityCollection)
        viewModelScope.launch {
            voiceManager.initialize()
        }
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
                        started = false,
                        input = currentState.chat.input
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

            is VoiceInteractionAction.ChatStop -> {
                currentState.copy(
                    chat = currentState.chat.copy(
                        started = false
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
                dataSource.saveChatInput(currentState.chat.input)
                navigator.navigateBack()
            }

            is VoiceInteractionAction.WakeUpListen -> {
                wakeUpListen()
            }

            is VoiceInteractionAction.WakeUpStop -> {
                voiceManager.wakeUp.stop()
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

            is VoiceInteractionAction.ChatStart -> {
                chatStart(currentState.chat.input)
            }

            is VoiceInteractionAction.ChatStop -> {
                chatStop()
            }

            else -> {
            }
        }
    }

    private fun wakeUpListen() {
        viewModelScope.launch {
            val result = voiceManager.wakeUp.listen {
                Log.d(TAG, "onWakeUp: $it")
                sendEvent(VoiceInteractionEvent.ShowToast(it.toString()))
            }
            Log.d(TAG, result.toString())
            sendAction(VoiceInteractionAction.WakeUpStop)
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

    private fun chatStart(chatText: String) {
        viewModelScope.launch {
            val result = voiceManager.chat.chat(Chat.Message("user", chatText))
            Log.d(TAG, result.toString())
            if (result.isSuccess) {
                sendEvent(VoiceInteractionEvent.ShowToast(result.message.content, true))
            }
            sendAction(VoiceInteractionAction.ChatStop)
        }
    }

    private fun chatStop() {
        voiceManager.chat.stop()
    }

    override fun onCleared() {
        voiceManager.release()
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
