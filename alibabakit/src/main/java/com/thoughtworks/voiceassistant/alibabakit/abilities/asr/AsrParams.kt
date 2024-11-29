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

    object VadMode {
        const val KEY = "vad_mode"

        object VALUES {
            const val P2T = "P2T"
            const val VAD = "VAD"
        }
    }

    object EnableVoiceDetection {
        const val KEY = "enable_voice_detection"
        object VALUES {
            const val TRUE = true
            const val FALSE = false
        }
    }

    object MaxStartSilence {
        const val KEY = "max_start_silence"
        object VALUES {
            const val DEFAULT = 10000
        }
    }

    object MaxEndSilence {
        const val KEY = "max_end_silence"
        object VALUES {
            const val DEFAULT = 1500
        }
    }

    object SpeechNoiseThreshold {
        const val KEY = "speech_noise_threshold"
        object VALUES {
            const val DEFAULT = 0.7f
        }
    }

    object VocabularyId {
        const val KEY = "vocabulary_id"
    }

    object EnableAcousticEchoCanceler {
        const val KEY = "enable_acoustic_echo_canceler"
        object VALUES {
            const val TRUE = true
            const val FALSE = false
        }
    }

    object EnableNoiseSuppression {
        const val KEY = "enable_noise_suppression"

        object VALUES {
            const val TRUE = true
            const val FALSE = false
        }
    }
}