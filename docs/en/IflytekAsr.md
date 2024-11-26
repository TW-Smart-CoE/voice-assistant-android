# IFLYTEK Asr

## Configure

-  [iFLYTEK Intelligent Voice Configuration](IflytekConfig.md)
-  Configure the SDK as needed in the [iFLYTEK Speech Dictation App Configuration](https://console.xfyun.cn/services/iat). After configuring, download the SDK, or re-download the SDK and replace the so libraries in the app.
-  Refer to the [iFLYTEK Speech Dictation Android SDK Documentation](https://www.xfyun.cn/doc/asr/voicedictation/Android-SDK.html) for parameter configuration to customize ASR parameters.

## Sample

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