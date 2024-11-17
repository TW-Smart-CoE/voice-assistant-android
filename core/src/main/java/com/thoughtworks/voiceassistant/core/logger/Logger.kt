package com.thoughtworks.voiceassistant.core.logger

interface Logger {
    fun verbose(message: String)
    fun debug(message: String)
    fun info(message: String)
    fun warn(message: String)
    fun error(message: String)
    fun wtf(message: String)
}

fun Logger.verbose(subTag: String, message: String) {
    verbose("[$subTag] $message")
}

fun Logger.debug(subTag: String, message: String) {
    debug("[$subTag] $message")
}

fun Logger.info(subTag: String, message: String) {
    info("[$subTag] $message")
}

fun Logger.warn(subTag: String, message: String) {
    warn("[$subTag] $message")
}

fun Logger.error(subTag: String, message: String) {
    error("[$subTag] $message")
}

fun Logger.wtf(subTag: String, message: String) {
    wtf("[$subTag] $message")
}