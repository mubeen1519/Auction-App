package com.companies.auctionapp.presentation.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.companies.auctionapp.domain.RetrofitInstance
import com.companies.auctionapp.ui.utils.Result
import com.companies.auctionapp.ui.utils.SharedPreferencesHelper
import kotlinx.coroutines.launch

class SoldItemViewModel : ViewModel() {

    private val _soldItems = MutableLiveData<Result>()
    val soldItems: LiveData<Result> = _soldItems

    fun getSoldItems(username: String,context: Context) {
        val userDetails = SharedPreferencesHelper.getUserDetails()
        if(userDetails?.username?.isEmpty() == true){
           Toast.makeText(context,"Please Login First to see items",Toast.LENGTH_SHORT).show()
            return
        }
        viewModelScope.launch {
            try {
                _soldItems.value = Result.Loading

                val response = userDetails?.let { RetrofitInstance.apiService.login(it) }
                val newToken = response?.body()?.token
                Log.d("TAG", "addAuctionItem: $newToken")
                if (newToken != null) {
                    SharedPreferencesHelper.saveJwtToken(newToken)
                    val soldApi = RetrofitInstance.apiService.getSoldItems("Bearer $newToken",username)
                    if(soldApi.isSuccessful){
                            _soldItems.value = Result.Success(soldApi)
                        Log.d("TAG", "getSoldItems: ${soldApi.body()}")

                    } else {
                        Log.d("TAG", "getSoldItems: $soldApi")
                    }
                }
            } catch (e: Exception) {
                _soldItems.value = Result.Error(e)
            }
        }
    }
}