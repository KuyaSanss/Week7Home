package com.gavin.week7home.soal1.view

import androidx.compose.ui.graphics.Color
import java.util.Locale

// Warna-warna untuk tampilan PokéTracker (tema gelap)
object PokeColors {
    val Background = Color(0xFF0F141B)
    val Surface = Color(0xFF1A212B)
    val SurfaceLight = Color(0xFF242C38)
    val Red = Color(0xFFE53935)
    val Green = Color(0xFF22C55E)
    val Yellow = Color(0xFFFACC15)
    val TextPrimary = Color.White
    val TextSecondary = Color(0xFF8B95A5)
}

// Ubah angka harga jadi teks, contoh: 21.78 -> "$21.78"
fun formatPrice(value: Double?): String {
    if (value == null) return "-"
    return String.format(Locale.US, "$%.2f", value)
}
