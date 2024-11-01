package com.thoughtworks.voiceassistant.app.di

import android.content.Context
import com.google.gson.Gson
import com.thoughtworks.voiceassistant.app.data.DataSource
import com.thoughtworks.voiceassistant.app.foundation.dispatcher.CoroutineDispatchers
import com.thoughtworks.voiceassistant.app.utils.navigator.Navigator
import kotlinx.coroutines.CoroutineScope

interface Dependency {
    fun setNavigator(navigator: Navigator)

    val navigator: Navigator
    val context: Context
    val coroutineDispatchers: CoroutineDispatchers
    val coroutineScope: CoroutineScope
    val gson: Gson
    val dataSource: DataSource
}