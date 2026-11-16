package com.gavin.week7home.soal2.service

import com.gavin.week7home.soal2.model.AlbumResponse
import com.gavin.week7home.soal2.model.ArtistResponse
import com.gavin.week7home.soal2.model.TrackResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// SERVICE: daftar endpoint TheAudioDB
interface AudioDbService {

    @GET("search.php")
    suspend fun searchArtist(@Query("s") artistName: String): ArtistResponse

    @GET("searchalbum.php")
    suspend fun searchAlbums(@Query("s") artistName: String): AlbumResponse

    @GET("album.php")
    suspend fun getAlbumDetail(@Query("m") albumId: String): AlbumResponse

    @GET("track.php")
    suspend fun getTracks(@Query("m") albumId: String): TrackResponse
}

object AudioDbApi {
    // "123" adalah API key publik gratis dari TheAudioDB
    private const val BASE_URL = "https://www.theaudiodb.com/api/v1/json/123/"

    val service: AudioDbService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(AudioDbService::class.java)
}
