package com.companies.auctionapp.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.companies.auctionapp.R
import com.companies.auctionapp.data.AuctionItem
import com.companies.auctionapp.data.Bid
import com.companies.auctionapp.presentation.viewModel.DetailViewModel
import com.companies.auctionapp.ui.utils.Result
import com.companies.auctionapp.ui.utils.SharedPreferencesHelper

@Composable
fun DetailsScreen(
    auctionItem: AuctionItem,
    viewModel: DetailViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    val bids = viewModel.bids.observeAsState()
    val isBidPlaced = viewModel.bidPlaced.observeAsState()
    val isItemPurchased = viewModel.itemPurchased.observeAsState()
    val isLoggedIn = SharedPreferencesHelper.getUserDetails()
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.fetchBids(auctionItem.id, context)
    }

    var showDialog by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
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
            Text(text = "Starting Price: ${auctionItem.startingPrice}")
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Start Date: ${auctionItem.startDateTime}")
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "End Date: ${auctionItem.endDateTime}")
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Category: ${auctionItem.category}")
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Highest Bid: ${auctionItem.highestBid}")
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Seller: ${auctionItem.seller}")

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.bids),
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
                                    Text(text = bidItem.amount.toString() ?: "0")
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
                    Text(text = stringResource(id = R.string.login_first_see_bids))
                }
            }
        }
        if (isLoggedIn?.username?.isNotEmpty() == true || isLoggedIn?.username != auctionItem.seller) {
            if (isBidPlaced.value == true || isItemPurchased.value == true) {
                Text(
                    text = if (isItemPurchased.value == false) stringResource(id = R.string.you_already_place_bid) else stringResource(id = R.string.purchase_this_item)
                )
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 80.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(onClick = { showDialog = true }) {
                        Text(text = stringResource(id = R.string.place_bid))
                    }

                    if (showDialog) {
                        BidDialog(
                            onDismiss = { showDialog = false },
                            onDone = {
                                viewModel.placeBid(auctionItem.id, it.toDouble(), context)
                                viewModel.fetchBids(auctionItem.id, context)
                            },
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun BidDialog(
    onDismiss: () -> Unit,
    onDone: (String) -> Unit
) {
    var amount by remember {
        mutableStateOf("")
    }
    AlertDialog(onDismissRequest = onDismiss, confirmButton = {
        TextButton(onClick = {
            onDone(amount)
            onDismiss()
        }) {
            Text(text = stringResource(id = R.string.place_bid))
        }
    },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = stringResource(id = R.string.cancel))
            }
        },
        title = {
            Text(text = stringResource(id = R.string.enter_amount))
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.enter_decimal)
                        )
                    })
            }
        }
    )

}