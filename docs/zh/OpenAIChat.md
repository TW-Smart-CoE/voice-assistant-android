# OpenAI ChatGPT

## 后台配置

- 请前往[OpenAI API keys](https://platform.openai.com/account/api-keys)。 创建一个 API Key。

## SDK/API Key 配置
AndroidManifest.xml 中 application 标签下配置（也可以在代码中配置）：
```xml
<meta-data
    android:name="OPENAI_API_KEY"
    android:value="${OPENAI_API_KEY}" />
```

## 示例代码
```kotlin
// initialize chat
val chat = ivAssistant.createChat(
    ChatType.ChatGpt,
    mapOf(
        Pair("base_url", "https://api.openai.com"),
        Pair("model", "gpt-3.5-turbo"),
        Pair("temperature", 1.0f),
        Pair("max_history_len", 20),
    )
)
chat.initialize()

// use chat
chat.chat(getString(R.string.hello), object : ChatCallback {
    override fun onResult(text: String) {
        tts.play(text)
    }

    override fun onError(errorMessage: String) {
        Log.e(TAG, "chat onError: $errorMessage")
    }
})

// release chat
chat.release()
```