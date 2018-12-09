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
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dattilio.weather.R
import com.dattilio.weather.location.model.LocationUiState
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_location.*
import timber.log.Timber

interface OnLocationClickedListener {
    fun onLocationClicked(view: View, zip: String)
}

class LocationFragment : Fragment(), OnLocationClickedListener {

    companion object {
        fun newInstance() = LocationFragment()
    }

    private lateinit var viewModel: LocationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    private lateinit var locationAdapter: LocationAdapter

    private lateinit var subscription: Disposable

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationAdapter = LocationAdapter(this)
        recyclerview.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        recyclerview.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
        recyclerview.adapter = locationAdapter

        viewModel = ViewModelProviders.of(this).get(LocationViewModel::class.java)
        viewModel.setup(LocationRepository(requireContext()))
        subscription =
                viewModel.locationSubject.subscribe({ uiState -> updateUi(uiState) }, { error -> Timber.e(error) })
        floatingActionButton.setOnClickListener { onFabClicked(view) }
    }

    private fun updateUi(uiState: LocationUiState) {
        when (uiState) {
            LocationUiState.Loading -> showLoading()
            LocationUiState.Empty -> showEmpty()
            is LocationUiState.Success -> showLocations(uiState)
            is LocationUiState.Error -> showError()
        }
    }

    private fun showLocations(uiState: LocationUiState.Success) {
        locationAdapter.update(uiState.data)
        loading.visibility = GONE
        addLocation.visibility = GONE
        recyclerview.visibility = VISIBLE
    }

    private fun showError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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


    private fun onFabClicked(view: View) {
        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_NUMBER
        input.filters = arrayOf(InputFilter.LengthFilter(5))
        AlertDialog.Builder(requireContext())
            .setTitle("Select a location")
            .setMessage("Enter your zipcode")
            .setView(input)
            .setPositiveButton("Ok") { _, _ ->
                val zip = input.text.toString()
                onLocationClicked(view, zip)
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    override fun onLocationClicked(view: View, zip: String) {
        val action = LocationFragmentDirections.openWeatherAction(zip)
        view.findNavController().navigate(action)
    }
}
