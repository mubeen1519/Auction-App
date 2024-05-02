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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.companies.auctionapp.data.AuctionItem
import com.companies.auctionapp.presentation.navigation.DETAIL_SCREEN
import com.companies.auctionapp.presentation.viewModel.HomeViewModel
import com.companies.auctionapp.ui.utils.Result

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    navigate: (String) -> Unit
) {
    val auctionItemsResult by viewModel.auctionItemsResult.observeAsState()
    val categoryItemResult by viewModel.categoriesItemsResult.observeAsState()

    var dialogOpen by remember {
        mutableStateOf(false)
    }



    Scaffold(
        topBar = {
            TopAppBar(title = { /*TODO*/ },
                actions = {
                    IconButton(onClick = { dialogOpen = true }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                    }
                })
        }
    ) {

        if (dialogOpen) {
            SearchDialog(onSearch = { category, maxPrice ->
                viewModel.fetchAuctionItems(category = category, maxPrice = maxPrice)
            }, onDismiss = { dialogOpen = false })
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            when (val categoriesResult = categoryItemResult) {
                is Result.Success -> {
                    val categories = categoriesResult.data as List<String>
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        items(categories) { category ->
                            // Render UI for category
                            CategoryItem(category)
                        }
                    }
                }

                is Result.Loading -> {
                    // Show loading indicator for categories
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()

                    }
                }

                is Result.Error -> {
                    // Show error message for categories
                    Text(text = "Error: ${categoriesResult.failure.message}")

                }

                else -> {

                }
            }
            when (val result = auctionItemsResult) {
                is Result.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        // Show loading indicator
                        CircularProgressIndicator()
                    }
                }

                is Result.Success -> {
                    // Display auction items
                    val auctionItems = result.data as List<AuctionItem>
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        items(auctionItems) { auctionItem ->
                            AuctionItemRow(
                                auctionItem,
                                onClick = { navigate("$DETAIL_SCREEN/${auctionItem.id}") })
                        }
                    }
                }

                is Result.Error -> {
                    // Show error message
                    Text(text = "Error: ${result.failure.message}")
                }

                else -> {

                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuctionItemRow(auctionItem: AuctionItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),

        ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = auctionItem.name, style = TextStyle(fontWeight = FontWeight.Bold))
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Arrow Right"
                )
            }
        }
    }
}

@Composable
fun CategoryItem(item: String) {
    Card(
        modifier = Modifier
            .wrapContentWidth()
            .height(50.dp)
            .padding(5.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(3.dp),
    ) {
        Text(text = item, modifier = Modifier.padding(8.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchDialog(
    onSearch: (category: String?, maxPrice: Int?) -> Unit,
    onDismiss: () -> Unit
) {
    var categoryName by remember { mutableStateOf("") }
    var maxPriceText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Search Items") },
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.padding(8.dp)
            ) {
                Button(
                    onClick = {
                        val maxPrice = maxPriceText.toIntOrNull()
                        onSearch(categoryName.takeIf { it.isNotEmpty() }, maxPrice)
                        onDismiss()
                    }
                ) {
                    Text(text = "Search")
                }
            }
        },
        text = {
            Column(modifier = Modifier.padding(8.dp)) {
                OutlinedTextField(
                    value = categoryName,
                    onValueChange = { categoryName = it },
                    label = { Text("Category Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = maxPriceText,
                    onValueChange = { maxPriceText = it },
                    label = { Text("Max Price") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}

