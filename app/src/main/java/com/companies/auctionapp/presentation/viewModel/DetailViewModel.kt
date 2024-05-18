package com.companies.auctionapp.presentation.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.companies.auctionapp.data.BidPlaced
import com.companies.auctionapp.domain.RetrofitInstance
import com.companies.auctionapp.ui.utils.Result
import com.companies.auctionapp.ui.utils.SharedPreferencesHelper
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {

    private val _bids = MutableLiveData<Result>()
    val bids: LiveData<Result> = _bids

    private val _bidPlaced = MutableLiveData(false)
    val bidPlaced: LiveData<Boolean> = _bidPlaced

    private val _itemPurchased = MutableLiveData(false)
    val itemPurchased: LiveData<Boolean> = _itemPurchased


    private val _bidAmount = MutableLiveData<Double>()
    val bidAmount: LiveData<Double> = _bidAmount



    fun fetchBids(itemId: Int, context: Context) {
        val userDetails = SharedPreferencesHelper.getUserDetails()
        if (userDetails?.username?.isEmpty() == true) {
            Toast.makeText(context, "Please Login First to see items", Toast.LENGTH_SHORT).show()
            return
        }
        viewModelScope.launch {
            try {
                val response = userDetails?.let { RetrofitInstance.apiService.login(it) }
                val newToken = response?.body()?.token
                Log.d("TAG", "new token: $newToken")
                _bids.value = Result.Loading
                if (newToken != null) {
                    val bidList =
                        RetrofitInstance.apiService.getBidsForItem("Bearer $newToken", itemId)
                    _bids.value = Result.Success(bidList)
                    Log.d("TAG", bidList.toString())
                }
            } catch (e: Exception) {
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
                    val token = loginResponse.body()?.token
                    token?.let { SharedPreferencesHelper.saveJwtToken(it) }
                    // Handle the login response if needed
                } catch (e: Exception) {
                    // Handle login failure
                }
            }
        }
    }

    fun placeBid(itemId: Int, context: Context, amount: Double) {
        val userDetails = SharedPreferencesHelper.getUserDetails()
        if (userDetails?.username?.isEmpty() == true) {
            Toast.makeText(context, "Please Login First to see items", Toast.LENGTH_SHORT).show()
            return
        }
        viewModelScope.launch {
            try {
                val response = userDetails?.let { RetrofitInstance.apiService.login(it) }
                val newToken = response?.body()?.token
                Log.d("TAG", "addAuctionItem: $newToken")

                if (newToken != null) {
                    val placeBid = RetrofitInstance.apiService.makeBid(
                        "Bearer $newToken",
                        itemId,
                        BidPlaced(amount)
                    )

                    if (placeBid.isSuccessful) {
                        _bidPlaced.value = true
                        Toast.makeText(context,"Bid placed successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        val errorBody = placeBid.body()?.message
                        Toast.makeText(context, errorBody, Toast.LENGTH_SHORT).show()
                        Log.e("TAG", "placeBid error: ${errorBody ?: "Unknown error"}")
                    }
                    Log.d("TAG", "placeBid: ${placeBid.body()},${bidAmount.value}")
                }
            } catch (e: Exception) {
                Log.e("TAG", "placeBid network error: ${e.message}")
                // Provide user-friendly feedback (e.g., Toast or snackbar)
            }
        }
    }
}