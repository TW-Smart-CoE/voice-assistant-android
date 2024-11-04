package com.thoughtworks.voiceassistant.app.utils.navigator

import androidx.navigation.NavController
import com.thoughtworks.voiceassistant.app.ui.navigation.Screen

class NavigatorImpl(private val navController: NavController) : Navigator {
    override fun navigateBack() {
        navController.navigateUp()
    }

    override fun navigateToAbilityConfigScreen() {
        navController.navigate(Screen.AbilityConfigScreen.route) {
            popUpTo(Screen.PermissionScreen.route) {
                inclusive = true // This removes the PermissionScreen from the back stack
            }
            launchSingleTop = true // Ensures that only one instance of AbilityConfigScreen is created

        }
    }

    override fun navigateToVoiceScreen() {
        navController.navigate(Screen.VoiceInteractionScreen.route)
    }
}