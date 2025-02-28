package com.getcova.core.network

import com.getcova.core.model.CurrencyConversionResponse
import com.getcova.core.model.CurrencyResponse
import com.getcova.core.model.SymbolsResponse
import com.getcova.core.model.TimeSeriesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    @GET("latest")
    suspend fun getLatestRates(
        @Query("access_key") apiKey: String,
    ): CurrencyResponse

    @GET("convert")
    suspend fun convertCurrency(
        @Query("access_key") apiKey: String,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: Double,
    ): CurrencyConversionResponse

    @GET("timeseries")
    suspend fun getTimeSeriesRates(
        @Query("access_key") apiKey: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("base") base: String,
        @Query("symbols") symbols: String,
    ): TimeSeriesResponse

    @GET("symbols")
    suspend fun getSymbols(
        @Query("access_key") accessKey: String,
    ): SymbolsResponse
}
