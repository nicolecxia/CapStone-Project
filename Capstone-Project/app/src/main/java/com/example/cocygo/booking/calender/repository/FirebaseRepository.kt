package com.example.cocygo.booking.calender.repository

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import com.example.cocygo.booking.calender.model.CalenderModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


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
                    val id = document.id // Get the document ID from Firestore
                    if (date != null) {
                        Log.d("FirebaseRepository", "Document: ${document.id}, Date: $date, Time: $time")
                        dates.add(CalenderModel(id = id, date = date, time = time ?: ""))
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
    // Function to delete a booking from Firestore

    fun deleteBookingById(id: String, callback: (Boolean) -> Unit) {
        // Access the collection where bookings are stored and delete by ID
        db.collection("BookingList") // Ensure this is the correct collection name
            .document(id) // Document reference by id
            .delete() // Delete the document
            .addOnSuccessListener {
                Log.d("FirebaseRepository", "Booking with id $id successfully deleted.")
                callback(true) // Notify success via callback
            }
            .addOnFailureListener { error ->
                Log.e("FirebaseRepository", "Error deleting booking: ${error.message}")
                callback(false) // Notify failure via callback
            }
    }

    private fun showAlert(context: Context, title: String, message: String) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}