# Google Tts

## Configuration
Building Google Speech TTS requires GoogleCredentials, and GoogleCredentials cannot be directly created by Google Cloud's API Key.
GoogleCredentials requires a service account key. Below is the creation process for the service account key.

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

Use the content downloaded to the json file as credentials when creating Google TTS.

## Precautions

If using Google Tts, you need to add the following configuration to the app module's build.gradle file:

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

## SDK/API Key Configuration

Configure credentials when create TTS

## Sample

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