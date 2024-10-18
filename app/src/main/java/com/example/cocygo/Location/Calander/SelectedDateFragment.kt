package com.example.cocygo.Location.Calander

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.cocygo.Location.Calander.ViewModel.DatePickerViewModel
import com.example.cocygo.R
import com.example.cocygo.databinding.FragmentSelectedDateBinding
import com.google.firebase.firestore.FirebaseFirestore

class SelectedDateFragment : Fragment() {

private lateinit var binding: FragmentSelectedDateBinding
    private lateinit var datePickerViewModel: DatePickerViewModel
    private val firebaseRepository = FirebaseRepository()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSelectedDateBinding.inflate(inflater, container, false)
        datePickerViewModel = ViewModelProvider(requireActivity()).get(DatePickerViewModel::class.java)


//         Observe the selected date from the ViewModel
        datePickerViewModel.selectedDataLiveData.observe(viewLifecycleOwner) { selectedDate ->
            binding.retrievedate.text =  "Date: $selectedDate"
        }
        datePickerViewModel.selectedTimeLiveData.observe(viewLifecycleOwner) { selectedTime ->
            binding.retrievedate.append("\nTime: $selectedTime") // Append time instead of replacing
        }


        datePickerViewModel.fetchDates()
        return binding.root
    }

}