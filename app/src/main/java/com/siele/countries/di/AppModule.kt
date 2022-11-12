package com.siele.countries.di

import com.siele.countries.api.CountryApi
import com.siele.countries.api.RetrofitInstance
import com.siele.countries.repository.CountriesRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideApi():CountryApi= RetrofitInstance.retrofit.create(CountryApi::class.java)

    @Provides
    @Singleton
    fun provideRepository(countryApi: CountryApi):CountriesRepo= CountriesRepo(countryApi)
}