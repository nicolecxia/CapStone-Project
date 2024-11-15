package com.example.cocygo.homeFragment.adapter

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


class ServicesListViewModel : ViewModel() {
    val db = Firebase.firestore

    val getServiceListFlag = MutableLiveData<Boolean>()
    val serviceLists = MutableLiveData<ArrayList<Service>>()



    fun getServiceList() {
        val docRef = db.collection("ServiceList")
            .get()
            .addOnSuccessListener { documents ->
                val serviceList = ArrayList<Service>()
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

                Log.d("ServiceList", "ServiceList: ${serviceList}")
            }
            .addOnFailureListener {
                getServiceListFlag.postValue(false)
                Log.d("getServiceList", "getServiceList: ${it.message}")
            }
    }

}