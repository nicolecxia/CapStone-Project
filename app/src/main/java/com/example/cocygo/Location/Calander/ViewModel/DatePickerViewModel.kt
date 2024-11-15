import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cocygo.Location.Calander.model.CalenderModel

class DatePickerViewModel : ViewModel() {
    private val repository = FirebaseRepository()

    val selectedDataLiveData = MutableLiveData<String>()
    val selectedTimeLiveData = MutableLiveData<String>()
    val datesLiveData = MutableLiveData<List<CalenderModel>>()

    fun saveSelectedDate(selectedDate: String, selectedTime: String, cartId: String) {
        repository.saveSelectedDate(selectedDate, selectedTime, cartId) { success ->
            if (success) {
                selectedDataLiveData.value = selectedDate
                selectedTimeLiveData.value = selectedTime
            }
        }
    }

    fun updateSelectedDate(selectedDate: String) {
        selectedDataLiveData.value = selectedDate
    }

    fun fetchDates() {
        repository.readDates { dates ->
            datesLiveData.value = dates
        }
    }
}