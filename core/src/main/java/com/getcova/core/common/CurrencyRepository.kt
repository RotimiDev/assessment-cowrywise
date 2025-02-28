package com.getcova.core.common

import com.getcova.core.Constants
import com.getcova.core.model.CurrencyConversionResponse
import com.getcova.core.model.CurrencyResponse
import com.getcova.core.model.TimeSeriesResponse
import com.getcova.core.network.APIService
import com.getcova.core.network.ApiResult
import javax.inject.Inject

class CurrencyRepository
    @Inject
    constructor(
        private val apiService: APIService,
    ) {
        suspend fun convertCurrency(
            from: String,
            to: String,
            amount: Double,
        ): ApiResult<CurrencyConversionResponse> =
            try {
                val response = apiService.convertCurrency(Constants.FIXER_API_KEY, from, to, amount)

                if (response.success) {
                    ApiResult.Success(response)
                } else {
                    ApiResult.Error(response.error?.info ?: "Currency conversion failed")
                }
            } catch (e: Exception) {
                ApiResult.Error(message = e.localizedMessage ?: "An unknown error occurred")
            }

        suspend fun getLatestRates(): ApiResult<CurrencyResponse> =
            try {
                val response = apiService.getLatestRates(Constants.FIXER_API_KEY)
                ApiResult.Success(response)
            } catch (e: Exception) {
                ApiResult.Error(message = e.localizedMessage ?: "An unknown error occurred")
            }

        suspend fun getTimeSeriesRates(
            startDate: String,
            endDate: String,
            base: String,
            symbols: String,
        ): ApiResult<TimeSeriesResponse> =
            try {
                val response =
                    apiService.getTimeSeriesRates(
                        Constants.FIXER_API_KEY,
                        startDate,
                        endDate,
                        base,
                        symbols,
                    )
                ApiResult.Success(response)
            } catch (e: Exception) {
                ApiResult.Error(message = e.localizedMessage ?: "An unknown error occurred")
            }

        suspend fun getCurrencySymbols(): ApiResult<Map<String, String>> =
            try {
                val response = apiService.getSymbols(Constants.FIXER_API_KEY)

                if (response.success) {
                    ApiResult.Success(response.symbols)
                } else {
                    ApiResult.Error("Failed to fetch currency symbols")
                }
            } catch (e: Exception) {
                ApiResult.Error(message = e.localizedMessage ?: "An unknown error occurred")
            }
    }
