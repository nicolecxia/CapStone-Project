package com.example.cocygo.booking.calender.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cocygo.booking.calender.model.CalenderModel
import com.example.cocygo.booking.calender.repository.FirebaseRepository

class DatePickerViewModel : ViewModel() {

    private val repository = FirebaseRepository()

//    Livedata for the selected date
  val selectedDataLiveData = MutableLiveData<String>()
    val selectedTimeLiveData = MutableLiveData<String>()
//    val datesLiveData = MutableLiveData<List<Map<String, String>>>()
    val datesLiveData = MutableLiveData<List<CalenderModel>>()

    fun saveSelectedDate(selectedDate:String, selectedTime: String) {
        repository.saveSelectedDate(selectedDate, selectedTime) {success ->
            if (success) {
                selectedDataLiveData.value = selectedDate
                selectedTimeLiveData.value = selectedTime
            }
        }
    }

    fun updateSelectedDate(selectedDate: String) {
        selectedDataLiveData.value = selectedDate
    }
//
    fun fetchDates() {
        repository.readDates { dates ->
            datesLiveData.value = dates // Update LiveData with fetched dates
        }
    }
}