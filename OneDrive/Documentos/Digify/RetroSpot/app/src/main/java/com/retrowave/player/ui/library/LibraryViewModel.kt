package com.retrowave.player.ui.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.retrowave.player.data.repository.MusicRepository
import com.retrowave.player.data.repository.PlaylistRepository
import com.retrowave.player.domain.model.Album
import com.retrowave.player.domain.model.Artist
import com.retrowave.player.domain.model.Playlist
import com.retrowave.player.domain.model.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LibraryUiState(
    val songs: List<Song> = emptyList(),
    val albums: List<Album> = emptyList(),
    val artists: List<Artist> = emptyList(),
    val playlists: List<Playlist> = emptyList(),
    val isLoading: Boolean = true,
    val selectedTabIndex: Int = 0
)

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
    private val playlistRepository: PlaylistRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LibraryUiState())
    val uiState: StateFlow<LibraryUiState> = _uiState.asStateFlow()

    fun setSelectedTab(index: Int) {
        _uiState.value = _uiState.value.copy(selectedTabIndex = index)
    }

    fun deletePlaylist(playlistId: Long) {
        viewModelScope.launch {
            playlistRepository.deleteById(playlistId)
        }
    }

    fun deleteSong(songId: Long) {
        viewModelScope.launch {
            musicRepository.deleteSongById(songId)
        }
    }

    fun renamePlaylist(playlistId: Long, newName: String) {
        viewModelScope.launch {
            playlistRepository.rename(playlistId, newName)
        }
    }

    fun loadData() {
        viewModelScope.launch {
            musicRepository.getAllSongs().collect { songs ->
                _uiState.value = _uiState.value.copy(songs = songs, isLoading = false)
            }
        }
        viewModelScope.launch {
            musicRepository.getAlbums().collect { albums ->
                _uiState.value = _uiState.value.copy(albums = albums)
            }
        }
        viewModelScope.launch {
            musicRepository.getArtists().collect { artists ->
                _uiState.value = _uiState.value.copy(artists = artists)
            }
        }
        viewModelScope.launch {
            playlistRepository.getAll().collect { playlists ->
                _uiState.value = _uiState.value.copy(playlists = playlists)
            }
        }
    }
}
