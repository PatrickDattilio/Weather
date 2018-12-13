package com.dattilio.weather.forecast.model

sealed class ForecastUiState {
    object Loading : ForecastUiState()
    data class Success(val weather: Weather) : ForecastUiState()
    data class Error(val error: String) : ForecastUiState()
}

data class Weather(
    val hourlyForecastList: List<HourlyForecast>,
    val cityName: String,
    val zip: String
)

data class HourlyForecast(
    val day: String,
    val time: String,
    val description: String,
    val iconUrl: String,
    val highTemp: String,
    val lowTemp: String,
    val shouldShowLowTemp: Boolean
)