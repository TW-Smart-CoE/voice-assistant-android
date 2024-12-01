# OpenAI Chat

## Configure

- Go to [OpenAI API keys](https://platform.openai.com/account/api-keys) or other platforms which use OpenAI API. Create an API Key.

## Chat

### Sample

```kotlin
// create chat
val chat = OpenAIChat.create(
    context,
    mapOf(
        ChatParams.ApiKey.KEY to BuildConfig.OPENAI_API_KEY,
        ChatParams.BaseUrl.KEY to ChatParams.BaseUrl.VALUES.KIMI,
        ChatParams.ApiVersion.KEY to ChatParams.ApiVersion.VALUES.V1,
        ChatParams.Model.KEY to "moonshot-v1-8k",
    )

// init chat
viewModelScope.launch {
    chat.initialize()
}

// use chat
viewModelScope.launch {
    val result = chat.chat(Chat.Message("user", chatText))
    Log.d(TAG, result.toString())
}

// stop chat
chat.stop()

// release chat
chat.release()
```

## Reference
- [OpenAI](https://platform.openai.com)
- [Kimi](https://platform.moonshot.cn)
- [Doubao](https://www.volcengine.com/docs/82379/1298454)
- [Spark](https://www.xfyun.cn/doc/spark/HTTP%E8%B0%83%E7%94%A8%E6%96%87%E6%A1%A3.html)
- [GLM](https://open.bigmodel.cn/overview)