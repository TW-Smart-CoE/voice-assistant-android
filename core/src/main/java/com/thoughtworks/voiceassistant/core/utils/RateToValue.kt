package com.thoughtworks.voiceassistant.core.utils

object RateToValue {
    data class Range(val min: Float, val max: Float)

    fun calculateValueFromRate(rate: Float, range: Range): Float {
        val maxMultiplier = 4f
        val midValue = (range.max + range.min) / 2

        return when {
            rate >= 1 -> midValue + (range.max - midValue) * (rate - 1) / (maxMultiplier - 1)
            else -> midValue - (midValue - range.min) * (1 - rate)
        }
    }
}