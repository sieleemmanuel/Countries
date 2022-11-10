package com.siele.countries.api

import retrofit2.http.GET

interface CountryApi {
    @GET("all")
    suspend fun getCountries()
}