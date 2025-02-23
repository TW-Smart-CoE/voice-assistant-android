# 火山引擎 Asr

## 配置

在 [火山引擎语音应用管理](https://console.volcengine.com/speech/app)中创建自己的语音应用，得到 APP_ID。点击左侧语音识别 -> 一句话识别或流式语音识别，就可以拿到 AccessToken。
因为 Volcengine 使用了旧的 support library，所以需要在 Android 工程的 gradle.properties 中配置 android.enableJetifier=true。

### 添加仓库

需要在 App 的 settings.gradle.kts 中添加 `maven { setUrl("https://artifact.bytedance.com/repository/Volcengine/") }`。
例如：
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

### 关于 android.enableJetifier=true

在项目根目录  gradle.properties 里确保有这两行：

```properties
android.useAndroidX=true
android.enableJetifier=true
```

android.enableJetifier=true 是一个 Gradle 属性，用于将依赖项中基于旧版 Android Support 库的引用自动转换为 AndroidX 的引用。也就是说，当你的项目已经迁移到 AndroidX，但某些第三方库仍旧使用旧的 com.android.support 库时，开启 Jetifier 可以自动将这些库中的旧类和方法重定向到对应的 AndroidX 版本，从而避免因两种库共存而导致的冲突。

开启后 Gradle 在构建过程中会扫描所有依赖，并将其中的旧引用转换为 AndroidX 兼容的引用，这帮助你平滑过渡到 AndroidX，解决依赖冲突问题。


## Asr

### 示例

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

## 参考文档
- [火山 ASR 集成指南](https://www.volcengine.com/docs/6561/113641)
- [火山 ASR 调用流程](https://www.volcengine.com/docs/6561/113642)
