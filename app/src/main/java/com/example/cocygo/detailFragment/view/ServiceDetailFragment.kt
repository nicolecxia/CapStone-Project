package com.example.cocygo.detailFragment.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.CalendarContract
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
import com.example.cocygo.homeFragment.adapter.ServicesListViewModel
import java.util.Calendar
import java.util.TimeZone


class ServiceDetailFragment(image: String?, name: String?, tittle: String?) : DialogFragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private var binding: FragmentServiceDetailBinding? = null
    var name = name
    var image = image
    var tittle = tittle
    private lateinit var datePickerViewModel: DatePickerViewModel
    private var selectedDate: String? = null
    private var selectedTime: String? = null
    private var selectedCartId: String? = null

    //ViewModel
    private lateinit var servicesListViewModel: ServicesListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getCalendars(requireContext())
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
        val bitmap = decodeBase64ToBitmap(image!!.trim())

        binding?.title?.text = tittle
        binding?.headerImage?.setImageBitmap(bitmap)
        binding?.body?.text = name
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
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        val timeInMillis = calendar.timeInMillis

        val dateTime = "$selectedDate $selectedTime"
        Toast.makeText(requireContext(), "Selected Date and Time: $dateTime", Toast.LENGTH_SHORT).show()


        addEventToCalendar(
            requireContext(),
            title ="You have booked a event" ,
            description = binding?.title?.text.toString(),
            timeInMillis = timeInMillis
        )
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
    private fun addEventToCalendar(context: Context, title: String, description: String, timeInMillis: Long) {
        val calendarId = 3L

        val values = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, timeInMillis)
            put(CalendarContract.Events.DTEND, timeInMillis + 60 * 60 * 1000) // 默认事件持续时间为1小时
            put(CalendarContract.Events.TITLE, title)
            put(CalendarContract.Events.DESCRIPTION, description)
            put(CalendarContract.Events.CALENDAR_ID, calendarId)
            put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
        }

        try {
            val uri = context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
            if (uri != null) {
                Toast.makeText(context, "Event added successfully!", Toast.LENGTH_SHORT).show()
                println("Event URI: $uri")
            } else {
                Toast.makeText(context, "Failed to add event to calendar.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: SecurityException) {
            Toast.makeText(context, "Permission denied for adding to calendar.", Toast.LENGTH_LONG).show()
            println("Error: ${e.message}")
        }
    }

    private fun getCalendars(context: Context) {
        val uri = CalendarContract.Calendars.CONTENT_URI
        val projection = arrayOf(
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.NAME,
            CalendarContract.Calendars.ACCOUNT_NAME
        )
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getLong(0)
                val name = it.getString(1)
                val accountName = it.getString(2)
                println("Calendar ID: $id, Name: $name, Account: $accountName")
            }
        }
    }

}