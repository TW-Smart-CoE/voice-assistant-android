package com.thoughtworks.voiceassistant.core.utils

import java.util.Locale

object LanguageUtils {
    fun isEnglish(): Boolean {
        return Locale.getDefault().language.startsWith("en")
    }

    fun isChinese(): Boolean {
        return Locale.getDefault().language.startsWith("zh")
    }
}