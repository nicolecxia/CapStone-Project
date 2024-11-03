package com.example.capstonefragment.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.capstonefragment.model.Service

class ServiceDetailViewModel:ViewModel() {
    val serviceInfo = MutableLiveData<Service>()

}