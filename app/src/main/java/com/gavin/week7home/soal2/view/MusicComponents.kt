package com.gavin.week7home.soal2.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Warna tema Music Artist (gelap + hijau)
object MusicColors {
    val Background = Color(0xFF121317)
    val Surface = Color(0xFF1C1E24)
    val Green = Color(0xFF1DB954)
    val Red = Color(0xFFEF5350)
    val TextPrimary = Color.White
    val TextSecondary = Color(0xFF9AA0AA)
}

// Logo hijau dengan garis-garis seperti gelombang suara
@Composable
fun MusicLogo() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .size(34.dp)
            .background(MusicColors.Green, CircleShape)
            .padding(horizontal = 9.dp)
    ) {
        listOf(8, 14, 18, 12, 6).forEach { h ->
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(h.dp)
                    .background(Color.White, RoundedCornerShape(1.dp))
            )
        }
    }
}

// Badge kecil hijau, contoh: "51 Album"
@Composable
fun GreenBadge(text: String) {
    Text(
        text = text,
        color = MusicColors.Green,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .background(MusicColors.Green.copy(alpha = 0.15f), RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}

@Composable
fun MusicLoadingView(message: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(color = MusicColors.Green)
        Spacer(modifier = Modifier.height(16.dp))
        Text(message, color = MusicColors.TextSecondary, fontSize = 13.sp)
    }
}

// Tampilan error dengan tema yang sama di semua halaman
@Composable
fun MusicErrorView(message: String, onRetry: () -> Unit) {
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
                .size(44.dp)
                .border(3.dp, MusicColors.Red, CircleShape)
        ) {
            Text("!", color = MusicColors.Red, fontWeight = FontWeight.Bold, fontSize = 22.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Terjadi Kendala", color = MusicColors.TextPrimary, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(6.dp))
        Text(message, color = MusicColors.TextSecondary, fontSize = 13.sp, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(18.dp))
        Button(
            onClick = onRetry,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = MusicColors.Green)
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Coba Lagi", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}
