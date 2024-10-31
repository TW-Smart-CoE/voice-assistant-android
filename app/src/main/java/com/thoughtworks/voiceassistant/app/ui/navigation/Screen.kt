package com.thoughtworks.voiceassistant.app.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Main : Screen

    @Serializable
    data class Voice(val text: String) : Screen
}

