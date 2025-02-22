package com.thoughtworks.voiceassistant.core.utils

import com.thoughtworks.voiceassistant.core.utils.RateToValue.Range
import com.thoughtworks.voiceassistant.core.utils.RateToValue.calculateValueFromRate
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class RateToValueTest {
    @Test
    fun test_calculateValueFromRate_with_range_negative_500_to_500() {
        val range = Range(min = -500f, max = 500f)

        println(calculateValueFromRate(1f, range))
        println(calculateValueFromRate(2f, range))
        println(calculateValueFromRate(0.5f, range))
        println(calculateValueFromRate(4f, range))
        println(calculateValueFromRate(0.1f, range))
    }

    @Test
    fun test_calculateValueFromRate_with_range_1_to_100() {
        val range = Range(min = 1f, max = 100f)

        println(calculateValueFromRate(1f, range))
        println(calculateValueFromRate(2f, range))
        println(calculateValueFromRate(0.5f, range))
        println(calculateValueFromRate(4f, range))
        println(calculateValueFromRate(0.1f, range))
    }
}