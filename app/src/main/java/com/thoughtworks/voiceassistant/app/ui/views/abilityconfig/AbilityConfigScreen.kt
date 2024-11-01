package com.thoughtworks.voiceassistant.app.ui.views.abilityconfig

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thoughtworks.voiceassistant.app.R
import com.thoughtworks.voiceassistant.app.definitions.Ability
import com.thoughtworks.voiceassistant.app.definitions.ServiceProvider
import com.thoughtworks.voiceassistant.app.di.Dependency
import com.thoughtworks.voiceassistant.app.ui.components.AbilityItem

@Composable
fun AbilityConfigScreen(
    dependency: Dependency,
) {
    val factory = remember { AbilityConfigViewModelFactory(dependency) }
    val viewModel: AbilityConfigViewModel = viewModel(factory = factory)
    val state = viewModel.uiState.collectAsState()

    val context = LocalContext.current

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
                style = MaterialTheme.typography.titleLarge
            )
            AbilityList(
                state.value,
                viewModel::sendAction,
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Button(
            onClick = {
                viewModel.sendAction(AbilityConfigAction.ClickOk)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            Text("OK")
        }
    }
}

@Composable
private fun AbilityList(
    state: AbilityConfigState,
    sendAction: (AbilityConfigAction) -> Unit,
) {
    AbilityItem(
        Ability.TTS,
        state.ttsProviderList,
        ServiceProvider.valueOf(state.abilityDataCollection.tts.provider),
    ) { sendAction(AbilityConfigAction.SelectTtsProvider(it)) }
    AbilityItem(
        Ability.ASR,
        state.asrProviderList,
        ServiceProvider.valueOf(state.abilityDataCollection.asr.provider),
    ) { sendAction(AbilityConfigAction.SelectAsrProvider(it)) }
    AbilityItem(
        Ability.WAKE_UP,
        state.wakeUpProviderList,
        ServiceProvider.valueOf(state.abilityDataCollection.wakeUp.provider),
    ) { sendAction(AbilityConfigAction.SelectWakeUpProvider(it)) }
    AbilityItem(
        Ability.CHAT,
        state.chatProviderList,
        ServiceProvider.valueOf(state.abilityDataCollection.chat.provider),
    ) { sendAction(AbilityConfigAction.SelectChatProvider(it)) }
}