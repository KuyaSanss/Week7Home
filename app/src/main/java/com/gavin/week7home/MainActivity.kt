package com.gavin.week7home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import com.gavin.week7home.soal1.view.PokemonScreen
import com.gavin.week7home.soal2.view.MusicApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme(colorScheme = darkColorScheme()) {

                // ===== PILIH SOAL: comment / uncomment salah satu =====

                // Soal 1 - Pokemon Card
                PokemonScreen()

                // Soal 2 - Music Artist (TheAudioDB)
                // MusicApp()
            }
        }
    }
}

