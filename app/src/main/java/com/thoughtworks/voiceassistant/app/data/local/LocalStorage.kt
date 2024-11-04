package com.thoughtworks.voiceassistant.app.data.local

import com.thoughtworks.voiceassistant.app.data.models.AbilityDataCollection

interface LocalStorage {
    fun loadAbilityDataCollection(): AbilityDataCollection?
    fun saveAbilityDataCollection(abilityDataCollection: AbilityDataCollection)
    fun loadTtsPrompt(): String
    fun saveTtsPrompt(ttsPrompt: String)
}