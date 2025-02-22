package com.thoughtworks.voiceassistant.volcenginekit.abilities.asr

import com.thoughtworks.voiceassistant.volcenginekit.abilities.asr.models.HotwordsData

object AsrParams {
    object AppId {
        const val KEY = "app_id"
    }

    object AppToken {
        const val KEY = "app_token"
    }

    object Cluster {
        const val KEY = "cluster"
    }

    object VadMaxSpeechDuration {
        const val KEY = "vad_max_speech_duration"

        object VALUES {
            const val DEFAULT = 60000
            const val INFINITE = -1
        }
    }

    object AutoStop {
        const val KEY = "auto_stop"

        object VALUES {
            const val FALSE = false
            const val TRUE = true
        }
    }

    object AudioSource {
        const val KEY = "audio_source"

        object VALUES {
            const val DEFAULT = "default"
            const val COMMUNICATION = "communication"
        }
    }

    object RecognitionType {
        const val KEY = "recognition_type"

        object VALUES {
            const val SINGLE_SENTENCE = "single_sentence"
            const val LONG = "long"
        }
    }

    object Hotwords {
        const val KEY = "hotwords"

        object VALUES {
            val DEFAULT = HotwordsData(emptyList())
        }
    }

    object UserId {
        const val KEY = "user_id"

        object VALUES {
            const val DEFAULT = "default"
        }
    }
}