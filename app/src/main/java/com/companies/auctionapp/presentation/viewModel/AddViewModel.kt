package com.companies.auctionapp.presentation.viewModel

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.companies.auctionapp.data.AddAuctionModel
import com.companies.auctionapp.domain.RetrofitInstance
import com.companies.auctionapp.presentation.navigation.HOME_SCREEN
import com.companies.auctionapp.ui.utils.Result
import com.companies.auctionapp.ui.utils.SharedPreferencesHelper
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class AddViewModel : ViewModel() {

    private val _categoriesItemResult = MutableLiveData<Result>()
    val categoriesItemsResult: LiveData<Result> = _categoriesItemResult

    var data = mutableStateOf(AddAuctionModel())


    init {
        fetchAllCategories()
    }

    fun onNameChanged(newValue: String) {
        data.value = data.value.copy(name = newValue)
    }

    fun onDescriptionChanged(newValue: String) {
        data.value = data.value.copy(description = newValue)
    }

    fun onStartPriceChanged(newValue: String) {
        data.value = data.value.copy(startingPrice = newValue.toDouble())
    }

    fun category(newValue: String) {
        data.value = data.value.copy(category = newValue)
    }

    private var _startDateAndTime = mutableStateOf("")
    val startDateAndTime: State<String> = _startDateAndTime

    private var _endDateAndTime = mutableStateOf("")
    val endDateAndTime: State<String> = _endDateAndTime

    @RequiresApi(Build.VERSION_CODES.O)
    fun onStartDateTimeSelected(selectedDateMillis: String) {
        _startDateAndTime.value = selectedDateMillis
        data.value = data.value.copy(startDateTime = _startDateAndTime.value)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onEndDateTimeSelected(selectedDateMillis: String) {
        _endDateAndTime.value = "$selectedDateMillis"
        data.value = data.value.copy(endDateTime = _endDateAndTime.value)

    }

    fun addAuctionItem(navigate: (String) -> Unit, context: Context) {
        val name = data.value.name
        val description = data.value.description
        val category = data.value.description
        val startingPrice = data.value.startingPrice
        if (name.isEmpty() || description.isEmpty() || category.isEmpty() || startingPrice.toString().isEmpty()) {
            return
        }
        viewModelScope.launch {
            try {
                val userDetails = SharedPreferencesHelper.getUserDetails()
                val response = userDetails?.let { RetrofitInstance.apiService.login(it) }
                val newToken = response?.body()?.token
                Log.d("TAG", "addAuctionItem: $newToken")
                if (newToken != null) {
                    SharedPreferencesHelper.saveJwtToken(newToken)
                    val addItemResponse = RetrofitInstance.apiService.addItem(data.value, "Bearer $newToken")
                    if (addItemResponse.isSuccessful) {
                        navigate(HOME_SCREEN)
                        Log.d("TAG", "addAuctionItem: $addItemResponse")
                    } else {
                        // Handle addItem API error response
                        Toast.makeText(context,"Something Went Wrong",Toast.LENGTH_SHORT).show()
                        Log.d("TAG", "addAuctionItem: $addItemResponse")
                    }
                } else {
                        Toast.makeText(context,"First Login to Add Item",Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.d("TAG", "addAuctionItem: ${e.message}")
            }
        }
    }
    private fun fetchAllCategories() {
        viewModelScope.launch {
            try {
                _categoriesItemResult.value = Result.Loading
                val response = RetrofitInstance.apiService.getAllCategories()

                if (response.isNotEmpty()) {
                    _categoriesItemResult.value = Result.Success(response)
                }
            } catch (e: Exception) {
                _categoriesItemResult.value = Result.Error(e)
            }
        }
    }
}

