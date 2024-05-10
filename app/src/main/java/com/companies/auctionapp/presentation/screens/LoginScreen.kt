package com.companies.auctionapp.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.companies.auctionapp.R
import com.companies.auctionapp.presentation.navigation.REGISTRATION_SCREEN
import com.companies.auctionapp.presentation.viewModel.LoginViewModel
import com.companies.auctionapp.ui.utils.Result

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    navigate: (String) -> Unit
) {
    val loginResult by viewModel.loginResult.observeAsState()
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        OutlinedTextField(
            value = viewModel.data.value.username,
            onValueChange = { viewModel.onUsernameChange(it) },
            label = { Text(stringResource(id = R.string.username)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        // Password TextField
        OutlinedTextField(
            value = viewModel.data.value.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = { Text(stringResource(id = R.string.password)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        // Register Button
        Button(
            onClick = { viewModel.login(context,navigate) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(stringResource(id = R.string.login))
        }

        Text(
            text = stringResource(id = R.string.or),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            textAlign = TextAlign.Center
        )

        Button(
            onClick = { navigate(REGISTRATION_SCREEN) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(stringResource(id = R.string.create_Account))
        }


        // Loading Indicator
        if (loginResult is Result.Loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
            }
        }
    }
}
