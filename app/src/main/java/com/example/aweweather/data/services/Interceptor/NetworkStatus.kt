package com.example.aweweather.data.services.Interceptor

sealed class NetworkStatus {
    /** Device has a valid internet connection */
    object Connected: NetworkStatus()
    /** Device has no internet connection */
    object Disconnected: NetworkStatus()
}
