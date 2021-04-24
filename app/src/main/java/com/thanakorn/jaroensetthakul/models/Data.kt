package com.thanakorn.jaroensetthakul.models

data class Data(
    val base: Base,
    val coins: MutableList<Coin>,
    val stats: Stats
)