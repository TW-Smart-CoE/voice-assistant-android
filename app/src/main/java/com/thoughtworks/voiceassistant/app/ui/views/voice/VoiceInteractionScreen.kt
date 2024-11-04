package com.thoughtworks.voiceassistant.app.ui.views.voice

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.thoughtworks.voiceassistant.app.R
import com.thoughtworks.voiceassistant.app.definitions.Ability
import com.thoughtworks.voiceassistant.app.di.Dependency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceInteractionScreen(dependency: Dependency) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(context.getString(R.string.app_name)) },
                navigationIcon = {
                    IconButton(onClick = { dependency.navigator.navigateBack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back_24_black),
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
                item { WakeUpCard() }
                item { AsrCard() }
                item { TtsCard() }
                item { ChatCard() }
            }
        }
    )
}

@Composable
fun WakeUpCard() {
    val context = LocalContext.current

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(Ability.WAKE_UP.displayName, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { /* TODO: Start Wakeup */ }) {
                    Text(context.getString(R.string.start_btn))
                }
                Button(onClick = { /* TODO: Stop Wakeup */ }) {
                    Text(context.getString(R.string.stop_btn))
                }
            }
        }
    }
}

@Composable
fun AsrCard() {
    val context = LocalContext.current

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(Ability.ASR.displayName, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { /* TODO: Start Listening */ }) {
                    Text(context.getString(R.string.start_btn))
                }
                Button(onClick = { /* TODO: Stop Listening */ }) {
                    Text(context.getString(R.string.stop_btn))
                }
            }
        }
    }
}

@Composable
fun TtsCard() {
    val context = LocalContext.current
    var text by remember { mutableStateOf(context.getString(R.string.tts_prompt)) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(Ability.TTS.displayName, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            BasicTextField(
                value = text,
                onValueChange = { text = it },
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
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { /* TODO: Play TTS */ }) {
                    Text(context.getString(R.string.play_btn))
                }
                Button(onClick = { /* TODO: Stop Play */ }) {
                    Text(context.getString(R.string.stop_btn))
                }
            }
        }
    }
}

@Composable
fun ChatCard() {
    val context = LocalContext.current
    var chatInput by remember { mutableStateOf("") }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(Ability.CHAT.displayName, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            BasicTextField(
                value = chatInput,
                onValueChange = { chatInput = it },
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
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { /* TODO: Clear Chat History */ }) {
                    Text(context.getString(R.string.clear_history_btn))
                }
                Button(onClick = { /* TODO: Chat */ }) {
                    Text(context.getString(R.string.chat_btn))
                }
            }
        }
    }
}