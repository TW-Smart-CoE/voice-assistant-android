package com.thoughtworks.voiceassistant.app.foundation.mvi

import com.thoughtworks.voiceassistant.app.foundation.mvi.model.Action
import com.thoughtworks.voiceassistant.app.foundation.mvi.model.Event
import com.thoughtworks.voiceassistant.app.foundation.mvi.model.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

interface Store<S : State, E : Event, A : Action> {
    var scope: CoroutineScope

    val uiState: StateFlow<S>
    val uiEvent: Flow<E>
    val action: Flow<A>

    fun sendState(state: S)

    fun sendAction(action: A)

    fun sendEvent(event: E)
}

class DefaultStore<S : State, E : Event, A : Action>(
    initialState: S,
) : Store<S, E, A> {
    override lateinit var scope: CoroutineScope

    private val _uiState = MutableStateFlow(initialState)
    override val uiState: StateFlow<S>
        get() = _uiState.asStateFlow()

    private val _uiEvent = Channel<E>(Channel.BUFFERED)
    override val uiEvent: Flow<E>
        get() = _uiEvent.receiveAsFlow()

    private val _action = Channel<A>(Channel.BUFFERED)
    override val action: Flow<A>
        get() = _action.receiveAsFlow()

    override fun sendAction(action: A) {
        scope.launch {
            _action.send(action)
        }
    }

    override fun sendState(state: S) {
        scope.launch {
            _uiState.value = state
        }
    }

    override fun sendEvent(event: E) {
        scope.launch {
            _uiEvent.send(event)
        }
    }
}
