package com.companies.auctionapp.domain.remote

import com.companies.auctionapp.data.AddAuctionModel
import com.companies.auctionapp.data.AuctionItem
import com.companies.auctionapp.data.Bid
import com.companies.auctionapp.data.BidPlaced
import com.companies.auctionapp.data.LoginData
import com.companies.auctionapp.data.LoginResponse
import com.companies.auctionapp.data.PurchaseItemData
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
import java.time.temporal.TemporalAmount

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

    @POST("auction/Items/{itemId}/bids")
    suspend fun makeBid(@Header("Authorization") token : String,@Path("itemId") itemId: Int, @Body amount: Double) : Response<BidPlaced>

    @POST("auction/Items")
    suspend fun addItem(@Body addItem : AddAuctionModel,@Header("Authorization") token : String) : Response<AddAuctionModel>

    @GET("auction/Sellers/{username}/items")
    suspend fun getSoldItems(@Header("Authorization") token : String, @Path("username") username : String) : Response<List<String>>

    @GET("auction/Buyers/{username}/items")
    suspend fun getPurchasedItems(@Header("Authorization") token : String, @Path("username") username : String) : Response<List<PurchaseItemData>>

    @GET("auction/Buyers/{username}/items/{itemId}/payment")
    suspend fun purchaseItem(@Header("Authorization") token : String, @Path("username") username : String,@Path("itemId") itemId: Int) : Response<RegistrationResponse>
}