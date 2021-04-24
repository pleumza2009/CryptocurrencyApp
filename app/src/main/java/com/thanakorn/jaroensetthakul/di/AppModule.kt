package com.thanakorn.jaroensetthakul.di

import com.thanakorn.jaroensetthakul.api.CoinApi
import com.thanakorn.jaroensetthakul.repositories.CoinRepository
import com.thanakorn.jaroensetthakul.utilities.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCoinApi() : CoinApi{
        return   Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CoinApi::class.java)
    }

    @Singleton
    @Provides
    fun provideCoinRepository(api : CoinApi) = CoinRepository(api)


}