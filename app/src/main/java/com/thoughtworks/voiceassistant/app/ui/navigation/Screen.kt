package com.thoughtworks.voiceassistant.app.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object AbilityConfig : Screen

    @Serializable
    data object VoiceInteraction : Screen
}

