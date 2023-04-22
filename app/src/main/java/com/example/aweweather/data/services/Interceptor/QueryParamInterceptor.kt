package com.example.aweweather.data.services.Interceptor

import com.example.aweweather.Constants
import okhttp3.Interceptor
import okhttp3.Response

class QueryParamInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val url = chain.request().url.newBuilder()
            .addQueryParameter("appid", Constants.API_KEY)
            .build()

        val request = chain.request().newBuilder()
            .url(url)
            .build()

        return chain.proceed(request)
    }
}