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
import com.companies.auctionapp.R
import com.companies.auctionapp.data.PurchaseItemData
import com.companies.auctionapp.presentation.viewModel.PurchasedViewModel
import com.companies.auctionapp.ui.utils.Result
import com.companies.auctionapp.ui.utils.SharedPreferencesHelper

@Composable
fun PurchasedItemScreen(viewModel: PurchasedViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val purchasedItem = viewModel.purchasedItems.observeAsState()
    val isLoggedIn = SharedPreferencesHelper.getUserDetails()
    val context = LocalContext.current


    LaunchedEffect(key1 = Unit) {
        if (isLoggedIn?.username?.isNotEmpty() == true) {
            viewModel.getPurchasedItem(context)
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
            Text(text = stringResource(id = R.string.purchase_item))
        }
        Spacer(modifier = Modifier.height(10.dp))


        if (isLoggedIn?.username?.isNotEmpty() == true) {
            when (val result = purchasedItem.value) {
                is Result.Loading -> {
                    CircularProgressIndicator()
                }

                is Result.Success -> {
                    val purchasedItems =  result.data as List<PurchaseItemData>
                    if (purchasedItems.isEmpty()) {
                        Text(text = stringResource(id = R.string.no_purchased_Item))
                    } else {
                        LazyColumn(modifier = Modifier.padding(bottom = 100.dp)) {
                            items(purchasedItems) { item ->
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
                                        Text(text = "Seller: ${item.seller}")
                                        Text(text = "Payed ${item.payed}")
                                    }
                                    Row(modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Button(onClick = {
                                            viewModel.purchaseItem(item.id,context)
                                        }) {
                                            Text(text = "Purchase")
                                        }
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
                Text(text = stringResource(id = R.string.login_to_see_purchased))
            }
        }
    }
}