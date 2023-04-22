package com.example.aweweather.di

import android.content.Context
import com.example.aweweather.Constants
import com.example.aweweather.data.Repository.WeatherRepository
import com.example.aweweather.data.services.Interceptor.NetworkConnectionInterceptor
import com.example.aweweather.data.services.Interceptor.QueryParamInterceptor
import com.example.aweweather.data.services.WeatherService
import com.example.aweweather.ui.view.viewmodel.MainActivityViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


private fun provideRetrofit(context: Context):
        Retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(Constants.BASE_URL)
    .client(provideOkHttpClient(context))
    .build()


private fun provideOkHttpClient(context: Context) : OkHttpClient {
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return OkHttpClient.Builder()
        .addInterceptor(QueryParamInterceptor())
        .addInterceptor(loggingInterceptor)
        .addInterceptor(NetworkConnectionInterceptor(context))
        .writeTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()
}


private val networkModule = module {
    single { provideRetrofit(get()) }

    single { get<Retrofit>().create(WeatherService::class.java) }
}

private val repositoryModule = module {
    single { WeatherRepository(get()) }
}

private val viewModule = module {
    viewModel { MainActivityViewModel(get()) }
}

val allModules = listOf(
    networkModule,
    repositoryModule,
    viewModule

)