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
    val locationSubject: BehaviorSubject<LocationUiState> = BehaviorSubject.createDefault(LocationUiState.Loading)
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
        locationSubject.onNext(LocationUiState.Error("Oops"))
    }

    private fun handleSuccess(list: List<Location>) {
        if (list.isEmpty()) {
            locationSubject.onNext(LocationUiState.Empty)
        } else {
            locationSubject.onNext(LocationUiState.Success(list))
        }
    }

    override fun onCleared() {
        subscription.dispose()
        super.onCleared()
    }
}
