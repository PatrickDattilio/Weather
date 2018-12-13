package com.dattilio.weather.location.model

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

sealed class LocationUiState {
    object Loading : LocationUiState()
    object Empty: LocationUiState()
    data class Success(val locations: List<Location>) : LocationUiState()
    data class Error(val error: String) : LocationUiState()
}

@Parcelize
@JsonClass(generateAdapter = true)
data class Location(val name: String, val zip: String) : Parcelable{
    override fun toString(): String {
        return "$name, $zip"
    }
}