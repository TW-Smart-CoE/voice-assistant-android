# 阿里智能语音交互服务

## 后台配置
- 开通[阿里云智能语音交互服务](https://nls-portal.console.aliyun.com/overview)，创建项目，在[项目列表](https://nls-portal.console.aliyun.com/applist)中打开创建的 App，得到 APP_KEY，在这里配置项目功能。
- 在 [RAM 访问控制](https://ram.console.aliyun.com/overview)中点击 AccessKey，进入[访问凭证管理](https://ram.console.aliyun.com/manage/ak)页面。在这里创建 Access Key 后得到 ACCESS_KEY 和 ACCESS_KEY_SECRET。

## Asr

### 示例

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

// init asr
viewModelScope.launch {
    asr.initialize()
}

// use asr
viewModelScope.launch {
    val result = asr.listen {
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

### 示例

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
    val result = tts.speak(text, emptyMap())
    Log.d(TAG, result.toString())
}

// stop tts
tts.stop()

// release tts
tts.release()
```

## 参考文档
- [阿里巴巴一句话识别接口说明](https://help.aliyun.com/zh/isi/developer-reference/overview-3?spm=5176.22414175.sslink.1.21883e74dCEmFW)
- [阿里巴巴一句话识别 Android SDK](https://help.aliyun.com/zh/isi/developer-reference/nui-sdk-for-android?spm=a2c4g.11186623.help-menu-30413.d_3_0_0_2_1.7f874bb4rNCrfz)
- [阿里巴巴实时语音识别接口说明](https://help.aliyun.com/zh/isi/developer-reference/api-reference?spm=a2c4g.11186623.help-menu-30413.d_3_0_1_0.3b6c4a51NEzylc)
- [阿里巴巴实时语音识别 Android SDK](https://help.aliyun.com/zh/isi/developer-reference/nui-sdk-for-android-1?spm=a2c4g.11186623.help-menu-30413.d_3_0_1_1_1.44b8626b9ObDWE)
