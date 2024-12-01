package com.example.cocygo.booking.calender.view

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.cocygo.R
import com.example.cocygo.booking.calender.viewModel.DatePickerViewModel
import com.example.cocygo.databinding.FragmentCalanderBinding
import java.util.Calendar
import java.util.TimeZone

class CalenderFragment : DialogFragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private lateinit var binding: FragmentCalanderBinding
    private lateinit var datePickerViewModel: DatePickerViewModel
    private var selectedCalendar: Calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalanderBinding.inflate(inflater, container, false)
        datePickerViewModel =
            ViewModelProvider(requireActivity()).get(DatePickerViewModel::class.java)

        binding.btnOpenDatePicker.setOnClickListener {
            showDatePickerDialog()
        }

        binding.btnOpenTimePicker.setOnClickListener {
            showTimePickerDialog()
        }

        return binding.root
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        println("Opening DatePickerDialog with initial date: $year-${month + 1}-$day")
        DatePickerDialog(requireContext(), this, year, month, day).show()
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hours = calendar.get(Calendar.HOUR_OF_DAY)
        val minutes = calendar.get(Calendar.MINUTE)

        println("Opening TimePickerDialog with initial time: $hours:$minutes")
        TimePickerDialog(
            requireContext(),
            this,
            hours,
            minutes,
            true
        ).show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        selectedCalendar.set(Calendar.YEAR, year)
        selectedCalendar.set(Calendar.MONTH, month)
        selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        println("Selected Date: $year-${month + 1}-$dayOfMonth")
        Toast.makeText(
            requireContext(),
            "Selected Date: ${year}-${month + 1}-$dayOfMonth",
            Toast.LENGTH_SHORT
        ).show()

        showTimePickerDialog()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        selectedCalendar.set(Calendar.MINUTE, minute)
        selectedCalendar.set(Calendar.SECOND, 0)
        selectedCalendar.set(Calendar.MILLISECOND, 0)
        addEventToCalendar()
        println("Selected Time: $hourOfDay:$minute")
        Toast.makeText(
            requireContext(),
            "Selected Time: $hourOfDay:$minute",
            Toast.LENGTH_SHORT
        ).show()

    }

    private fun addEventToCalendar() {
        println("Preparing to add event to calendar...")
        val calendarPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                println("Calendar WRITE permission granted.")
                addEventToCalendar()
            } else {
                println("Calendar WRITE permission denied.")
                Toast.makeText(requireContext(), "Calendar permission is required to add events.", Toast.LENGTH_SHORT).show()
            }
        }

        val calendarId = getCalendarId()
        println("Obtained calendar ID: $calendarId")
        if (calendarId == -1L) {
            println("No calendar account found.")
            Toast.makeText(requireContext(), "No calendar account found", Toast.LENGTH_SHORT).show()
            return
        }

        val eventValues = ContentValues().apply {
            put(CalendarContract.Events.CALENDAR_ID, calendarId)
            put(CalendarContract.Events.TITLE, "My Event")
            put(CalendarContract.Events.DESCRIPTION, "This is a sample event")
            put(CalendarContract.Events.DTSTART, selectedCalendar.timeInMillis)
            put(CalendarContract.Events.DTEND, selectedCalendar.timeInMillis + 60 * 60 * 1000) // 1-hour event
            put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
        }

        val uri = requireContext().contentResolver.insert(CalendarContract.Events.CONTENT_URI, eventValues)
        if (uri != null) {
            println("Event successfully added to calendar. Event URI: $uri")
            Toast.makeText(requireContext(), "Event added to calendar", Toast.LENGTH_SHORT).show()
        } else {
            println("Failed to add event to calendar.")
            Toast.makeText(requireContext(), "Failed to add event", Toast.LENGTH_SHORT).show()
        }
    }


    private fun getCalendarId(): Long {
        val projection = arrayOf(CalendarContract.Calendars._ID, CalendarContract.Calendars.CALENDAR_DISPLAY_NAME)
        val uri = CalendarContract.Calendars.CONTENT_URI
        val cursor = requireContext().contentResolver.query(uri, projection, null, null, null)

        cursor?.use {
            if (it.moveToFirst()) {
                val calendarId = it.getLong(it.getColumnIndexOrThrow(CalendarContract.Calendars._ID))
                println("Found calendar ID: $calendarId")
                return calendarId
            }
        }
        println("No calendar ID found.")
        return -1L
    }
}
