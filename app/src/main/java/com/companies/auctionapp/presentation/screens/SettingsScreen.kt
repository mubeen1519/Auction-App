package com.companies.auctionapp.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.companies.auctionapp.presentation.navigation.LOGIN_SCREEN

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(navigate: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Card(onClick = { navigate(LOGIN_SCREEN) }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Login", modifier = Modifier.padding(8.dp))
        }
    }
}



