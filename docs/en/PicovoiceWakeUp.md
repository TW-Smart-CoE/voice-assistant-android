## Picovoice WakeUp

## Configuration

- Please head to the [picovoice console](https://console.picovoice.ai/) to register, log in, and obtain an AccessKey.
- Please go to [picovoice wake word](https://console.picovoice.ai/ppn). Set up the wake word and download the ppn file. Place the ppn file in the `_project/app/src/main/assets` directory, it can be placed in a subdirectory. If multiple wake words are needed, multiple files can be downloaded.
- Please visit [Porcupine Wake Word Android quick start](https://picovoice.ai/docs/quick-start/porcupine-android/) and refer to the Android SDK documentation.


## SDK/API Key Configure

In `AndroidManifest.xml`, configure under the `application` tag (it can also be configured in code):
```xml
<meta-data
    android:name="PICOVOICE_ACCESS_KEY"
    android:value="${PICOVOICE_ACCESS_KEY}" />
```

## Sample

```kotlin
// initialize wakeUp
wakeUp = ivAssistant.createWakeUp(
    WakeUpType.Picovoice,
    mapOf(
        Pair(
            "keyword_paths", listOf(
                "wakeup/picovoice/Hi-Joey_en_android_v3_0_0.ppn",
            )
        ),
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
