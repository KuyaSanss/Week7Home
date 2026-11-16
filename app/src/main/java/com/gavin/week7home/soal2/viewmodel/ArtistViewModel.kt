package com.gavin.week7home.soal2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gavin.week7home.soal2.model.Album
import com.gavin.week7home.soal2.model.Artist
import com.gavin.week7home.soal2.repository.MusicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ArtistUiState(
    val query: String = "Coldplay",
    val isLoading: Boolean = false,
    val artist: Artist? = null,
    val albums: List<Album> = emptyList(),
    val errorMessage: String? = null
)

// VIEWMODEL untuk halaman artis
class ArtistViewModel : ViewModel() {

    private val repository = MusicRepository()

    private val _uiState = MutableStateFlow(ArtistUiState())
    val uiState: StateFlow<ArtistUiState> = _uiState

    init {
        search()
    }

    fun onQueryChange(newQuery: String) {
        _uiState.value = _uiState.value.copy(query = newQuery)
    }

    fun search() {
        val keyword = _uiState.value.query
        if (keyword.isBlank()) return

        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        // Coroutine: panggil API tanpa membuat aplikasi macet
        viewModelScope.launch {
            try {
                val artist = repository.searchArtist(keyword)
                val albums = repository.getAlbums(keyword)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    artist = artist,
                    albums = albums
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    artist = null,
                    albums = emptyList(),
                    errorMessage = e.message ?: "Terjadi kesalahan"
                )
            }
        }
    }
}
