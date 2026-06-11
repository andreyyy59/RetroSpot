package com.retrowave.player.ui.library

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.retrowave.player.domain.model.Album
import com.retrowave.player.domain.model.Artist
import com.retrowave.player.domain.model.Playlist
import com.retrowave.player.domain.model.Song
import com.retrowave.player.ui.components.AsciiArtPlaceholder
import com.retrowave.player.ui.components.MarqueeText
import com.retrowave.player.ui.components.PixelText
import com.retrowave.player.ui.components.RetroCard
import com.retrowave.player.ui.components.chromeBorder
import com.retrowave.player.ui.components.songColor
import com.retrowave.player.ui.theme.*

enum class LibraryTab(val title: String, val icon: ImageVector) {
    SONGS("Canciones", Icons.Default.MusicNote),
    ALBUMS("Álbumes", Icons.Default.Album),
    ARTISTS("Artistas", Icons.Default.Person),
    PLAYLISTS("Playlists", Icons.AutoMirrored.Filled.QueueMusic)
}

@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel,
    onSongClick: (Long) -> Unit,
    onPlaylistClick: (Long) -> Unit = {},
    onAlbumClick: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    val selectedTab = LibraryTab.entries.getOrElse(uiState.selectedTabIndex) { LibraryTab.SONGS }

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

        Column(modifier = Modifier.fillMaxSize()) {
            PixelText(
                text = "BIBLIOTECA",
                color = RetroVfdGreen,
                pixelSize = 5.dp,
                modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 8.dp)
            )

            TabRow(
                selectedTabIndex = LibraryTab.entries.indexOf(selectedTab),
                containerColor = RetroDarkerBg,
                contentColor = RetroCyan,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[LibraryTab.entries.indexOf(selectedTab)]),
                        color = RetroVfdGreen
                    )
                },
                divider = { HorizontalDivider(color = RetroMetallicBlue.copy(alpha = 0.3f)) }
            ) {
                LibraryTab.entries.forEach { tab ->
                    Tab(
                        selected = selectedTab == tab,
                        onClick = { viewModel.setSelectedTab(LibraryTab.entries.indexOf(tab)) },
                        text = {
                            Text(
                                text = tab.title,
                                color = if (selectedTab == tab) RetroVfdGreen else RetroDimGray,
                                style = MaterialTheme.typography.labelLarge
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.title,
                                tint = if (selectedTab == tab) RetroVfdGreen else RetroDimGray,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    )
                }
            }

            when (selectedTab) {
                LibraryTab.SONGS -> SongsTab(
                    uiState.songs,
                    onSongClick,
                    onDelete = { viewModel.deleteSong(it) }
                )
                LibraryTab.ALBUMS -> AlbumsTab(uiState.albums, onAlbumClick)
                LibraryTab.ARTISTS -> ArtistsTab(uiState.artists)
                LibraryTab.PLAYLISTS -> PlaylistsTab(
                    uiState.playlists,
                    onPlaylistClick,
                    onDelete = { viewModel.deletePlaylist(it) },
                    onRename = { id, name -> viewModel.renamePlaylist(id, name) }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SongsTab(
    songs: List<Song>,
    onSongClick: (Long) -> Unit,
    onDelete: (Long) -> Unit = {}
) {
    var showDeleteConfirm by remember { mutableStateOf<Song?>(null) }

    showDeleteConfirm?.let { song ->
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = null },
            shape = RoundedCornerShape(12.dp),
            title = {
                PixelText("BORRAR CANCIÓN", color = RetroCyan, pixelSize = 3.dp)
            },
            text = {
                Text(
                    "¿Eliminar \"${song.title}\" de la biblioteca?",
                    color = RetroDimGray,
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete(song.id)
                        showDeleteConfirm = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RetroCyan.copy(alpha = 0.2f),
                        contentColor = RetroSilver
                    )
                ) { Text("BORRAR") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = null }) {
                    Text("CANCELAR", color = RetroDimGray)
                }
            }
        )
    }

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(songs) { song ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .chromeBorder(cornerRadius = 6.dp)
                    .combinedClickable(
                        onClick = { onSongClick(song.id) },
                        onLongClick = { showDeleteConfirm = song }
                    )
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
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth()
                    )
                    MarqueeText(
                        text = "${song.artist} • ${song.album}",
                        color = RetroDimGray,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        item { Spacer(Modifier.height(12.dp)) }
    }
}

@Composable
private fun AlbumsTab(albums: List<Album>, onAlbumClick: (String) -> Unit = {}) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(albums) { album ->
            RetroCard(
                title = album.title,
                subtitle = "${album.artist} • ${album.songCount} canciones",
                imageUri = album.albumArtUri,
                onClick = { onAlbumClick(album.title) },
                asciiArtSeed = if (album.albumArtUri.isNullOrBlank()) album.title else null
            )
        }
        item { Spacer(Modifier.height(12.dp)) }
    }
}

