package com.example.cocygo.Location.Calander.repository

import android.util.Log
import com.example.cocygo.Location.Calander.model.CalenderModel
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseRepository {
    private val db = FirebaseFirestore.getInstance()

    // Function to save the selected date and time to Firebase
    fun saveSelectedDate(selectedDate: String, selectedTime: String, callback: (Boolean) -> Unit) {
        val dateData = hashMapOf(
            "date" to selectedDate,
            "time" to selectedTime,
           "cartId" to ""
        )

        // Add the date to Firestore
        db.collection("BookingList")
            .add(dateData)
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener { error ->
                Log.e("FirebaseRepository", "Error saving date: ${error.message}")
                callback(false)
            }
    }


    // Function to read dates from Firebase
    fun readDates(callback: (List<CalenderModel>) -> Unit) {
        db.collection("BookingList")
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Log.e("FirebaseRepository", "No documents found in BookingList collection.")
                } else {
                    Log.d("FirebaseRepository", "Found ${documents.size()} documents.")
                }
                val dates = mutableListOf<CalenderModel>()
                for (document in documents) {
                    val date = document.getString("date")
                    val time = document.getString("time")
                    if (date != null) {
                        Log.d("FirebaseRepository", "Document: ${document.id}, Date: $date, Time: $time")
                        dates.add(CalenderModel(date = date, time = time ?: ""))
                    } else {
                        Log.e("FirebaseRepository", "Document does not contain a 'date' field.")
                    }
                }
                callback(dates)
            }
            .addOnFailureListener { error ->
                Log.e("FirebaseRepository", "Error reading dates: ${error.message}")
                callback(emptyList())
            }

    }
}