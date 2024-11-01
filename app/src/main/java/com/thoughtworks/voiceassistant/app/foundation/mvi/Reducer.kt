package com.thoughtworks.voiceassistant.app.foundation.mvi

import com.thoughtworks.voiceassistant.app.foundation.mvi.model.Action
import com.thoughtworks.voiceassistant.app.foundation.mvi.model.Event
import com.thoughtworks.voiceassistant.app.foundation.mvi.model.State

interface Reducer<S : State, E : Event, A : Action> {
    fun reduce(
        currentState: S,
        action: A,
    ): S

    fun runSideEffect(
        action: A,
        currentState: S,
    )
}
