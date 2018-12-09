package com.dattilio.weather.location

import com.dattilio.weather.RxTestRule
import com.dattilio.weather.location.model.Location
import com.dattilio.weather.location.model.LocationUiState
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Single
import org.junit.ClassRule
import org.junit.Test

class LocationViewModelTest {
    companion object {
        @ClassRule
        @JvmField
        val rxTestRule = RxTestRule()
    }

    @Test
    fun noSetup() {
        val locationViewmodel = LocationViewModel()
        val testObserver = locationViewmodel.locationSubject.test()
        testObserver.assertValue(LocationUiState.Loading)
    }

    @Test
    fun setupThenEmpty() {
        val locationViewmodel = LocationViewModel()
        val mockLocationRepository = mock<LocationRepository> {
            on { getLocations() } doReturn Single.just(emptyList())
        }
        locationViewmodel.setup(mockLocationRepository)
        val testObserver = locationViewmodel.locationSubject.test()
        testObserver.assertValue(LocationUiState.Empty)
    }

    @Test
    fun setupThenError() {
        val locationViewmodel = LocationViewModel()
        val mockLocationRepository = mock<LocationRepository> {
            on { getLocations() } doReturn Single.error(Throwable("test"))
        }
        locationViewmodel.setup(mockLocationRepository)
        val testObserver = locationViewmodel.locationSubject.test()
        testObserver.assertValue(LocationUiState.Error("Oops"))
    }

    @Test
    fun setupThenSuccess() {
        val locationViewmodel = LocationViewModel()
        val list = listOf(Location("Hagerstown","21742"))
        val mockLocationRepository = mock<LocationRepository> {
            on { getLocations() } doReturn Single.just(list)
        }
        locationViewmodel.setup(mockLocationRepository)
        val testObserver = locationViewmodel.locationSubject.test()
        testObserver.assertValue(LocationUiState.Success(list))
    }
}