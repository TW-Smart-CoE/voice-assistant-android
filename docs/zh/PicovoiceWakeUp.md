## Picovoice WakeUp

## 后台配置

- 请前往[picovoice 控制台](https://console.picovoice.ai/)注册登录并拿到 AccessKey。
- 请前往[picovoice 唤醒词](https://console.picovoice.ai/ppn)。设置唤醒词并下载 ppn 文件。 将 ppn 文件放在 _project/app/src/main/assets_ 目录下，可以存放到子目录下。如果需要多个唤醒词，可下载多个文件。
- 请前往[Porcupine Wake Word Android quick start](https://picovoice.ai/docs/quick-start/porcupine-android/)。参考 Android SDK 文档。

## SDK/API Key 配置

AndroidManifest.xml 中 application 标签下配置（也可以在代码中配置）：
```xml
<meta-data
    android:name="PICOVOICE_ACCESS_KEY"
    android:value="${PICOVOICE_ACCESS_KEY}" />
```

## 示例代码

```kotlin
// initialize wakeUp
wakeUp = ivAssistant.createWakeUp(
    WakeUpType.Picovoice,
    mapOf(
        Pair(
            "keyword_paths", listOf(
                "wakeup/picovoice/丽芙丽芙_zh_android_v3_0_0.ppn",
            )
        ),
        Pair("model_path", "wakeup/picovoice/models/porcupine_params_zh.pv"),
    )
)
wakeUp.initialize()

// use wakeUp
wakeUp.start(object : WakeUpCallback {
    override fun onSuccess(keywordIndex: Int) {
        Log.d(TAG, "wakeUp onSuccess $keywordIndex")
    }

    override fun onError(errorCode: Int, errorMessage: String) {
        Log.e(TAG, "errorCode: $errorCode, errorMessage: $errorMessage")
    }

    override fun onStop() {
        Log.d(TAG, "wakeUp onStop")
    }
})

// stop & release wakeUp
wakeUp.stop()
wakeUp.release()

```

