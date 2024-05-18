package com.companies.auctionapp.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.companies.auctionapp.R
import com.companies.auctionapp.presentation.navigation.LOGIN_SCREEN
import com.companies.auctionapp.presentation.navigation.PURCHASED_ITEM_SCREEN
import com.companies.auctionapp.presentation.navigation.SOLD_ITEM_SCREEN
import com.companies.auctionapp.ui.utils.SharedPreferencesHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(navigate: (String) -> Unit) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Card(onClick = { navigate(LOGIN_SCREEN) }, modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(id = R.string.login), modifier = Modifier.padding(8.dp))
        }
        Spacer(modifier = Modifier.height(10.dp))
        Card(onClick = { navigate(SOLD_ITEM_SCREEN) }, modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(id = R.string.sold_items), modifier = Modifier.padding(8.dp))
        }
        Spacer(modifier = Modifier.height(10.dp))
        Card(onClick = { navigate(PURCHASED_ITEM_SCREEN) }, modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(id = R.string.purchase_item), modifier = Modifier.padding(8.dp))
        }
        Spacer(modifier = Modifier.height(10.dp))
        Card(onClick = {
            SharedPreferencesHelper.deleteUserDetails()
            SharedPreferencesHelper.deleteAccountType()
            Toast.makeText(context,R.string.logout_text,Toast.LENGTH_SHORT).show()
        }, modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(id = R.string.logout), modifier = Modifier.padding(8.dp))
        }
    }
}



