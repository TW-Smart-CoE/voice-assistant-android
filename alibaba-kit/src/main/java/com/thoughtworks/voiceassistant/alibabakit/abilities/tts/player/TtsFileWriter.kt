package com.thoughtworks.voiceassistant.alibabakit.abilities.tts.player

import com.thoughtworks.voiceassistant.alibabakit.abilities.tts.TtsConfig
import com.thoughtworks.voiceassistant.core.logger.Logger
import com.thoughtworks.voiceassistant.core.logger.error
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class TtsFileWriter(
    private val logger: Logger,
    private val ttsConfig: TtsConfig,
) {
    private var outputStream: OutputStream? = null

    fun createFile() {
        outputStream?.close()

        val file = File(ttsConfig.ttsFilePath)

        // Check if directory exists, if not then create it
        if (!file.parentFile?.exists()!!) {
            if (!file.parentFile?.mkdirs()!!) {
                throw IOException("Unable to create directory")
            }
        }

        // Create the file if it doesn't exist
        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw IOException("File creation failed")
            }
        }

        outputStream = FileOutputStream(file)
    }

    fun writeData(data: ByteArray) {
        if (data.isNotEmpty()) {
            try {
                outputStream?.write(data)
            } catch (e: IOException) {
                e.message?.let { logger.error(TAG, it) }
            }
        }
    }

    fun closeFile() {
        try {
            outputStream?.close()
        } catch (e: IOException) {
            e.message?.let { logger.error(TAG, it) }
        }
    }

    companion object {
        private const val TAG = "TtsFileWriter"
    }
}