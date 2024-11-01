package com.thoughtworks.voiceassistant.app.data.local

import android.content.Context
import com.google.gson.Gson
import com.thoughtworks.voiceassistant.app.data.models.AbilityDataCollection

class LocalStorageImpl(
    context: Context,
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

    companion object {
        private const val KEY_ABILITY_DATA_COLLECTION = "ability_data_collection"
    }
}