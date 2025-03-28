package com.thoughtworks.voiceassistant.app.ui.views.voice

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thoughtworks.voiceassistant.app.R
import com.thoughtworks.voiceassistant.app.di.Dependency
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceInteractionScreen(dependency: Dependency) {
    val factory = remember { VoiceInteractionViewModelFactory(dependency) }
    val viewModel: VoiceInteractionViewModel = viewModel(factory = factory)
    val state = viewModel.uiState.collectAsState()
    val event = viewModel.uiEvent
    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    BackHandler {
        viewModel.sendAction(VoiceInteractionAction.NavigateBack)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(context.getString(R.string.app_name)) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.sendAction(VoiceInteractionAction.NavigateBack) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back_24_black),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { WakeUpCard(state.value, viewModel::sendAction) }
                item { AsrCard(state.value, viewModel::sendAction) }
                item { TtsCard(state.value, viewModel::sendAction) }
                item { ChatCard(state.value, viewModel::sendAction) }
            }
        }
    )

    LaunchedEffect(Unit) {
        event.onEach { event ->
            when (event) {
                is VoiceInteractionEvent.ShowToast -> {
                    Toast.makeText(
                        context,
                        event.text,
                        if (event.isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }.launchIn(scope)
    }
}

@Composable
fun WakeUpCard(
    state: VoiceInteractionState,
    sendAction: (VoiceInteractionAction) -> Unit,
) {
    val context = LocalContext.current

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(state.wakeUp.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    enabled = !state.wakeUp.listening && state.wakeUp.title.contains(":"),
                    onClick = { sendAction(VoiceInteractionAction.WakeUpListen) }
                ) {
                    Text(context.getString(R.string.listen_btn))
                }
                Button(
                    modifier = Modifier.weight(1f),
                    enabled = state.wakeUp.listening && state.wakeUp.title.contains(":"),
                    onClick = { sendAction(VoiceInteractionAction.WakeUpStop) }
                ) {
                    Text(context.getString(R.string.stop_btn))
                }
            }
        }
    }
}

@Composable
fun AsrCard(
    state: VoiceInteractionState,
    sendAction: (VoiceInteractionAction) -> Unit,
) {
    val context = LocalContext.current

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(state.asr.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    enabled = !state.asr.listening && state.asr.title.contains(":"),
                    onClick = { sendAction(VoiceInteractionAction.AsrListen) }
                ) {
                    Text(context.getString(R.string.listen_btn))
                }
                Button(
                    modifier = Modifier.weight(1f),
                    enabled = state.asr.listening && state.asr.title.contains(":"),
                    onClick = { sendAction(VoiceInteractionAction.AsrStop) }
                ) {
                    Text(context.getString(R.string.stop_btn))
                }
            }
        }
    }
}

@Composable
fun TtsCard(
    state: VoiceInteractionState,
    sendAction: (VoiceInteractionAction) -> Unit,
) {
    val context = LocalContext.current

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(state.tts.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            BasicTextField(
                value = state.tts.input,
                onValueChange = { sendAction(VoiceInteractionAction.ChangeTtsPrompt(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(8.dp)
                    .background(
                        MaterialTheme.colorScheme.surface,
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(8.dp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    enabled = !state.tts.speaking && state.tts.title.contains(":"),
                    onClick = { sendAction(VoiceInteractionAction.TtsSpeak) }
                ) {
                    Text(context.getString(R.string.speak_btn))
                }
                Button(
                    modifier = Modifier.weight(1f),
                    enabled = state.tts.speaking && state.tts.title.contains(":"),
                    onClick = { sendAction(VoiceInteractionAction.TtsStop) }
                ) {
                    Text(context.getString(R.string.stop_btn))
                }
            }
        }
    }
}

@Composable
fun ChatCard(
    state: VoiceInteractionState,
    sendAction: (VoiceInteractionAction) -> Unit,
) {
    val context = LocalContext.current

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(state.chat.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            BasicTextField(
                value = state.chat.input,
                onValueChange = { sendAction(VoiceInteractionAction.ChangeChatInput(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(8.dp)
                    .background(
                        MaterialTheme.colorScheme.surface,
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(8.dp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    enabled = !state.chat.started && state.chat.title.contains(":"),
                    onClick = { sendAction(VoiceInteractionAction.ChatStart) }
                ) {
                    Text(context.getString(R.string.chat_btn))
                }
                Button(
                    modifier = Modifier.weight(1f),
                    enabled = state.chat.started && state.chat.title.contains(":"),
                    onClick = { sendAction(VoiceInteractionAction.ChatStop) }
                ) {
                    Text(context.getString(R.string.stop_btn))
                }
            }
        }
    }
}