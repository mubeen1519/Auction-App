package com.companies.auctionapp.data

data class SoldResponse(
    val name: String,
    val description: String,
    val startingPrice: Int,
    val startDateTime: String,
    val endDateTime: String,
    val category: String,
    val buyer: String,
    val price: Int
)
