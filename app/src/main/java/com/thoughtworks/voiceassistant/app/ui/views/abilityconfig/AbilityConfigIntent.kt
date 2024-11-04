package com.thoughtworks.voiceassistant.app.ui.views.abilityconfig

import com.thoughtworks.voiceassistant.app.data.models.AbilityDataCollection
import com.thoughtworks.voiceassistant.app.definitions.ServiceProvider
import com.thoughtworks.voiceassistant.app.foundation.mvi.model.Action
import com.thoughtworks.voiceassistant.app.foundation.mvi.model.Event
import com.thoughtworks.voiceassistant.app.foundation.mvi.model.State

data class AbilityConfigState(
    val abilityDataCollection: AbilityDataCollection,
    val ttsProviderList: List<ServiceProvider> = emptyList(),
    val asrProviderList: List<ServiceProvider> = emptyList(),
    val wakeUpProviderList: List<ServiceProvider> = emptyList(),
    val chatProviderList: List<ServiceProvider> = emptyList(),
) : State

sealed class AbilityConfigEvent : Event

sealed interface AbilityConfigAction : Action {
    data class SelectTtsProvider(val provider: ServiceProvider) : AbilityConfigAction
    data class SelectAsrProvider(val provider: ServiceProvider) : AbilityConfigAction
    data class SelectWakeUpProvider(val provider: ServiceProvider) : AbilityConfigAction
    data class SelectChatProvider(val provider: ServiceProvider) : AbilityConfigAction
    data object ClickOk : AbilityConfigAction
}