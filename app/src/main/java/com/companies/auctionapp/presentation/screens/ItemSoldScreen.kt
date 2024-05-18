package com.companies.auctionapp.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.companies.auctionapp.R
import com.companies.auctionapp.data.PurchaseItemData
import com.companies.auctionapp.data.SoldResponse
import com.companies.auctionapp.presentation.viewModel.SoldItemViewModel
import com.companies.auctionapp.ui.utils.Result
import com.companies.auctionapp.ui.utils.SharedPreferencesHelper

@Composable
fun ItemSoldScreen(soldItemViewModel: SoldItemViewModel = viewModel()) {

    val soldItem = soldItemViewModel.soldItems.observeAsState()
    val isLoggedIn = SharedPreferencesHelper.getUserDetails()
    val context = LocalContext.current


    LaunchedEffect(key1 = Unit) {
        if (isLoggedIn?.username?.isNotEmpty() == true) {
            soldItemViewModel.getSoldItems(isLoggedIn.username, context)
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = stringResource(id = R.string.sold_items))
        }
        Spacer(modifier = Modifier.height(10.dp))


        if (isLoggedIn?.username?.isNotEmpty() == true) {
            when (val result = soldItem.value) {
                is Result.Loading -> {
                    CircularProgressIndicator()
                }

                is Result.Success -> {
                    val soldItems =  result.data as List<SoldResponse>
                    if (soldItems.isEmpty()) {
                        Text(text = stringResource(id = R.string.you_dont_have_sold_items))
                    } else {
                        LazyColumn {
                            items(soldItems) { item ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                    ) {
                                        Text(text = "Name: ${item.name}")
                                        Text(text = "Price: ${item.price}")
                                        Text(text = "Description: ${item.description}")
                                        Text(text = "Category: ${item.category}")
                                        Text(text = "Start Date: ${item.startDateTime}")
                                        Text(text = "End Date: ${item.endDateTime}")
                                        Text(text = "Buyer: ${item.buyer}")
                                    }
                                }
                            }
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
            Column {
                Text(text = stringResource(id = R.string.login_to_see_sold))
            }
        }
    }
}