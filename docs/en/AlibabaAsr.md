# Alibaba Asr

## Configure

[Ali intelligent voice configure](AlibabaConfig)

## Sample

```kotlin
// create asr
val asr = AlibabaAsr.create(
        context,
        mapOf(
            AsrParams.AccessKey.KEY to BuildConfig.ALI_IVS_ACCESS_KEY,
            AsrParams.AccessKeySecret.KEY to BuildConfig.ALI_IVS_ACCESS_KEY_SECRET,
            AsrParams.AppKey.KEY to BuildConfig.ALI_IVS_APP_KEY,
            AsrParams.AudioSource.KEY to AsrParams.AudioSource.VALUES.COMMUNICATION,
            AsrParams.RecognitionType.KEY to AsrParams.RecognitionType.VALUES.LONG,
        )
    )
}

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

// release asr
asr.release()
```