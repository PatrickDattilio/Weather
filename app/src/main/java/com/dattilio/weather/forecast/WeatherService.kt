package com.dattilio.weather.forecast

import com.dattilio.weather.forecast.model.WeatherApiModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("forecast?units=imperial&APPID=f7a322e44c44dc1ab6cf9cb05e388d55")
    fun weatherByZip(@Query("zip") zip: String): Single<WeatherApiModel>
}