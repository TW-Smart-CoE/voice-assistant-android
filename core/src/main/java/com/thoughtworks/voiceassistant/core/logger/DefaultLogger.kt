package com.thoughtworks.voiceassistant.core.logger

import android.util.Log

class DefaultLogger : Logger {
    override fun verbose(message: String) {
        Log.v(TAG, message)
    }

    override fun debug(message: String) {
        Log.d(TAG, message)
    }

    override fun info(message: String) {
        Log.i(TAG, message)
    }

    override fun warn(message: String) {
        Log.w(TAG, message)
    }

    override fun error(message: String) {
        Log.e(TAG, message)
    }

    override fun wtf(message: String) {
        Log.wtf(TAG, message)
    }

    companion object {
        private const val TAG = "voice-assistant"
    }
}