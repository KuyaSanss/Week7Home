package com.gavin.week7home.soal1.service

import com.gavin.week7home.soal1.model.PokemonResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

// SERVICE: daftar endpoint API yang dipanggil dengan Retrofit
interface PokemonService {

    // Contoh: https://www.pokemonpricetracker.com/api/v2/cards?search=charizard&limit=20
    @GET("api/v2/cards")
    suspend fun searchCards(
        @Header("Authorization") auth: String,
        @Query("search") search: String,
        @Query("limit") limit: Int = 20
    ): PokemonResponse
}

// Object untuk membuat Retrofit satu kali saja
object PokemonApi {
    // API key dari https://www.pokemonpricetracker.com/ (limit 100 request/hari)
    const val API_KEY = "pokeprice_free_8b27c81eddbb63997f4e9df61d1f5610aa9ef1211a569bbe"

    private const val BASE_URL = "https://www.pokemonpricetracker.com/"

    val service: PokemonService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PokemonService::class.java)
}
