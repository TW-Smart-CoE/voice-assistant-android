package com.thoughtworks.voiceassistant.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.thoughtworks.voiceassistant.app.di.Dependency
import com.thoughtworks.voiceassistant.app.ui.navigation.Navigation
import com.thoughtworks.voiceassistant.app.ui.theme.VoiceassistantandroidTheme

class MainActivity : ComponentActivity() {
    private lateinit var dependency: Dependency

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
        enableEdgeToEdge()
        setContent {
            VoiceassistantandroidTheme {
                Navigation(dependency)
            }
        }
    }

    private fun initialize() {
        dependency = (application as VoiceApp).getDependency()
    }
}
