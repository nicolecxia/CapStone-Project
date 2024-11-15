package com.example.cocygo.booking.calender.view

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cocygo.booking.calender.repository.FirebaseRepository
import com.example.cocygo.booking.calender.viewModel.DatePickerViewModel
import com.example.cocygo.databinding.FragmentSelectedDateBinding

class SelectedDateFragment : Fragment() {

    private lateinit var binding: FragmentSelectedDateBinding
    private lateinit var datePickerViewModel: DatePickerViewModel
    private val firebaseRepository = FirebaseRepository()
    private lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSelectedDateBinding.inflate(inflater, container, false)
        datePickerViewModel =
            ViewModelProvider(requireActivity()).get(DatePickerViewModel::class.java)

//        Initialize the adpter with an empty list
        adapter = Adapter(emptyList())
//        binding.rv.adapter = adapter
//        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        val spaceInPixels = 20
        binding.rv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@SelectedDateFragment.adapter
            addItemDecoration(
                SpaceItemDecoration(spaceInPixels)
            )
        }
        binding.retrieveDate.setTextColor(Color.parseColor("#51004F"))

//         Observe the selected date from the ViewModel
        datePickerViewModel.selectedDataLiveData.observe(viewLifecycleOwner) { selectedDate ->
            binding.retrieveDate.text = "Upcomming\n $selectedDate "
        }
        datePickerViewModel.selectedTimeLiveData.observe(viewLifecycleOwner) { selectedTime ->
            binding.retrieveDate.append("|  $selectedTime") // Append time instead of replacing
        }
        datePickerViewModel.datesLiveData.observe(viewLifecycleOwner) { dates ->
            adapter.updateData(dates)
        }

        datePickerViewModel.fetchDates()
        return binding.root
    }

}