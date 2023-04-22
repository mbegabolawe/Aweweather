package com.example.aweweather.data.services

import com.example.aweweather.data.models.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherService {

    @GET("weather?lat={lat}&lon={lon}")
    suspend fun getWeatherByGeo(@Query("lat") lat: String, @Query("lon") lon: String) : Response<WeatherResponse>

    @GET("weather?")
    suspend fun getWeatherByCity(@Query("q") city: String) : Response<WeatherResponse>

}