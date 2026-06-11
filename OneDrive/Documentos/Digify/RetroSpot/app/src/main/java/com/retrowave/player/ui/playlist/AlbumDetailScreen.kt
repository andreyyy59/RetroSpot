package com.retrowave.player.ui.playlist

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.retrowave.player.domain.model.Song
import com.retrowave.player.ui.components.MarqueeText
import com.retrowave.player.ui.components.PixelText
import com.retrowave.player.ui.components.chromeBorder
import com.retrowave.player.ui.components.songColor
import com.retrowave.player.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetailScreen(
    viewModel: AlbumDetailViewModel,
    onSongClick: (Long) -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(RetroDarkerBg)
    ) {
        Canvas(Modifier.fillMaxSize().matchParentSize()) {
            var y = 0f
            while (y < size.height) {
                drawLine(Color.Black.copy(alpha = 0.06f),
                    Offset(0f, y),
                    Offset(size.width, y), 1f)
                y += 4.dp.toPx()
            }
        }

        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Column {
                        PixelText(
                            text = uiState.album?.title ?: "ALBUM",
                            color = RetroVfdGreen,
                            pixelSize = 5.dp
                        )
                        uiState.album?.let { album ->
                            Text(
                                text = "${album.artist} • ${album.songCount} canciones",
                                color = RetroDimGray,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver",
                            tint = RetroVfdGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = RetroDarkerBg)
            )

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = RetroVfdGreen)
                }
            } else if (uiState.songs.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("SIN CANCIONES", color = RetroDimGray.copy(alpha = 0.4f),
                        style = MaterialTheme.typography.headlineLarge, letterSpacing = 3.sp)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(uiState.songs) { song ->
                        SongRow(song = song, onClick = { onSongClick(song.id) })
                    }
                    item { Spacer(Modifier.height(12.dp)) }
                }
            }
        }
    }
}

@Composable
private fun SongRow(song: Song, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .chromeBorder(cornerRadius = 6.dp)
            .clickable(onClick = onClick)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(songColor(song.title).copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.MusicNote, contentDescription = null,
                tint = songColor(song.title), modifier = Modifier.size(22.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            MarqueeText(
                text = song.title,
                color = RetroSilver,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "${song.artist} • ${song.album}",
                color = RetroDimGray,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
