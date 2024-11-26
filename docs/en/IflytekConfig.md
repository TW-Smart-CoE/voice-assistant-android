# IFLYTEK Intelligent Voice Interaction Service

## Backend Configuration

- Open the [iFLYTEK Open Platform](https://console.xfyun.cn), create a new application, and open the created app in [My Apps](https://console.xfyun.cn/app/myapp) to get the APPID. This is where you configure the project's features.
- When using the Android SDK, there's no need to configure APIKey and APISecret because the downloaded SDK is generated based on this information.
- Select Speech Recognition -> Speech Dictation (Streaming Version). Download the "Speech Dictation (Streaming Version) SDK" for Android MSC. On the SDK download page, select the target application, choose the Android platform, and select "Standard Version" for AI capabilities. Depending on your needs, select different capabilities, such as "Speech Dictation (Streaming Version)" and voice wake-up. The downloaded SDK will include these capabilities.
- Extract the downloaded SDK, and place the arm64-v8a and armeabi-v7a directories from the libs directory into the src/main/jniLibs/iflytek directory under your app directory.
- Configure the following sourceSets in your app's build.gradle. This is because the so libraries contain the APIKey and APISecret, which are unique to each application, so we must configure them in the app.

```groovy
sourceSets {
    main {
        jniLibs.srcDirs = ['src/main/jniLibs/iflytek']
    }
}
```

## SDK/API Key Configuration

In the AndroidManifest.xml file, configure under the application tag (can also be configured in code):

```xml
<meta-data
    android:name="IFLYTEK_IVS_APP_ID"
    android:value="\${IFLYTEK_IVS_APP_ID}" />
```

`IFLYTEK_IVS_APP_ID` is the APPID of the app created on the iFLYTEK Open Platform and needs to be set in the environment variables.