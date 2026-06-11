package com.retrowave.player.ui.playlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.retrowave.player.data.repository.MusicRepository
import com.retrowave.player.data.repository.PlaylistRepository
import com.retrowave.player.domain.model.Playlist
import com.retrowave.player.domain.model.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlaylistDetailUiState(
    val playlist: Playlist? = null,
    val songs: List<Song> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class PlaylistDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val playlistRepository: PlaylistRepository,
    private val musicRepository: MusicRepository
) : ViewModel() {

    private val playlistId: Long = savedStateHandle.get<String>("playlistId")?.toLongOrNull() ?: 0L

    private val _uiState = MutableStateFlow(PlaylistDetailUiState())
    val uiState: StateFlow<PlaylistDetailUiState> = _uiState.asStateFlow()

    init {
        loadPlaylist()
    }

    fun loadPlaylist() {
        viewModelScope.launch {
            val playlist = playlistRepository.getPlaylistWithSongs(playlistId)
            val songs = if (playlist != null) {
                musicRepository.getSongsByIds(playlist.songIds)
            } else {
                emptyList()
            }
            _uiState.value = PlaylistDetailUiState(
                playlist = playlist,
                songs = songs,
                isLoading = false
            )
        }
    }
}
