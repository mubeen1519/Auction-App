package com.companies.auctionapp.domain.remote

import com.companies.auctionapp.data.LoginData
import com.companies.auctionapp.data.LoginResponse
import com.companies.auctionapp.data.RegisterData
import com.companies.auctionapp.data.RegistrationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("Authentication/register")
    suspend fun register(
        @Body requestBody: RegisterData
    ): Response<RegistrationResponse>

    @POST("Authentication/login")
    suspend fun login(@Body loginData: LoginData): Response<LoginResponse>
}