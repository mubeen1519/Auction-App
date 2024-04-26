package com.companies.auctionapp.presentation.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.companies.auctionapp.data.LoginData
import com.companies.auctionapp.domain.RetrofitInstance
import com.companies.auctionapp.ui.utils.Result
import com.companies.auctionapp.ui.utils.SharedPreferencesHelper
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _loginResult = MutableLiveData<Result>()
    val loginResult: LiveData<Result> get() = _loginResult

    var isLoading = mutableStateOf(false)
    var data = mutableStateOf(LoginData())
    var toastMessage = mutableStateOf("")

    fun onUsernameChange(value: String){
        data.value = data.value.copy(username = value)
    }

    fun onPasswordChange(value: String){
        data.value = data.value.copy(password = value)
    }

    fun login(context: Context) {
        viewModelScope.launch {
            try {
                _loginResult.value = Result.Loading
                val response = RetrofitInstance.apiService.login(data.value)
                if (response.isSuccessful) {
                    isLoading.value = false
                    _loginResult.value = Result.Success(response.body()!!)
                    Log.d("TAG", "login: ${response.body()}")
                    Toast.makeText(context,response.body().toString(), Toast.LENGTH_SHORT).show()
                    val token = response.body()?.token
                    token?.let { SharedPreferencesHelper.saveJwtToken(context, it) }

                } else {
                    isLoading.value = false
                    _loginResult.value = Result.Error(Exception(response.message()))
                    Toast.makeText(context,response.message(), Toast.LENGTH_SHORT).show()

                }
            } catch (e: Exception) {
                isLoading.value = false
                _loginResult.value = Result.Error(e)
                Toast.makeText(context,e.message, Toast.LENGTH_SHORT).show()

            }
        }
    }
}