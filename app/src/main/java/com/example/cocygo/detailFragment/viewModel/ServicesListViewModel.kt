package com.example.capstonefragment.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.capstonefragment.model.Cart
import com.example.capstonefragment.model.Service
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class ServicesListViewModel : ViewModel() {
    val db = Firebase.firestore

    val addToCartFlag = MutableLiveData<Boolean>()

    val getCartByUserFlag = MutableLiveData<Boolean>()
    val cartLists = MutableLiveData<ArrayList<Cart>>()

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

    fun getCartByUser(userID: String) {
        db.collection("CartList")
            .whereEqualTo("userID", userID)
            .get()
            .addOnSuccessListener { documents ->
                var cartList = ArrayList<Cart>()
                for (document in documents) {
                    val cartItem = Cart(
                        document.data["userID"].toString(),
                        document.data["serviceID"].toString()
                    )
                    cartList.add(cartItem)
                }
                cartLists.postValue(cartList)
                getCartByUserFlag.postValue(true)

//                Log.d("CartList", "getCartByUser: ${cartList}")
            }
            .addOnFailureListener {  it ->
                getCartByUserFlag.postValue(false)
                Log.d("getCartByUser", "getCartByUser: ${it.message}")
            }
    }

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
                        document.data["description"].toString()
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

    fun duplicateDocument(originalDocPath: String, newDocPath: String) {
        val originalDocRef = db.document(originalDocPath)
        val newDocRef = db.document(newDocPath)

        originalDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // Get the original data
                    val data = documentSnapshot.data

                    // Write the data to the new document
                    newDocRef.set(data ?: emptyMap<String, Any>())
                        .addOnSuccessListener {
                            println("Document duplicated successfully!")
                        }
                        .addOnFailureListener { e ->
                            println("Error duplicating document: $e")
                        }
                } else {
                    println("Original document does not exist.")
                }
            }
            .addOnFailureListener { e ->
                println("Error fetching original document: $e")
            }
    }

}