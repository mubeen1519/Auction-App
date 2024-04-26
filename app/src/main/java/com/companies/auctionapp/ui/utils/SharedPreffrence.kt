package com.companies.auctionapp.ui.utils

import android.content.Context
import androidx.core.content.edit
import com.companies.auctionapp.data.RegisterData
import com.google.gson.Gson

object SharedPreferencesHelper {
    private const val PREF_NAME = "UserPrefs"
    private const val KEY_USER_DETAILS = "userDetails"
    private const val KEY_JWT_TOKEN = "jwtToken"

    fun saveUserDetails(context: Context, userDetails: RegisterData) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit {
            putString(KEY_USER_DETAILS, Gson().toJson(userDetails))
        }
    }

    fun getUserDetails(context: Context): RegisterData? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val userDetailsJson = prefs.getString(KEY_USER_DETAILS, null)
        return Gson().fromJson(userDetailsJson, RegisterData::class.java)
    }

    fun saveJwtToken(context: Context, jwtToken: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
       prefs.edit {
           putString(KEY_JWT_TOKEN, jwtToken)
       }
    }

    fun getJwtToken(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_JWT_TOKEN, null)
    }
}
