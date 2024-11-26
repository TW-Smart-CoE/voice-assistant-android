# 阿里 Tts

## 配置

[阿里智能语音配置](AliConfig.md)

## 示例代码

```kotlin
// create tts
val encodeType = TtsParams.EncodeType.VALUES.WAV

val tts = AlibabaTts.create(
    context,
    mapOf(
        TtsParams.AccessKey.KEY to BuildConfig.ALI_IVS_ACCESS_KEY,
        TtsParams.AccessKeySecret.KEY to BuildConfig.ALI_IVS_ACCESS_KEY_SECRET,
        TtsParams.AppKey.KEY to BuildConfig.ALI_IVS_APP_KEY,
        TtsParams.EncodeType.KEY to encodeType,
        TtsParams.TtsFilePath.KEY to "${context.externalCacheDir?.absolutePath}/tts.${encodeType}",
    )
)

// init tts
viewModelScope.launch {
    tts.initialize()
}

// play tts
viewModelScope.launch {
    val result = voiceManager.tts.speak(text, emptyMap())
    Log.d(TAG, result.toString())
}

// release tts
tts.release()
```