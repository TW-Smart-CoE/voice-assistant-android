package com.thoughtworks.voiceassistant.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.thoughtworks.voiceassistant.app.di.Dependency
import com.thoughtworks.voiceassistant.app.ui.views.MainScreen
import com.thoughtworks.voiceassistant.app.ui.views.VoiceScreen

@Composable
fun Navigation(dependency: Dependency) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Main) {
        composable<Screen.Main> {
            MainScreen(
                dependency,
                onNavigateToVoiceScreen = {
                    navController.navigate(Screen.Voice("Hello, world!"))
                }
            )
        }
        composable<Screen.Voice> { backStackEntry ->
            val voice: Screen.Voice = backStackEntry.toRoute()
            VoiceScreen(dependency, voice)
        }
    }
}