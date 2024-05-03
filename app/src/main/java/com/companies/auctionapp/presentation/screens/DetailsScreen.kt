package com.companies.auctionapp.presentation.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.companies.auctionapp.data.AuctionItem
import com.companies.auctionapp.data.Bid
import com.companies.auctionapp.presentation.viewModel.DetailViewModel
import com.companies.auctionapp.presentation.viewModel.LoginViewModel
import com.companies.auctionapp.ui.utils.Result
import com.companies.auctionapp.ui.utils.SharedPreferencesHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.Calendar

@Composable
fun DetailsScreen(
    auctionItem: AuctionItem,
    viewModel: DetailViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    val bids = viewModel.bids.observeAsState()
    val isLoggedIn = SharedPreferencesHelper.getUserDetails()

    LaunchedEffect(key1 = Unit) {
        viewModel.fetchBids(auctionItem.id)
        Log.d("TAG", "DetailsScreen: ${SharedPreferencesHelper.getJwtToken()}, and ${SharedPreferencesHelper.getUserDetails()}")

    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = auctionItem.name,
                style = TextStyle(fontWeight = FontWeight.Bold),
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
        Text(text = auctionItem.description)
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Starting Price: $${auctionItem.startingPrice}")
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Start Date: ${auctionItem.startDateTime}")
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "End Date: ${auctionItem.endDateTime}")
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Category: ${auctionItem.category}")
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Highest Bid: $${auctionItem.highestBid}")
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Seller: ${auctionItem.seller}")

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            Text(
                text = "Bids",
                textAlign = TextAlign.Center,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
            if (isLoggedIn?.username?.isNotEmpty() == true) {
                when (val result = bids.value) {
                    is Result.Loading -> {
                        CircularProgressIndicator()
                    }
                    is Result.Success -> {
                        val bids = result.data as List<Bid>
                        LazyColumn {
                            items(bids) { bidItem ->
                                Text(text = bidItem.amount.toString())
                            }
                        }
                    }
                    is Result.Error -> {
                        Text(text = "Error : ${result.failure.message}")
                    }
                    else -> {

                    }
                }
            } else {
                Text(text = "Login First to see bids")
            }
        }
    }
}