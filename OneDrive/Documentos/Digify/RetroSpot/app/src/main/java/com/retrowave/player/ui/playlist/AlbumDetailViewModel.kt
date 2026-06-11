package com.retrowave.player.ui.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.retrowave.player.data.repository.MusicRepository
import com.retrowave.player.domain.model.Album
import com.retrowave.player.domain.model.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AlbumDetailUiState(
    val album: Album? = null,
    val songs: List<Song> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class AlbumDetailViewModel @Inject constructor(
    private val musicRepository: MusicRepository
) : ViewModel() {

    private var albumName: String = ""

    private val _uiState = MutableStateFlow(AlbumDetailUiState())
    val uiState: StateFlow<AlbumDetailUiState> = _uiState.asStateFlow()

    fun setAlbumName(name: String) {
        albumName = name
        loadAlbum()
    }

    private fun loadAlbum() {
        viewModelScope.launch {
            musicRepository.getSongsByAlbum(albumName).collect { songs ->
                val album = if (songs.isNotEmpty()) {
                    val first = songs.first()
                    Album(
                        id = first.id,
                        title = albumName,
                        artist = first.artist,
                        songCount = songs.size,
                        albumArtUri = first.albumArtUri,
                        year = first.year
                    )
                } else null
                _uiState.value = AlbumDetailUiState(
                    album = album,
                    songs = songs,
                    isLoading = false
                )
            }
        }
    }
}
