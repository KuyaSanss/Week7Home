package com.gavin.week7home.soal2.view

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// Navigasi Soal 2: halaman artis -> halaman detail album
@Composable
fun MusicApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "artist") {

        composable("artist") {
            ArtistScreen(
                onAlbumClick = { albumId -> navController.navigate("album/$albumId") }
            )
        }

        composable("album/{albumId}") { backStackEntry ->
            val albumId = backStackEntry.arguments?.getString("albumId") ?: ""
            AlbumDetailScreen(
                albumId = albumId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
