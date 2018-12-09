package com.dattilio.weather.forecast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dattilio.weather.databinding.ItemWeatherBinding
import com.dattilio.weather.forecast.model.HourlyForecast

class WeatherAdapter : RecyclerView.Adapter<WeatherViewHolder>() {
    var data: MutableList<HourlyForecast> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val databinding = ItemWeatherBinding.inflate(inflater, parent, false)
        return WeatherViewHolder(databinding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weather = data[position]
        holder.bind(weather)
    }

    fun updateWeather(newData: List<HourlyForecast>) {
        data.addAll(newData)
        notifyDataSetChanged()
    }
}

class WeatherViewHolder(val databinding: ItemWeatherBinding) : RecyclerView.ViewHolder(databinding.root) {
    fun bind(weather: HourlyForecast) {
        databinding.weather = weather
    }
}
