package com.companies.auctionapp.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.companies.auctionapp.presentation.screens.AddNewItemScreen
import com.companies.auctionapp.presentation.screens.DetailsScreen
import com.companies.auctionapp.presentation.screens.HomeScreen
import com.companies.auctionapp.presentation.screens.ItemSoldScreen
import com.companies.auctionapp.presentation.screens.LoginScreen
import com.companies.auctionapp.presentation.screens.PurchasedItemScreen
import com.companies.auctionapp.presentation.screens.RegistrationScreen
import com.companies.auctionapp.presentation.screens.SettingScreen
import com.companies.auctionapp.presentation.viewModel.HomeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = HOME_SCREEN) {
        composable(route = LOGIN_SCREEN) {
            LoginScreen(navigate = { route -> navHostController.navigate(route) })
        }
        composable(route = REGISTRATION_SCREEN) {
            RegistrationScreen(navigate = { route -> navHostController.navigate(route) })
        }

        composable(route = HOME_SCREEN) {
            HomeScreen(navigate = { route -> navHostController.navigate(route) })
        }

        composable(
            route = "$DETAIL_SCREEN/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId")
            val viewModel: HomeViewModel = viewModel()
            val auctionItem = viewModel.getAuctionItemDetails(itemId ?: -1)
            if (auctionItem != null) {
                DetailsScreen(auctionItem = auctionItem)
            }
        }

        composable(route = SETTINGS_SCREEN) {
            SettingScreen(navigate = { route -> navHostController.navigate(route) })
        }

        composable(route = ADD_SCREEN) {
            AddNewItemScreen(navigate = { route -> navHostController.navigate(route) })
        }

        composable(route = SOLD_ITEM_SCREEN){
            ItemSoldScreen()
        }

        composable(route = PURCHASED_ITEM_SCREEN){
            PurchasedItemScreen()
        }
    }
}