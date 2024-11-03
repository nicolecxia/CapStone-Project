package com.example.cocygo.Location.Location

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.cocygo.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
class FullScreenDialogFragment : DialogFragment(), OnMapReadyCallback {

    private var listener: FullScreenDialogListener? = null
    private lateinit var googleMap: GoogleMap
    private var location: LatLng? = null
    private lateinit var mapViewModel: MapViewModel

    interface FullScreenDialogListener {
        fun onDialogLocationSelected(location: LatLng)
    }

    companion object {
        fun newInstance(location: LatLng): FullScreenDialogFragment {
            val args = Bundle()
            args.putDouble("latitude", location.latitude)  // Use putDouble
            args.putDouble("longitude", location.longitude)
            val fragment = FullScreenDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FullScreenDialogListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement FullScreenDialogListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true

        // Initialize the ViewModel
        mapViewModel = ViewModelProvider(requireActivity()).get(MapViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val latitude = arguments?.getDouble("latitude") ?: 0.0
        val longitude = arguments?.getDouble("longitude") ?: 0.0
        location = LatLng(latitude, longitude)  // Set the initial location

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_full_screen_dialog, container, false)

        // Initialize the map
        val mapFragment = childFragmentManager.findFragmentById(R.id.full_screen_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Observe location changes from the ViewModel
        mapViewModel.location.observe(viewLifecycleOwner) { latLng ->
            location = latLng // Update the local location variable

            // Only update the map if it's ready
            if (::googleMap.isInitialized) {
                googleMap.addMarker(MarkerOptions().position(latLng).title("We are here"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            }
        }



        // Add close button functionality
        val closeButton = view.findViewById<Button>(R.id.close_button)
        closeButton.setOnClickListener {
            location?.let { listener?.onDialogLocationSelected(it) }
            dismiss()
        }
        return view
    }

    override fun onMapReady(map: GoogleMap) {
        if (map != null) {
            googleMap = map
            // Add a marker for the initial location
            location?.let {
                googleMap.addMarker(MarkerOptions().position(it).title("We are here"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 15f)) // Adjust zoom level as needed
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }


}



