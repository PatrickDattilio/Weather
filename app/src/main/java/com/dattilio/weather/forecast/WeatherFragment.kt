package com.dattilio.weather.forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dattilio.weather.DelayedToolbarTitleChanger
import com.dattilio.weather.MainActivity
import com.dattilio.weather.R
import com.dattilio.weather.forecast.model.ForecastUiState
import com.dattilio.weather.forecast.model.Weather
import com.dattilio.weather.location.LocationRepository
import com.dattilio.weather.location.model.Location
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_weather.*
import timber.log.Timber

class WeatherFragment : Fragment() {

    private lateinit var viewModel: WeatherViewModel
    private lateinit var weatherAdapter: WeatherAdapter
    private lateinit var locationRepository: LocationRepository
    private lateinit var subscription: Disposable
    private lateinit var titleChanger: DelayedToolbarTitleChanger

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val zip = WeatherFragmentArgs.fromBundle(arguments).zip

        locationRepository = LocationRepository(requireActivity().applicationContext)
        weatherAdapter = WeatherAdapter()
        recyclerview.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        recyclerview.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
        recyclerview.adapter = weatherAdapter

        viewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java)
        viewModel.setZip(zip)
        subscription =
                viewModel.forecastUiState.subscribe({ uiState -> updateUi(uiState) }, { error -> Timber.e(error) })
    }

    override fun onDestroyView() {
        subscription.dispose()
        super.onDestroyView()
    }

    private fun updateUi(uiState: ForecastUiState) {
        when (uiState) {
            ForecastUiState.Loading -> showLoading()
            is ForecastUiState.Success -> showWeather(uiState.weather)
            is ForecastUiState.Error -> showError()
        }
    }

    private fun showError() {
    }

    private fun showWeather(weather: Weather) {
        weatherAdapter.updateWeather(weather.hourlyForecastList)
        updateCity(weather.cityName, weather.zip)
        loading.visibility = GONE
        recyclerview.visibility = VISIBLE
    }


    private fun updateCity(cityName: String, zip: String) {
        titleChanger.changeTitle(cityName)
        locationRepository.addLocation(Location(cityName, zip))
    }

    private fun showLoading() {
        recyclerview.visibility = GONE
        loading.visibility = VISIBLE
    }

    fun setDelayedToolbarTitleChanger(changer: DelayedToolbarTitleChanger){
        titleChanger = changer
    }
}
