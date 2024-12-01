# Alibaba Cloud Intelligent Speech Interaction Service

## Backend Configuration

- Enable [Alibaba Cloud Intelligent Speech Interaction Service](https://nls-portal.console.aliyun.com/overview). Create a project and open the created app in the [Project List](https://nls-portal.console.aliyun.com/applist) to obtain the APP_KEY. Configure the project features here.
- [In the RAM Access Control](https://ram.console.aliyun.com/overview), click on AccessKey to enter the [Access Credential Management page](https://ram.console.aliyun.com/manage/ak). Create an Access Key here to obtain the ACCESS_KEY and ACCESS_KEY_SECRET.

## Asr 

### Sample

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
            AsrParams.EnableAcousticEchoCanceler.KEY to true,
            AsrParams.EnableNoiseSuppression.KEY to true,
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

// stop asr
asr.stop()

// release asr
asr.release()
```

## Tts

### Sample

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
        TtsParams.PlayMode.KEY to TtsParams.PlayMode.VALUES.MEDIA,
    )
)


// init tts
viewModelScope.launch {
    tts.initialize()
}

// use tts
viewModelScope.launch {
    val result = voiceManager.tts.speak(text, emptyMap())
    Log.d(TAG, result.toString())
}

// stop tts
tts.stop()

// release tts
tts.release()
```

## Reference
- [Alibaba One-sentence Asr interface description](https://help.aliyun.com/zh/isi/developer-reference/overview-3?spm=5176.22414175.sslink.1.21883e74dCEmFW)
- [Alibaba One-sentence Asr Android SDK](https://help.aliyun.com/zh/isi/developer-reference/nui-sdk-for-android?spm=a2c4g.11186623.help-menu-30413.d_3_0_0_2_1.7f874bb4rNCrfz)
- [Alibaba Real-time Asr interface description](https://help.aliyun.com/zh/isi/developer-reference/api-reference?spm=a2c4g.11186623.help-menu-30413.d_3_0_1_0.3b6c4a51NEzylc)
- [Alibaba Real-time Asr Android SDK](https://help.aliyun.com/zh/isi/developer-reference/nui-sdk-for-android-1?spm=a2c4g.11186623.help-menu-30413.d_3_0_1_1_1.44b8626b9ObDWE)
