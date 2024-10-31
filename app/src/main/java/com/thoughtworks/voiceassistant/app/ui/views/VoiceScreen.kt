package com.thoughtworks.voiceassistant.app.ui.views

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import com.thoughtworks.voiceassistant.app.di.Dependency
import com.thoughtworks.voiceassistant.app.ui.navigation.Screen


@Composable
fun VoiceScreen(dependency: Dependency, voice: Screen.Voice) {
    Log.d(TAG, voice.text)
    Box {  }
}

const val TAG = "VoiceScreen"