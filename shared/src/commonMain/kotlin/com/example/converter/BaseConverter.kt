package com.example.converter

interface BaseConverter {
    fun convert(amount: Double, fromUnit: String, toUnit: String): Double

    fun calc(converter: BaseConverter,
             input: String,
             fromUnit: String,
             toUnit: String): String {

        val amountValue = input.toDoubleOrNull() ?: 0.0
        val convertedAmount = converter.convert(amountValue, fromUnit, toUnit)
        val result = String.format("%.2f %s", convertedAmount, toUnit)

        return result
    }

    fun processNumberInput(converter: BaseConverter,
                           input: String,
                           sign: String,
                           fromUnit: String,
                           toUnit: String): Pair<String, String> {
        val amount = when (sign) {
            "d" -> {
                if (input.length == 1) "0" else input.dropLast(1)
            }
            else -> {
                if (input == "0") sign else input + sign
            }
        }

        val result = calc(converter, amount, fromUnit, toUnit)

        return Pair(amount, result)
    }
}