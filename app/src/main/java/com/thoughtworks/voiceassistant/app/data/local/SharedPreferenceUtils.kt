package com.thoughtworks.voiceassistant.app.data.local

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceUtils(context: Context) {
    companion object {
        private const val PREFS_NAME = "com.thoughtworks.voiceassistant.app.preferences"
    }

    private val preferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setString(key: String, value: String) {
        val editor = preferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    @SuppressLint("ApplySharedPref")
    fun setStringSync(key: String, value: String) {
        val editor = preferences.edit()
        editor.putString(key, value)
        editor.commit()
    }

    fun getString(key: String, defaultValue: String? = null): String? {
        return preferences.getString(key, defaultValue)
    }

    fun setInt(key: String, value: Int) {
        val editor = preferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    @SuppressLint("ApplySharedPref")
    fun setIntSync(key: String, value: Int) {
        val editor = preferences.edit()
        editor.putInt(key, value)
        editor.commit()
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        return preferences.getInt(key, defaultValue)
    }

    fun setBoolean(key: String, value: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    @SuppressLint("ApplySharedPref")
    fun setBooleanSync(key: String, value: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(key, value)
        editor.commit()
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return preferences.getBoolean(key, defaultValue)
    }

    fun setFloat(key: String, value: Float) {
        val editor = preferences.edit()
        editor.putFloat(key, value)
        editor.apply()
    }

    @SuppressLint("ApplySharedPref")
    fun setFloatSync(key: String, value: Float) {
        val editor = preferences.edit()
        editor.putFloat(key, value)
        editor.commit()
    }

    fun getFloat(key: String, defaultValue: Float = 0f): Float {
        return preferences.getFloat(key, defaultValue)
    }

    fun setLong(key: String, value: Long) {
        val editor = preferences.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    @SuppressLint("ApplySharedPref")
    fun setLongSync(key: String, value: Long) {
        val editor = preferences.edit()
        editor.putLong(key, value)
        editor.commit()
    }

    fun getLong(key: String, defaultValue: Long = 0L): Long {
        return preferences.getLong(key, defaultValue)
    }
}