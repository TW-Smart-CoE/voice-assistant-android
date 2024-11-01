package com.thoughtworks.voiceassistant.app.foundation.dispatcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class AppDispatchers : CoroutineDispatchers {
    override val defaultDispatcher: CoroutineDispatcher
        get() = Dispatchers.Default
    override val ioDispatcher: CoroutineDispatcher
        get() = Dispatchers.IO
    override val mainDispatcher: CoroutineDispatcher
        get() = Dispatchers.Main
    override val mainImmediateDispatcher: CoroutineDispatcher
        get() = Dispatchers.Main.immediate
}