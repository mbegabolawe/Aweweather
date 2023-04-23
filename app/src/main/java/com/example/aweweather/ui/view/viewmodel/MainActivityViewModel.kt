package com.example.aweweather.ui.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aweweather.R
import com.example.aweweather.data.repository.WeatherRepository
import com.example.aweweather.data.models.Coord
import com.example.aweweather.data.models.WeatherForecast
import com.example.aweweather.data.models.WeatherResponse
import com.example.aweweather.data.services.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivityViewModel(private val weatherRepository: WeatherRepository): ViewModel() {

    private val _currentWeather = MutableLiveData<WeatherResponse>()
    val currentWeather : LiveData<WeatherResponse>
    get() = _currentWeather


    private val _weatherForecast = MutableLiveData<WeatherForecast>()
    val weatherForecast: LiveData<WeatherForecast>
    get() = _weatherForecast

    private val _title = MutableLiveData<String>()
    val title : LiveData<String>
    get() = _title

    private val _primaryColor = MutableLiveData<Int>()
    val primaryColor : LiveData<Int>
    get() = _primaryColor

    private val _loading = MutableLiveData<Boolean>()
    val loading : LiveData<Boolean>
    get() = _loading

    private val _error = MutableLiveData<String>()
    val error : LiveData<String>
        get() = _error

    //To be used for search capability
    fun getWeatherByCity(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.getWeatherByCity(city).collect {
                when (it) {
                    is NetworkResult.Error -> {
                        _loading.postValue(false)
                        it.message?.let { message ->
                            Timber.d("Error: ${it.message}")
                            _error.postValue(message)
                        }
                    }
                    is NetworkResult.Loading -> {
                        _loading.postValue(true)
                    }
                    is NetworkResult.Success -> {
                        it.data?.let { response ->
                            Timber.d("Success: ${response}")
                            _loading.postValue(false)
                            _currentWeather.postValue(response)
                            _title.postValue(response.name)
                            setPrimaryColor(response.weather[0].main)
                        }
                    }
                }
            }
        }
    }

    private fun getWeatherByGeo(coord: Coord) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.getWeatherByGeo(coord.lat.toString(), coord.lon.toString()).collect{
                when (it) {
                    is NetworkResult.Error -> {
                        it.message?.let { message ->
                            Timber.d("Error: ${it.message}")
                            _error.postValue(message)
                        }
                    }
                    is NetworkResult.Loading -> {
                        Timber.d("loading")
                        _loading.postValue(true)
                    }
                    is NetworkResult.Success -> {
                        _loading.postValue(false)
                        it.data?.let { response ->
                            Timber.d("Success: ${response}")
                            _currentWeather.postValue(response)
                            _title.postValue(response.name)
                            setPrimaryColor(response.weather[0].main)
                        }
                    }

                }
            }
        }
    }

    private fun getWeatherForecast(coord: Coord) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.getWeatherForecast(coord.lat.toString(), coord.lon.toString()).collect{
                when (it) {
                    is NetworkResult.Error -> {
                        it.message?.let { message ->
                            Timber.d("Error: ${it.message}")
                            _error.postValue(message)
                        }
                    }
                    is NetworkResult.Loading -> {
                        Timber.d("loading")
                        _loading.postValue(true)
                    }
                    is NetworkResult.Success -> {
                        _loading.postValue(false)
                        it.data?.let { response ->
                            Timber.d("Success: ${response}")
                            _weatherForecast.postValue(response)
                        }
                    }
                }
            }
        }
    }

    private fun setPrimaryColor(main: String) {
        val color = when (main) {
            "Rain" -> R.color.rainy
            "Clear" -> R.color.sunny
            "Clouds" -> R.color.cloudy
            //default
            else -> R.color.rainy
        }
        _primaryColor.postValue(color)
    }

    fun fetchWeather(coord: Coord) {
        //Improve: merge call flows in repo
        getWeatherByGeo(coord)
        getWeatherForecast(coord)
    }
}