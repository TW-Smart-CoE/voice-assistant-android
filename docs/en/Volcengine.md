# Volcengine Asr

## Configure

Create your own speech application in the [Volcengine Speech Application Management](https://console.volcengine.com/speech/app) to obtain the APP_ID. Click on Speech Recognition on the left -> One Sentence Recognition or Streaming Speech Recognition to get the AccessToken.
Since Volcengine uses an old support library, you need to configure `android.enableJetifier=true` in the `gradle.properties` of your Android project.

## Asr

### Sample

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

## Reference
- [Volcengine Asr integration guide](https://www.volcengine.com/docs/6561/113641)
- [Volcengine Asr implementation guide](https://www.volcengine.com/docs/6561/113642)
