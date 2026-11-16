package com.gavin.week7home.soal2.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.gavin.week7home.soal2.model.Album

// COMPONENT: satu album di dalam grid diskografi
@Composable
fun AlbumCardItem(album: Album, onClick: () -> Unit) {
    Column(modifier = Modifier.clickable { onClick() }) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MusicColors.Surface)
        ) {
            AsyncImage(
                model = album.strAlbumThumb,
                contentDescription = album.strAlbum,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = album.strAlbum ?: "-",
            color = MusicColors.TextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(album.intYearReleased ?: "-", color = MusicColors.Green, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text(album.strGenre ?: "", color = MusicColors.TextSecondary, fontSize = 11.sp, maxLines = 1)
        }
    }
}
