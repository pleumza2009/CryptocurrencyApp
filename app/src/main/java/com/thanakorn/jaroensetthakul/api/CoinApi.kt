package com.thanakorn.jaroensetthakul.api

import com.thanakorn.jaroensetthakul.models.CoinsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinApi {

    @GET("v1/public/coins")
    suspend fun getCoins(
            @Query("base")
            base :String = "USD",
            @Query("limit")
            limit :Int = 10
    ): Response<CoinsResponse>
}