package com.thanakorn.jaroensetthakul.models

data class Stats(
    val base: String,
    val limit: Int,
    val offset: Int,
    val order: String,
    val total: Int,
    val total24hVolume: Double,
    val totalExchanges: Int,
    val totalMarketCap: Double,
    val totalMarkets: Int
)