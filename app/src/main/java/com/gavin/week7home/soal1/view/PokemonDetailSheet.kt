package com.gavin.week7home.soal1.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.gavin.week7home.soal1.model.PokemonCard

// Halaman detail kartu (muncul dari bawah / bottom sheet)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailSheet(
    card: PokemonCard,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = PokeColors.Background
    ) {
        PokemonDetailContent(card = card, onDismiss = onDismiss)
    }
}

// Isi detail kartu (dipisah supaya kodenya lebih rapi)
@Composable
fun PokemonDetailContent(
    card: PokemonCard,
    onDismiss: () -> Unit
) {
    // verticalScroll supaya isi detail bisa di-scroll
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
    ) {
        // Bagian atas: gambar kartu dengan background gradasi merah
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF5A1E24), PokeColors.Background)
                    )
                )
                .padding(vertical = 16.dp)
        ) {
            AsyncImage(
                model = card.imageCdnUrl800,
                contentDescription = card.name,
                modifier = Modifier
                    .align(Alignment.Center)
                    .height(260.dp)
            )
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 12.dp)
                    .size(36.dp)
                    .background(PokeColors.SurfaceLight, CircleShape)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Tutup", tint = Color.White)
            }
        }

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = card.name ?: "-",
                color = PokeColors.TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = card.setName ?: "-",
                color = PokeColors.TextSecondary,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Dua kotak harga
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                PriceBox(
                    title = "Market Price",
                    price = formatPrice(card.prices?.market),
                    color = PokeColors.Green,
                    icon = Icons.Default.Star,
                    modifier = Modifier.weight(1f)
                )
                PriceBox(
                    title = "Lowest Price",
                    price = formatPrice(card.prices?.low),
                    color = PokeColors.Yellow,
                    icon = Icons.Default.ShoppingCart,
                    modifier = Modifier.weight(1f)
                )
            }

            SectionTitle("Market Activity")
            InfoBox(
                listOf(
                    "Active Listings" to (card.prices?.listings?.toString() ?: "-"),
                    "Verified Sellers" to (card.prices?.sellers?.toString() ?: "-"),
                    "Primary Printing" to (card.prices?.primaryPrinting ?: "-")
                )
            )

            SectionTitle("Card Specifications")
            InfoBox(
                listOf(
                    "Card Number" to (card.cardNumber ?: "-"),
                    "Rarity" to (card.rarity ?: "-"),
                    "Type" to (card.pokemonType ?: "-"),
                    "HP" to (card.hp ?: "-"),
                    "Illustrator" to (card.artist ?: "-")
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PokeColors.Red),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Done", fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun PriceBox(
    title: String,
    price: String,
    color: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(PokeColors.Surface, RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier
                    .background(color.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                    .padding(4.dp)
                    .size(14.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(title, color = PokeColors.TextSecondary, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(price, color = color, fontWeight = FontWeight.Bold, fontSize = 22.sp)
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        color = PokeColors.TextPrimary,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        modifier = Modifier.padding(top = 20.dp, bottom = 8.dp)
    )
}

// Kotak berisi baris "label ...... value"
@Composable
private fun InfoBox(rows: List<Pair<String, String>>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(PokeColors.Surface, RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        rows.forEach { (label, value) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(label, color = PokeColors.TextSecondary, fontSize = 13.sp)
                Text(value, color = PokeColors.TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}
