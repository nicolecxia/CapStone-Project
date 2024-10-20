package com.example.cocygo.service.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cocygo.service.model.Service

class ServiceDetailViewModel: ViewModel() {
    val serviceInfo = MutableLiveData<Service>()
}