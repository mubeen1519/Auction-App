package com.companies.auctionapp.data

data class AddAuctionModel(
    val name: String = "",
    val description: String = "",
    val startingPrice: Double = 0.0,
    val startDateTime: String = "",
    val endDateTime: String? = null,
    val category: String = "",
)
