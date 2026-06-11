package com.retrowave.player.ui.search

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.retrowave.player.domain.model.Song
import com.retrowave.player.data.download.SpotifyDownloadUiState
import com.retrowave.player.data.download.SpotifySongData
import com.retrowave.player.ui.components.MarqueeText
import com.retrowave.player.ui.components.PixelText
import com.retrowave.player.ui.components.RetroCard
import com.retrowave.player.ui.components.chromeBorder
import com.retrowave.player.ui.theme.*
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onSongClick: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val spotifyState by viewModel.spotifyState.collectAsState()
    var query by remember { mutableStateOf("") }
    val isSpotifyTrackUrl = viewModel.isSpotifyTrackUrl(query)
    val isAnySpotifyUrl = viewModel.isSpotifyUrl(query) && !isSpotifyTrackUrl

    LaunchedEffect(Unit) {
        viewModel.loadGenres()
    }

            LaunchedEffect(query) {
        if (query.isEmpty()) {
            viewModel.resetSpotifyState()
            viewModel.resetSpotifySearch()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(RetroDarkerBg)
    ) {
        Canvas(Modifier.fillMaxSize().matchParentSize()) {
            var y = 0f
            while (y < size.height) {
                drawLine(Color.Black.copy(alpha = 0.06f),
                    androidx.compose.ui.geometry.Offset(0f, y),
                    androidx.compose.ui.geometry.Offset(size.width, y), 1f)
                y += 4.dp.toPx()
            }
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = query,
                onValueChange = { newQuery ->
                    query = newQuery
                    if (viewModel.isSpotifyTrackUrl(newQuery)) {
                        viewModel.fetchSpotifySongInfo(newQuery)
                    } else if (viewModel.isSpotifyUrl(newQuery)) {
                        viewModel.resetSpotifyState()
                    } else if (newQuery.isNotBlank()) {
                        viewModel.search(newQuery)
                        viewModel.resetSpotifySearch()
                    } else {
                        viewModel.search("")
                        viewModel.resetSpotifySearch()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .chromeBorder(cornerRadius = 8.dp),
                placeholder = {
                    Text("BUSCAR O PEGAR LINK DE SPOTIFY...", color = RetroDimGray, style = MaterialTheme.typography.bodyMedium)
                },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = RetroVfdGreen)
                },
                textStyle = MaterialTheme.typography.titleMedium.copy(color = RetroVfdGreen),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = RetroVfdGreen,
                    focusedContainerColor = RetroPanelDark,
                    unfocusedContainerColor = RetroPanelDark
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Spotify download from URL or Spotify API search result
            if (spotifyState.songInfo != null || (isSpotifyTrackUrl && spotifyState.isLoading)) {
                SpotifyDownloadSection(
                    state = spotifyState,
                    onDownload = { data -> viewModel.downloadSong(data) },
                    onReset = { query = ""; viewModel.resetSpotifyState(); viewModel.resetSpotifySearch() }
                )
            } else if (isAnySpotifyUrl) {
                SpotifyUrlNotSupported(
                    onReset = { query = ""; viewModel.resetSpotifyState(); viewModel.resetSpotifySearch() }
                )
            } else if (uiState.isSpotifySearching) {
                SpotifySearchLoading()
            } else if (uiState.spotifySearchError != null) {
                SpotifySearchError(
                    error = uiState.spotifySearchError!!,
                    onRetry = { viewModel.searchSpotify(query) },
                    onReset = { query = ""; viewModel.resetSpotifySearch() }
                )
            } else if (query.isNotEmpty() && uiState.searchResults.isNotEmpty()) {
                LocalSearchResults(
                    results = uiState.searchResults,
                    onSongClick = onSongClick,
                    onSpotifySearch = { viewModel.searchSpotify(query) },
                    isSpotifySearching = uiState.isSpotifySearching
                )
            } else if (query.isEmpty()) {
                GenreSection(
                    genres = uiState.genres,
                    onGenreClick = { query = it; viewModel.search(it) }
                )
            } else {
                // Texto sin resultados locales: ofrecer búsqueda en Spotify directamente
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "SIN RESULTADOS LOCALES",
                            color = RetroDimGray.copy(alpha = 0.5f),
                            style = MaterialTheme.typography.headlineSmall,
                            letterSpacing = 4.sp
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Prueba a buscarlo en Spotify",
                            color = RetroDimGray.copy(alpha = 0.4f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(Modifier.height(20.dp))
                        Button(
                            onClick = { viewModel.searchSpotify(query) },
                            enabled = !uiState.isSpotifySearching,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = RetroCyan.copy(alpha = 0.2f),
                                contentColor = RetroSilver
                            ),
                            modifier = Modifier.chromeBorder(cornerRadius = 8.dp)
                        ) {
                            Text("BUSCAR EN SPOTIFY", letterSpacing = 2.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LocalSearchResults(
    results: List<Song>,
    onSongClick: (Long) -> Unit,
    onSpotifySearch: () -> Unit,
    isSpotifySearching: Boolean
) {
    Column(modifier = Modifier.fillMaxSize()) {
        PixelText(
            text = "RESULTADOS LOCALES",
            color = RetroVfdGreen,
            pixelSize = 3.5.dp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(results) { song ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSongClick(song.id) }
                        .padding(vertical = 6.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .chromeBorder(cornerRadius = 6.dp)
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(RetroMetallicBlue.copy(alpha = 0.4f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = song.title.take(1).uppercase(),
                            color = RetroVfdGreen,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        MarqueeText(
                            text = song.title,
                            color = RetroSilver,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = song.artist,
                            color = RetroDimGray,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            item {
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = onSpotifySearch,
                    enabled = !isSpotifySearching,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .chromeBorder(cornerRadius = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RetroCyan.copy(alpha = 0.15f),
                        contentColor = RetroSilver
                    )
                ) {
                    if (isSpotifySearching) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = RetroCyan,
                            strokeWidth = 2.dp
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                    Text("BUSCAR EN SPOTIFY", letterSpacing = 2.sp)
                }
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun GenreSection(
    genres: List<String>,
    onGenreClick: (String) -> Unit
) {
    PixelText(
        text = "GENEROS",
        color = RetroVfdGreen,
        pixelSize = 3.5.dp,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
    )

    if (genres.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "ESCANEA MÚSICA LOCAL PARA VER GÉNEROS",
                color = RetroDimGray.copy(alpha = 0.5f),
                style = MaterialTheme.typography.bodyMedium,
                letterSpacing = 2.sp
            )
        }
        return
    }

    val chunked = genres.chunked(3)
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(chunked) { rowGenres ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                rowGenres.forEach { genre ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .chromeBorder(cornerRadius = 8.dp)
                            .background(RetroPanelDark)
                            .clickable { onGenreClick(genre) },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            PixelText(
                                text = genre.uppercase().take(4),
                                color = RetroCyan,
                                pixelSize = 3.dp
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = genre.uppercase(),
                                color = RetroSilver,
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SpotifyDownloadSection(
    state: SpotifyDownloadUiState,
    onDownload: (SpotifySongData) -> Unit,
    onReset: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        PixelText(
            text = "DESCARGAR DE SPOTIFY",
            color = RetroVfdGreen,
            pixelSize = 3.5.dp,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(
                            color = RetroCyan,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(
                            "OBTENIENDO INFORMACIÓN...",
                            color = RetroCyan,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            state.error != null && state.songInfo == null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "ERROR",
                        color = RetroRedAccent,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = state.error,
                            color = RetroDimGray,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(16.dp))
                        TextButton(onClick = onReset) {
                            Text("LIMPIAR", color = RetroCyan)
                        }
                    }
                }
            }

            state.songInfo != null -> {
                val data = state.songInfo
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .chromeBorder(cornerRadius = 8.dp)
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = data.cover,
                            contentDescription = null,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .chromeBorder(cornerRadius = 6.dp),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            MarqueeText(
                                text = data.title,
                                color = RetroSilver,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(4.dp))
                            MarqueeText(
                                text = data.artist,
                                color = RetroVfdGreen,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(2.dp))
                            MarqueeText(
                                text = data.album,
                                color = RetroDimGray,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                if (state.isDownloading) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "DESCARGANDO...",
                            color = RetroCyan,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { state.downloadProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .chromeBorder(cornerRadius = 4.dp),
                            color = RetroCyan,
                            trackColor = RetroPanelDark
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "${(state.downloadProgress * 100).toInt()}%",
                            color = RetroDimGray,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else if (state.downloadedSongId != null) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "¡DESCARGADA!",
                            color = RetroVfdGreen,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(Modifier.height(12.dp))
                        TextButton(onClick = onReset) {
                            Text("DESCARGAR OTRA", color = RetroCyan)
                        }
                    }
                } else {
                    Button(
                        onClick = { onDownload(data) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .chromeBorder(cornerRadius = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RetroCyan.copy(alpha = 0.2f),
                            contentColor = RetroSilver
                        )
                    ) {
                        Text(
                            "DESCARGAR MP3",
                            style = MaterialTheme.typography.titleMedium,
                            letterSpacing = 2.sp
                        )
                    }
                }
            }
        }

        if (state.error != null && state.songInfo != null) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = state.error,
                color = RetroRedAccent,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun SpotifySearchLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = RetroCyan, modifier = Modifier.size(48.dp))
            Spacer(Modifier.height(12.dp))
            Text(
                "BUSCANDO EN SPOTIFY...",
                color = RetroCyan,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun SpotifySearchError(
    error: String,
    onRetry: () -> Unit,
    onReset: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = error,
                color = RetroRedAccent,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Pega el enlace de Spotify manualmente",
                color = RetroDimGray,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TextButton(onClick = onRetry) { Text("REINTENTAR", color = RetroCyan) }
                TextButton(onClick = onReset) { Text("LIMPIAR", color = RetroDimGray) }
            }
        }
    }
}

@Composable
private fun SpotifyUrlNotSupported(
    onReset: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = "SOLO CANCIONES",
                color = RetroRedAccent,
                style = MaterialTheme.typography.headlineMedium,
                letterSpacing = 2.sp
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Pega el link de una canción individual de Spotify\n(ej: open.spotify.com/track/...)",
                color = RetroDimGray,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(24.dp))
            TextButton(onClick = onReset) {
                Text("LIMPIAR", color = RetroCyan)
            }
        }
    }
}
