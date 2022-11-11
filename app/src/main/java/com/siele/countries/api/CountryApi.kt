package com.siele.countries.api

import com.siele.countries.model.Country
import retrofit2.Response
import retrofit2.http.GET

interface CountryApi {
    @GET("all")
    suspend fun getCountries():Response<List<Country>>
}