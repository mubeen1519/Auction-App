package com.companies.auctionapp.data

enum class AccountType {
    Free,
    Gold,
    Platinum
}

data class RegisterData(
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val accountType: AccountType = AccountType.Free
)