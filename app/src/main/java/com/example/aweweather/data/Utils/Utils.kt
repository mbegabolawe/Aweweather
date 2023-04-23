package com.example.aweweather.data.Utils

import java.text.SimpleDateFormat
import java.util.*

fun getDayFromTimeStamp(dt: Long): String {
    val seconds = dt * 1000
    val date = Date(seconds)
    val sdf = SimpleDateFormat("EEEE", Locale.getDefault());
    return sdf.format(date)
}