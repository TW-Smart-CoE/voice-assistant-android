package com.thoughtworks.voiceassistant.app.data

import android.content.Context
import com.google.gson.Gson
import com.thoughtworks.voiceassistant.app.data.local.LocalStorageImpl
import com.thoughtworks.voiceassistant.app.data.models.AbilityDataCollection
import com.thoughtworks.voiceassistant.app.definitions.Ability
import com.thoughtworks.voiceassistant.app.definitions.ServiceProvider

class DataRepository(
    context: Context,
    gson: Gson,
) : DataSource {
    private val localStorage = LocalStorageImpl(context, gson)

    override fun getAbilityServiceProviderList(ability: Ability): List<ServiceProvider> {
        return when (ability) {
            Ability.TTS -> getTtsServiceProviderList()
            Ability.ASR -> getAsrServiceProviderList()
            Ability.WAKE_UP -> getWakeUpServiceProviderList()
            Ability.CHAT -> getChatServiceProviderList()
        }
    }

    override fun loadAbilityDataCollection(): AbilityDataCollection? {
        return localStorage.loadAbilityDataCollection()
    }

    override fun saveAbilityDataCollection(abilityDataCollection: AbilityDataCollection) {
        localStorage.saveAbilityDataCollection(abilityDataCollection)
    }

    override fun loadTtsInput(): String {
        return localStorage.loadTtsInput()
    }

    override fun saveTtsInput(ttsInput: String) {
        localStorage.saveTtsInput(ttsInput)
    }

    private fun getTtsServiceProviderList(): List<ServiceProvider> {
        return listOf(ServiceProvider.ALIBABA)
    }

    private fun getAsrServiceProviderList(): List<ServiceProvider> {
        return listOf(ServiceProvider.ALIBABA)
    }

    private fun getWakeUpServiceProviderList(): List<ServiceProvider> {
        return listOf(ServiceProvider.PICOVOICE)
    }

    private fun getChatServiceProviderList(): List<ServiceProvider> {
        return listOf(ServiceProvider.OPENAI)
    }
}