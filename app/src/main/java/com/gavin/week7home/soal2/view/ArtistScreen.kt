package com.gavin.week7home.soal2.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.gavin.week7home.soal2.model.Artist
import com.gavin.week7home.soal2.viewmodel.ArtistUiState
import com.gavin.week7home.soal2.viewmodel.ArtistViewModel

// VIEW: halaman artis (mengambil data dari ViewModel)
@Composable
fun ArtistScreen(
    onAlbumClick: (String) -> Unit,
    viewModel: ArtistViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    ArtistContent(
        uiState = uiState,
        onQueryChange = { viewModel.onQueryChange(it) },
        onSearch = { viewModel.search() },
        onAlbumClick = onAlbumClick
    )
}

// Isi tampilan saja: menerima state dan event dari Screen di atas
@Composable
fun ArtistContent(
    uiState: ArtistUiState,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onAlbumClick: (String) -> Unit
) {
    val keyboard = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MusicColors.Background)
            .statusBarsPadding()
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            MusicLogo()
            Spacer(modifier = Modifier.width(10.dp))
            Text("Music Artist", color = MusicColors.TextPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }

        // Search bar
        TextField(
            value = uiState.query,
            onValueChange = { onQueryChange(it) },
            placeholder = { Text("Cari artis...") },
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MusicColors.Green) },
            trailingIcon = {
                if (uiState.query.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(Icons.Default.Close, contentDescription = "Hapus", tint = MusicColors.TextSecondary)
                    }
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                keyboard?.hide()
                onSearch()
            }),
            shape = RoundedCornerShape(50),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MusicColors.Surface,
                unfocusedContainerColor = MusicColors.Surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = MusicColors.TextPrimary,
                unfocusedTextColor = MusicColors.TextPrimary,
                cursorColor = MusicColors.Green
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        val artist = uiState.artist
        when {
            uiState.isLoading -> MusicLoadingView("Mencari artis...")

            uiState.errorMessage != null -> MusicErrorView(
                message = uiState.errorMessage ?: "",
                onRetry = { onSearch() }
            )

            artist != null -> {
                // Satu LazyVerticalGrid untuk semua isi, supaya seluruh halaman bisa di-scroll
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 32.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        ArtistBanner(artist)
                    }
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        ArtistBiography(artist.strBiography)
                    }
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Diskografi Album", color = MusicColors.TextPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                Text("Koleksi rilisan resmi", color = MusicColors.TextSecondary, fontSize = 11.sp)
                            }
                            GreenBadge("${uiState.albums.size} Album")
                        }
                    }

                    items(uiState.albums) { album ->
                        AlbumCardItem(
                            album = album,
                            onClick = { album.idAlbum?.let { onAlbumClick(it) } }
                        )
                    }
                }
            }
        }
    }
}

// Gambar besar artis + nama, genre, dan negara
@Composable
private fun ArtistBanner(artist: Artist) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        AsyncImage(
            model = artist.strArtistFanart ?: artist.strArtistThumb,
            contentDescription = artist.strArtist,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        // Gradasi gelap di bawah supaya teks terbaca
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(listOf(Color.Transparent, MusicColors.Background))
                )
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(14.dp)
        ) {
            AsyncImage(
                model = artist.strArtistThumb,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.White, CircleShape)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(artist.strArtist ?: "-", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 26.sp)
                Row {
                    Text(artist.strGenre ?: "-", color = MusicColors.Green, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text("  •  ${artist.strCountry ?: "-"}", color = MusicColors.TextSecondary, fontSize = 11.sp)
                }
            }
        }
    }
}

// Biografi singkat yang bisa dibuka / ditutup
@Composable
private fun ArtistBiography(biography: String?) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text("Tentang Artis", color = MusicColors.TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        Spacer(modifier = Modifier.height(6.dp))

        if (biography.isNullOrBlank()) {
            Text("Biografi tidak tersedia.", color = MusicColors.TextSecondary, fontSize = 13.sp)
        } else {
            Text(
                text = biography,
                color = MusicColors.TextSecondary,
                fontSize = 13.sp,
                lineHeight = 19.sp,
                maxLines = if (expanded) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (expanded) "Tutup ringkasan" else "Baca selengkapnya",
                color = MusicColors.Green,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { expanded = !expanded }
            )
        }
    }
}
