package com.companies.auctionapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.companies.auctionapp.domain.service.LoginScheduler
import com.companies.auctionapp.presentation.navigation.ADD_SCREEN
import com.companies.auctionapp.presentation.navigation.HOME_SCREEN
import com.companies.auctionapp.presentation.navigation.NavGraph
import com.companies.auctionapp.presentation.navigation.SETTINGS_SCREEN
import com.companies.auctionapp.presentation.screens.RegistrationScreen
import com.companies.auctionapp.ui.theme.AuctionAppTheme
import com.companies.auctionapp.ui.utils.SharedPreferencesHelper

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {
    private lateinit var loginScheduler: LoginScheduler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginScheduler = LoginScheduler(this)
        loginScheduler.startLoginScheduler()
        SharedPreferencesHelper.initialize(this)
        setContent {
            AuctionAppTheme {
                // A surface container using the 'background' color from the theme
                val navHostController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = {
                            BottomAppBar(modifier = Modifier.fillMaxWidth(), actions = {
                                IconButton(
                                    onClick = { navHostController.navigate(HOME_SCREEN) }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Home,
                                        contentDescription = "Home"
                                    )

                                }

                                IconButton(
                                    onClick = { navHostController.navigate(SETTINGS_SCREEN) }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = "Settings"
                                    )
                                }


                            }, contentPadding = PaddingValues(10.dp), floatingActionButton = {
                                FloatingActionButton(
                                    onClick = { navHostController.navigate(ADD_SCREEN) },
                                    containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                                ) {
                                    Icon(Icons.Filled.Add, "Localized description")
                                }

                            })
                        }
                    ) { _ ->
                        NavGraph(navHostController = navHostController)
                    }

                }
            }
        }
    }
}
