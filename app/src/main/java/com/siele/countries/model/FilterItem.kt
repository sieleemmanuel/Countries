package com.siele.countries.model

import androidx.compose.runtime.MutableState

data class FilterItem(val filter:String, val selected:MutableState<Boolean>)
