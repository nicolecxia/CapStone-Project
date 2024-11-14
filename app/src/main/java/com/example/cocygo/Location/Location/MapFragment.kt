package com.example.cocygo.Location.Location

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.cocygo.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback, FullScreenDialogFragment.FullScreenDialogListener {

    private lateinit var googleMap: GoogleMap
    private lateinit var mapViewModel: MapViewModel
    private var isMapReady = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        mapViewModel = ViewModelProvider(requireActivity()).get(MapViewModel::class.java)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return view
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        isMapReady = true

        googleMap.setOnMapClickListener { location ->
            showFullScreenDialog(location)
        }

        mapViewModel.location.observe(viewLifecycleOwner) { location ->
            location?.let {
                if (isMapReady) {
                    updateMap(it)
                }
            }
        }
    }

    fun updateMap(location: LatLng) {
        googleMap.clear()
        googleMap.addMarker(MarkerOptions().position(location).title("View on Map"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
    }

    private fun showFullScreenDialog(location: LatLng) {
        val dialog = FullScreenDialogFragment.newInstance(location)
        dialog.show(parentFragmentManager, "FullScreenDialog") // Use parentFragmentManager
    }
    override fun onDialogLocationSelected(location: LatLng) {
        updateMap(location)
    }
}