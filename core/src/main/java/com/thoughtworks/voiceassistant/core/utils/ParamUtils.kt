package com.thoughtworks.voiceassistant.core.utils

object ParamUtils {
    fun Map<String, Any>.requireKey(key: String) {
        if (!this.containsKey(key)) {
            throw IllegalArgumentException("The key '$key' is required but was not provided.")
        }
    }

    fun Map<String, Any>.requireAtLeastOneKeyGroup(vararg keyGroups: List<String>) {
        val isAnyGroupSatisfied = keyGroups.any { group ->
            group.all { key -> this.containsKey(key) }
        }

        if (!isAnyGroupSatisfied) {
            throw IllegalArgumentException("At least one of the key groups must be fully provided: ${keyGroups.joinToString { it.toString() }}")
        }
    }
}