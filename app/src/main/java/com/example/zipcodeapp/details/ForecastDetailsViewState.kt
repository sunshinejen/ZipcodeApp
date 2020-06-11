package com.example.zipcodeapp.details

import com.example.zipcodeapp.api.WeatherDescription

data class ForecastDetailsViewState(
    val temp: Float,
    val description: String,
    val date: String,
    val iconUrl:String
)