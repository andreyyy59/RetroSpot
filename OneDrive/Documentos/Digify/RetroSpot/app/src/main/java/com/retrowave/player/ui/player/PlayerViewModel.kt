package com.retrowave.player.ui.player

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.retrowave.player.data.local.player.MusicService
import com.retrowave.player.data.repository.MusicRepository
import com.retrowave.player.domain.model.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlayerUiState(
    val currentSong: Song? = null,
    val playlist: List<Song> = emptyList(),
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0,
    val duration: Long = 0,
    val isShuffled: Boolean = false,
    val repeatMode: Int = Player.REPEAT_MODE_OFF,
    val miniPlayerVisible: Boolean = false,
    val notificationDenied: Boolean = false
)

@HiltViewModel
class PlayerViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val musicRepository: MusicRepository,
    val player: ExoPlayer
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    init {
        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _uiState.value = _uiState.value.copy(isPlaying = isPlaying)
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    _uiState.value = _uiState.value.copy(duration = player.duration)
                }
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                val song = _uiState.value.playlist.find { it.id.toString() == mediaItem?.mediaId }
                if (song != null) {
                    _uiState.value = _uiState.value.copy(currentSong = song)
                }
            }

            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                _uiState.value = _uiState.value.copy(isShuffled = shuffleModeEnabled)
            }

            override fun onRepeatModeChanged(repeatMode: Int) {
                _uiState.value = _uiState.value.copy(repeatMode = repeatMode)
            }
        })

        viewModelScope.launch {
            while (true) {
                _uiState.value = _uiState.value.copy(
                    currentPosition = player.currentPosition,
                    isPlaying = player.isPlaying,
                    duration = player.duration
                )
                delay(250)
            }
        }
    }

    fun playSong(songId: Long) {
        viewModelScope.launch {
            try {
                val song = musicRepository.getSongById(songId) ?: return@launch
                val currentPlaylist = musicRepository.getAllSongs().first()

                if (player.currentMediaItem?.mediaId == songId.toString() && player.isPlaying) return@launch

                if (player.currentMediaItem?.mediaId == songId.toString()) {
                    player.playWhenReady = true
                    return@launch
                }

                _uiState.value = _uiState.value.copy(
                    currentSong = song,
                    playlist = currentPlaylist,
                    miniPlayerVisible = true
                )

                val mediaItems = currentPlaylist.map { s ->
                    MediaItem.Builder()
                        .setMediaId(s.id.toString())
                        .setUri(android.net.Uri.parse(s.dataUri))
                        .setMediaMetadata(
                            androidx.media3.common.MediaMetadata.Builder()
                                .setTitle(s.title)
                                .setArtist(s.artist)
                                .setAlbumTitle(s.album)
                                .setArtworkUri(if (s.albumArtUri != null) android.net.Uri.parse(s.albumArtUri) else null)
                                .build()
                        )
                        .build()
                }

                val currentIndex = currentPlaylist.indexOfFirst { it.id == songId }.coerceAtLeast(0)

                player.stop()
                player.setMediaItems(mediaItems, currentIndex, 0)
                player.prepare()
                player.playWhenReady = true

                startMusicService()
            } catch (e: Exception) {
                android.util.Log.e("RetroSpot", "playSong error", e)
            }
        }
    }

    fun togglePlayPause() {
        if (player.isPlaying) player.pause() else player.play()
    }

    fun seekTo(position: Long) {
        player.seekTo(position)
    }

    fun skipToNext() {
        player.seekToNext()
    }

    fun skipToPrevious() {
        player.seekToPrevious()
    }

    fun toggleShuffle() {
        player.shuffleModeEnabled = !player.shuffleModeEnabled
    }

    fun cycleRepeatMode() {
        player.repeatMode = when (player.repeatMode) {
            Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ONE
            Player.REPEAT_MODE_ONE -> Player.REPEAT_MODE_ALL
            else -> Player.REPEAT_MODE_OFF
        }
    }

    fun hideMiniPlayer() {
        player.stop()
        player.clearMediaItems()
        _uiState.value = _uiState.value.copy(
            miniPlayerVisible = false,
            currentSong = null,
            currentPosition = 0,
            duration = 0,
            notificationDenied = false
        )
        val intent = Intent(context, MusicService::class.java)
        context.stopService(intent)
    }

    fun showMiniPlayer() {
        _uiState.value = _uiState.value.copy(miniPlayerVisible = true)
    }

    fun onNotificationPermissionDenied() {
        _uiState.value = _uiState.value.copy(notificationDenied = true)
    }

    private fun startMusicService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                android.util.Log.w("RetroSpot", "POST_NOTIFICATIONS not granted, skipping service")
                _uiState.value = _uiState.value.copy(notificationDenied = true)
                return
            }
        }
        _uiState.value = _uiState.value.copy(notificationDenied = false)
        MusicService.setPlayer(player)
        val intent = Intent(context, MusicService::class.java)
        ContextCompat.startForegroundService(context, intent)
    }

    override fun onCleared() {
        // Don't stop/clear player — it's a singleton shared with MusicService
    }
}
