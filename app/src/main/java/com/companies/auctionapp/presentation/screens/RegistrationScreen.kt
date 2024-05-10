package com.companies.auctionapp.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.companies.auctionapp.R
import com.companies.auctionapp.data.AccountType
import com.companies.auctionapp.presentation.viewModel.RegistrationViewModel
import com.companies.auctionapp.ui.utils.Result

@Composable
fun RegistrationScreen(viewModel: RegistrationViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),navigate : (String) -> Unit) {
    val registrationResult by viewModel.registrationResult.observeAsState()
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Email TextField
        OutlinedTextField(
            value = viewModel.data.value.email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = { Text(stringResource(id = R.string.email)) },
            modifier = Modifier.fillMaxWidth()
        )

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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AccountType.entries.forEach { accountType ->
                RadioButton(
                    selected = viewModel.data.value.accountType == accountType,
                    onClick = { viewModel.onAccountTypeSelected(accountType) }
                )
                Text(text = accountType.name)
            }
        }


        // Register Button
        Button(
            onClick = { viewModel.register(context,navigate) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(stringResource(id = R.string.register))
        }

        // Loading Indicator
        if (registrationResult is Result.Loading) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
        }
    }
}
