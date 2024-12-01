package com.thoughtworks.voiceassistant.app.data.local

import com.thoughtworks.voiceassistant.app.data.models.AbilityDataCollection

interface LocalStorage {
    fun loadAbilityDataCollection(): AbilityDataCollection?
    fun saveAbilityDataCollection(abilityDataCollection: AbilityDataCollection)
    fun loadTtsInput(): String
    fun saveTtsInput(ttsInput: String)
    fun loadChatInput(): String
    fun saveChatInput(chatInput: String)
}