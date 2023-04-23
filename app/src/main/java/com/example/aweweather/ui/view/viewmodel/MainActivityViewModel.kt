package com.example.aweweather.ui.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aweweather.data.Repository.WeatherRepository
import com.example.aweweather.data.models.Coord
import com.example.aweweather.data.models.WeatherForecast
import com.example.aweweather.data.models.WeatherResponse
import com.example.aweweather.data.services.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivityViewModel(val weatherRepository: WeatherRepository): ViewModel() {

    private val _currentWeather = MutableLiveData<WeatherResponse>()
    val currentWeather : LiveData<WeatherResponse>
    get() = _currentWeather


    private val _weatherForecast = MutableLiveData<WeatherForecast>()
    val weatherForecast: LiveData<WeatherForecast>
    get() = _weatherForecast

    fun getWeatherByCity(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.getWeatherByCity(city).collect {
                when (it) {
                    is NetworkResult.Error -> Timber.d("Error: ${it.message}")
                    is NetworkResult.Loading -> Timber.d("loading")
                    is NetworkResult.Success -> {
                        it.data?.let { response ->
                            Timber.d("Success: ${response}")
                            _currentWeather.value = response
                        }
                    }
                }
            }
        }
    }

    fun getWeatherByGeo(coord: Coord) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.getWeatherByGeo(coord.lat.toString(), coord.lon.toString()).collect{
                when (it) {
                    is NetworkResult.Error -> Timber.d("Error: ${it.message}")
                    is NetworkResult.Loading -> Timber.d("loading")
                    is NetworkResult.Success -> {
                        it.data?.let { response ->
                            Timber.d("Success: ${response}")
                            _currentWeather.postValue(response)
                        }
                    }

                }
            }
        }
    }

    fun getWeatherForecast(coord: Coord) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.getWeatherForecast(coord.lat.toString(), coord.lon.toString()).collect{
                when (it) {
                    is NetworkResult.Error -> Timber.d("Error: ${it.message}")
                    is NetworkResult.Loading -> Timber.d("loading")
                    is NetworkResult.Success -> {
                        it.data?.let { response ->
                            Timber.d("Success: ${response}")
                            _weatherForecast.postValue(response)
                        }
                    }
                }
            }
        }
    }
}