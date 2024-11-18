package com.thoughtworks.voiceassistant.iflytekkit.utils

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class ASRResponse(
    @SerializedName("sn") val sn: Int,
    @SerializedName("ls") val ls: Boolean,
    @SerializedName("bg") val bg: Int,
    @SerializedName("ed") val ed: Int,
    @SerializedName("ws") val ws: List<Word>
)

data class Word(
    @SerializedName("bg") val bg: Int,
    @SerializedName("cw") val cw: List<ChineseWord>
)

data class ChineseWord(
    @SerializedName("sc") val sc: Double,
    @SerializedName("w") val w: String
)

// Parse the Gson string and return the recognized Chinese
fun parseAsrResponse(jsonString: String): String {
    val gson = Gson()

    // Parse the JSON string to the data class
    val asrResponse = gson.fromJson(jsonString, ASRResponse::class.java)

    // Used to save the parsed result
    val stringBuilder = StringBuilder()

    // Traverse the ws array and merge the "w" field with the highest sc in cw into the result
    asrResponse.ws.forEach { word ->
        var bestScore = Double.NEGATIVE_INFINITY
        var bestWord: String? = null

        // Traverse each object in the cw array and select the segmentation with the highest sc
        word.cw.forEach { chineseWord ->
            if (chineseWord.sc > bestScore) {
                bestScore = chineseWord.sc
                bestWord = chineseWord.w
            }
        }

        bestWord?.let { stringBuilder.append(it) }
    }

    // Return the recognized Chinese result
    return stringBuilder.toString()
}