package com.thoughtworks.voiceassistant.alibabakit.abilities.tts

import java.util.UUID

class TaskIdManager {
    private var taskId = ""

    @Synchronized
    fun generateTaskId(): String {
        taskId = UUID.randomUUID().toString().replace("-", "")
        return taskId
    }

    @Synchronized
    fun getTaskId(): String {
        return taskId
    }

    @Synchronized
    fun clearTaskId() {
        taskId = ""
    }
}