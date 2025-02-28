package com.getcova.core.model

import com.google.gson.annotations.SerializedName

data class TimeSeriesResponse(
    val success: Boolean,
    @SerializedName("timeseries")
    val timeSeries: Boolean,
    @SerializedName("start_date")
    val startDate: String,
    @SerializedName("end_date")
    val endDate: String,
    val base: String,
    val rates: Map<String, Map<String, Double>>,
)
