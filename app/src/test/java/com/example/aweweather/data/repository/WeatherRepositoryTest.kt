package com.example.aweweather.data.repository

import com.example.aweweather.data.models.WeatherResponse
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK

internal class WeatherRepositoryTest {

    @MockK
    lateinit var weatherResponse: WeatherResponse


    @org.junit.jupiter.api.BeforeEach
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @org.junit.jupiter.api.Test
    fun getWeatherByGeo() {
    }

    @org.junit.jupiter.api.Test
    fun getWeatherByCity() {
    }

    @org.junit.jupiter.api.Test
    fun getWeatherForecast() {
    }

    @org.junit.jupiter.api.Test
    fun getApi() {
    }
}