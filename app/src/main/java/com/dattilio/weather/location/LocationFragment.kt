package com.dattilio.weather.location

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dattilio.weather.R
import com.dattilio.weather.location.model.Location
import com.dattilio.weather.location.model.LocationUiState
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_location.*
import timber.log.Timber

interface OnLocationClickedListener {
    fun onLocationClicked(zip: String)
}

class LocationFragment : Fragment(), OnLocationClickedListener {

    private lateinit var viewModel: LocationViewModel
    private lateinit var locationAdapter: LocationAdapter
    private lateinit var subscription: Disposable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationAdapter = LocationAdapter(this)
        recyclerview.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        recyclerview.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
        recyclerview.adapter = locationAdapter
        floatingActionButton.setOnClickListener { onFabClicked() }

        viewModel = ViewModelProviders.of(this).get(LocationViewModel::class.java)
        viewModel.setup(LocationRepository(requireActivity().applicationContext))
        subscription =
                viewModel.locationStateSubject.subscribe({ uiState -> updateUi(uiState) }, { error -> Timber.e(error) })

    }

    private fun updateUi(uiState: LocationUiState) {
        when (uiState) {
            LocationUiState.Loading -> showLoading()
            LocationUiState.Empty -> showEmpty()
            is LocationUiState.Success -> showLocations(uiState.locations)
            is LocationUiState.Error -> showError()
        }
    }

    private fun showError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun showLocations(locations: List<Location>) {
        locationAdapter.update(locations)
        loading.visibility = GONE
        addLocation.visibility = GONE
        recyclerview.visibility = VISIBLE
    }

    private fun showEmpty() {
        recyclerview.visibility = GONE
        loading.visibility = GONE
        addLocation.visibility = VISIBLE
    }

    private fun showLoading() {
        recyclerview.visibility = GONE
        addLocation.visibility = GONE
        loading.visibility = VISIBLE
    }


    private fun onFabClicked() {
        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_NUMBER
        input.filters = arrayOf(InputFilter.LengthFilter(5))
        AlertDialog.Builder(requireContext())
            .setTitle("Select a location")
            .setMessage("Enter your zipcode")
            .setView(input)
            .setPositiveButton("Ok") { _, _ ->
                val zip = input.text.toString()
                onLocationClicked(zip)
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    override fun onLocationClicked(zip: String) {
        val action = LocationFragmentDirections.openWeatherAction(zip)
        findNavController(this).navigate(action)
    }
}
