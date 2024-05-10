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

class DetailViewModel : ViewModel() {

    private val _bids = MutableLiveData<Result>()
    val bids: LiveData<Result> = _bids

    private val _bidPlaced = MutableLiveData(false)
    val bidPlaced : LiveData<Boolean> = _bidPlaced

    private val _itemPurchased = MutableLiveData(false)
    val itemPurchased : LiveData<Boolean> = _itemPurchased




    fun fetchBids(itemId : Int,context: Context) {
        val userDetails = SharedPreferencesHelper.getUserDetails()
        if(userDetails?.username?.isEmpty() == true){
            Toast.makeText(context,"Please Login First to see items",Toast.LENGTH_SHORT).show()
            return
        }
        viewModelScope.launch {
            try {
                val response = userDetails?.let { RetrofitInstance.apiService.login(it) }
                val newToken = response?.body()?.token
                Log.d("TAG", "new token: $newToken")
                _bids.value = Result.Loading
                if (newToken!= null) {
                    val bidList = RetrofitInstance.apiService.getBidsForItem("Bearer $newToken", itemId)
                        _bids.value = Result.Success(bidList)
                        Log.d("TAG",bidList.toString())
                    }
            } catch (e : Exception){
               _bids.value = Result.Error(e)
            }
        }
    }

    fun purchaseItem(username : String,itemId: Int,context: Context){
        val userDetails = SharedPreferencesHelper.getUserDetails()
        if(userDetails?.username?.isEmpty() == true){
            Toast.makeText(context,"Please Login First to see items",Toast.LENGTH_SHORT).show()
            return
        }
        viewModelScope.launch {
            try {
            val response = userDetails?.let { RetrofitInstance.apiService.login(it) }
            val newToken = response?.body()?.token
            Log.d("TAG", "addAuctionItem: $newToken")
                if (newToken!= null) {
                    val purchaseResponse = RetrofitInstance.apiService.purchaseItem("Bearer $newToken", username,itemId)
                    if(purchaseResponse.isSuccessful){
                        Toast.makeText(context,purchaseResponse.body()?.message,Toast.LENGTH_SHORT).show()
                        Log.d("TAG",purchaseResponse.message())
                        _itemPurchased.value = true
                    }
                } else {
                    Toast.makeText(context,"Something Went wrong",Toast.LENGTH_SHORT).show()
                }

            } catch (e : Exception){
                Log.d("TAG", "purchaseItem: $e")
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

    fun placeBid(itemId: Int,amount : Double,context: Context){
        val userDetails = SharedPreferencesHelper.getUserDetails()
        if(userDetails?.username?.isEmpty() == true){
            Toast.makeText(context,"Please Login First to see items",Toast.LENGTH_SHORT).show()
            return
        }
        viewModelScope.launch {
            val response = userDetails?.let { RetrofitInstance.apiService.login(it) }
            val newToken = response?.body()?.token
            Log.d("TAG", "addAuctionItem: $newToken")
            if (newToken != null) {
                val placeBid = RetrofitInstance.apiService.makeBid("Bearer $newToken",itemId,amount)
                if(placeBid.isSuccessful){
                    _bidPlaced.value = true
                }
                Log.d("TAG", "placeBid: $placeBid")
            }
        }
    }
}