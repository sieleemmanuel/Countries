package com.siele.countries.ui

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.siele.countries.PrefsStore
import com.siele.countries.api.CountryApi
import com.siele.countries.model.Country
import com.siele.countries.repository.CountriesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.net.ssl.HttpsURLConnection

@HiltViewModel
class CountriesViewModel @Inject constructor(private val countriesRepo: CountriesRepo):ViewModel() {
    val emptySearch = mutableStateOf(false)
    val countries = mutableStateOf(listOf<Country>())
    var isLoading = mutableStateOf(false)
    var isError = mutableStateOf("")
    var networkError = mutableStateOf("")

    var isDarkModeActive = mutableStateOf(false)
    fun getDataStore(context: Context) = PrefsStore(context = context)
    fun setDarkMode(enabled:Boolean, prefsStore: PrefsStore) {
        viewModelScope.launch {
            prefsStore.toggleDayMode(enabled)
        }

    }

    fun setLanguage(language:String, prefsStore: PrefsStore) {
        viewModelScope.launch {
            prefsStore.setLanguage(language)
        }

    }

    private fun getCountries(){
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = countriesRepo.getCountries()
                when {
                    response.isSuccessful -> {
                        countries.value = response.body()!!
                        isError.value =""
                        isLoading.value = false
                    }
                    else -> {
                        isError.value = response.message()
                        isLoading.value = false
                    }
                }
            }catch (e:Exception){
                networkError.value = "No internet connection. Check and try again"
                isLoading.value = false
                throw e
            }

        }
    }

    fun getCountry(name:String):Country?{
        if (countries.value.isNotEmpty()) {
            return countries.value.find { it.name.common == name }!!
        }
        return null
    }
    init {
        getCountries()
    }

}