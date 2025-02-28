package com.getcova.core.model

data class CurrencyConversionResponse(
    val success: Boolean,
    val query: QueryData?,
    val info: InfoData?,
    val historical: Boolean?,
    val date: String?,
    val result: Double?,
    val error: ErrorResponse?
)

data class QueryData(
    val from: String,
    val to: String,
    val amount: Double
)

data class InfoData(
    val timestamp: Long,
    val rate: Double
)

data class ErrorResponse(
    val code: Int,
    val type: String,
    val info: String
)
