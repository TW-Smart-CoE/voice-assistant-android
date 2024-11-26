# 科大讯飞 WakeUp

## 配置

- [科大智能语音配置](IflytekConfig.md)
- 在 [科大讯飞语音唤醒 App 配置](https://console.xfyun.cn/services/awaken) 中配置唤醒词。配置好后再下载 SDK，或者重新下载 SDK，替换 app 中的 so 库。
- 将下载的 SDK 解压缩，将 `res/ivw/{app_id}.jet` 复制到 app 的 `src/main/assets/` 目录下，可以有子目录，也可以改文件名，例如 `src/main/assets/wakeup/iflytek/xiaozhi.jet`。然后在 createWakeUp 时的参数中配置 `ivw_res_path_assets` 为 assets 下该文件的目录，例如 `wakeup/iflytek/xiaozhi.jet`。
- 参考 [科大讯飞语音唤醒 Android SDK 文档](https://www.xfyun.cn/doc/asr/awaken/Android-SDK.html) 中的参数配置定制 WakeUp 参数。

## 示例代码

```kotlin
// initialize wakeup
val wakeUp = ivAssistant.createWakeUp(
    WakeUpType.Iflytek,
    mapOf(
        Pair("app_id", "0******2"),
        Pair("ivw_res_path_assets", "wakeup/iflytek/xiaozhi.jet"),
        Pair("ivw_threshold", 1450),
        Pair("keep_live", 1),
    )
)
wakeUp.initialize()

// use wakeUp
wakeUp.start(object : WakeUpCallback {
    override fun onSuccess(keywordIndex: Int) {
        Log.d(TAG, "wakeUp onSuccess $keywordIndex")
    }

    override fun onError(errorCode: Int, errorMessage: String) {
        Log.e(TAG, "errorCode: $errorCode, errorMessage: $errorMessage")
    }

    override fun onStop() {
        Log.d(TAG, "wakeUp onStop")
    }
})

// stop & release wakeUp
wakeUp.stop()
wakeUp.release()

```