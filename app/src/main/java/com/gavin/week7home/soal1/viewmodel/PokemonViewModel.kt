package com.gavin.week7home.soal1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gavin.week7home.soal1.model.PokemonCard
import com.gavin.week7home.soal1.repository.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// State = semua data yang dibutuhkan layar
data class PokemonUiState(
    val query: String = "Charizard",
    val isLoading: Boolean = false,
    val cards: List<PokemonCard> = emptyList(),
    val errorMessage: String? = null,
    val selectedCard: PokemonCard? = null // kartu yang sedang dibuka detailnya
)

// VIEWMODEL: menyimpan state dan memanggil repository
class PokemonViewModel : ViewModel() {

    private val repository = PokemonRepository()

    private val _uiState = MutableStateFlow(PokemonUiState())
    val uiState: StateFlow<PokemonUiState> = _uiState

    init {
        searchCards()
    }

    fun onQueryChange(newQuery: String) {
        _uiState.value = _uiState.value.copy(query = newQuery)
    }

    // Dipanggil saat tombol search di keyboard ditekan / chip diklik / Try Again
    fun searchCards(keyword: String = _uiState.value.query) {
        if (keyword.isBlank()) return

        _uiState.value = _uiState.value.copy(query = keyword, isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                val result = repository.searchCards(keyword)
                if (result.isEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        cards = emptyList(),
                        errorMessage = "Kartu \"$keyword\" tidak ditemukan"
                    )
                } else {
                    _uiState.value = _uiState.value.copy(isLoading = false, cards = result)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    cards = emptyList(),
                    errorMessage = e.message ?: "Terjadi kesalahan"
                )
            }
        }
    }

    fun selectCard(card: PokemonCard) {
        _uiState.value = _uiState.value.copy(selectedCard = card)
    }

    fun closeDetail() {
        _uiState.value = _uiState.value.copy(selectedCard = null)
    }
}
