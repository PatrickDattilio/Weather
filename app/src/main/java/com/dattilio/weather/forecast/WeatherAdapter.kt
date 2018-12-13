package com.dattilio.weather.forecast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dattilio.weather.databinding.ItemWeatherBinding
import com.dattilio.weather.forecast.model.HourlyForecast

class WeatherAdapter : RecyclerView.Adapter<WeatherViewHolder>() {
    private var hourlyForecasts: MutableList<HourlyForecast> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val weatherBinding = ItemWeatherBinding.inflate(inflater, parent, false)
        return WeatherViewHolder(weatherBinding)
    }

    override fun getItemCount(): Int {
        return hourlyForecasts.size
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weather = hourlyForecasts[position]
        holder.bind(weather)
    }

    fun updateWeather(newData: List<HourlyForecast>) {
        hourlyForecasts.addAll(newData)
        notifyDataSetChanged()
    }
}

class WeatherViewHolder(private val weatherBinding: ItemWeatherBinding) : RecyclerView.ViewHolder(weatherBinding.root) {
    fun bind(weather: HourlyForecast) {
        weatherBinding.weather = weather
    }
}
