package com.thoughtworks.voiceassistant.alibabakit.abilities.tts

object TtsParams {
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

    object EncodeType {
        const val KEY = "encode_type"

        object VALUES {
            const val WAV = "wav"
            const val MP3 = "mp3"
        }
    }

    object TtsFilePath {
        const val KEY = "tts_file_path"
    }

    object SampleRate {
        const val KEY = "sample_rate"
        object VALUES {
            const val RATE_8K = 8000
            const val RATE_16K = 16000
            const val RATE_24K = 24000
            const val RATE_32K = 32000
            const val RATE_48K = 48000
        }
    }

    object FontName {
        const val KEY = "font_name"

        object VALUES {
            const val ZHIFENG_EMO = "zhifeng_emo"
            const val ZHIBING_EMO = "zhibing_emo"
            const val ZHIMIAO_EMO = "zhimiao_emo"
            const val ZHIMI_EMO = "zhimi_emo"
            const val ZHIYAN_EMO = "zhiyan_emo"
            const val ZHIBEI_EMO = "zhibei_emo"
            const val ZHITIAN_EMO = "zhitian_emo"
            const val AITONG = "aitong"
        }
    }

    object EnableSubtitle {
        const val KEY = "enable_subtitle"

        object VALUES {
            const val ENABLE = "1"
            const val DISABLE = "0"
        }
    }

    object PlaySound {
        const val KEY = "play_sound"
        object VALUES {
            const val TRUE = true
            const val FALSE = false
        }
    }

    object StopAndStartDelay {
        const val KEY = "stop_and_start_delay"
        object VALUES {
            const val DEFAULT = 50
        }
    }

    object PlayMode {
        const val KEY = "play_mode"
        object VALUES {
            const val MEDIA = "media"
            const val COMMUNICATION = "communication"
        }
    }

    object RemoveWavHeader {
        const val KEY = "remove_wav_header"
        object VALUES {
            const val TRUE = true
            const val FALSE = false
        }
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
}

object SpeakParams {
    object Emotion {
        const val KEY = "emotion"

        object VALUES {
            const val NEUTRAL = "neutral"
            const val HAPPY = "happy"
            const val ANGRY = "angry"
            const val SAD = "sad"
            const val FEAR = "fear"
            const val HATE = "hate"
            const val SURPRISE = "surprise"
        }
    }

    object Intensity {
        const val KEY = "intensity"

        object VALUES {
            const val DEFAULT = 1.0f
        }
    }
}