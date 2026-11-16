package com.gavin.week7home.soal2.repository

import com.gavin.week7home.soal2.model.Album
import com.gavin.week7home.soal2.model.Artist
import com.gavin.week7home.soal2.model.Track
import com.gavin.week7home.soal2.service.AudioDbApi

// REPOSITORY: perantara antara ViewModel dan Service
class MusicRepository {

    private val service = AudioDbApi.service

    suspend fun searchArtist(name: String): Artist {
        val response = service.searchArtist(name)
        // Kalau artis tidak ada, API mengembalikan "artists": null
        return response.artists?.firstOrNull()
            ?: throw Exception("Artis \"$name\" tidak ditemukan")
    }

    suspend fun getAlbums(artistName: String): List<Album> {
        return service.searchAlbums(artistName).album ?: emptyList()
    }

    suspend fun getAlbumDetail(albumId: String): Album {
        return service.getAlbumDetail(albumId).album?.firstOrNull()
            ?: throw Exception("Album tidak ditemukan")
    }

    suspend fun getTracks(albumId: String): List<Track> {
        return service.getTracks(albumId).track ?: emptyList()
    }
}
