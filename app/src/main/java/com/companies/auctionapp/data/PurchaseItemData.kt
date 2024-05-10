package com.companies.auctionapp.data

data class PurchaseItemData(
    val id: Int,
    val name: String,
    val description: String,
    val startingPrice: Double,
    val startDateTime: String,
    val endDateTime: String,
    val payed: Boolean,
    val category: String,
    val seller: String,
    val price: Double
)
