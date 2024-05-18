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

class PurchasedViewModel : ViewModel() {

    private val _purchasedItems = MutableLiveData<Result>()
    val purchasedItems: LiveData<Result> = _purchasedItems

    fun getPurchasedItem(context: Context) {
        val userDetails = SharedPreferencesHelper.getUserDetails()
        if (userDetails?.username?.isEmpty() == true) {
            Toast.makeText(context, "Please Login First to see items", Toast.LENGTH_SHORT).show()
            return
        }
        viewModelScope.launch {
            try {
                _purchasedItems.value = Result.Loading
                val response = userDetails?.let { RetrofitInstance.apiService.login(it) }
                val newToken = response?.body()?.token
                Log.d("TAG", "addAuctionItem: $newToken")
                if (newToken != null) {
                    SharedPreferencesHelper.saveJwtToken(newToken)
                    val purchasedApi =
                        RetrofitInstance.apiService.getPurchasedItems("Bearer $newToken", userDetails.username)
                    if (purchasedApi.isSuccessful) {

                        val purchasedItems = purchasedApi.body() // Extract the actual data from the response
                        _purchasedItems.value = purchasedItems?.let { Result.Success(it) }
                        Log.d("TAG", "getItems: ${purchasedApi.body()}")

                    } else {
                        Log.d("TAG", "getSoldItems: $purchasedApi")
                    }
                }
            } catch (e: Exception) {
                _purchasedItems.value = Result.Error(e)
            }
        }
    }

    fun purchaseItem(itemId: Int, context: Context) {
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
                    val purchaseResponse = RetrofitInstance.apiService.purchaseItem(
                        "Bearer $newToken",
                        userDetails.username,
                        itemId
                    )
                    if (purchaseResponse.isSuccessful) {
                        Toast.makeText(
                            context,
                            "Purchase Successful",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("TAG", purchaseResponse.message())
                    } else {
                        Toast.makeText(context, purchaseResponse.message(), Toast.LENGTH_SHORT).show()
                        Log.e("TAG", "placeBid error: ${purchaseResponse.body() ?: "Unknown error"}")
                    }
                }

            } catch (e: Exception) {
                Log.d("TAG", "purchaseItem: $e")
            }
        }
    }
}