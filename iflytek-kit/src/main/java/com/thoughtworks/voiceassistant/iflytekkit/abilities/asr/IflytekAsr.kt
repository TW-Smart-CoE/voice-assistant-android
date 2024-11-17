package com.thoughtworks.voiceassistant.iflytekkit.abilities.asr

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.iflytek.cloud.ErrorCode
import com.iflytek.cloud.InitListener
import com.iflytek.cloud.RecognizerListener
import com.iflytek.cloud.RecognizerResult
import com.iflytek.cloud.SpeechConstant
import com.iflytek.cloud.SpeechError
import com.iflytek.cloud.SpeechRecognizer
import com.iflytek.cloud.SpeechUtility
import com.thoughtworks.voiceassistant.core.abilities.Asr
import com.thoughtworks.voiceassistant.core.abilities.AsrCallback
import com.thoughtworks.voiceassistant.iflytekkit.utils.JsonParser

class IflytekAsr(
    val context: Context,
    private val params: Map<String, Any> = mapOf(),
) : Asr {
    private val engineType = SpeechConstant.TYPE_CLOUD
    private lateinit var asr: SpeechRecognizer
    private val cloudGrammar = """
       #ABNF 1.0 UTF-8;
        language zh-CN;
        mode voice;

        root \${'$'}main;
        \${'$'}main = \${'$'}place1 到 \${'$'}place2;
        \${'$'}place1 = 北京|武汉|南京|天津|东京;
        \${'$'}place2 = 上海|合肥; 
    """.trimIndent()
    private var grammarID = ""

    private val initListener = InitListener { code ->
        Log.d(TAG, "SpeechRecognizer init() code = $code")
        if (code != ErrorCode.SUCCESS) {
            Log.d(
                TAG,
                "init failed, error code：$code, please visit https://www.xfyun.cn/document/error-code for help"
            )
        }
    }

    override fun initialize() {
//        val appId = params["app_id"]?.toString()
//            ?: context.getManifestMetaData(IflyTekConstant.META_IFLYTEK_IVS_APP_ID)

//        SpeechUtility.createUtility(context, "appid=$appId")
        asr = SpeechRecognizer.createRecognizer(context, initListener)
        setParams()
    }

    override fun startListening(asrCallback: AsrCallback?) {
        asr.stopListening() // Ensure any existing session is stopped
        val ret = asr.startListening(IflyTekRecognizerListener(engineType, asrCallback))
        if (ret != ErrorCode.SUCCESS) {
            Log.e(
                TAG,
                "detect failed, error code: $ret, please visit https://www.xfyun.cn/document/error-code for help"
            )
            asrCallback?.onError("Error code: $ret")
        }
    }

    override fun stopListening() {
        asr.stopListening()
    }

    override fun release() {
        asr.cancel()
        asr.destroy()
    }

    private fun setParams() {
        asr.setParameter(SpeechConstant.CLOUD_GRAMMAR, null)
        asr.setParameter(SpeechConstant.SUBJECT, null)
        asr.setParameter(SpeechConstant.RESULT_TYPE, "json")
        asr.setParameter(SpeechConstant.ENGINE_TYPE, engineType)
        asr.setParameter(SpeechConstant.LANGUAGE, params["language"]?.toString() ?: "zh_cn")
        asr.setParameter(SpeechConstant.ACCENT, params["accent"]?.toString() ?: "mandarin")
        asr.setParameter(SpeechConstant.VAD_BOS, params["vad_bos"]?.toString() ?: "4000")
        asr.setParameter(SpeechConstant.VAD_EOS, params["vad_eos"]?.toString() ?: "1000")
        asr.setParameter(SpeechConstant.ASR_PTT, params["asr_ptt"]?.toString() ?: "1")
        asr.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true")
    }

    private inner class IflyTekRecognizerListener(
        private val engineType: String,
        private val asrCallback: AsrCallback?,
    ) : RecognizerListener {
        private val recognizedBuffer = StringBuffer()

        override fun onVolumeChanged(volume: Int, data: ByteArray) {
            asrCallback?.onVolumeChanged(volume.toFloat())
        }

        override fun onResult(result: RecognizerResult, isLast: Boolean) {
            val text = if ("cloud".equals(engineType, ignoreCase = true)) {
                JsonParser.parseGrammarResult(result.resultString)
            } else {
                JsonParser.parseLocalGrammarResult(result.resultString)
            }

            recognizedBuffer.append(text)
            if (isLast) {
                Log.d(TAG, recognizedBuffer.toString())
                asrCallback?.onResult(recognizedBuffer.toString())
            }
        }

        override fun onEndOfSpeech() {
            Log.d(TAG, "onEndOfSpeech")
        }

        override fun onBeginOfSpeech() {
            Log.d(TAG, "onBeginOfSpeech")
        }

        override fun onError(error: SpeechError) {
            Log.e(TAG, "onError Code：" + error.errorCode)
            asrCallback?.onError(error.errorDescription)
        }

        override fun onEvent(eventType: Int, arg1: Int, arg2: Int, bundle: Bundle?) {
            Log.d(TAG, "onEvent: $eventType $arg1 $arg2 $bundle")
        }
    }

    companion object {
        private const val TAG = "IflytekAsr"
    }
}