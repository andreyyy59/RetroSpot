package com.retrowave.player.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.retrowave.player.data.download.SpotifyDownloadRepository
import com.retrowave.player.data.download.SpotifyDownloadUiState
import com.retrowave.player.data.download.SpotifySearchRepository
import com.retrowave.player.data.download.SpotifySongData
import com.retrowave.player.data.repository.MusicRepository
import com.retrowave.player.domain.model.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class SearchUiState(
    val searchResults: List<Song> = emptyList(),
    val genres: List<String> = emptyList(),
    val isSearching: Boolean = false,
    val isSpotifySearching: Boolean = false,
    val spotifySearchError: String? = null
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
    private val spotifyDownloadRepository: SpotifyDownloadRepository,
    private val spotifySearchRepository: SpotifySearchRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _spotifyState = MutableStateFlow(SpotifyDownloadUiState())
    val spotifyState: StateFlow<SpotifyDownloadUiState> = _spotifyState.asStateFlow()

    private var allSongs: List<Song> = emptyList()

    init {
        viewModelScope.launch {
            musicRepository.getAllSongs().collect { songs ->
                allSongs = songs
            }
        }
    }

    fun loadGenres() {
        viewModelScope.launch {
            val genres = musicRepository.getAllGenres().first()
            _uiState.value = _uiState.value.copy(genres = genres)
        }
    }

    fun search(query: String) {
        if (query.isBlank()) {
            _uiState.value = _uiState.value.copy(searchResults = emptyList())
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSearching = true)
            val results = allSongs.filter { song ->
                song.title.contains(query, ignoreCase = true) ||
                song.artist.contains(query, ignoreCase = true) ||
                song.album.contains(query, ignoreCase = true) ||
                (song.genre?.contains(query, ignoreCase = true) == true)
            }
            _uiState.value = _uiState.value.copy(
                searchResults = results,
                isSearching = false
            )
        }
    }

    fun isSpotifyUrl(text: String): Boolean {
        return text.contains("open.spotify.com/")
    }

    fun isSpotifyTrackUrl(text: String): Boolean {
        return text.contains("open.spotify.com/track/")
    }

    fun fetchSpotifySongInfo(url: String) {
        viewModelScope.launch {
            _spotifyState.value = SpotifyDownloadUiState(isLoading = true)
            val result = spotifyDownloadRepository.fetchSongInfo(url)
            result.onSuccess { data ->
                _spotifyState.value = SpotifyDownloadUiState(songInfo = data)
            }.onFailure { error ->
                _spotifyState.value = SpotifyDownloadUiState(
                    error = error.message ?: "Error al obtener información"
                )
            }
        }
    }

    fun downloadSong(songData: SpotifySongData) {
        viewModelScope.launch {
            _spotifyState.value = _spotifyState.value.copy(
                isDownloading = true,
                downloadProgress = 0f,
                error = null,
                downloadedSongId = null
            )
            val result = spotifyDownloadRepository.downloadAndSave(songData) { progress ->
                _spotifyState.value = _spotifyState.value.copy(downloadProgress = progress)
            }
            result.onSuccess { songId ->
                _spotifyState.value = _spotifyState.value.copy(
                    isDownloading = false,
                    downloadProgress = 1f,
                    downloadedSongId = songId
                )
            }.onFailure { error ->
                _spotifyState.value = _spotifyState.value.copy(
                    isDownloading = false,
                    error = error.message ?: "Error al descargar"
                )
            }
        }
    }

    fun searchSpotify(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSpotifySearching = true,
                spotifySearchError = null
            )
            _spotifyState.value = SpotifyDownloadUiState()

            val result = spotifySearchRepository.searchTrack(query)
            result.onSuccess { track ->
                _uiState.value = _uiState.value.copy(isSpotifySearching = false)
                fetchSpotifySongInfo(track.spotifyUrl)
            }.onFailure { error ->
                Timber.e(error, "Spotify search failed")
                _uiState.value = _uiState.value.copy(
                    isSpotifySearching = false,
                    spotifySearchError = error.message ?: "Error al buscar en Spotify"
                )
            }
        }
    }

    fun resetSpotifyState() {
        _spotifyState.value = SpotifyDownloadUiState()
    }

    fun resetSpotifySearch() {
        _uiState.value = _uiState.value.copy(
            isSpotifySearching = false,
            spotifySearchError = null
        )
    }
}
