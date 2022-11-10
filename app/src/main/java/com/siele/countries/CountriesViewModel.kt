package com.siele.countries

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CountriesViewModel:ViewModel() {

    var isDarkModeActive = mutableStateOf(false)
    fun getDataStore(context: Context) = PrefsStore(context = context)

    fun isDarkMode(prefsStore: PrefsStore){
        viewModelScope.launch {
            prefsStore.getDayMode.collect{
                isDarkModeActive.value = it?:false
            }
        }

    }

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

}