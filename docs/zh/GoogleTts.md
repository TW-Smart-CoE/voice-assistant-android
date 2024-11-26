# Google Tts

## 后台配置

构建 Google Speech TTS 需要 GoogleCredentials，而 GoogleCredentials 不能直接由 Google Cloud的API Key 创建。
GoogleCredentials 需要一个 service account key。下面是 service account key 的创建流程。

Create a service account key:
- Go to the [Google Cloud Console](https://console.cloud.google.com/)
- Select your project.
- Go to IAM & Admin > Service Accounts.
- Click on CREATE SERVICE ACCOUNT.
- Give your service account a name and click CREATE.
- Under Service account permissions (optional), add roles that you need for your project. For example, if you're using Text-to-Speech, you might add the role roles/cloudtexttospeech.editor.
- Click CONTINUE and then DONE.
- Click on the service account that you just created, then on the Keys tab, and then on ADD KEY > Create new key.
- Select JSON and click CREATE. The JSON key will be downloaded.

将下载到 json 文件中的内容用作创建 Google TTS 时的 credentials。

## 注意事项

如果使用 Google Tts，需要在 app 模块的 build.gradle 文件中添加以下配置：

```kotlin
android {
    //...
    packagingOptions {
        pickFirst("META-INF/io.netty.versions.properties")
        pickFirst("META-INF/DEPENDENCIES")
        pickFirst("META-INF/INDEX.LIST")
    }
}
```

## SDK/API Key 配置

创建 TTS 时配置 credentials

## 示例代码

```kotlin
// initialize tts
val ivAssistant = IVAssistant(this)

tts = ivAssistant.createTts(
    TtsType.Google,
    mapOf(
        Pair(
            "credentials", ByteArrayInputStream(
                """
                   // your google credentials in json format
                """.toByteArray()
            )
        ),
        Pair("language_code", "en-US"),
        Pair("name", "en-US-Wavenet-F"),
        Pair("speaking_rate", 1.0), // Speech speed. Default is 1.0. Range is 0.25 to 4.0.
        Pair(
            "volume_gain_db",
            0.0
        ), //Volume gain (in dB) of the normal native volume, supported by the specific voice, in the range [-96.0, 16.0]
    )
)

tts.initialize()

// use tts
tts.play(text, object : TtsCallback {
    override fun onPlayEnd() {
        Log.d(TAG, "onPlayEnd")
    }

    override fun onTTSFileSaved(ttsFilePath: String) {
        Log.d(TAG, "onTTSFileSaved: $ttsFilePath")
    }
})

// release tts
tts.release()
```