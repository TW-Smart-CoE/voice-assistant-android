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

    object SpeechNoiseThreshold {
        const val KEY = "speech_noise_threshold"
        const val DEFAULT_VALUE = 0.7f
    }

    object VadMode {
        const val KEY = "vad_mode"
        object VALUES {
            const val VAD = "VAD"
            const val P2T = "P2T"
        }
    }
}