package com.dattilio.weather.forecast

import androidx.lifecycle.ViewModel
import com.dattilio.weather.forecast.model.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class WeatherViewModel : ViewModel() {
    val weatherSubject: BehaviorSubject<ForecastUiState> = BehaviorSubject.createDefault(ForecastUiState.Loading)

    val weatherService = Retrofit.Builder()
        .baseUrl("http://api.openweathermap.org/data/2.5/")
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(WeatherService::class.java)
    lateinit var subscription: Disposable

    fun setZip(zip: String) {
        subscription = weatherService.weatherByZip(zip)
            .map { weatherApiResponse -> mapApiToUi(weatherApiResponse, zip) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ newData -> success(newData) },
                { error -> error(error) })
    }

    fun success(data: Weather) {
        weatherSubject.onNext(ForecastUiState.Success(data))
    }

    fun error(error: Throwable) {
        Timber.e(error)
        weatherSubject.onNext(ForecastUiState.Error("Oops"))
    }

    override fun onCleared() {
        subscription.dispose()
        super.onCleared()
    }

    private fun mapApiToUi(apiResponse: WeatherApiModel, zip: String): Weather {
        return Weather(
            apiResponse.list.map { hourly -> mapHourlyApiToUi(hourly) },
            apiResponse.city.name,
            zip
        )
    }

    private val MILLISECONDS_IN_SECONDS = 1000
    private val dayFormat = SimpleDateFormat("E")
    private val timeFormat = SimpleDateFormat("HH:mm")
    private fun mapHourlyApiToUi(hourlyApiModel: HourlyWeatherResponse): HourlyForecast {
        val date = Date(hourlyApiModel.dt * MILLISECONDS_IN_SECONDS)
        val weatherStatus = hourlyApiModel.weather[0]
        val maxTemp = hourlyApiModel.main.temp_max.roundToInt()
        val minTemp = hourlyApiModel.main.temp_min.roundToInt()
        val shouldShowLowTemp = maxTemp != minTemp
        return HourlyForecast(
            dayFormat.format(date),
            timeFormat.format(date),
            weatherStatus.description,
            "http://openweathermap.org/img/w/${weatherStatus.icon}.png",
            "$maxTemp \u2109",
            "$minTemp \u2109",
            shouldShowLowTemp
        )
    }
}
