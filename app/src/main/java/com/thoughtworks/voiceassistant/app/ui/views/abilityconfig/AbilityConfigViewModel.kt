package com.thoughtworks.voiceassistant.app.ui.views.abilityconfig

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thoughtworks.voiceassistant.app.data.models.AbilityData
import com.thoughtworks.voiceassistant.app.data.models.AbilityDataCollection
import com.thoughtworks.voiceassistant.app.definitions.Ability
import com.thoughtworks.voiceassistant.app.di.Dependency
import com.thoughtworks.voiceassistant.app.foundation.mvi.DefaultStore
import com.thoughtworks.voiceassistant.app.foundation.mvi.MVIViewModel
import com.thoughtworks.voiceassistant.app.foundation.mvi.Store
import kotlinx.coroutines.launch


class AbilityConfigViewModel(
    dependency: Dependency,
    store: Store<AbilityConfigState, AbilityConfigEvent, AbilityConfigAction> =
        DefaultStore(
            initialState = AbilityConfigState(
                abilityDataCollection = AbilityDataCollection(
                    AbilityData(
                        Ability.TTS.name,
                        dependency.dataSource.getAbilityServiceProviderList(Ability.TTS)
                            .first().name
                    ),
                    AbilityData(
                        Ability.ASR.name,
                        dependency.dataSource.getAbilityServiceProviderList(Ability.ASR)
                            .first().name
                    ),
                    AbilityData(
                        Ability.WAKE_UP.name,
                        dependency.dataSource.getAbilityServiceProviderList(Ability.WAKE_UP)
                            .first().name
                    ),
                    AbilityData(
                        Ability.CHAT.name,
                        dependency.dataSource.getAbilityServiceProviderList(Ability.CHAT)
                            .first().name
                    ),
                ),
                ttsProviderList = dependency.dataSource.getAbilityServiceProviderList(Ability.TTS),
                asrProviderList = dependency.dataSource.getAbilityServiceProviderList(Ability.ASR),
                wakeUpProviderList = dependency.dataSource.getAbilityServiceProviderList(Ability.WAKE_UP),
                chatProviderList = dependency.dataSource.getAbilityServiceProviderList(Ability.CHAT),
            )
        ),
) : MVIViewModel<AbilityConfigState, AbilityConfigEvent, AbilityConfigAction>(store) {

    private val navigator = dependency.navigator
    private val dataSource = dependency.dataSource

    init {
        loadAbilityDataCollection()
    }

    override fun reduce(
        currentState: AbilityConfigState,
        action: AbilityConfigAction,
    ): AbilityConfigState {
        return when (action) {
            is AbilityConfigAction.OnLoadAbilityDataCollection -> {
                currentState.copy(
                    abilityDataCollection = action.abilityDataCollection.copy(
                        tts = action.abilityDataCollection.tts,
                        asr = action.abilityDataCollection.asr,
                        wakeUp = action.abilityDataCollection.wakeUp,
                        chat = action.abilityDataCollection.chat,
                    )
                )
            }

            is AbilityConfigAction.SelectTtsProvider -> {
                currentState.copy(
                    abilityDataCollection = currentState.abilityDataCollection.copy(
                        tts = AbilityData(Ability.TTS.name, action.provider.name)
                    )
                )
            }

            is AbilityConfigAction.SelectAsrProvider -> {
                currentState.copy(
                    abilityDataCollection = currentState.abilityDataCollection.copy(
                        asr = AbilityData(Ability.ASR.name, action.provider.name)
                    )
                )
            }

            is AbilityConfigAction.SelectWakeUpProvider -> {
                currentState.copy(
                    abilityDataCollection = currentState.abilityDataCollection.copy(
                        wakeUp = AbilityData(Ability.WAKE_UP.name, action.provider.name)
                    )
                )
            }

            is AbilityConfigAction.SelectChatProvider -> {
                currentState.copy(
                    abilityDataCollection = currentState.abilityDataCollection.copy(
                        chat = AbilityData(Ability.CHAT.name, action.provider.name)
                    )
                )
            }

            else -> {
                currentState
            }
        }
    }

    override fun runSideEffect(action: AbilityConfigAction, currentState: AbilityConfigState) {
        when (action) {
            is AbilityConfigAction.ClickOk -> {
                saveAndNavigateToVoiceScreen(currentState.abilityDataCollection)
            }

            else -> {
            }
        }
    }

    private fun saveAndNavigateToVoiceScreen(abilityDataCollection: AbilityDataCollection) {
        scope.launch {
            dataSource.saveAbilityDataCollection(abilityDataCollection)
            navigator.navigateToVoiceScreen()
        }
    }

    private fun loadAbilityDataCollection() {
        scope.launch {
            val abilityDataCollection = dataSource.loadAbilityDataCollection()
            abilityDataCollection?.let {
                sendAction(AbilityConfigAction.OnLoadAbilityDataCollection(it))
            }
        }
    }
}

class AbilityConfigViewModelFactory(
    private val dependency: Dependency,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AbilityConfigViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AbilityConfigViewModel(dependency) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
