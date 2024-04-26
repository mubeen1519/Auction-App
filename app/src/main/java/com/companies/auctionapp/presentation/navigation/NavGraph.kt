package com.companies.auctionapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.companies.auctionapp.presentation.screens.LoginScreen
import com.companies.auctionapp.presentation.screens.RegistrationScreen

@Composable
fun NavGraph(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = LOGIN_SCREEN) {
        composable(route = LOGIN_SCREEN) {
            LoginScreen(navigate = { route -> navHostController.navigate(route) })
        }
        composable(route = REGISTRATION_SCREEN) {
            RegistrationScreen(navigate = {route -> navHostController.navigate(route)})
        }
    }
}