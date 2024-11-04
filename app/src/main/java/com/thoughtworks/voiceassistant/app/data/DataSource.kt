package com.thoughtworks.voiceassistant.app.data

import com.thoughtworks.voiceassistant.app.data.models.AbilityDataCollection
import com.thoughtworks.voiceassistant.app.definitions.Ability
import com.thoughtworks.voiceassistant.app.definitions.ServiceProvider

interface DataSource {
    fun getAbilityServiceProviderList(ability: Ability): List<ServiceProvider>
    fun loadAbilityDataCollection(): AbilityDataCollection?
    fun saveAbilityDataCollection(abilityDataCollection: AbilityDataCollection)
    fun loadTtsInput(): String
    fun saveTtsInput(ttsInput: String)
}