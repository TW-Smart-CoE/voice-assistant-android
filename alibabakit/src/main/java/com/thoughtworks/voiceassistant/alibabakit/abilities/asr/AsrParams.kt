package com.thoughtworks.voiceassistant.alibabakit.abilities.asr

object AsrParams {
    object AccessKey {
        const val KEY = "access_key"
    }

    object AccessKeySecret {
        const val KEY = "access_key_secret"
    }

    object AppKey {
        const val KEY = "app_key"
    }

    object Token {
        const val KEY = "token"
    }

    object AudioSource {
        const val KEY = "audio_source"
        object VALUES {
            const val DEFAULT = "default"
            const val COMMUNICATION = "COMMUNICATION"
        }
    }

    object VadMode {
        const val KEY = "vad_mode"
        object VALUES {
            const val P2T = "P2T"
            const val VAD = "VAD"
        }
    }

    object EnableVoiceDetection {
        const val KEY = "enable_voice_detection"
        const val DEFAULT_VALUE = true
    }

    object MaxStartSilence {
        const val KEY = "max_start_silence"
        const val DEFAULT_VALUE = 10000
    }

    object MaxEndSilence {
        const val KEY = "max_end_silence"
        const val DEFAULT_VALUE = 800
    }

    object SpeechNoiseThreshold {
        const val KEY = "speech_noise_threshold"
        const val DEFAULT_VALUE = 0.7f
    }
}