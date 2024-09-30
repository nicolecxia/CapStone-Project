package com.example.cocygo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class MapViewModel: ViewModel() {
    private val _location = MutableLiveData<LatLng>()
    val location: LiveData<LatLng> get() =_location

    init {
        _location.value = LatLng(43.01343, -81.19885)
    }

    fun updateLocation(lat: Double, lng: Double) {
        _location.value = LatLng(lat, lng)
    }
}