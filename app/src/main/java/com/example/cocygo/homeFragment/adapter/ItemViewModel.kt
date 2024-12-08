package com.example.cocygo.homeFragment.adapter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class ItemViewModel : ViewModel() {
    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>> get() = _items
    val db = Firebase.firestore
    val firebaseAuth = FirebaseAuth.getInstance()

    val getServiceListFlag = MutableLiveData<Boolean>()
    val serviceLists = MutableLiveData<ArrayList<Service>>()
    val serviceList = ArrayList<Service>()


    val addToCartFlag = MutableLiveData<Boolean>()
    val getCartByUserFlag = MutableLiveData<Boolean>()
    val cartLists = MutableLiveData<List<Cart>>()

    init {
        loadItems()
    }

    private fun loadItems() {
        firebaseAuth.currentUser?.let { getServiceList(it.uid) }
    }

    fun getServiceList(userID: String) {
        // Query all liked serviceID from CartList by userid
        db.collection("CartList")
            .whereEqualTo("userID", userID)
            .get()
            .addOnSuccessListener { documentsCart ->
                // Get all serviceID from collection
                val serviceIDsLiked = documentsCart.map { it.getString("serviceID") }

                val docRef = db.collection("ServiceList")
                    .get()
                    .addOnSuccessListener { documents ->
                        serviceList.clear() // Clear previous data if necessary
                        for (document in documents) {

                            val serviceID = document.id

                            // Step 4: Set status to true or false based on presence in collection B
                            val likeStatus = if (serviceID in serviceIDsLiked) {
                                true
                            } else {
                                false
                            }

                            val serviceItem = Service(
                                document.id,
                                document.data["duration"].toString().toInt(),
                                document.data["image"].toString(),
                                document.data["rating"].toString().toFloat(),
                                document.data["serviceName"].toString(),
                                document.data["state"].toString(),
                                document.data["stylist"].toString(),
                                document.data["description"].toString(),
                                likeStatus
                            )
                            Log.d(
                                "documentFromFirebase",
                                document.data.toString() + "-----id:" + document.id
                            )
                            serviceList.add(serviceItem)
                            Log.d("item likeStatus", "${serviceItem.likeStatus}")
                        }

                        serviceLists.postValue(serviceList)
                        getServiceListFlag.postValue(true)

                        // Update LiveData with new items after fetching
                        _items.value = serviceList.mapIndexed { index, service ->
//                    give a false likeStatus when the first time load
                            Item(
                                service.id,
                                service.image,
                                service.serviceName,
                                service.description,
                                service.likeStatus
                            )
                        }

                        Log.d("ServiceList", "ServiceList: $serviceList")
                    }
                    .addOnFailureListener {
                        getServiceListFlag.postValue(false)
                        Log.d("getServiceList", "getServiceList: ${it.message}")
                    }

            }
            .addOnFailureListener { it ->
                getCartByUserFlag.postValue(false)
                Log.d("getCartByUser", "getCartByUser: ${it.message}")
            }
    }


}