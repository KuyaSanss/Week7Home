package com.gavin.week7home.soal1.model

// MODEL: bentuk data dari JSON API pokemonpricetracker.com
// Nama variabel harus sama persis dengan nama field di JSON.

data class PokemonResponse(
    val data: List<PokemonCard>?
)

data class PokemonCard(
    val id: String?,
    val name: String?,
    val setName: String?,
    val cardNumber: String?,
    val rarity: String?,
    val hp: String?,
    val pokemonType: String?,
    val artist: String?,
    val imageCdnUrl400: String?,
    val imageCdnUrl800: String?,
    val prices: CardPrices?
)

data class CardPrices(
    val market: Double?,
    val low: Double?,
    val sellers: Int?,
    val listings: Int?,
    val primaryPrinting: String?
)
