package com.thoughtworks.voiceassistant.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.thoughtworks.voiceassistant.app.di.Dependency
import com.thoughtworks.voiceassistant.app.ui.views.abilityconfig.AbilityConfigScreen
import com.thoughtworks.voiceassistant.app.ui.views.voice.VoiceInteractionScreen
import com.thoughtworks.voiceassistant.app.utils.navigator.NavigatorImpl

@Composable
fun Navigation(dependency: Dependency) {
    val navController = rememberNavController()
    dependency.setNavigator(NavigatorImpl(navController))

    NavHost(navController = navController, startDestination = Screen.AbilityConfigScreen.route) {
        composable(route = Screen.AbilityConfigScreen.route) {
            AbilityConfigScreen(dependency)
        }
        composable(route = Screen.VoiceInteractionScreen.route) {
            VoiceInteractionScreen(dependency)
        }
    }
}