package com.example.converter

class CurrencyConverter : BaseConverter {
    private val exchangeRates = mapOf(
        "USD" to 1.0,
        "EUR" to 0.85,
        "GBP" to 0.73,
        "JPY" to 110.0
    )

    override fun convert(amount: Double, fromUnit: String, toUnit: String): Double {
        val fromRate = exchangeRates[fromUnit] ?: throw IllegalArgumentException("Invalid from currency")
        val toRate = exchangeRates[toUnit] ?: throw IllegalArgumentException("Invalid to currency")
        return amount * (toRate / fromRate)
    }
}