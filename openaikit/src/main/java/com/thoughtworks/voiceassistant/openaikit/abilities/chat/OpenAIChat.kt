import android.content.Context
import com.google.gson.Gson
import com.thoughtworks.voiceassistant.core.abilities.Chat
import com.thoughtworks.voiceassistant.core.logger.DefaultLogger
import com.thoughtworks.voiceassistant.core.logger.Logger
import com.thoughtworks.voiceassistant.core.logger.debug
import com.thoughtworks.voiceassistant.openaikit.abilities.chat.ChatConfig
import com.thoughtworks.voiceassistant.openaikit.abilities.chat.ChatParams
import com.thoughtworks.voiceassistant.openaikit.abilities.chat.models.ChatRequest
import com.thoughtworks.voiceassistant.openaikit.abilities.chat.models.ChatResponse
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume

class OpenAIChat(
    val context: Context,
    val logger: Logger,
    val config: ChatConfig,
) : Chat {
    private var systemPromptList = mutableListOf<Chat.Message>()
    private var conversionList = mutableListOf<Chat.Message>()
    private val gson: Gson = Gson()
    private var isChatting = false
    private var currentRequest: okhttp3.Call? = null

    interface ChatListener {
        fun onResult(message: Chat.Message)
        fun onError(errorMessage: String)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .readTimeout(config.readTimeout, TimeUnit.MILLISECONDS)
        .writeTimeout(config.writeTimeout, TimeUnit.MILLISECONDS)
        .build()

    override suspend fun initialize() {
        logger.debug(TAG, "OpenAIChat initialized")
    }

    private fun chat(userMessage: Chat.Message, listener: ChatListener) {
        val messages = systemPromptList + conversionList + listOf(userMessage)

        val chatRequest = ChatRequest(
            model = config.model,
            messages = messages,
            temperature = ChatParams.Temperature.VALUES.DEFAULT,
            maxTokens = ChatParams.MaxTokens.VALUES.DEFAULT
        )

        val requestBody = gson.toJson(chatRequest).toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("${config.baseUrl}/${config.apiVersion}/chat/completions")
            .addHeader("Authorization", "Bearer ${config.apiKey}")
            .post(requestBody)
            .build()

        isChatting = true
        currentRequest = okHttpClient.newCall(request)

        currentRequest?.enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                if (call.isCanceled()) {
                    listener.onError("Request cancelled")
                } else {
                    listener.onError(e.message ?: "Unknown error")
                }
                isChatting = false
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                isChatting = false
                if (!response.isSuccessful) {
                    listener.onError("Unexpected code $response")
                    return
                }

                val responseBody = response.body?.string() ?: run {
                    listener.onError("Empty response body")
                    return
                }

                val chatResponse = gson.fromJson(responseBody, ChatResponse::class.java)
                val resMessage = chatResponse.choices[0].message

                if (conversionList.size > ChatParams.MaxHistoryLen.VALUES.DEFAULT) {
                    conversionList.removeAt(0)
                    conversionList.removeAt(0)
                }

                conversionList.add(userMessage)
                conversionList.add(resMessage)

                listener.onResult(resMessage)
            }
        })
    }

    override suspend fun chat(userMessage: Chat.Message): Chat.Result =
        suspendCancellableCoroutine { continuation ->
            chat(userMessage, object : ChatListener {
                private fun resumeWithoutComplain(result: Chat.Result) {
                    try {
                        continuation.resume(
                            result
                        )
                    } catch (_: Exception) {
                    } finally {
                        isChatting = false
                    }
                }

                override fun onResult(message: Chat.Message) {
                    resumeWithoutComplain(Chat.Result(isSuccess = true, message = message))
                }

                override fun onError(errorMessage: String) {
                    resumeWithoutComplain(
                        Chat.Result(
                            isSuccess = false,
                            errorMessage = errorMessage
                        )
                    )
                }
            })
        }

    override fun isChatting(): Boolean {
        return isChatting
    }

    override fun stop() {
        currentRequest?.cancel()
        currentRequest = null
        logger.debug(TAG, "OpenAIChat stopped")
    }

    override fun release() {
        stop()
        logger.debug(TAG, "OpenAIChat released")
    }

    override fun getSystemContext(): List<Chat.Message> {
        return systemPromptList
    }

    override fun setSystemContext(contextMessages: List<Chat.Message>) {
        systemPromptList = contextMessages.toMutableList()
    }

    override fun getConversationHistory(): List<Chat.Message> {
        return conversionList
    }

    override fun clearConversationHistory() {
        conversionList.clear()
    }

    companion object {
        private const val TAG = "OpenAIChat"

        fun create(
            context: Context,
            params: Map<String, Any> = emptyMap(),
            logger: Logger = DefaultLogger(),
        ): Chat {
            val chatConfig = ChatConfig.create(context, params)
            return OpenAIChat(context, logger, chatConfig)
        }
    }
}
