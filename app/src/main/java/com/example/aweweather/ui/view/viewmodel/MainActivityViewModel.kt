package com.example.aweweather.ui.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aweweather.data.Repository.WeatherRepository
import com.example.aweweather.data.services.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivityViewModel(val weatherRepository: WeatherRepository): ViewModel() {

    fun getWeather() {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.getWeatherByCity("Cape Town").collect {
                when (it) {
                    is NetworkResult.Error -> Timber.d("Error: ${it.message}")
                    is NetworkResult.Loading -> Timber.d("loading")
                    is NetworkResult.Success -> Timber.d("Success: ${it.data}")
                }
            }
        }
    }
}