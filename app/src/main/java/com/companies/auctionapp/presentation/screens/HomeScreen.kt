package com.companies.auctionapp.presentation.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.companies.auctionapp.R
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
                        Icon(
                            imageVector = Icons.Default.Search, contentDescription = stringResource(
                                id = R.string.search
                            )
                        )
                    }
                })
        }
    ) {

        if (dialogOpen) {
            when (val result = categoryItemResult) {
                is Result.Success -> {
                    val categories = result.data as List<String>
                    SearchDialog(
                        categories = categories,
                        onSearch = { category, maxPrice ->
                            viewModel.fetchAuctionItems(category = category, maxPrice = maxPrice)
                        },
                        onDismiss = { dialogOpen = false }
                    )
                }
                // Handle other cases if needed
                else -> {
                    // Handle loading or error cases
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
//            when (val categoriesResult = categoryItemResult) {
//                is Result.Success -> {
//                    val categories = categoriesResult.data as List<String>
//                    LazyRow(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(10.dp)
//                    ) {
//                        items(categories) { category ->
//                            // Render UI for category
//                            CategoryItem(category)
//                        }
//                    }
//                }

//                is Result.Loading -> {
//                    // Show loading indicator for categories
//                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                        CircularProgressIndicator()
//
//                    }
//                }
//
//                is Result.Error -> {
//                    // Show error message for categories
//                    Text(text = "Error: ${categoriesResult.failure.message}")
//
//                }
//
//                else -> {
//
//                }
//            }
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
                            .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 100.dp)
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = auctionItem.name, style = TextStyle(fontWeight = FontWeight.Bold))
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = stringResource(id = R.string.arrow_right)
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
    categories: List<String>,
    onSearch: (category: String?, maxPrice: Int?) -> Unit,
    onDismiss: () -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    var selectedCategoryIndex by remember { mutableStateOf(0) }
    var maxPriceText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = stringResource(id = R.string.search_items)) },
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.padding(8.dp)
            ) {
                Button(
                    onClick = {
                        val maxPrice = maxPriceText.toIntOrNull()
                        val category =
                            if (selectedCategoryIndex == 0) null else categories[selectedCategoryIndex - 1]
                        onSearch(category, maxPrice)
                        onDismiss()
                    }
                ) {
                    Text(text = stringResource(id = R.string.search))
                }
            }
        },
        text = {
            Column(modifier = Modifier.padding(8.dp)) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = { expanded = true })
                    ) {
                        Text(
                            text = if (selectedCategoryIndex == 0) stringResource(id = R.string.select_category) else categories[selectedCategoryIndex - 1],
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        categories.forEachIndexed { index, category ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedCategoryIndex = index + 1
                                    expanded = false
                                },
                                text = {
                                    Text(text = category)
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = maxPriceText,
                        onValueChange = { maxPriceText = it },
                        label = { Text(stringResource(id = R.string.max_price)) },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    )
}

