package com.example.converter

class MassConverter : BaseConverter {
    private val exchangeRates = mapOf(
        "kg" to 1.0,
        "lb" to 2.20462262,
        "oz" to 35.2739619,
    )

    override fun convert(amount: Double, fromUnit: String, toUnit: String): Double {
        val fromRate = exchangeRates[fromUnit] ?: throw IllegalArgumentException("Invalid from mass unit")
        val toRate = exchangeRates[toUnit] ?: throw IllegalArgumentException("Invalid to mass unit")
        return amount * (toRate / fromRate)
    }
}