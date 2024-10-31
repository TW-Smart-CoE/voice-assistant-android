package com.thoughtworks.voiceassistant.app

import android.app.Application
import com.thoughtworks.voiceassistant.app.di.Dependency
import com.thoughtworks.voiceassistant.app.di.DependencyImpl

class VoiceApp : Application() {
    private lateinit var dependency: DependencyImpl

    override fun onCreate() {
        super.onCreate()
        dependency = DependencyImpl(this)
    }

    fun getDependency(): Dependency {
        return dependency
    }
}