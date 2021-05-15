package com.thanakorn.jaroensetthakul.models

data class Coin(
    val allTimeHigh: AllTimeHigh?,
    val approvedSupply: Boolean?,
    val change: Double?,
    val circulatingSupply: Float?,
    val color: String?,
    val confirmedSupply: Boolean?,
    val description: String?,
    val firstSeen: Long?,
    val history: List<String>?,
    val iconType: String?,
    val iconUrl: String?,
    val id: Int?,
    val links: List<Link>?,
    val listedAt: Int?,
    val marketCap: Long?,
    val name: String?,
    val numberOfExchanges: Int?,
    val numberOfMarkets: Int?,
    val penalty: Boolean?,
    val price: String?,
    val rank: Int?,
    val slug: String?,
    val socials: List<Social>?,
    val symbol: String?,
    val totalSupply: Float?,
    val type: String?,
    val uuid: String?,
    val volume: Long?,
    val websiteUrl: String?
)