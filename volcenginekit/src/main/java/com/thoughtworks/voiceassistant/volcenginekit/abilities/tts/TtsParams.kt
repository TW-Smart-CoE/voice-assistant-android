package com.thoughtworks.voiceassistant.volcenginekit.abilities.tts

object TtsParams {
    object AppId {
        const val KEY = "app_Id"
    }

    object AppToken {
        const val KEY = "app_token"
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

    object VoicePitch {
        const val KEY = "voice_pitch"

        object VALUES {
            const val DEFAULT = 1.0f
        }
    }

    object VoiceSpeed {
        const val KEY = "voice_speed"

        object VALUES {
            const val DEFAULT = 1.0f
        }
    }

    object UserId {
        const val KEY = "user_id"

        object VALUES {
            const val DEFAULT = "default"
        }
    }
}