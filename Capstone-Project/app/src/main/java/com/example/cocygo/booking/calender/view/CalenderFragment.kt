package com.example.cocygo.booking.calender.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.cocygo.R
import com.example.cocygo.booking.calender.viewModel.DatePickerViewModel
import com.example.cocygo.databinding.FragmentCalanderBinding
import java.util.Calendar

class CalenderFragment : DialogFragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private lateinit var binding: FragmentCalanderBinding
    private lateinit var datePickerViewModel: DatePickerViewModel
    private var selectedDate: String? = null
    private var selectedTime: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCalanderBinding.inflate(inflater, container, false)
        datePickerViewModel =
            ViewModelProvider(requireActivity()).get(DatePickerViewModel::class.java)

        // Set up the date picker button listener
        binding.btnOpenDatePicker.setOnClickListener {
            showDatePickerDialog()
        }

        // Set up the time picker button listener
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
            .replace(R.id.calender, selectedDateFragment)
            .addToBackStack(null)
            .commit()
    }
}