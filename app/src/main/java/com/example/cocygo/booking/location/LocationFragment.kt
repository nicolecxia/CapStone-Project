package com.example.cocygo.booking.location

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.cocygo.Location.Location.MapViewModel
import com.example.cocygo.booking.calender.view.CalenderFragment
import com.example.cocygo.R
import com.example.cocygo.databinding.FragmentLocationBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.FirebaseApp

class LocationFragment : Fragment(), OnMapReadyCallback,
    FullScreenDialogFragment.FullScreenDialogListener {

    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!

    private lateinit var googleMap: GoogleMap
    private val mapViewModel: MapViewModel by viewModels()

    // Flag to check if map is ready
    private var isMapReady = false
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationBinding.inflate(inflater, container, false)

        FirebaseApp.initializeApp(requireContext())

        // Check for location permission
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            setupMap()
        }

        binding.buttonBook.setOnClickListener {
            binding.buttonBook.visibility = View.GONE
            if (savedInstanceState == null) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.calender, CalenderFragment())
                    .commit()
            }
        }

        // Observe location data from the ViewModel
        mapViewModel.location.observe(viewLifecycleOwner, Observer { location ->
            location?.let {
                // Call updateMap only if the map is ready
                if (isMapReady) {
                    updateMap(it)
                }
            }
        })

        return binding.root
    }

    private fun setupMap() {
        // Get the SupportMapFragment and request notification when the map is ready to be used.
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        isMapReady = true

        // Enable the My Location layer if permission is granted
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.isMyLocationEnabled = true
        }

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
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10f))
    }

    override fun onDialogLocationSelected(location: LatLng) {
        updateMap(location)
    }

    private fun showFullScreenDialog(location: LatLng) {
        val dialog = FullScreenDialogFragment.newInstance(location)
        dialog.show(parentFragmentManager, "FullScreenDialog")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                setupMap() // Set up map if permission granted
            } else {
                // Handle the case where permission is denied
                // You could show a message to the user
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}