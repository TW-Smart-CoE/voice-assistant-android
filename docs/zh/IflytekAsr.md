# 科大讯飞 Asr

## 配置

- [科大智能语音配置](IflytekConfig.md)
- 在 [科大讯飞语音听写 App 配置](https://console.xfyun.cn/services/iat) 中根据需要配置 SDK。配置好后再下载 SDK，或者重新下载 SDK，替换 app 中的 so 库。
- 参考 [科大讯飞语音听写 Android SDK 文档](https://www.xfyun.cn/doc/asr/voicedictation/Android-SDK.html) 中的参数配置定制 ASR 参数。

## 示例代码

```kotlin
// initialize asr
val ivAssistant = IVAssistant(this)
val asr = ivAssistant.createAsr(AsrType.Iflytek, mapOf(
    Pair("app_id", "0******2"),
    Pair("language", "zh_cn"),
    Pair("vad_bos", 10000),
    Pair("vad_eos", 1000),
    Pair("asr_ptt", 0) 
))
asr.initialize()

// use asr
asr.startListening(object : AsrCallback {
    override fun onResult(text: String) {
        Log.d(TAG, "asr onResult: $text")
        if (text.isEmpty()) {
            return
        }
        tts.play(text)
    }

    override fun onError(errorMessage: String) {
        Log.e(TAG, "onError: $errorMessage")
    }

    override fun onVolumeChanged(volume: Float) {
        Log.d(TAG, "onVolumeChanged: $volume")
    }
})


// release asr
asr.release()
```