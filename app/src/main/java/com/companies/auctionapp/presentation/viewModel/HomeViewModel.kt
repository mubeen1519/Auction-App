package com.companies.auctionapp.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.companies.auctionapp.data.AuctionItem
import com.companies.auctionapp.domain.RetrofitInstance
import com.companies.auctionapp.ui.utils.Result
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _auctionItemsResult = MutableLiveData<Result>()
    val auctionItemsResult: LiveData<Result> = _auctionItemsResult

    private val _categoriesItemResult = MutableLiveData<Result>()
    val categoriesItemsResult: LiveData<Result> = _categoriesItemResult



    init {
        fetchAuctionItems()
        fetchAllCategories()
    }

    fun fetchAuctionItems(category: String? = null, maxPrice: Int? = null) {
        viewModelScope.launch {
            try {
                _auctionItemsResult.value = Result.Loading
                val response = RetrofitInstance.apiService.getAuctionItems(category, maxPrice)

                if (response.isNotEmpty()) {
                    _auctionItemsResult.value = Result.Success(response)
                    Log.d("TAG", "fetchAuctionItems: $response")
                } else {
                    _auctionItemsResult.value = Result.Error(Exception("No items found"))
                }
            } catch (e: Exception) {
                _auctionItemsResult.value = Result.Error(e)
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
    fun getAuctionItemDetails(itemId: Int): AuctionItem? {
        val auctionItems = (_auctionItemsResult.value as? Result.Success)?.data as? List<AuctionItem>
        return auctionItems?.find { it.id == itemId }
    }
}