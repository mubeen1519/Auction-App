package com.companies.auctionapp.data

data class AuctionItem(
    val id: Int,
    val name: String,
    val description: String,
    val startingPrice: Int,
    val startDateTime: String,
    val endDateTime: String,
    val category: String,
    val highestBid: Int,
    val seller: String
)
