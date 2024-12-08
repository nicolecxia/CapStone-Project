package com.example.cocygo.booking.calender.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cocygo.R
import com.example.cocygo.booking.calender.viewModel.DatePickerViewModel
import com.example.cocygo.databinding.FragmentSelectedDateBinding

class SelectedDateFragment : Fragment() {

    private lateinit var binding: FragmentSelectedDateBinding
    private lateinit var datePickerViewModel: DatePickerViewModel
    private lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("StringFormatInvalid")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectedDateBinding.inflate(inflater, container, false)
        datePickerViewModel = ViewModelProvider(requireActivity()).get(DatePickerViewModel::class.java)

        adapter = Adapter(emptyList())
        adapter.setOnDeleteClickListener { id ->
            datePickerViewModel.deleteBooking(id)
        }

        binding.rv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@SelectedDateFragment.adapter
            addItemDecoration(SpaceItemDecoration(20))  // Customize space as needed
        }

        binding.retrieveDate.setTextColor(Color.parseColor("#51004F"))

        datePickerViewModel.selectedDataLiveData.observe(viewLifecycleOwner) { selectedDate ->
            binding.retrieveDate.text = getString(R.string.upcomming, selectedDate)
        }

        datePickerViewModel.selectedTimeLiveData.observe(viewLifecycleOwner) { selectedTime ->
            binding.retrieveDate.append(getString(R.string.pipe_with_space, selectedTime))
        }

        datePickerViewModel.datesLiveData.observe(viewLifecycleOwner) { dates ->
            adapter.updateData(dates)
        }

        datePickerViewModel.deleteStatusLiveData.observe(viewLifecycleOwner) { isDeleted ->
            if (isDeleted) {
                Toast.makeText(requireContext(), getString(R.string.booking_deleted), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), getString(R.string.delete_failed), Toast.LENGTH_SHORT).show()
            }
        }

        datePickerViewModel.fetchDates()
        return binding.root
    }
}
