package com.example.converter

class DistanceConverter : BaseConverter {
    private val exchangeRates = mapOf(
        "m" to 1.0,
        "ft" to 3.2808399,
        "in" to 39.3700787,
    )

    override fun convert(amount: Double, fromUnit: String, toUnit: String): Double {
        val fromRate = exchangeRates[fromUnit] ?: throw IllegalArgumentException("Invalid from currency")
        val toRate = exchangeRates[toUnit] ?: throw IllegalArgumentException("Invalid to currency")
        return amount * (toRate / fromRate)
    }
}