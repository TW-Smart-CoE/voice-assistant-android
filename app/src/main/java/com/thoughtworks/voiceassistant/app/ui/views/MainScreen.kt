package com.thoughtworks.voiceassistant.app.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.thoughtworks.voiceassistant.app.R
import com.thoughtworks.voiceassistant.app.ui.components.AbilityItem
import com.thoughtworks.voiceassistant.app.definitions.Ability
import com.thoughtworks.voiceassistant.app.definitions.ServiceProvider

@Composable
fun MainScreen() {
    val context = LocalContext.current

    var ttsProvider by remember { mutableStateOf(ServiceProvider.ALIBABA) }
    var asrProvider by remember { mutableStateOf(ServiceProvider.ALIBABA) }
    var wakeUpProvider by remember { mutableStateOf(ServiceProvider.BAIDU) }
    var chatProvider by remember { mutableStateOf(ServiceProvider.OPENAI) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(Alignment.CenterHorizontally),
                text = context.getString(R.string.app_name),
                style = MaterialTheme.typography.titleLarge)
            AbilityItem(
                Ability.TTS, listOf(
                    ServiceProvider.ALIBABA,
                    ServiceProvider.GOOGLE,
                ), ttsProvider
            ) { ttsProvider = it }
            AbilityItem(
                Ability.ASR, listOf(
                    ServiceProvider.BAIDU,
                    ServiceProvider.IFLYTEK,
                ), asrProvider
            ) { asrProvider = it }
            AbilityItem(
                Ability.WAKE_UP, listOf(
                    ServiceProvider.BAIDU,
                    ServiceProvider.IFLYTEK,
                ), wakeUpProvider
            ) { wakeUpProvider = it }
            AbilityItem(
                Ability.CHAT, listOf(
                    ServiceProvider.BAIDU,
                    ServiceProvider.OPENAI,
                ), chatProvider
            ) { chatProvider = it }

            Spacer(modifier = Modifier.weight(1f))
        }

        Button(
            onClick = { /* Handle OK click */ },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            Text("OK")
        }
    }
}