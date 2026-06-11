package com.retrowave.player.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.net.URLEncoder
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.retrowave.player.ui.components.chromeBorder
import com.retrowave.player.ui.home.HomeScreen
import com.retrowave.player.ui.home.HomeViewModel
import com.retrowave.player.ui.library.LibraryScreen
import com.retrowave.player.ui.library.LibraryViewModel
import com.retrowave.player.ui.playlist.AlbumDetailScreen
import com.retrowave.player.ui.playlist.AlbumDetailViewModel
import com.retrowave.player.ui.playlist.PlaylistDetailScreen
import com.retrowave.player.ui.playlist.PlaylistDetailViewModel
import com.retrowave.player.ui.player.MiniPlayer
import com.retrowave.player.ui.player.PlayerScreen
import com.retrowave.player.ui.player.PlayerViewModel
import com.retrowave.player.ui.search.SearchScreen
import com.retrowave.player.ui.search.SearchViewModel
import com.retrowave.player.ui.theme.*

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object Home : Screen("home", "HOME", Icons.Default.Home)
    data object Search : Screen("search", "BUSCAR", Icons.Default.Search)
    data object Library : Screen("library", "BIBLIOTECA", Icons.Default.LibraryMusic)
    data object Player : Screen("player/{songId}", "PLAYER", Icons.Default.Home) {
        fun createRoute(songId: Long) = "player/$songId"
    }
    data object PlaylistDetail : Screen("playlist/{playlistId}", "PLAYLIST", Icons.AutoMirrored.Filled.QueueMusic) {
        fun createRoute(playlistId: Long) = "playlist/$playlistId"
    }
    data object AlbumDetail : Screen("album/{albumName}", "ALBUM", Icons.Default.Album) {
        fun createRoute(albumName: String) = "album/${URLEncoder.encode(albumName, "UTF-8")}"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RetroSpotNavigation() {
    val navController = rememberNavController()
    val playerViewModel: PlayerViewModel = hiltViewModel()
    val playerUiState by playerViewModel.uiState.collectAsState()

    val bottomItems = listOf(Screen.Home, Screen.Search, Screen.Library)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val isPlayerRoute = currentDestination?.route?.startsWith("player") == true

    Scaffold(
        bottomBar = {
            Column {
                if (!isPlayerRoute) {
                    MiniPlayer(
                        song = playerUiState.currentSong,
                        isPlaying = playerUiState.isPlaying,
                        isVisible = playerUiState.miniPlayerVisible,
                        currentPosition = playerUiState.currentPosition,
                        duration = playerUiState.duration,
                        onPlayPause = { playerViewModel.togglePlayPause() },
                        onExpand = {
                            playerUiState.currentSong?.let {
                                navController.navigate(Screen.Player.createRoute(it.id))
                            }
                        },
                        onDismiss = { playerViewModel.hideMiniPlayer() }
                    )
                }

                if (!isPlayerRoute) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(RetroPanelDark)
                            .chromeBorder(cornerRadius = 12.dp)
                    ) {
                        NavigationBar(
                            containerColor = RetroPanelDark,
                            tonalElevation = 0.dp,
                            windowInsets = WindowInsets(0, 0, 0, 0)
                        ) {
                            bottomItems.forEach { screen ->
                                val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

                                NavigationBarItem(
                                    icon = {
                                        Icon(
                                            imageVector = screen.icon,
                                            contentDescription = screen.title,
                                            modifier = Modifier.size(26.dp)
                                        )
                                    },
                                    label = {
                                        Text(
                                            screen.title,
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    },
                                    selected = selected,
                                    onClick = {
                                        if (selected) return@NavigationBarItem
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = RetroVfdGreen,
                                        selectedTextColor = RetroVfdGreen,
                                        unselectedIconColor = RetroDimGray,
                                        unselectedTextColor = RetroDimGray,
                                        indicatorColor = RetroMetallicBlue.copy(alpha = 0.3f)
                                    )
                                )
                            }
                        }
                    }
                }
            }
        },
        containerColor = RetroDarkerBg
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                val viewModel: HomeViewModel = hiltViewModel()
                val scope = rememberCoroutineScope()
                HomeScreen(
                    viewModel = viewModel,
                    onSongClick = { songId ->
                        navController.navigate(Screen.Player.createRoute(songId))
                    },
                    onPlaylistClick = { rec ->
                        scope.launch {
                            try {
                                viewModel.createPlaylistFromRecommendation(rec) { playlistId ->
                                    navController.navigate(Screen.PlaylistDetail.createRoute(playlistId))
                                }
                            } catch (e: Exception) {
                                android.util.Log.e("RetroSpot", "Error creating playlist from recommendation", e)
                            }
                        }
                    }
                )
            }
            composable(Screen.Search.route) {
                val viewModel: SearchViewModel = hiltViewModel()
                SearchScreen(
                    viewModel = viewModel,
                    onSongClick = { songId ->
                        navController.navigate(Screen.Player.createRoute(songId))
                    }
                )
            }
            composable(Screen.Library.route) {
                val viewModel: LibraryViewModel = hiltViewModel()
                LibraryScreen(
                    viewModel = viewModel,
                    onSongClick = { songId ->
                        navController.navigate(Screen.Player.createRoute(songId))
                    },
                    onPlaylistClick = { playlistId ->
                        navController.navigate(Screen.PlaylistDetail.createRoute(playlistId))
                    },
                    onAlbumClick = { albumName ->
                        navController.navigate(Screen.AlbumDetail.createRoute(albumName))
                    }
                )
            }
            composable(Screen.AlbumDetail.route) { backStackEntry ->
                val albumName = backStackEntry.arguments?.getString("albumName") ?: ""
                val decodedAlbumName = URLDecoder.decode(albumName, "UTF-8")
                val viewModel: AlbumDetailViewModel = hiltViewModel()
                LaunchedEffect(decodedAlbumName) {
                    viewModel.setAlbumName(decodedAlbumName)
                }
                AlbumDetailScreen(
                    viewModel = viewModel,
                    onSongClick = { songId ->
                        navController.navigate(Screen.Player.createRoute(songId))
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.PlaylistDetail.route) { backStackEntry ->
                val playlistId = backStackEntry.arguments?.getString("playlistId")?.toLongOrNull() ?: 0L
                val viewModel: PlaylistDetailViewModel = hiltViewModel()
                PlaylistDetailScreen(
                    viewModel = viewModel,
                    onSongClick = { songId ->
                        navController.navigate(Screen.Player.createRoute(songId))
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.Player.route) { backStackEntry ->
                val songId = backStackEntry.arguments?.getString("songId")?.toLongOrNull() ?: 0L
                PlayerScreen(
                    viewModel = playerViewModel,
                    songId = songId,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
