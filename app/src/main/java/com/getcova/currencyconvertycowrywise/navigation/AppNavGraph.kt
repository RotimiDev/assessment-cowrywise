package com.getcova.currencyconvertycowrywise.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.getcova.feature.ui.CurrencyConverterScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "currencyConverter"
    ) {
        composable("currencyConverter") {
            CurrencyConverterScreen()
        }
    }
}
