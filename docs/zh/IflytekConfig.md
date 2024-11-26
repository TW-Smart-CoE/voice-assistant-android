# 科大讯飞智能语音交互服务

## 后台配置
- 开通[讯飞开放平台](https://console.xfyun.cn)，创建新应用，在[我的应用](https://console.xfyun.cn/app/myapp)中打开创建的 App，得到 APPID，在这里配置项目功能。
- 使用 Android SDK 无需配置 APIKey 和 APISecret，因为下载的 SDK 是根据这些信息生成的。
- 选择语音识别 -> 语音听写（流式版）。下载“语音听写（流式版） SDK” Android MSC。在 SDK 下载页面选择目标应用，平台选择 Android，AI 能力选择“普通版本”，根据需要选择不同的能力，例如选择“语音听写（流式版）”和语音唤醒。这样下载的 SDK 就包含了这些能力。
- 将下载的 SDK 解压，将其中的 libs 目录下的 arm64-v8a 和 armeabi-v7a 放到你的 app 目录下的 src/main/jniLibs/iflytek 下。
- 在 app 的 build.gradle 下配置下面的 sourceSets。这是因为 so 库中包含了 APIKey 和 APISecret，每个应用都不同，所以我们必须将其配置到 app 中。
 
```groovy
sourceSets {
    main {
        jniLibs.srcDirs = ['src/main/jniLibs/iflytek']
    }
}
```

## SDK/API Key 配置

AndroidManifest.xml 中 application 标签下配置（也可以在代码中配置）：

```xml
<meta-data
    android:name="IFLYTEK_IVS_APP_ID"
    android:value="\${IFLYTEK_IVS_APP_ID}" />
```

`IFLYTEK_IVS_APP_ID` 为讯飞开放平台创建的 App 的 APPID，需要将其配置到环境变量中。