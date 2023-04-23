package com.example.aweweather.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aweweather.R
import com.example.aweweather.data.Utils.getDayFromTimeStamp
import com.example.aweweather.data.models.Day

class ForecastAdapter : ListAdapter<Day, ForecastAdapter.DayViewHolder>(DayDiffCallback) {

    class DayViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            private val dayOfWeek: TextView = itemView.findViewById(R.id.day)
            private val temp: TextView = itemView.findViewById(R.id.temp)
            private val weatherImage: ImageView = itemView.findViewById(R.id.weather_image)

        fun bind(day: Day) {
            dayOfWeek.text = getDayFromTimeStamp(day.dt).toString()
            temp.text = itemView.context.getString(R.string.degrees, day.temp.day)

            day.weather.get(0).let {
                when (it.main) {
                    "Rain" -> Glide.with(itemView.context).load(R.drawable.clear).into(weatherImage)
                    "Clear" -> Glide.with(itemView.context).load(R.drawable.clear).into(weatherImage)
                    "Clouds" -> Glide.with(itemView.context).load(R.drawable.partlysunny).into(weatherImage)
                    //default
                    else -> Glide.with(itemView.context).load(R.drawable.rain).into(weatherImage)
                }
            }
        }
    }

    object DayDiffCallback: DiffUtil.ItemCallback<Day>() {
        override fun areItemsTheSame(oldItem: Day, newItem: Day): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Day, newItem: Day): Boolean {
            return oldItem.temp.day == newItem.temp.day
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.forecast_list_item, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val day = getItem(position)
        holder.bind(day)
    }
}