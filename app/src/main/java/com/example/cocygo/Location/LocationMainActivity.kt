package com.example.cocygo.Location

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.cocygo.R

import com.example.cocygo.databinding.LocationActivityMainBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class LocationMainActivity : AppCompatActivity(), OnMapReadyCallback, FullScreenDialogFragment.FullScreenDialogListener {

    private lateinit var binding: LocationActivityMainBinding

    private lateinit var googleMap: GoogleMap
    private val mapViewModel: MapViewModel by viewModels()

    // Flag to check if map is ready
    private var isMapReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LocationActivityMainBinding.inflate(layoutInflater)
//
        enableEdgeToEdge()
        setContentView(binding.root)

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Observe location data from the ViewModel
        mapViewModel.location.observe(this, Observer { location ->
            location?.let {
                // Call updateMap only if the map is ready
                if (isMapReady) {
                    updateMap(it)
                }
            }
        })
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        isMapReady = true

        // Set a listener to show the full-screen dialog when the map is clicked
        googleMap.setOnMapClickListener { location ->
            showFullScreenDialog(location)
        }
        // Update map with the initial location from the ViewModel
        mapViewModel.location.value?.let {
            updateMap(it)
        }


    }



    private fun updateMap(location: LatLng) {
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(location).title("View on Map"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 1f))
    }



    override fun onDialogLocationSelected(location: LatLng) {
        updateMap(location)
    }

    private fun showFullScreenDialog(location: LatLng) {
        val dialog = FullScreenDialogFragment.newInstance(location)
        dialog.show(supportFragmentManager, "FullScreenDialog")
    }
}



