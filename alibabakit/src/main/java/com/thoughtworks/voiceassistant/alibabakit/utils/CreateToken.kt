package com.thoughtworks.voiceassistant.alibabakit.utils

import android.annotation.SuppressLint
import android.util.Base64
import com.alibaba.fastjson.JSON
import com.thoughtworks.voiceassistant.core.logger.Logger
import com.thoughtworks.voiceassistant.core.logger.debug
import com.thoughtworks.voiceassistant.core.logger.error
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.Arrays
import java.util.Date
import java.util.SimpleTimeZone
import java.util.UUID
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class CreateToken(
    private val logger: Logger,
) {
    data class TokenResult(
        val token: String = "",
        val expireTime: Long = System.currentTimeMillis(),
    )

    /**
     * Get timestamp in ISO8601 format.
     * It must comply with ISO8601 standard and use UTC time with +0 timezone.
     */
    @SuppressLint("SimpleDateFormat")
    private fun getISO8601Time(date: Date?): String {
        val nowDate = date ?: Date()
        val df = SimpleDateFormat(FORMAT_ISO8601)
        df.timeZone = SimpleTimeZone(0, TIME_ZONE)
        return df.format(nowDate)
    }

    /**
     * Get UUID.
     */
    private fun getUniqueNonce(): String {
        val uuid = UUID.randomUUID()
        return uuid.toString()
    }

    /**
     * URL encoding.
     * Encode request parameters and values using UTF-8 charset according to RFC3986.
     */
    @Throws(UnsupportedEncodingException::class)
    private fun percentEncode(value: String?): String? {
        return value?.let {
            URLEncoder.encode(it, URL_ENCODING).replace("+", "%20")
                .replace("*", "%2A").replace("%7E", "~")
        }
    }

    /***
     * Sort parameters, normalize them, and compose into a request string.
     * @param queryParamsMap   All request parameters
     * @return Normalized request string
     */
    private fun canonicalizedQuery(queryParamsMap: Map<String, String>): String? {
        val sortedKeys = queryParamsMap.keys.toTypedArray()
        Arrays.sort(sortedKeys)
        return try {
            val canonicalizedQueryString = StringBuilder()
            for (key in sortedKeys) {
                canonicalizedQueryString.append("&")
                    .append(percentEncode(key)).append("=")
                    .append(percentEncode(queryParamsMap[key]))
            }
            canonicalizedQueryString.toString().substring(1).also {
                logger.debug(TAG, "Normalized request parameter string: $it")
            }
        } catch (e: UnsupportedEncodingException) {
            logger.error(TAG, "UTF-8 encoding is not supported.")
            e.printStackTrace()
            null
        }
    }

    /***
     * Construct signature string.
     * @param method       HTTP request method
     * @param urlPath      HTTP request resource path
     * @param queryString  Normalized request string
     * @return Signature string
     */
    private fun createStringToSign(method: String, urlPath: String, queryString: String): String? {
        return try {
            val strBuilderSign = StringBuilder()
            strBuilderSign.append(method)
            strBuilderSign.append("&")
            strBuilderSign.append(percentEncode(urlPath))
            strBuilderSign.append("&")
            strBuilderSign.append(percentEncode(queryString))
            strBuilderSign.toString().also {
                logger.debug(TAG, "Constructed signature string: $it")
            }
        } catch (e: UnsupportedEncodingException) {
            logger.error(TAG, "UTF-8 encoding is not supported.")
            e.printStackTrace()
            null
        }
    }

    /***
     * Calculate signature.
     * @param stringToSign      Signature string
     * @param accessKeySecret   Alibaba Cloud AccessKey Secret with an appended ampersand (&)
     * @return Calculated signature
     */
    private fun sign(stringToSign: String, accessKeySecret: String): String? {
        return try {
            val mac = Mac.getInstance(ALGORITHM_NAME)
            mac.init(SecretKeySpec(accessKeySecret.toByteArray(charset(ENCODING)), ALGORITHM_NAME))
            val signData = mac.doFinal(stringToSign.toByteArray(charset(ENCODING)))
            val signBase64 = Base64.encodeToString(signData, Base64.NO_WRAP)
            logger.debug(TAG, "Calculated signature: $signBase64")
            percentEncode(signBase64).also {
                logger.debug(TAG, "URL-encoded signature: $it")
            }
        } catch (e: NoSuchAlgorithmException) {
            throw IllegalArgumentException(e.toString())
        } catch (e: UnsupportedEncodingException) {
            throw IllegalArgumentException(e.toString())
        } catch (e: InvalidKeyException) {
            throw IllegalArgumentException(e.toString())
        }
    }

    /***
     * Send HTTP GET request to get token and expiration timestamp.
     * @param queryString Request parameters
     */
    @SuppressLint("SimpleDateFormat")
    private suspend fun processGETRequest(queryString: String): TokenResult =
        withContext(Dispatchers.IO) {
            var url = "http://nls-meta.cn-shanghai.aliyuncs.com"
            url += "/"
            url += "?$queryString"
            logger.debug(TAG, "HTTP request link: $url")
            val request = Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .get()
                .build()
            try {
                val client = OkHttpClient()
                val response = client.newCall(request).execute()
                val result = response.body?.string()
                if (response.isSuccessful) {
                    val rootObj = JSON.parseObject(result)
                    val tokenObj = rootObj.getJSONObject("Token")
                    if (tokenObj != null) {
                        val token = tokenObj.getString("Id")
                        val expireTime = tokenObj.getLongValue("ExpireTime")
                        logger.debug(
                            TAG,
                            "Obtained Token: $token, Expiration timestamp (seconds): $expireTime"
                        )
                        // Convert 10-digit timestamp to Beijing time
                        val expireDate =
                            SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(expireTime * 1000))
                        logger.debug(TAG, "Token expiration Beijing time: $expireDate")
                        response.close()
                        return@withContext TokenResult(token, expireTime)
                    } else {
                        logger.error(TAG, "Failed to submit request for token: $result")
                        response.close()
                        return@withContext TokenResult()
                    }
                } else {
                    logger.error(TAG, "Failed to submit request for token: $result")
                    response.close()
                    return@withContext TokenResult()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext TokenResult()
            }
        }

    /**
     * Public function to retrieve token using accessKey and accessKeySecret.
     */
    suspend fun getToken(accessKeyId: String, accessKeySecret: String): TokenResult {
        logger.debug(TAG, getISO8601Time(null))
        // All request parameters
        val queryParamsMap = HashMap<String, String>()
        queryParamsMap["AccessKeyId"] = accessKeyId
        queryParamsMap["Action"] = "CreateToken"
        queryParamsMap["Version"] = "2019-02-28"
        queryParamsMap["Timestamp"] = getISO8601Time(null)
        queryParamsMap["Format"] = "JSON"
        queryParamsMap["RegionId"] = "cn-shanghai"
        queryParamsMap["SignatureMethod"] = "HMAC-SHA1"
        queryParamsMap["SignatureVersion"] = "1.0"
        queryParamsMap["SignatureNonce"] = getUniqueNonce()

        // 1. Construct normalized request string
        val queryString = canonicalizedQuery(queryParamsMap)
        if (queryString == null) {
            logger.error(TAG, "Failed to construct normalized request string!")
            return TokenResult()
        }

        // 2. Construct signature string
        val method = "GET"  // HTTP request method, GET
        val urlPath = "/"   // Request path
        val stringToSign = createStringToSign(method, urlPath, queryString)
        if (stringToSign == null) {
            logger.error(TAG, "Failed to construct signature string!")
            return TokenResult()
        }

        // 3. Calculate signature
        val signature = sign(stringToSign, "$accessKeySecret&")
        if (signature == null) {
            logger.error(TAG, "Failed to calculate signature!")
            return TokenResult()
        }

        // 4. Add signature to the request string obtained in step 1
        val queryStringWithSign = "Signature=$signature&$queryString"
        logger.debug(TAG, "Request string with signature: $queryStringWithSign")

        // 5. Send HTTP GET request to get token.
        return processGETRequest(queryStringWithSign)
    }

    companion object {
        private const val TAG = "CreateToken"
        private const val TIME_ZONE = "GMT"
        private const val FORMAT_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        private const val URL_ENCODING = "UTF-8"
        private const val ALGORITHM_NAME = "HmacSHA1"
        private const val ENCODING = "UTF-8"
    }
}
