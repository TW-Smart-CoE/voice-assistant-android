package com.thoughtworks.voiceassistant.app.utils.navigator

import androidx.navigation.NavController
import com.thoughtworks.voiceassistant.app.ui.navigation.Screen

class NavigatorImpl(private val navController: NavController) : Navigator {
    override fun navigateToAbilityConfigScreen() {
        navController.navigate(Screen.AbilityConfigScreen.route)
    }

    override fun navigateToVoiceScreen() {
        navController.navigate(Screen.VoiceInteractionScreen.route)
    }
}