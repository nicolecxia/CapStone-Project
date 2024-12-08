package com.example.cocygo.booking.calender.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cocygo.booking.calender.model.CalenderModel
import com.example.cocygo.booking.calender.repository.FirebaseRepository
import kotlinx.coroutines.launch

class DatePickerViewModel : ViewModel() {

    private val repository = FirebaseRepository()

//    Livedata for the selected date
  val selectedDataLiveData = MutableLiveData<String>()
    val selectedTimeLiveData = MutableLiveData<String>()
//    val datesLiveData = MutableLiveData<List<Map<String, String>>>()
    val datesLiveData = MutableLiveData<List<CalenderModel>>()
    val deleteStatusLiveData = MutableLiveData<Boolean>()

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
    fun deleteBooking(id: String) {
        repository.deleteBookingById(id) { success ->
            if (success) {
                Log.d("Deletion", "Did it deleted")
                deleteStatusLiveData.postValue(true)
                fetchDates()
            } else {
                deleteStatusLiveData.postValue(false)
            }
        }
    }

}