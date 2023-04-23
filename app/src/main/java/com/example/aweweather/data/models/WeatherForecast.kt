package com.example.aweweather.data.models

data class WeatherForecast(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<Day>,
    val message: Double
)