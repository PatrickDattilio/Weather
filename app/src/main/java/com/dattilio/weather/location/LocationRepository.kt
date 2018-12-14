package com.dattilio.weather.location

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.dattilio.weather.location.model.Location
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.Single


class LocationRepository(val context: Context) {
    private val locationPreference = "LOCATION_PREFERENCE"
    private val locationList = "LOCATION_LIST"
    private val moshi = Moshi.Builder().build()
    private val listMyData = Types.newParameterizedType(List::class.java, Location::class.java)
    private val adapter = moshi.adapter<List<Location>>(listMyData)
    private val preference = context.getSharedPreferences(locationPreference, MODE_PRIVATE)

    fun getLocations(): Single<List<Location>> {
        return Single.defer {
            Single.just(retrieveLocation())
        }
    }

    private fun retrieveLocation(): List<Location> {
        val listAsString = preference.getString(locationList, "") ?: ""
        return if (listAsString.isEmpty()) {
            mutableListOf()
        } else {
            adapter.fromJson(listAsString)?.toMutableList() ?: mutableListOf()
        }
    }

    fun addLocation(location: Location) {
        val list = retrieveLocation()
        if(!list.contains(location)) {
            list.toMutableList().add(location)
            preference.edit().putString(locationList, adapter.toJson(list)).apply()
        }
    }
}
