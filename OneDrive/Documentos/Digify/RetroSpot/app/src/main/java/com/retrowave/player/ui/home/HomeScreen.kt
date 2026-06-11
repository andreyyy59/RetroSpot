package com.retrowave.player.ui.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.retrowave.player.data.ai.models.AIRecommendation
import com.retrowave.player.domain.model.Song
import com.retrowave.player.ui.components.MarqueeText
import com.retrowave.player.ui.components.PixelText
import com.retrowave.player.ui.components.RetroCard
import com.retrowave.player.ui.components.chromeBorder
import com.retrowave.player.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onSongClick: (Long) -> Unit,
    onPlaylistClick: (AIRecommendation) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    var showSaveDialog by remember { mutableStateOf(false) }
    var selectedRec by remember { mutableStateOf<AIRecommendation?>(null) }

    if (showSaveDialog && selectedRec != null) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            shape = RoundedCornerShape(12.dp),
            title = {
                PixelText(
                    text = "GUARDAR PLAYLIST",
                    color = RetroCyan,
                    pixelSize = 3.dp
                )
            },
            text = {
                Column {
                    Text(
                        text = selectedRec!!.playlistName,
                        color = RetroSilver,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = selectedRec!!.description,
                        color = RetroDimGray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    if (selectedRec!!.mood.isNotBlank()) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Estado de ánimo: ${selectedRec!!.mood}",
                            color = RetroVfdGreen.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSaveDialog = false
                        onPlaylistClick(selectedRec!!)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RetroCyan.copy(alpha = 0.2f),
                        contentColor = RetroSilver
                    )
                ) {
                    Text("GUARDAR", letterSpacing = 1.sp)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSaveDialog = false }) {
                    Text("CANCELAR", color = RetroDimGray)
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(RetroDarkerBg)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val lineSpacingPx = 4.dp.toPx()
            var y = lineSpacingPx
            while (y < size.height) {
                drawLine(
                    color = Color.Black.copy(alpha = 0.06f),
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1f
                )
                y += lineSpacingPx
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 8.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                PixelText(
                    text = "RETROSPOT",
                    color = RetroVfdGreen,
                    pixelSize = 5.dp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            item {
                if (uiState.isScanning) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = RetroCyan,
                        trackColor = RetroMetallicBlue
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .chromeBorder(cornerRadius = 8.dp)
                            .clickable { viewModel.scanMusic() }
                            .padding(horizontal = 20.dp, vertical = 12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                tint = RetroVfdGreen,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Escanear música local",
                                style = MaterialTheme.typography.labelLarge,
                                color = RetroSilver
                            )
                        }
                    }
                }
            }

            if (uiState.recommendations.isNotEmpty()) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        PixelText(
                            text = "RECOMENDADO",
                            color = RetroCyan,
                            pixelSize = 3.5.dp,
                            modifier = Modifier.weight(1f)
                        )
                        val infiniteTransition = rememberInfiniteTransition(label = "spin")
                        val rotation by infiniteTransition.animateFloat(
                            initialValue = 0f,
                            targetValue = 360f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(1000, easing = LinearEasing),
                                repeatMode = RepeatMode.Restart
                            ),
                            label = "rotation"
                        )
                        IconButton(
                            onClick = { viewModel.refreshRecommendations() },
                            enabled = !uiState.isLoading
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Regenerar",
                                tint = RetroVfdGreen,
                                modifier = Modifier
                                    .size(22.dp)
                                    .then(
                                        if (uiState.isLoading) Modifier.rotate(rotation)
                                        else Modifier
                                    )
                            )
                        }
                    }
                }
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.recommendations) { rec ->
                            RetroCard(
                                title = rec.playlistName,
                                subtitle = rec.description,
                                imageUri = null,
                                onClick = {
                                    selectedRec = rec
                                    showSaveDialog = true
                                }
                            )
                        }
                    }
                }
            }

            if (uiState.songs.isNotEmpty()) {
                item {
                    PixelText(
                        text = "CANCIONES",
                        color = RetroCyan,
                        pixelSize = 3.5.dp
                    )
                }
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.songs.take(10)) { song ->
                            RetroCard(
                                title = song.title,
                                subtitle = song.artist,
                                imageUri = song.albumArtUri,
                                onClick = { onSongClick(song.id) }
                            )
                        }
                    }
                }
            }

            if (uiState.genres.isNotEmpty()) {
                item {
                    PixelText(
                        text = "GENEROS",
                        color = RetroCyan,
                        pixelSize = 3.5.dp
                    )
                }
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.genres) { genre ->
                            RetroCard(
                                title = genre,
                                subtitle = "Género",
                                imageUri = null,
                                onClick = { }
                            )
                        }
                    }
                }
            }

            if (uiState.songs.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    PixelText(
                        text = "TODAS",
                        color = RetroCyan,
                        pixelSize = 3.5.dp
                    )
                }
                items(uiState.songs) { song ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                            .clickable { onSongClick(song.id) }
                            .padding(vertical = 10.dp, horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MarqueeText(
                            text = song.title,
                            color = RetroSilver,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = song.artist,
                            color = RetroDimGray,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
