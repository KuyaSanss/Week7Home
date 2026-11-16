package com.gavin.week7home.soal2.model

// MODEL: bentuk data dari JSON TheAudioDB
// Nama variabel harus sama persis dengan nama field di JSON.

// search.php?s=...
data class ArtistResponse(
    val artists: List<Artist>?
)

data class Artist(
    val idArtist: String?,
    val strArtist: String?,
    val strGenre: String?,
    val strCountry: String?,
    val strBiography: String?,
    val strArtistThumb: String?,
    val strArtistFanart: String?,
    val strArtistBanner: String?
)

// searchalbum.php?s=...  dan  album.php?m=...
data class AlbumResponse(
    val album: List<Album>?
)

data class Album(
    val idAlbum: String?,
    val strAlbum: String?,
    val strArtist: String?,
    val intYearReleased: String?,
    val strGenre: String?,
    val strAlbumThumb: String?,
    val strDescription: String?
)

// track.php?m=...
data class TrackResponse(
    val track: List<Track>?
)

data class Track(
    val idTrack: String?,
    val strTrack: String?,
    val intDuration: String?,   // dalam milidetik
    val intTrackNumber: String?,
    val strGenre: String?
)
