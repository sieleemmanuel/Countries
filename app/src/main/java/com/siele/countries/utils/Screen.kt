package com.siele.countries.utils

sealed class Screen(val route:String){
    object ListScreen: Screen("list_screen")
    object DetailsScreen: Screen("details_screen")
}
