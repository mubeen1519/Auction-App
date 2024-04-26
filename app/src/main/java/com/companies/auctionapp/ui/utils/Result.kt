package com.companies.auctionapp.ui.utils

sealed class Result {
    object Loading : Result()
    class Success(var data: Any) : Result()
    class Error(var failure: Exception) : Result()
}