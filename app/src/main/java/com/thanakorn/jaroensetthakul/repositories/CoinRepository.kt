package com.thanakorn.jaroensetthakul.repositories

import com.thanakorn.jaroensetthakul.api.CoinApi
import javax.inject.Inject

class CoinRepository @Inject constructor(
    val coinApi: CoinApi
) {

    suspend fun getCoins(limit : Int) = coinApi.getCoins(base="USD",limit)



}