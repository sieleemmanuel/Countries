package com.siele.countries

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.rememberNavController
import com.siele.countries.ui.theme.CountriesTheme
import dagger.hilt.android.AndroidEntryPoint

val Context.dataStore:DataStore<Preferences> by preferencesDataStore(name = "settings")
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel:CountriesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       setContent {
           val dataStore = viewModel.getDataStore(this)
           val darkModeOn = dataStore.getDayMode.collectAsState(initial = isSystemInDarkTheme()).value
            CountriesTheme(darkTheme = darkModeOn?:false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavigationGraph()
                    Log.d(MainActivity::class.simpleName, "DarkMode: ${viewModel.isDarkModeActive.value} ")
                }
            }
        }
    }
}

/*
@Preview(showBackground = true,
    showSystemUi = true
)
@Composable
fun DefaultPreview() {
    CountriesTheme {
        ListCountries(navController = rememberNavController())
    }
}*/