@Composable
private fun ArtistsTab(artists: List<Artist>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(artists) { artist ->
            RetroCard(
                title = artist.name,
                subtitle = "${artist.albumCount} álbumes • ${artist.songCount} canciones",
                imageUri = null
            )
        }
        item { Spacer(Modifier.height(12.dp)) }
    }
}

@Composable
private fun PlaylistsTab(
    playlists: List<Playlist>,
    onPlaylistClick: (Long) -> Unit = {},
    onDelete: (Long) -> Unit = {},
    onRename: (Long, String) -> Unit = { _, _ -> }
) {
    var showMenuFor by remember { mutableStateOf<Playlist?>(null) }
    var showDeleteConfirm by remember { mutableStateOf<Playlist?>(null) }
    var showRenameDialog by remember { mutableStateOf<Playlist?>(null) }
    var renameText by remember { mutableStateOf(TextFieldValue("")) }

    // Delete confirmation
    showDeleteConfirm?.let { playlist ->
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = null },
            shape = RoundedCornerShape(12.dp),
            title = {
                PixelText("BORRAR PLAYLIST", color = RetroCyan, pixelSize = 3.dp)
            },
            text = {
                Text(
                    "¿Eliminar \"${playlist.name}\" para siempre?",
                    color = RetroDimGray,
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete(playlist.id)
                        showDeleteConfirm = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RetroCyan.copy(alpha = 0.2f),
                        contentColor = RetroSilver
                    )
                ) { Text("BORRAR") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = null }) {
                    Text("CANCELAR", color = RetroDimGray)
                }
            }
        )
    }

    // Rename dialog
    showRenameDialog?.let { playlist ->
        AlertDialog(
            onDismissRequest = { showRenameDialog = null },
            shape = RoundedCornerShape(12.dp),
            title = {
                PixelText("RENOMBRAR", color = RetroCyan, pixelSize = 3.dp)
            },
            text = {
                OutlinedTextField(
                    value = renameText,
                    onValueChange = { renameText = it },
                    singleLine = true,
                    label = { Text("Nuevo nombre", color = RetroDimGray) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RetroCyan,
                        unfocusedBorderColor = RetroDimGray,
                        focusedTextColor = RetroSilver,
                        unfocusedTextColor = RetroSilver,
                        cursorColor = RetroVfdGreen
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (renameText.text.isNotBlank()) {
                                onRename(playlist.id, renameText.text.trim())
                                showRenameDialog = null
                            }
                        }
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (renameText.text.isNotBlank()) {
                            onRename(playlist.id, renameText.text.trim())
                            showRenameDialog = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RetroCyan.copy(alpha = 0.2f),
                        contentColor = RetroSilver
                    )
                ) { Text("GUARDAR") }
            },
            dismissButton = {
                TextButton(onClick = { showRenameDialog = null }) {
                    Text("CANCELAR", color = RetroDimGray)
                }
            }
        )
    }

    if (playlists.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "SIN PLAYLISTS",
                color = RetroDimGray.copy(alpha = 0.4f),
                style = MaterialTheme.typography.headlineLarge,
                letterSpacing = 3.sp
            )
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(playlists) { playlist ->
                RetroCard(
                    title = playlist.name,
                    subtitle = playlist.description ?: "PLAYLIST",
                    imageUri = null,
                    onClick = { onPlaylistClick(playlist.id) },
                    onLongClick = { showMenuFor = playlist }
                )
            }
            item { Spacer(Modifier.height(12.dp)) }
        }
    }
    // Action dialog (replaces DropdownMenu to keep layout clean)
    showMenuFor?.let { playlist ->
        AlertDialog(
            onDismissRequest = { showMenuFor = null },
            shape = RoundedCornerShape(12.dp),
            title = {
                PixelText(playlist.name.uppercase(), color = RetroCyan, pixelSize = 3.dp)
            },
            text = {
                Column {
                    TextButton(
                        onClick = {
                            showMenuFor = null
                            renameText = TextFieldValue(text = playlist.name, selection = TextRange(playlist.name.length))
                            showRenameDialog = playlist
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("RENOMBRAR", color = RetroSilver, modifier = Modifier.fillMaxWidth()) }
                    Spacer(Modifier.height(4.dp))
                    TextButton(
                        onClick = {
                            showMenuFor = null
                            showDeleteConfirm = playlist
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("BORRAR", color = RetroRedAccent, modifier = Modifier.fillMaxWidth()) }
                }
            },
            confirmButton = {
                TextButton(onClick = { showMenuFor = null }) { Text("CANCELAR", color = RetroDimGray) }
            }
        )
    }
}
