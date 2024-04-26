package com.companies.auctionapp.data

data class RegisterData(
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val accountType: String = "Free"
)