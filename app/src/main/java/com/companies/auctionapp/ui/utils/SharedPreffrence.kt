package com.companies.auctionapp.ui.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.companies.auctionapp.data.LoginData
import com.companies.auctionapp.data.RegisterData
import com.google.gson.Gson

object SharedPreferencesHelper {
    private const val PREF_NAME = "UserPrefs"
    private const val KEY_USER_DETAILS = "userDetails"
    private const val KEY_JWT_TOKEN = "jwtToken"

    private lateinit var sharedPreferences: SharedPreferences

    fun initialize(context: Context) {
        sharedPreferences = context.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveUserDetails(userDetails: LoginData) {
        sharedPreferences.edit {
            putString(KEY_USER_DETAILS, Gson().toJson(userDetails))
        }
    }

    fun getUserDetails(): LoginData? {
        val userDetailsJson = sharedPreferences.getString(KEY_USER_DETAILS, null)
        return Gson().fromJson(userDetailsJson, LoginData::class.java)
    }

    fun saveJwtToken(jwtToken: String) {
        sharedPreferences.edit {
            putString(KEY_JWT_TOKEN, jwtToken)
        }
    }

    fun getJwtToken(): String? {
        return sharedPreferences.getString(KEY_JWT_TOKEN, null)
    }
}
