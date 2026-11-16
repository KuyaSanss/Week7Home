package com.gavin.week7home.soal1.repository

import com.gavin.week7home.soal1.model.PokemonCard
import com.gavin.week7home.soal1.service.PokemonApi

// REPOSITORY: perantara antara ViewModel dan Service
class PokemonRepository {

    private val service = PokemonApi.service

    suspend fun searchCards(keyword: String): List<PokemonCard> {
        val response = service.searchCards(
            auth = "Bearer ${PokemonApi.API_KEY}",
            search = keyword
        )
        return response.data ?: emptyList()
    }
}
