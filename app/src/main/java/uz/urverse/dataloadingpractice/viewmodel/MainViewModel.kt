import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uz.urverse.dataloadingpractice.model.ApiResponse
import uz.urverse.dataloadingpractice.network.RetrofitInstance

class MainViewModel : ViewModel() {

    private val _data = MutableLiveData<List<ApiResponse>>()
    val data: LiveData<List<ApiResponse>> get() = _data

    private var currentPage = 1
    private val pageSize = 10

    fun fetchData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.getData(page = currentPage, limit = pageSize)
                Log.d("MainViewModel", "Fetched ${response.size} items.")
                for (item in response) {
                    Log.d("MainViewModel", "Item: ${item.title}, Thumbnail URL: ${item.thumbnailUrl}")
                }
                _data.postValue(response)
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error fetching data", e)
            }
        }
    }

    fun fetchMoreData() {
        currentPage++
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.getData(page = currentPage, limit = pageSize)
                val currentList = _data.value?.toMutableList() ?: mutableListOf()
                currentList.addAll(response)
                _data.postValue(currentList)
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error fetching more data", e)
            }
        }
    }
}
