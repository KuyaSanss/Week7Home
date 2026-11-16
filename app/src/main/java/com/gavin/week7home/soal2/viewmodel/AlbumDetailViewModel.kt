package com.gavin.week7home.soal2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gavin.week7home.soal2.model.Album
import com.gavin.week7home.soal2.model.Track
import com.gavin.week7home.soal2.repository.MusicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AlbumDetailUiState(
    val isLoading: Boolean = false,
    val album: Album? = null,
    val tracks: List<Track> = emptyList(),
    val errorMessage: String? = null
)

// VIEWMODEL untuk halaman detail album
class AlbumDetailViewModel : ViewModel() {

    private val repository = MusicRepository()

    private val _uiState = MutableStateFlow(AlbumDetailUiState())
    val uiState: StateFlow<AlbumDetailUiState> = _uiState

    fun loadAlbum(albumId: String) {
        _uiState.value = AlbumDetailUiState(isLoading = true)

        viewModelScope.launch {
            try {
                val album = repository.getAlbumDetail(albumId)
                val tracks = repository.getTracks(albumId)
                _uiState.value = AlbumDetailUiState(album = album, tracks = tracks)
            } catch (e: Exception) {
                _uiState.value = AlbumDetailUiState(errorMessage = e.message ?: "Terjadi kesalahan")
            }
        }
    }
}
