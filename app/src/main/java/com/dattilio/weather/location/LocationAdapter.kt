package com.dattilio.weather.location

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dattilio.weather.databinding.ItemLocationBinding
import com.dattilio.weather.location.model.Location

class LocationAdapter(val onLocationClicked: OnLocationClickedListener) : RecyclerView.Adapter<LocationViewHolder>() {
    val data: MutableList<Location> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val databinding = ItemLocationBinding.inflate(inflater, parent, false)
        return LocationViewHolder(databinding, onLocationClicked)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val location = data[position]
        holder.bind(location)
    }

    fun update(newData: List<Location>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }
}

class LocationViewHolder(
    val databinding: ItemLocationBinding,
    val onLocationClicked: OnLocationClickedListener
) : RecyclerView.ViewHolder(databinding.root) {
    fun bind(location: Location) {
        databinding.location = location
        databinding.locationClicked = onLocationClicked
    }
}