package com.siele.countries.repository

import com.siele.countries.api.CountryApi
import javax.inject.Inject

class CountriesRepo@Inject constructor(private val countryApi: CountryApi) {
    suspend fun getCountries() = countryApi.getCountries()
}