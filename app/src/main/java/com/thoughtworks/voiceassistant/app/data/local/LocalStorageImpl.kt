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

    override fun loadTtsPrompt(): String {
        return sharedPreferenceUtil.getString(KEY_TTS_PROMPT)
            ?: context.getString(R.string.tts_prompt)
    }

    override fun saveTtsPrompt(ttsPrompt: String) {
        sharedPreferenceUtil.setString(KEY_TTS_PROMPT, ttsPrompt)
    }

    companion object {
        private const val KEY_ABILITY_DATA_COLLECTION = "ability_data_collection"
        private const val KEY_TTS_PROMPT = "tts_prompt"
    }
}