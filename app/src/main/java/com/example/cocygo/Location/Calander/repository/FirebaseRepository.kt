import android.util.Log
import com.example.cocygo.Location.Calander.model.CalenderModel
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseRepository {
    private val db = FirebaseFirestore.getInstance()
    private val DEFAULT_CART_ID = "Ef1jiKhjYP0j1Cvs9vG0"

    // Function to read services from Firestore
    fun readServices(callback: (List<CalenderModel>) -> Unit) {
        db.collection("ServiceList")
            .get()
            .addOnSuccessListener { documents ->
                val services = mutableListOf<CalenderModel>()
                for (document in documents) {
                    val serviceId = document.id // Assuming ID is needed
                    val serviceName = document.getString("serviceName") ?: ""
                    // Create a CalenderModel instance
                    services.add(CalenderModel(date = "", time = "", cartId = serviceId, serviceName = serviceName))
                }
                callback(services)
            }
            .addOnFailureListener { error ->
                Log.e("FirebaseRepository", "Error reading services: ${error.message}")
                callback(emptyList())
            }
    }

    // Function to save selected date with service ID (cartId)
    fun saveSelectedDate(selectedDate: String, selectedTime: String, cartId: String, callback: (Boolean) -> Unit) {
        val effectiveCartId = cartId?.takeIf { it.isNotEmpty() } ?: DEFAULT_CART_ID
        val dateData = hashMapOf(
            "date" to selectedDate,
            "time" to selectedTime,
            "cartId" to effectiveCartId // Save the cartId
        )

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
                val dates = mutableListOf<CalenderModel>()
                if (documents.isEmpty) {
                    Log.e("FirebaseRepository", "No documents found in BookingList collection.")
                    callback(dates) // Return empty list
                    return@addOnSuccessListener
                } else {
                    Log.d("FirebaseRepository", "Found ${documents.size()} documents.")
                    val documentCount = documents.size()
                    var processedCount = 0 // To track processed documents

                    for (document in documents) {
                        val date = document.getString("date")
                        val time = document.getString("time")
                        val cartId = document.getString("cartId") ?: document.id // Use document ID if cartId is not found

                        // Log the cartId being used
                        Log.d("FirebaseRepository", "Using cartId: $cartId for document: ${document.id}")

                        // Fetch the service name
                        fetchServiceName(cartId) { serviceName ->
                            if (date != null) {
                                Log.d("FirebaseRepository", "Document: ${document.id}, Date: $date, Time: $time, Service Name: $serviceName")
                                dates.add(CalenderModel(date = date, time = time ?: "", cartId = cartId, serviceName = serviceName))
                            } else {
                                Log.e("FirebaseRepository", "Document does not contain a 'date' field.")
                            }

                            processedCount++
                            // Call the callback after all documents are processed
                            if (processedCount == documentCount) {
                                callback(dates)
                            }
                        }
                    }
                }
            }
            .addOnFailureListener { error ->
                Log.e("FirebaseRepository", "Error reading dates: ${error.message}")
                callback(emptyList())
            }
    }

    fun fetchServiceName(cartId: String, callback: (String) -> Unit) {
        if (cartId.isEmpty()) {
            Log.e("FirebaseRepository", "Invalid cartId: $cartId. Returning default service name.")
            callback("") // Or return a default service name if necessary
            return
        }

        db.collection("ServiceList").document(cartId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val serviceName = document.getString("serviceName") ?: ""
                    callback(serviceName)
                } else {
                    Log.e("FirebaseRepository", "No document found for cartId: $cartId")
                    callback("") // Return empty if document doesn't exist
                }
            }
            .addOnFailureListener { error ->
                Log.e("FirebaseRepository", "Error fetching service name: ${error.message}")
                callback("")
            }
    }
    fun deleteBooking(bookingId: String, callback: (Boolean) -> Unit) {
        // Check if the bookingId is valid
        if (bookingId.isEmpty()) {
            Log.e("FirebaseRepository", "Invalid bookingId: $bookingId. Cannot delete.")
            callback(false) // Return false to indicate failure
            return
        }

        db.collection("BookingList").document(bookingId)
            .delete()
            .addOnSuccessListener {
                Log.d("FirebaseRepository", "Booking successfully deleted: $bookingId")
                callback(true) // Return true to indicate success
            }
            .addOnFailureListener { error ->
                Log.e("FirebaseRepository", "Error deleting booking: ${error.message}")
                callback(false) // Return false to indicate failure
            }
    }
}