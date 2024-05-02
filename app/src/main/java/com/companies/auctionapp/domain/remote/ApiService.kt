package com.companies.auctionapp.domain.remote

import com.companies.auctionapp.data.AuctionItem
import com.companies.auctionapp.data.Bid
import com.companies.auctionapp.data.LoginData
import com.companies.auctionapp.data.LoginResponse
import com.companies.auctionapp.data.RegisterData
import com.companies.auctionapp.data.RegistrationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("Authentication/register")
    suspend fun register(
        @Body requestBody: RegisterData
    ): Response<RegistrationResponse>

    @POST("Authentication/login")
    suspend fun login(@Body loginData: LoginData): Response<LoginResponse>

    @GET("auction/Items/search")
    suspend fun getAuctionItems(
        @Query("Categories") category: String? = null,
        @Query("MaxPrice") maxPrice: Int? = null
    ): List<AuctionItem>

    @GET("auction/Categories")
    suspend fun getAllCategories() : List<String>

    @GET("auction/Items/{itemId}/biddings")
    suspend fun getBidsForItem(@Header("Authorization") token : String,@Path("itemId") itemId: Int): List<Bid>
}