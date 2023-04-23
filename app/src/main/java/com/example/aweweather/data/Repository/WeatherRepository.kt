package com.example.aweweather.data.Repository

import com.example.aweweather.data.models.WeatherForecast
import com.example.aweweather.data.models.WeatherResponse
import com.example.aweweather.data.services.NetworkResult
import com.example.aweweather.data.services.WeatherService
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

//todo add to roomDb
class WeatherRepository(val api: WeatherService) {

    suspend fun getWeatherByGeo(lat: String, lon: String) = flow<NetworkResult<WeatherResponse>> {
        emit(NetworkResult.Loading())

        with (api.getWeatherByGeo(lat, lon)) {
            if (isSuccessful) {
                emit(NetworkResult.Success(body()))
            } else {
                emit(NetworkResult.Error(errorBody()?.string()))
            }
        }
    }.catch {
        emit(NetworkResult.Error(it.localizedMessage))
    }

    suspend fun getWeatherByCity(city: String) = flow<NetworkResult<WeatherResponse>> {
        emit(NetworkResult.Loading())

        with (api.getWeatherByCity(city)) {
            if (isSuccessful) {
                emit(NetworkResult.Success(body()))
            } else {
                emit(NetworkResult.Error(errorBody()?.string()))
            }
        }
    }.catch {
        emit(NetworkResult.Error(it.localizedMessage))
    }

    suspend fun getWeatherForecast(lat: String, lon: String) = flow<NetworkResult<WeatherForecast>> {
        emit(NetworkResult.Loading())

        with(api.getWeatherForecastByGeo(lat, lon)) {
            if (isSuccessful) {
                emit(NetworkResult.Success(body()))
            } else {
                emit(NetworkResult.Error(errorBody()?.string()))
            }
        }
    }.catch {
        emit(NetworkResult.Error(it.localizedMessage))
    }
}