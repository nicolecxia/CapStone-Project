package com.example.cocygo.detailFragment.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.cocygo.R
import com.example.cocygo.booking.calender.view.CalenderFragment
import com.example.cocygo.booking.calender.view.SelectedDateFragment
import com.example.cocygo.booking.calender.viewModel.DatePickerViewModel
import com.example.cocygo.booking.location.LocationFragment
import com.example.cocygo.databinding.FragmentServiceDetailBinding
import com.example.cocygo.homeFragment.adapter.ItemViewModel
import com.example.cocygo.homeFragment.adapter.ServicesListViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar


class ServiceDetailFragment(serviceID: String?,image: String?, name: String?, tittle: String?,likeStatus:Boolean?) : DialogFragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private var binding: FragmentServiceDetailBinding? = null
    var name = name
    var image = image
    var tittle = tittle
    var serviceID = serviceID
    var likeStatus = likeStatus
    private lateinit var datePickerViewModel: DatePickerViewModel
    private var selectedDate: String? = null
    private var selectedTime: String? = null

    private var firebaseAuth: FirebaseAuth =  FirebaseAuth.getInstance()
    //ViewModel
    private lateinit var servicesListViewModel: ServicesListViewModel
    private lateinit var viewModel: ItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentServiceDetailBinding.inflate(inflater, container, false)
        datePickerViewModel =
            ViewModelProvider(requireActivity()).get(DatePickerViewModel::class.java)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        servicesListViewModel =
            ViewModelProvider(requireActivity())[ServicesListViewModel::class.java]

        // Initialize ViewModel
        viewModel = ViewModelProvider(requireActivity()).get(ItemViewModel::class.java)

        val bitmap = decodeBase64ToBitmap(image!!.trim())

        binding?.title?.text = tittle
        binding?.headerImage?.setImageBitmap(bitmap)
        binding?.body?.text = name

//        show the like status
        if (likeStatus == true){
            binding?.heartImage?.visibility  = View.VISIBLE
            binding?.btnLike?.text = "Unlike this Service"
        }else{
            binding?.heartImage?.visibility = View.GONE
            binding?.btnLike?.text = "Like this Service"
        }

        servicesListViewModel.serviceLists.observe(requireActivity()) { serviceLists ->
//            binding?.title?.text = serviceLists[0].serviceName
//            binding?.subhead?.text = serviceLists[0].stylist
//            binding?.body?.text = serviceLists[0].description


        }
        binding?.btnBook?.setOnClickListener {
            showDatePickerDialog()

//            val locationFragment = CalenderFragment()
////            val locationFragment = LocationFragment()
//            //  addToBackStack(null) allows the user to press the back button to return to FirstFragment.
//            parentFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, locationFragment)
//                .addToBackStack(null)
//                .commit()
        }

        binding?.btnLike?.setOnClickListener{
         //unlike this service
            if (likeStatus == true){
                servicesListViewModel.deleteFromCart(serviceID.toString(), firebaseAuth.currentUser?.uid.toString())
                binding?.heartImage?.visibility  = View.GONE
                binding?.btnLike?.text = "Like this Service"

            }else{
                servicesListViewModel.addToCart(serviceID.toString(), firebaseAuth.currentUser?.uid.toString())
                binding?.heartImage?.visibility  = View.VISIBLE
                binding?.btnLike?.text = "Unlike this Service"
            }

            CoroutineScope(Dispatchers.Main).launch {
                delay(1000) // Suspend the coroutine for the delay duration
                //reload the service list data
                viewModel.getServiceList(firebaseAuth.currentUser?.uid.toString())
            }

        }
        }



    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


    private fun decodeBase64ToBitmap(base64: String): Bitmap? {
        val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and show
        DatePickerDialog(requireContext(), this, year, month, day).show()
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hours = calendar.get(Calendar.HOUR_OF_DAY)
        val minutes = calendar.get(Calendar.MINUTE)

        TimePickerDialog(
            requireContext(),
            this,
            hours,
            minutes,
            true
        ).show() // Show time picker dialog
    }

    // Callback when the user selects a date
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth) // Store selected date
        Toast.makeText(requireContext(), "Selected Date: $selectedDate", Toast.LENGTH_SHORT).show()

        // Optionally, you can proceed to select time immediately after selecting the date
        showTimePickerDialog()
    }

    // Callback when the user selects a time
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        selectedTime = String.format("%02d:%02d", hourOfDay, minute)
        val dateTime = "$selectedDate $selectedTime"
        datePickerViewModel.saveSelectedDate(selectedDate ?: "", selectedTime ?: "")

        Toast.makeText(requireContext(), "Selected Date and Time: $dateTime", Toast.LENGTH_SHORT).show()

        // Navigate to SelectedDateFragment
        navigateToSelectedDateFragment()
    }

    private fun navigateToSelectedDateFragment() {
        val selectedDateFragment = SelectedDateFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, selectedDateFragment)
            .addToBackStack(null)
            .commit()
    }
}