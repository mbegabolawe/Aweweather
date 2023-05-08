package com.example.aweweather.data.repository

import com.example.aweweather.data.models.WeatherForecast
import com.example.aweweather.data.models.WeatherModel
import com.example.aweweather.data.models.WeatherResponse
import com.example.aweweather.data.services.NetworkResult
import com.example.aweweather.data.services.WeatherService
import kotlinx.coroutines.flow.*

//todo add to roomDb
class WeatherRepository(private val api: WeatherService) {

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


    /**
     * Network calls to be used to be combined and returned as single response
     * ##improvement return NetworkResult and handle scenarios
     */
    private suspend fun getWeatherForecastObj(lat: String, lon: String) = flow {
        with(api.getWeatherForecastByGeo(lat, lon)) {
            if (isSuccessful) {
                emit(body())
            }
        }
    }

    private suspend fun getWeatherByGeoObj(lat: String, lon: String) = flow {
        with (api.getWeatherByGeo(lat, lon)) {
            if (isSuccessful) {
                emit(body())
            }
        }
    }

    suspend fun getWeatherResponse(lat: String, lon: String) = flow<NetworkResult<WeatherModel?>> {
        emit(NetworkResult.Loading())
        var weather: WeatherModel? = null

        //Combine the two network calls to return single data model
        combine(
            getWeatherByGeoObj(lat, lon),
            getWeatherForecastObj(lat, lon)
        ) { networkGeo, networkForecast ->
            //Add better checks for failure
            networkGeo?.let { geo ->
                networkForecast?.let { forecast ->
                    weather = WeatherModel(
                        geo.name,
                        geo.main.temp,
                        geo.main.temp_max,
                        geo.main.temp_min,
                        geo.weather[0].main,
                        forecast.list
                    )
                    emit(NetworkResult.Success(weather))
                }
            } ?: run {
                emit(NetworkResult.Error("failed to get updates"))
            }
        }.collect()
    }.catch {
        emit(NetworkResult.Error(it.localizedMessage))
    }
}