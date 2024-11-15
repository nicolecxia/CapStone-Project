import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cocygo.Location.Calander.view.Adapter
import com.example.cocygo.Location.Calander.view.SpaceItemDecoration
import com.example.cocygo.databinding.FragmentSelectedDateBinding

class SelectedDateFragment : Fragment() {
    private lateinit var binding: FragmentSelectedDateBinding
    private lateinit var datePickerViewModel: DatePickerViewModel
    private val firebaseRepository = FirebaseRepository()
    private lateinit var adapter: Adapter
    private var selectedServiceId: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectedDateBinding.inflate(inflater, container, false)
        datePickerViewModel = ViewModelProvider(requireActivity()).get(DatePickerViewModel::class.java)

        adapter = Adapter(emptyList()) { cartId ->
            selectedServiceId = cartId
            Toast.makeText(requireContext(), "Selected Service ID: $cartId", Toast.LENGTH_SHORT).show()
        }

        binding.rv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@SelectedDateFragment.adapter
            addItemDecoration(SpaceItemDecoration(20))
        }

// Fetch services and update adapter
        firebaseRepository.readServices { services ->
            adapter.updateData(services)
        }
        datePickerViewModel.selectedDataLiveData.observe(viewLifecycleOwner) { selectedDate ->
            binding.retrievedate.text = "Booked At\n $selectedDate"
        }

        datePickerViewModel.selectedTimeLiveData.observe(viewLifecycleOwner) { selectedTime ->
            binding.retrievedate.append("|  $selectedTime")
        }

        datePickerViewModel.datesLiveData.observe(viewLifecycleOwner) { dates ->
            adapter.updateData(dates)
        }

        datePickerViewModel.fetchDates()

//        binding.buttonConfirmBooking.setOnClickListener {
//            val selectedDate = datePickerViewModel.selectedDataLiveData.value ?: ""
//            val selectedTime = datePickerViewModel.selectedTimeLiveData.value ?: ""
//            if (!selectedServiceId.isNullOrEmpty()) {
//                datePickerViewModel.saveSelectedDate(selectedDate, selectedTime, selectedServiceId!!)
//            } else {
//                Toast.makeText(requireContext(), "Please select a service", Toast.LENGTH_SHORT).show()
//            }
//        }

        return binding.root
    }
    private fun onDeleteBooking(bookingId: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Booking")
            .setMessage("Are you sure you want to delete this booking?")
            .setPositiveButton("Yes") { _, _ ->
                firebaseRepository.deleteBooking(bookingId) { success ->
                    if (success) {
                        Log.d("SelectedDateFragment", "Booking deleted successfully.")
                        // Refresh the list of bookings
                        datePickerViewModel.fetchDates() // Assuming this fetches and updates the list
                    } else {
                        Log.e("SelectedDateFragment", "Failed to delete booking.")
                        Toast.makeText(requireContext(), "Failed to delete booking.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("No", null)
            .show()
    }
}