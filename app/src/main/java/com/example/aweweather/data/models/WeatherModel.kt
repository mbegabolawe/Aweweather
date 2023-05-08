package com.example.aweweather.data.models

data class WeatherModel(
    val name: String,
    val temp: Double,
    val temp_max: Double,
    val temp_min: Double,
    val main: String,
    val weather: List<Day>
    )