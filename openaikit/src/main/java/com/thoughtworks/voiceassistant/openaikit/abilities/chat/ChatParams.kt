package com.thoughtworks.voiceassistant.openaikit.abilities.chat

object ChatParams {
    object ApiKey {
        const val KEY = "api_key"
    }

    object BaseUrl {
        const val KEY = "base_url"
        object VALUES {
            const val OPEN_AI = "https://api.openai.com"
            const val KIMI = "https://api.moonshot.cn"
            const val DOUBAO = "https://ark.cn-beijing.volces.com/api"
            const val SPARK = "https://spark-api-open.xf-yun.com"
            const val GLM = "https://open.bigmodel.cn/api/paas"
        }
    }

    object ApiVersion {
        const val KEY = "api_version"
        object VALUES {
            const val V1 = "v1"
            const val V2 = "v2"
            const val V3 = "v3"
            const val V4 = "v4"
            const val V5 = "v5"
            const val V6 = "v6"
            const val V7 = "v7"
            const val V8 = "v8"
            const val V9 = "v9"
        }
    }

    object Model {
        const val KEY = "model"
    }

    object MaxHistoryLen {
        const val KEY = "max_history_len"
        object VALUES {
            const val DEFAULT = 50
        }
    }

    object MaxTokens {
        const val KEY = "max_tokens"
        object VALUES {
            const val DEFAULT = 2048
        }
    }

    object Temperature {
        const val KEY = "temperature"
        object VALUES {
            const val DEFAULT = 0.7f
        }
    }

    object ReadTimeout {
        const val KEY = "read_timeout"
        object VALUES {
            const val DEFAULT = 20_000L
        }
    }

    object WriteTimeout {
        const val KEY = "write_timeout"
        object VALUES {
            const val DEFAULT = 5_000L
        }
    }
}