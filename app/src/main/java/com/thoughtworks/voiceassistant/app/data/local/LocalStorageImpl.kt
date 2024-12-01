package com.thoughtworks.voiceassistant.app.data.local

import android.content.Context
import com.google.gson.Gson
import com.thoughtworks.voiceassistant.app.R
import com.thoughtworks.voiceassistant.app.data.models.AbilityDataCollection

class LocalStorageImpl(
    private val context: Context,
    private val gson: Gson,
) : LocalStorage {
    private val sharedPreferenceUtil = SharedPreferenceUtils(context)

    override fun loadAbilityDataCollection(): AbilityDataCollection? {
        val abilityDataConfigJson = sharedPreferenceUtil.getString(KEY_ABILITY_DATA_COLLECTION)
        abilityDataConfigJson?.let {
            return gson.fromJson(it, AbilityDataCollection::class.java)
        }

        return null
    }

    override fun saveAbilityDataCollection(abilityDataCollection: AbilityDataCollection) {
        val abilityDataCollectionJson = gson.toJson(abilityDataCollection)
        sharedPreferenceUtil.setString(KEY_ABILITY_DATA_COLLECTION, abilityDataCollectionJson)
    }

    override fun loadTtsInput(): String {
        return sharedPreferenceUtil.getString(KEY_TTS_INPUT)
            ?: context.getString(R.string.tts_input)
    }

    override fun saveTtsInput(ttsInput: String) {
        sharedPreferenceUtil.setString(KEY_TTS_INPUT, ttsInput)
    }

    override fun loadChatInput(): String {
        return sharedPreferenceUtil.getString(KEY_CHAT_INPUT)
            ?: context.getString(R.string.chat_input)
    }

    override fun saveChatInput(chatInput: String) {
        sharedPreferenceUtil.setString(KEY_CHAT_INPUT, chatInput)
    }

    companion object {
        private const val KEY_ABILITY_DATA_COLLECTION = "ability_data_collection"
        private const val KEY_TTS_INPUT = "tts_input"
        private const val KEY_CHAT_INPUT = "chat_input"
    }
}