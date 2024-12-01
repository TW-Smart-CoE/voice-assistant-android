## Picovoice

## 后台配置

- 请前往[picovoice 控制台](https://console.picovoice.ai/)注册登录并拿到 AccessKey。
- 请前往[picovoice 唤醒词](https://console.picovoice.ai/ppn)。设置唤醒词并下载 ppn 文件。 将 ppn 文件放在 _project/app/src/main/assets_ 目录下，可以存放到子目录下。如果需要多个唤醒词，可下载多个文件。

## 示例

```kotlin

// create wakeUp
val wakeUp = PicovoiceWakeUp.create(
        context,
        mapOf(
            WakeUpParams.AccessKey.KEY to BuildConfig.PICOVOICE_ACCESS_KEY,
            WakeUpParams.ModelPath.KEY to if (LanguageUtils.isChinese()) "wakeup/picovoice/models/porcupine_params_zh.pv" else "",
            WakeUpParams.KeywordPaths.KEY to if (LanguageUtils.isChinese()) listOf(
                "wakeup/picovoice/小智_zh_android_v3_0_0.ppn",
            ) else listOf(
                "wakeup/picovoice/Hi-Joey_en_android_v3_0_0.ppn",
            )
        )
    )

// init wakeUp
viewModelScope.launch {
    wakeUp.initialize()
}

// use wakeUp
viewModelScope.launch {
    val result = voiceManager.wakeUp.listen {
        Log.d(TAG, "onWakeUp: $it")
    }
    Log.d(TAG, result.toString())
}

// stop wakeUp
wakeUp.stop()

// release wakeUp
wakeUp.release()
```

## 参考文档
[Porcupine Wake Word Android quick start](https://picovoice.ai/docs/quick-start/porcupine-android/)
