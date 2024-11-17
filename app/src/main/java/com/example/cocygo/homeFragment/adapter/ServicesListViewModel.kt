package com.example.cocygo.homeFragment.adapter

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


class ServicesListViewModel : ViewModel() {
    val db = Firebase.firestore

    val addToCartFlag = MutableLiveData<Boolean>()

    val getServiceListFlag = MutableLiveData<Boolean>()
    val serviceLists = MutableLiveData<ArrayList<Service>>()

    fun addToCart(serviceID: String, userID: String) {
        val cartList = db.collection("CartList")

        val cartMap = hashMapOf(
            "serviceID" to serviceID,
            "userID" to userID
        )

        cartList.document().set(cartMap)
            .addOnSuccessListener {
                addToCartFlag.postValue(true)
            }
            .addOnFailureListener {
                addToCartFlag.postValue(false)
            }
    }

    fun deleteFromCart(serviceID: String, userID: String){
        val cartList = db.collection("CartList")

        cartList
            .whereEqualTo("serviceID", serviceID)  // First condition
            .whereEqualTo("userID", userID)  // Second condition
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    Log.d("Firestore", "No matching documents found.")
                    return@addOnSuccessListener
                }
                for (document in querySnapshot) {
                    cartList.document(document.id)
                        .delete()
                        .addOnSuccessListener {
                            Log.d("Firestore", "Document ${document.id} in CartList successfully deleted!")
                        }
                        .addOnFailureListener { e ->
                            Log.w("Firestore", "Error deleting document ${document.id} in CartList", e)
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error fetching documents: ", e)
            }


    }


    fun getServiceList() {
        val docRef = db.collection("ServiceList")
            .get()
            .addOnSuccessListener { documents ->
                val serviceList = ArrayList<Service>()
                for (document in documents) {

                    val serviceItem = Service(
                        document.id,
                        document.data["duration"].toString().toInt(),
                        document.data["image"].toString(),
                        document.data["rating"].toString().toFloat(),
                        document.data["serviceName"].toString(),
                        document.data["state"].toString(),
                        document.data["stylist"].toString(),
                        document.data["description"].toString(),
                        false
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