package com.companies.auctionapp.presentation.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.companies.auctionapp.data.RegisterData
import com.companies.auctionapp.data.RegistrationResponse
import com.companies.auctionapp.domain.RetrofitInstance
import com.companies.auctionapp.presentation.navigation.LOGIN_SCREEN
import com.companies.auctionapp.ui.utils.Result
import com.companies.auctionapp.ui.utils.SharedPreferencesHelper
import kotlinx.coroutines.launch

class RegistrationViewModel : ViewModel() {

    private val _registrationResult = MutableLiveData<Result>()
    val registrationResult: LiveData<Result> get() = _registrationResult

    var isLoading = mutableStateOf(false)
    var data = mutableStateOf(RegisterData())

    fun onEmailChange(value : String){
        data.value = data.value.copy(email = value)
    }

    fun onUsernameChange(value: String){
        data.value = data.value.copy(username = value)
    }

    fun onPasswordChange(value: String){
        data.value = data.value.copy(password = value)
    }

    fun register(context: Context,navigate : (String) -> Unit) {
        val password = data.value.password
        val email = data.value.email
        val username = data.value.username

        // Validate password
        val passwordPattern = "[a-zA-Z0-9@.]+|\\d".toRegex()
        if (!passwordPattern.matches(password)) {
            // Password does not meet criteria, show toast
            Toast.makeText(context, "Password must contain at least one digit, alphanumeric and one uppercase letter", Toast.LENGTH_SHORT).show()
            return
        }

        // Validate other fields
        if (email.isEmpty() || username.isEmpty()) {
            // Email or username is empty, show toast
            Toast.makeText(context, "Email and username cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        viewModelScope.launch {
            try {
                _registrationResult.value = Result.Loading
                val response = RetrofitInstance.apiService.register(data.value)
                if (response.isSuccessful) {
                    isLoading.value = false
                    _registrationResult.value = Result.Success(response.body()!!)
                    Log.d("TAG", "register: ${response.body()}")
                    Toast.makeText(context,"Account Created, Please Login to Continue",Toast.LENGTH_SHORT).show()
                    val userDetails = RegisterData(email, username, password)
                    SharedPreferencesHelper.saveUserDetails(context, userDetails)
                    navigate(LOGIN_SCREEN)
                } else {
                    isLoading.value = false
                    _registrationResult.value = Result.Error(Exception(response.message()))
                    Toast.makeText(context,response.message(),Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                isLoading.value = false
                _registrationResult.value = Result.Error(e)
                Toast.makeText(context,e.message,Toast.LENGTH_SHORT).show()
            }
        }
    }
}


