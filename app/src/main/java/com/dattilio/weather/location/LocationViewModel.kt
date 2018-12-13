package com.dattilio.weather.location

import androidx.lifecycle.ViewModel
import com.dattilio.weather.location.model.Location
import com.dattilio.weather.location.model.LocationUiState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

class LocationViewModel : ViewModel() {
    val locationStateSubject: BehaviorSubject<LocationUiState> =
        BehaviorSubject.createDefault(LocationUiState.Loading)
    private lateinit var subscription: Disposable

    fun setup(locationRepository: LocationRepository) {
        subscription = locationRepository.getLocations()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ list ->
                handleSuccess(list)
            }, { error ->
                handleError(error)
            })
    }

    private fun handleError(error: Throwable) {
        Timber.e(error)
        locationStateSubject.onNext(LocationUiState.Error("Oops"))
    }

    private fun handleSuccess(locations: List<Location>) {
        if (locations.isEmpty()) {
            locationStateSubject.onNext(LocationUiState.Empty)
        } else {
            locationStateSubject.onNext(LocationUiState.Success(locations))
        }
    }

    override fun onCleared() {
        subscription.dispose()
        super.onCleared()
    }
}
