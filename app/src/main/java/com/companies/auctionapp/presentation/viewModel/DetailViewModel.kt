package com.companies.auctionapp.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.companies.auctionapp.domain.RetrofitInstance
import com.companies.auctionapp.ui.utils.Result
import com.companies.auctionapp.ui.utils.SharedPreferencesHelper
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {

    private val _bids = MutableLiveData<Result>()
    val bids: LiveData<Result> = _bids


    fun fetchBids(itemId : Int) {
        viewModelScope.launch {
            try {
                val jwt = SharedPreferencesHelper.getJwtToken()
                _bids.value = Result.Loading
                if (jwt!= null) {
                    val response = RetrofitInstance.apiService.getBidsForItem("Bearer $jwt", itemId)
                    if (response.isNotEmpty()) {
                        _bids.value = Result.Success(response)
                        Log.d("TAG",response.toString())
                    } else {
                        loginUserAgain()
                    }
                }

            } catch (e : Exception){
                loginUserAgain()
                _bids.value = Result.Error(e)
            }
        }
    }

     fun loginUserAgain() {
        val userDetails = SharedPreferencesHelper.getUserDetails()
        userDetails?.let {
            viewModelScope.launch {
                try {
                    val loginResponse = RetrofitInstance.apiService.login(userDetails)
                    // Handle the login response if needed
                } catch (e: Exception) {
                    // Handle login failure
                }
            }
        }
    }
}