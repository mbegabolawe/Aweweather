package com.example.aweweather.data.services

import com.example.aweweather.data.models.WeatherForecast
import com.example.aweweather.data.models.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherService {

    @GET("weather")
    suspend fun getWeatherByGeo(@Query("lat") lat: String, @Query("lon") lon: String) : Response<WeatherResponse>

    @GET("forecast/daily?")
    suspend fun getWeatherForecastByGeo(@Query("lat") lat: String, @Query("lon") lon: String) : Response<WeatherForecast>

    @GET("weather")
    suspend fun getWeatherByCity(@Query("q") city: String) : Response<WeatherResponse>

}