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
class FullScreenDialogFragment : DialogFragment() {

    private var listener: FullScreenDialogListener? = null
    private var location: LatLng? = null

    interface FullScreenDialogListener {
        fun onDialogLocationSelected(location: LatLng)
    }

    companion object {
        fun newInstance(location: LatLng): FullScreenDialogFragment {
            return FullScreenDialogFragment().apply {
                arguments = Bundle().apply {
                    putDouble("latitude", location.latitude)
                    putDouble("longitude", location.longitude)
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Check if the parentFragment implements the listener
        val parentFragment = parentFragment
        if (parentFragment is FullScreenDialogListener) {
            listener = parentFragment
        } else {
            throw RuntimeException("$parentFragment must implement FullScreenDialogListener")
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate your dialog layout
        return inflater.inflate(R.layout.fragment_full_screen_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve location from arguments
        val latitude = arguments?.getDouble("latitude") ?: 0.0
        val longitude = arguments?.getDouble("longitude") ?: 0.0
        location = LatLng(latitude, longitude)

        view.findViewById<Button>(R.id.close_button).setOnClickListener {
            location?.let { loc -> listener?.onDialogLocationSelected(loc) }
            dismiss()
        }
    }
}