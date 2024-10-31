package com.thoughtworks.voiceassistant.app.di

import android.content.Context


class DependencyImpl(context: Context) : Dependency {
    private val _context = context

    override val context: Context
        get() = _context
}