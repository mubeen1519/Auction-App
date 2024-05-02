package com.companies.auctionapp.domain.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.lifecycle.viewmodel.viewModelFactory
import com.companies.auctionapp.domain.RetrofitInstance
import com.companies.auctionapp.presentation.viewModel.DetailViewModel
import com.companies.auctionapp.ui.utils.SharedPreferencesHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("LoginService", "Service started")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val loginData = SharedPreferencesHelper.getUserDetails()
                val response = loginData?.let { RetrofitInstance.apiService.login(it) }
            } catch (e : Exception){
                Log.d("LoginService", e.message.toString())
            }
        }
        return START_NOT_STICKY
    }
}
