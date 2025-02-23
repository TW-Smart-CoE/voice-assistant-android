# Volcengine Asr

## Configure

Create your own speech application in the [Volcengine Speech Application Management](https://console.volcengine.com/speech/app) to obtain the APP_ID. Click on Speech Recognition on the left -> One Sentence Recognition or Streaming Speech Recognition to get the AccessToken.
Since Volcengine uses an old support library, you need to configure `android.enableJetifier=true` in the `gradle.properties` of your Android project.

### Add repository

You need to add `maven { setUrl("https://artifact.bytedance.com/repository/Volcengine/") }` in App's settings.gradle.kts.
For example:
```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
        maven { setUrl("https://artifact.bytedance.com/repository/Volcengine/") }
    }
}
```

### About android.enableJetifier=true

In your project's root folder, ensure your gradle.properties file contains the following two lines:
```properties
android.useAndroidX=true
android.enableJetifier=true
```
The property android.enableJetifier=true is a Gradle setting used to automatically convert references in dependencies that are based on the old Android Support libraries to the corresponding AndroidX references. In other words, when your project has migrated to AndroidX but some third-party libraries still use the old com.android.support libraries, enabling Jetifier will automatically redirect those old classes and methods to their corresponding AndroidX versions, thereby avoiding conflicts caused by having both libraries coexist.
Once enabled, Gradle will scan all dependencies during the build process and convert any outdated references to AndroidX-compatible ones. This helps you smoothly transition to AndroidX and resolve dependency conflicts.

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
