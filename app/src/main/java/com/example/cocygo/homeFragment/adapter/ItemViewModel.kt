package com.example.cocygo.homeFragment.adapter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class ItemViewModel : ViewModel() {
    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>> get() = _items
    val db = Firebase.firestore

    val getServiceListFlag = MutableLiveData<Boolean>()
    val serviceLists = MutableLiveData<ArrayList<Service>>()
    val serviceList = ArrayList<Service>()

    init {
        loadItems()

    }

    private fun loadItems() {
        getServiceList()
    }

    fun getServiceList() {
        val docRef = db.collection("ServiceList")
            .get()
            .addOnSuccessListener { documents ->
                serviceList.clear() // Clear previous data if necessary
                for (document in documents) {
                    val serviceItem = Service(
                        document.data["duration"].toString().toInt(),
                        document.data["image"].toString(),
                        document.data["rating"].toString().toFloat(),
                        document.data["serviceName"].toString(),
                        document.data["state"].toString(),
                        document.data["stylist"].toString(),
                        document.data["description"].toString(),
                    )
                    Log.d("documentFromFirebase", document.data.toString())
                    serviceList.add(serviceItem)
                }

                serviceLists.postValue(serviceList)
                getServiceListFlag.postValue(true)

                // Update LiveData with new items after fetching
                _items.value = serviceList.mapIndexed { index, service ->
                    Item(index,service.image, service.serviceName, service.description)
                }

                Log.d("ServiceList", "ServiceList: $serviceList")
            }
            .addOnFailureListener {
                getServiceListFlag.postValue(false)
                Log.d("getServiceList", "getServiceList: ${it.message}")
            }
    }
}