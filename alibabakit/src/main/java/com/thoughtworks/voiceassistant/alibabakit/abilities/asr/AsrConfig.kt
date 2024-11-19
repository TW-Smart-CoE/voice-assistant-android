package com.thoughtworks.voiceassistant.alibabakit.abilities.asr

import android.content.Context
import android.media.MediaRecorder
import com.alibaba.idst.nui.CommonUtils
import com.alibaba.idst.nui.Constants
import com.alibaba.idst.nui.Constants.VadMode
import com.thoughtworks.voiceassistant.alibabakit.utils.AlibabaConfig
import com.thoughtworks.voiceassistant.alibabakit.utils.AuthUtils
import com.thoughtworks.voiceassistant.core.logger.Logger
import com.thoughtworks.voiceassistant.core.utils.DeviceUtils
import org.json.JSONException
import org.json.JSONObject

class AsrConfig(
    accessKey: String = "",
    accessKeySecret: String = "",
    appKey: String = "",
    deviceId: String = "",
    workspace: String = "",
    token: String = "",
    val recognitionType: String = AsrParams.RecognitionType.VALUES.SINGLE,
    val debugPath: String = "",
    val audioSource: String = AsrParams.AudioSource.VALUES.DEFAULT,
    val vadMode: String = AsrParams.VadMode.VALUES.P2T,
    val enableVoiceDetection: Boolean = true,
    val maxStartSilence: Int = AsrParams.MaxStartSilence.DEFAULT_VALUE,
    val maxEndSilence: Int = AsrParams.MaxEndSilence.DEFAULT_VALUE,
    val speechNoiseThreshold: Float = AsrParams.SpeechNoiseThreshold.DEFAULT_VALUE,
) : AlibabaConfig(accessKey, accessKeySecret, appKey, deviceId, workspace, token) {
    fun getMediaRecorderAudioSource(): Int {
        return when (audioSource) {
            AsrParams.AudioSource.VALUES.COMMUNICATION -> MediaRecorder.AudioSource.VOICE_COMMUNICATION
            else -> MediaRecorder.AudioSource.DEFAULT
        }
    }

    fun getAliVadMode(): VadMode {
        return when (vadMode) {
            AsrParams.VadMode.VALUES.VAD -> VadMode.TYPE_VAD
            else -> VadMode.TYPE_P2T
        }
    }

    private fun getServiceType(): Int {
        return when (recognitionType) {
            AsrParams.RecognitionType.VALUES.LONG -> Constants.kServiceTypeSpeechTranscriber
            else -> Constants.kServiceTypeASR
        }
    }

    private fun getServiceMode(): String {
        return when (recognitionType) {
            AsrParams.RecognitionType.VALUES.LONG -> Constants.ModeFullCloud
            else -> Constants.ModeAsrCloud
        }
    }

    private fun isSingleRecognition(): Boolean {
        return recognitionType == AsrParams.RecognitionType.VALUES.SINGLE
    }

    override suspend fun generateTicket(
        context: Context,
        logger: Logger,
    ): String {
        val tokenValue = when (token.isNotEmpty()) {
            true -> token
            false -> AuthUtils(
                context, logger
            ).getToken(
                accessKey, accessKeySecret
            )
        }

        val jsonObj = JSONObject().apply {
            put("app_key", appKey)
            put("token", tokenValue)
            put("device_id", deviceId)
            put("url", url)
            put("workspace", workspace)
            put("sample_rate", "16000")
            put("format", "opus")
            put("debug_path", debugPath)
            put("service_mode", getServiceMode())
        }
        return jsonObj.toString()
    }

    fun genParams(): String {
        var genParams = ""
        try {
            val nlsConfig = JSONObject()
            nlsConfig.put("enable_intermediate_result", true)
            //参数可根据实际业务进行配置
            //接口说明可见: https://help.aliyun.com/document_detail/173298.html
            //nls_config.put("enable_punctuation_prediction", true);
            //nls_config.put("enable_inverse_text_normalization", true);
            //nls_config.put("customization_id", "test_id");
            //nls_config.put("vocabulary_id", "test_id");
            if (isSingleRecognition()) {
                nlsConfig.put("enable_voice_detection", enableVoiceDetection)
                nlsConfig.put("max_start_silence", maxStartSilence)
                nlsConfig.put("max_end_silence", maxEndSilence)
            }

            nlsConfig.put(
                "speech_noise_threshold", speechNoiseThreshold
            ) // https://help.aliyun.com/document_detail/316816.html

            //nls_config.put("sample_rate", 16000);
            //nls_config.put("sr_format", "opus");
            val parameters = JSONObject()
            parameters.put("nls_config", nlsConfig)
            parameters.put("service_type", getServiceType())

            //如果有HttpDns则可进行设置
            //parameters.put("direct_ip", Utils.getDirectIp());
            genParams = parameters.toString()
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return genParams
    }

    fun genDialogParams(): String {
        var params = ""
        try {
            val dialogParam = JSONObject()
            // 运行过程中可以在startDialog时更新临时参数，尤其是更新过期token
            // 注意: 若下一轮对话不再设置参数，则继续使用初始化时传入的参数
//            dialogParam.put("app_key", appKey)
//            dialogParam.put("token", token)
            params = dialogParam.toString()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return params
    }

    companion object {
        fun create(
            context: Context,
            params: Map<String, Any>,
        ): AsrConfig {
            var debugPath = ""
            context.externalCacheDir?.absolutePath?.also {
                debugPath = "$it/debug_${System.currentTimeMillis()}"
            }

            return AsrConfig(
                accessKey = params[AsrParams.AccessKey.KEY]?.toString() ?: "",
                accessKeySecret = params[AsrParams.AccessKeySecret.KEY]?.toString() ?: "",
                appKey = params[AsrParams.AppKey.KEY]?.toString() ?: "",
                token = params[AsrParams.Token.KEY]?.toString() ?: "",
                recognitionType = params[AsrParams.RecognitionType.KEY]?.toString()
                    ?: AsrParams.RecognitionType.VALUES.SINGLE,
                deviceId = DeviceUtils.getDeviceId(context),
                workspace = CommonUtils.getModelPath(context),
                debugPath = debugPath,
                audioSource = params[AsrParams.AudioSource.KEY]?.toString()
                    ?: AsrParams.AudioSource.VALUES.DEFAULT,
                vadMode = params[AsrParams.VadMode.KEY]?.toString() ?: AsrParams.VadMode.VALUES.P2T,
                enableVoiceDetection = params[AsrParams.EnableVoiceDetection.KEY]?.toString()
                    ?.toBoolean() != false,
                maxStartSilence = params[AsrParams.MaxStartSilence.KEY]?.toString()?.toInt()
                    ?: AsrParams.MaxStartSilence.DEFAULT_VALUE,
                maxEndSilence = params[AsrParams.MaxEndSilence.KEY]?.toString()?.toInt()
                    ?: AsrParams.MaxEndSilence.DEFAULT_VALUE,
                speechNoiseThreshold = params[AsrParams.SpeechNoiseThreshold.KEY]?.toString()
                    ?.toFloat() ?: AsrParams.SpeechNoiseThreshold.DEFAULT_VALUE,
            )
        }
    }
}