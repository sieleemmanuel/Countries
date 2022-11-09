package com.siele.countries

sealed class Screen(val route:String){
    object ListScreen:Screen("list_screen")
    object DetailsScreen:Screen("details_screen")
}
