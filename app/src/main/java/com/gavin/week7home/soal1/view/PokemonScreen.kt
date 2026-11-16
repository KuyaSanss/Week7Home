package com.gavin.week7home.soal1.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gavin.week7home.soal1.model.PokemonCard
import com.gavin.week7home.soal1.viewmodel.PokemonUiState
import com.gavin.week7home.soal1.viewmodel.PokemonViewModel

// VIEW: halaman utama PokéTracker (mengambil data dari ViewModel)
@Composable
fun PokemonScreen(viewModel: PokemonViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    PokemonContent(
        uiState = uiState,
        onQueryChange = { viewModel.onQueryChange(it) },
        onSearch = { keyword -> viewModel.searchCards(keyword) },
        onCardClick = { card -> viewModel.selectCard(card) }
    )

    // Kalau ada kartu yang dipilih, tampilkan bottom sheet detail
    val selected = uiState.selectedCard
    if (selected != null) {
        PokemonDetailSheet(card = selected, onDismiss = { viewModel.closeDetail() })
    }
}

// Isi tampilan saja: menerima state dan event dari Screen di atas
@Composable
fun PokemonContent(
    uiState: PokemonUiState,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onCardClick: (PokemonCard) -> Unit
) {
    val keyboard = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PokeColors.Background)
            .statusBarsPadding()
    ) {
        PokeHeader()

        // Search bar
        TextField(
            value = uiState.query,
            onValueChange = { onQueryChange(it) },
            placeholder = { Text("Cari kartu Pokemon...") },
            singleLine = true,
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null, tint = PokeColors.Red)
            },
            trailingIcon = {
                if (uiState.query.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(Icons.Default.Close, contentDescription = "Hapus", tint = PokeColors.TextSecondary)
                    }
                }
            },
            // Search baru jalan saat tombol search di keyboard ditekan (hemat kuota API)
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                keyboard?.hide()
                onSearch(uiState.query)
            }),
            shape = RoundedCornerShape(14.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = PokeColors.Surface,
                unfocusedContainerColor = PokeColors.Surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = PokeColors.TextPrimary,
                unfocusedTextColor = PokeColors.TextPrimary,
                cursorColor = PokeColors.Red
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        // Chip pencarian cepat (bisa di-scroll ke samping)
        val quickSearch = listOf("Charizard", "Pikachu", "Mewtwo", "Gengar", "Eevee", "Lugia")
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(quickSearch) { name ->
                Text(
                    text = name,
                    color = PokeColors.TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(PokeColors.SurfaceLight)
                        .clickable {
                            keyboard?.hide()
                            onSearch(name)
                        }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                )
            }
        }

        // Isi halaman: loading / error / grid kartu
        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PokeColors.Red)
                }
            }

            uiState.errorMessage != null -> {
                PokeErrorView(
                    message = uiState.errorMessage ?: "",
                    onRetry = { onSearch(uiState.query) }
                )
            }

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 32.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Judul ini memakai lebar penuh (2 kolom)
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Market Results (${uiState.cards.size})",
                                color = PokeColors.TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text("USD • Near Mint", color = PokeColors.TextSecondary, fontSize = 11.sp)
                        }
                    }

                    items(uiState.cards) { card ->
                        PokemonCardItem(
                            card = card,
                            onClick = { onCardClick(card) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PokeHeader() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Logo pokeball sederhana
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .background(PokeColors.Red, CircleShape)
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .border(3.dp, Color.White, CircleShape)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text("PokéTracker", color = PokeColors.TextPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text("Real-time TCG Card Valuation", color = PokeColors.TextSecondary, fontSize = 11.sp)
        }
        Text(
            text = "● LIVE API",
            color = PokeColors.Green,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(PokeColors.Green.copy(alpha = 0.15f), RoundedCornerShape(50))
                .padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}

// Tampilan saat gagal mengambil data kartu
@Composable
fun PokeErrorView(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(56.dp)
                .background(PokeColors.SurfaceLight, CircleShape)
        ) {
            Icon(Icons.Default.Warning, contentDescription = null, tint = PokeColors.TextSecondary)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text("Connection Failed", color = Color(0xFFFF8A80), fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(message, color = PokeColors.TextSecondary, fontSize = 12.sp, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onRetry,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PokeColors.Red)
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Try Again", fontWeight = FontWeight.Bold)
        }
    }
}
