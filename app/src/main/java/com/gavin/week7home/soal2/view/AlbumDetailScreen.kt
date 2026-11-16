package com.gavin.week7home.soal2.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.gavin.week7home.soal2.model.Album
import com.gavin.week7home.soal2.model.Track
import com.gavin.week7home.soal2.viewmodel.AlbumDetailUiState
import com.gavin.week7home.soal2.viewmodel.AlbumDetailViewModel

// VIEW: halaman detail album (mengambil data dari ViewModel)
@Composable
fun AlbumDetailScreen(
    albumId: String,
    onBack: () -> Unit,
    viewModel: AlbumDetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Ambil data album sekali saat halaman dibuka
    LaunchedEffect(albumId) {
        viewModel.loadAlbum(albumId)
    }

    AlbumDetailContent(
        uiState = uiState,
        onBack = onBack,
        onRetry = { viewModel.loadAlbum(albumId) }
    )
}

// Isi tampilan saja: menerima state dan event dari Screen di atas
@Composable
fun AlbumDetailContent(
    uiState: AlbumDetailUiState,
    onBack: () -> Unit,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MusicColors.Background)
            .statusBarsPadding()
    ) {
        // Top bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = MusicColors.TextPrimary)
            }
            Text("Detail Album", color = MusicColors.TextPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }

        val album = uiState.album
        when {
            uiState.isLoading -> MusicLoadingView("Memuat detail album & track list...")

            uiState.errorMessage != null -> MusicErrorView(
                message = uiState.errorMessage ?: "",
                onRetry = onRetry
            )

            album != null -> {
                LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 32.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item { AlbumHeader(album) }

                    item { AlbumDescription(album.strDescription) }

                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp, bottom = 8.dp)
                        ) {
                            Text("♪", color = MusicColors.Green, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Daftar Lagu",
                                color = MusicColors.TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                modifier = Modifier.weight(1f)
                            )
                            GreenBadge("${uiState.tracks.size} Lagu")
                        }
                    }

                    if (uiState.tracks.isEmpty()) {
                        item {
                            Text("Daftar lagu tidak tersedia.", color = MusicColors.TextSecondary, fontSize = 13.sp)
                        }
                    }

                    items(uiState.tracks) { track ->
                        TrackRow(track)
                    }
                }
            }
        }
    }
}

@Composable
private fun AlbumHeader(album: Album) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(shape = RoundedCornerShape(16.dp)) {
            AsyncImage(
                model = album.strAlbumThumb,
                contentDescription = album.strAlbum,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(200.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            album.strAlbum ?: "-",
            color = MusicColors.TextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            textAlign = TextAlign.Center
        )
        Text(album.strArtist ?: "-", color = MusicColors.Green, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            InfoChip("Tahun ${album.intYearReleased ?: "-"}")
            InfoChip(album.strGenre ?: "-")
        }
    }
}

@Composable
private fun InfoChip(text: String) {
    Text(
        text = text,
        color = MusicColors.TextSecondary,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .background(MusicColors.Surface, RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    )
}

@Composable
private fun AlbumDescription(description: String?) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(top = 20.dp)) {
        Text("Deskripsi Album", color = MusicColors.TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        Spacer(modifier = Modifier.height(6.dp))
        if (description.isNullOrBlank()) {
            Text("Deskripsi tidak tersedia.", color = MusicColors.TextSecondary, fontSize = 13.sp)
        } else {
            Text(
                text = description,
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

@Composable
private fun TrackRow(track: Track) {
    val number = (track.intTrackNumber ?: "0").padStart(2, '0') // 1 -> "01"

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Text(number, color = MusicColors.Green, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(track.strTrack ?: "-", color = MusicColors.TextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(track.strGenre ?: "", color = MusicColors.TextSecondary, fontSize = 12.sp)
            }
            Text("⏱ ${formatDuration(track.intDuration)}", color = MusicColors.TextSecondary, fontSize = 12.sp)
        }
        HorizontalDivider(color = MusicColors.Surface)
    }
}

// Ubah milidetik jadi menit:detik, contoh: 318626 -> "05:18"
private fun formatDuration(millis: String?): String {
    val ms = millis?.toLongOrNull() ?: return "--:--"
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}
