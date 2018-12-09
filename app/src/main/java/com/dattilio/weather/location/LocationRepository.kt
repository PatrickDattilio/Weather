package com.dattilio.weather.location

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.dattilio.weather.location.model.Location
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.Single


class LocationRepository(val context: Context) {
    val locationPreference = "LOCATION_PREFERENCE"
    val locationList = "LOCATION_LIST"
    val moshi = Moshi.Builder().build()
    val listMyData = Types.newParameterizedType(List::class.java, Location::class.java)
    val adapter = moshi.adapter<List<Location>>(listMyData)
    val preference = context.getSharedPreferences(locationPreference, MODE_PRIVATE)

    fun getLocations(): Single<List<Location>> {
        return Single.defer {
            Single.just(retrieveLocation())
        }
    }

    private fun retrieveLocation(): MutableList<Location> {
        val listAsString = preference.getString(locationList, "") ?: ""
        return if (listAsString.isEmpty()) {
            mutableListOf()
        } else {
            adapter.fromJson(listAsString)?.toMutableList() ?: mutableListOf()
        }
    }

    fun addLocation(location: Location) {
        val list = retrieveLocation()
        list.add(location)
        preference.edit().putString(locationList, adapter.toJson(list)).apply()
    }
}
