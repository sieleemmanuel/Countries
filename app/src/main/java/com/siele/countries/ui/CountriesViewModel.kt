package com.siele.countries.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.siele.countries.PrefsStore
import com.siele.countries.model.Country
import com.siele.countries.model.FilterItem
import com.siele.countries.repository.CountriesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@SuppressLint("MutableCollectionMutableState")
@HiltViewModel
class CountriesViewModel @Inject constructor(private val countriesRepo: CountriesRepo):ViewModel() {
    val searchActive = mutableStateOf(false)
    val filterActive = mutableStateOf(false)
    val isFilterSheet = mutableStateOf(false)
    val allCountries = mutableStateOf(listOf<Country>())
    val filteredCountries = mutableStateOf(listOf<Country>())
    private val continents = mutableStateOf(listOf<String>())
    val continentsFilters = mutableStateOf(listOf<FilterItem>())
    private val timeZones = mutableStateOf(listOf<String>())
    val timeZonesFilters = mutableStateOf(listOf<FilterItem>())
    var isLoading = mutableStateOf(false)
    var isServerError = mutableStateOf(false)
    var isNetworkError = mutableStateOf(false)
    var serverErrorMessage = mutableStateOf("")
    var networkErrorMessage = mutableStateOf("")

    var languages = listOf("Bahasa", "Deutch", "English", "Espanyol", "Francaise", "Italiano", "Pourtugies")

    var selectedFilters =  mutableStateOf(mutableListOf <FilterItem>())
    var stringFilters =  mutableStateOf(mutableListOf <String>())

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

    fun getCountries(){
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = countriesRepo.getCountries()
                when {
                    response.isSuccessful -> {
                        allCountries.value = response.body()!!.sortedBy {
                            it.name.common
                        }

                        continents.value = allCountries.value.map {
                            it.region
                        }.sorted().distinct()

                        continentsFilters.value = continents.value.map {
                            FilterItem(filter = it, selected = mutableStateOf(false))
                        }
                        timeZones.value = allCountries.value.map {
                            it.timezones?.first().toString()
                        }.sorted().distinct()
                        timeZonesFilters.value = timeZones.value.map {
                            FilterItem(filter = it, selected = mutableStateOf(false))
                        }

                        timeZones.value = allCountries.value.map {
                            it.timezones?.first().toString()
                        }.sorted().distinct()
                        serverErrorMessage.value =""
                        networkErrorMessage.value =""
                        isLoading.value = false
                        isServerError.value = false
                        isNetworkError.value = false
                    }
                    else -> {
                        serverErrorMessage.value = response.message()
                        isServerError.value = true
                        isNetworkError.value = false
                        isLoading.value = false
                    }
                }
            }catch (e:Exception){
                networkErrorMessage.value = "No internet connection. Check and try again"
                isNetworkError.value = true
                isServerError.value = false
                isLoading.value = false
               // throw e
            }

        }
    }

    fun resetFilters(){
        for (continent in continentsFilters.value){
            continent.selected.value = false
        }
        for (timeZone in timeZonesFilters.value){
            timeZone.selected.value = false
        }
    }
    fun getCountry(name:String):Country?{
        if (allCountries.value.isNotEmpty()) {
            return allCountries.value.find { it.name.common == name }!!
        }
        return null
    }
    init {
        getCountries()
    }

}