# 阿里智能语音交互服务

## 后台配置
- 开通[阿里云智能语音交互服务](https://nls-portal.console.aliyun.com/overview)，创建项目，在[项目列表](https://nls-portal.console.aliyun.com/applist)中打开创建的 App，得到 APP_KEY，在这里配置项目功能。
- 在 [RAM 访问控制](https://ram.console.aliyun.com/overview)中点击 AccessKey，进入[访问凭证管理](https://ram.console.aliyun.com/manage/ak)页面。在这里创建 Access Key 后得到 ACCESS_KEY 和 ACCESS_KEY_SECRET。

## SDK/API Key 配置

AndroidManifest.xml 中 application 标签下配置（也可以在代码中配置）：
```xml
<meta-data
    android:name="ALI_IVS_ACCESS_KEY"
    android:value="${ALI_IVS_ACCESS_KEY}" />
<meta-data
    android:name="ALI_IVS_ACCESS_KEY_SECRET"
    android:value="${ALI_IVS_ACCESS_KEY_SECRET}" />
<meta-data
    android:name="ALI_IVS_APP_KEY"
    android:value="${ALI_IVS_APP_KEY}" />
```