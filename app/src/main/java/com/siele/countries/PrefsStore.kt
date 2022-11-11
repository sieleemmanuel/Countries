package com.siele.countries

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.siele.countries.ui.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PrefsStore(private val context: Context) {
    companion object{
        val darkMode = booleanPreferencesKey("keyDark")
        val lang = stringPreferencesKey("keyLang")
    }
    val getDayMode: Flow<Boolean?> = context.dataStore.data.map { prefs ->
        prefs[darkMode]?:false
    }
    val getLang: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[lang]?:"en"
    }

    suspend fun toggleDayMode(isDarkMode:Boolean) {
        context.dataStore.edit { mutablePrefs ->
            mutablePrefs[darkMode] = isDarkMode
        }
    }

    suspend fun setLanguage(language: String){
        context.dataStore.edit { preference ->
            preference[lang]  = language
        }
    }

}