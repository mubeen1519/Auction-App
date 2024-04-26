package com.companies.auctionapp.data

data class LoginResponse(
    val token: String,
    val expiration: String,
    val type: String,

    )