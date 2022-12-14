package com.siele.countries.ui

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.siele.countries.utils.Screen

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NavigationGraph() {
val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.ListScreen.route){

        composable(route = Screen.ListScreen.route){
            ListCountries(navController = navController)
        }
        composable(
            route = Screen.DetailsScreen.route + "/{country}",
            arguments = listOf(
                navArgument("country"){
                    type = NavType.StringType
                }
            )
        ){ entry ->

            CountryDetails(
                navController = navController,
                selectedCountry = entry.arguments?.getString("country")
            )
        }

    }
}