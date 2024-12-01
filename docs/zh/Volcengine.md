# 火山引擎 Asr

## 配置

在 [火山引擎语音应用管理](https://console.volcengine.com/speech/app)中创建自己的语音应用，得到 APP_ID。点击左侧语音识别 -> 一句话识别或流式语音识别，就可以拿到 AccessToken。
因为 Volcengine 使用了旧的 support library，所以需要在 Android 工程的 gradle.properties 中配置 android.enableJetifier=true。


## Asr

### 示例

```kotlin
// create asr
val asr = VolcengineAsr.create(
        context,
        mapOf(
            AsrParams.AppId.KEY to BuildConfig.VOLCENGINE_APP_ID,
            AsrParams.AppToken.KEY to BuildConfig.VOLCENGINE_ACCESS_TOKEN,
            AsrParams.AsrCluster.KEY to BuildConfig.VOLCENGINE_ONE_SENTENCE_RECOGNITION_CLUSTER_ID,
            AsrParams.AudioSource.KEY to AsrParams.AudioSource.VALUES.COMMUNICATION,
            AsrParams.RecognitionType.KEY to AsrParams.RecognitionType.VALUES.LONG,
            AsrParams.VadMaxSpeechDuration.KEY to AsrParams.VadMaxSpeechDuration.VALUES.INFINITE,
        )
    )

// init asr
viewModelScope.launch {
    asr.initialize()
}

// use asr
viewModelScope.launch {
    val result = voiceManager.asr.listen {
        Log.d(TAG, "onHeard: $it")
    }
    Log.d(TAG, result.toString())
}

// stop asr
asr.stop()

// release asr
asr.release()

```

## 参考文档
- [火山 ASR 集成指南](https://www.volcengine.com/docs/6561/113641)
- [火山 ASR 调用流程](https://www.volcengine.com/docs/6561/113642)
