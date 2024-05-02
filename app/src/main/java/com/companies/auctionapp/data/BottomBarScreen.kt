package com.companies.auctionapp.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.companies.auctionapp.presentation.navigation.HOME_SCREEN

sealed class BottomBarScreen(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Home: BottomBarScreen(
        route= HOME_SCREEN,
        label="Home",
        icon= Icons.Rounded.Home
    )

    object Settings: BottomBarScreen(
        route="settings",
        label="Settings",
        icon= Icons.Rounded.Settings
    )
}