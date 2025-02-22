package com.thoughtworks.voiceassistant.volcenginekit.abilities.tts

object TtsParams {
    object AppId {
        const val KEY = "app_id"
    }

    object AccessToken {
        const val KEY = "access_token"
    }

    object Cluster {
        const val KEY = "cluster"
    }

    object VoiceName {
        const val KEY = "voice_name"
    }

    object VoiceType {
        const val KEY = "voice_type"
    }

    object VoicePitchRatio {
        const val KEY = "voice_pitch_ratio"

        object VALUES {
            const val DEFAULT = 1.0f
        }
    }

    object VoiceSpeedRatio {
        const val KEY = "voice_speed_ratio"

        object VALUES {
            const val DEFAULT = 1.0f
        }
    }

    object VoiceVolumeRatio {
        const val KEY = "voice_volume_ratio"

        object VALUES {
            const val DEFAULT = 1.0f
        }
    }

    object TtsFilePath {
        const val KEY = "tts_file_path"
    }

    object PlayMode {
        const val KEY = "play_mode"
        object VALUES {
            const val MEDIA = "media"
            const val COMMUNICATION = "communication"
        }
    }

    object UserId {
        const val KEY = "user_id"

        object VALUES {
            const val DEFAULT = "default"
        }
    }
}

object SpeakParams {
    object Emotion {
        const val KEY = "emotion"

        object VALUES {
            const val NEUTRAL = ""
            const val HAPPY = "happy"
            const val SAD = "sad"
            const val ANGRY = "angry"
            const val SCARE = "scare"
            const val HATE = "hate"
            const val SURPRISE = "surprise"
        }
    }
}