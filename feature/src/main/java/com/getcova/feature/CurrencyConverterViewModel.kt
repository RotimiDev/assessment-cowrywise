package com.getcova.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getcova.core.common.CurrencyRepository
import com.getcova.core.model.CurrencyConversionResponse
import com.getcova.core.model.CurrencyResponse
import com.getcova.core.model.TimeSeriesResponse
import com.getcova.core.network.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyConverterViewModel
    @Inject
    constructor(
        private val repository: CurrencyRepository,
    ) : ViewModel() {

    private val _conversionResult = MutableStateFlow<ApiResult<CurrencyConversionResponse>?>(null)
    val conversionResult: StateFlow<ApiResult<CurrencyConversionResponse>?> = _conversionResult

    private val _isConverting = MutableStateFlow(false)
    val isConverting: StateFlow<Boolean> = _isConverting

        private val _latestRates = MutableStateFlow<ApiResult<CurrencyResponse>>(ApiResult.Loading)
        val latestRates: StateFlow<ApiResult<CurrencyResponse>> = _latestRates

        private val _timeSeriesData = MutableStateFlow<ApiResult<TimeSeriesResponse>>(ApiResult.Loading)
        val timeSeriesData: StateFlow<ApiResult<TimeSeriesResponse>> = _timeSeriesData

        private val _currencySymbols =
            MutableStateFlow<ApiResult<Map<String, String>>>(ApiResult.Loading)
        val currencySymbols: StateFlow<ApiResult<Map<String, String>>> = _currencySymbols

        private val _amount = MutableStateFlow("1")
        val amount: StateFlow<String> = _amount

        private val _fromCurrency = MutableStateFlow("EUR")
        val fromCurrency: StateFlow<String> = _fromCurrency

        private val _toCurrency = MutableStateFlow("PLN")
        val toCurrency: StateFlow<String> = _toCurrency

    fun convertCurrency() {
        viewModelScope.launch {
            _isConverting.value = true
            _conversionResult.value = ApiResult.Loading

            val amountValue = _amount.value.toDoubleOrNull() ?: 1.0
            val result = repository.convertCurrency(_fromCurrency.value, _toCurrency.value, amountValue)

            _conversionResult.value = result
            _isConverting.value = false
        }
    }

    fun getLatestRates() {
            viewModelScope.launch {
                _latestRates.value = ApiResult.Loading
                _latestRates.value = repository.getLatestRates()
            }
        }

        fun fetchTimeSeriesRates(
            startDate: String = "2024-01-01",
            endDate: String = "2024-02-01",
        ) {
            viewModelScope.launch {
                _timeSeriesData.value = ApiResult.Loading
                _timeSeriesData.value =
                    repository.getTimeSeriesRates(
                        startDate,
                        endDate,
                        _fromCurrency.value,
                        "USD,GBP,PLN",
                    )
            }
        }

        fun getCurrencySymbols() {
            viewModelScope.launch {
                _currencySymbols.value = ApiResult.Loading
                delay(100)
                _currencySymbols.value = repository.getCurrencySymbols()
            }
        }

        fun updateAmount(newAmount: String) {
            _amount.value = newAmount
        }
    }
