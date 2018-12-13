package com.dattilio.weather.location

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dattilio.weather.databinding.ItemLocationBinding
import com.dattilio.weather.location.model.Location

class LocationAdapter(private val onLocationClicked: OnLocationClickedListener) : RecyclerView.Adapter<LocationViewHolder>() {
    private val locations: MutableList<Location> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val locationBinding = ItemLocationBinding.inflate(inflater, parent, false)
        return LocationViewHolder(locationBinding, onLocationClicked)
    }

    override fun getItemCount(): Int {
        return locations.size
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val location = locations[position]
        holder.bind(location)
    }

    fun update(newData: List<Location>) {
        locations.clear()
        locations.addAll(newData)
        notifyDataSetChanged()
    }
}

class LocationViewHolder(
    private val locationBinding: ItemLocationBinding,
    private val onLocationClicked: OnLocationClickedListener
) : RecyclerView.ViewHolder(locationBinding.root) {
    fun bind(location: Location) {
        locationBinding.location = location
        locationBinding.locationClicked = onLocationClicked
    }
}