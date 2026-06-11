package com.retrowave.player.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.retrowave.player.data.ai.RecommendationEngine
import com.retrowave.player.data.ai.models.AIRecommendation
import com.retrowave.player.data.repository.MusicRepository
import com.retrowave.player.data.repository.PlaylistRepository
import com.retrowave.player.domain.model.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val songs: List<Song> = emptyList(),
    val recommendations: List<AIRecommendation> = emptyList(),
    val genres: List<String> = emptyList(),
    val isScanning: Boolean = false,
    val isLoading: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
    private val playlistRepository: PlaylistRepository,
    private val recommendationEngine: RecommendationEngine
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            musicRepository.getAllSongs().collect { songs ->
                _uiState.value = _uiState.value.copy(
                    songs = songs,
                    isLoading = false
                )

                if (songs.isNotEmpty() && _uiState.value.recommendations.isEmpty()) {
                    loadRecommendations(songs)
                }
            }
        }

        viewModelScope.launch {
            musicRepository.getAllGenres().collect { genres ->
                _uiState.value = _uiState.value.copy(genres = genres)
            }
        }
    }

    private suspend fun loadRecommendations(songs: List<Song>) {
        val recommendations = recommendationEngine.getRecommendations(songs)
        _uiState.value = _uiState.value.copy(recommendations = recommendations)
    }

    fun refreshRecommendations() {
        viewModelScope.launch {
            val songs = _uiState.value.songs
            if (songs.isNotEmpty()) {
                _uiState.value = _uiState.value.copy(isLoading = true)
                loadRecommendations(songs)
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun createPlaylistFromRecommendation(
        rec: AIRecommendation,
        onCreated: (Long) -> Unit
    ) {
        viewModelScope.launch {
            val allSongs = musicRepository.getAllSongs().first()
            val matchedSongs = mutableListOf<Song>()

            // 1. Try matching by suggested song titles first
            if (rec.suggestedSongTitles.isNotEmpty()) {
                val titleMap = allSongs.groupBy { it.title.lowercase().trim() }
                for (title in rec.suggestedSongTitles) {
                    titleMap[title.lowercase().trim()]?.firstOrNull()?.let {
                        matchedSongs.add(it)
                    }
                }
            }

            // 2. Fall back to genre/artist matching for more songs
            if (matchedSongs.size < 3) {
                val genreArtistSongs = musicRepository.getSongsByGenresOrArtists(
                    genres = rec.suggestedGenres,
                    artists = rec.suggestedArtists
                )
                for (song in genreArtistSongs) {
                    if (song !in matchedSongs) matchedSongs.add(song)
                }
            }

            if (matchedSongs.isEmpty()) return@launch

            val playlistId = playlistRepository.create(
                name = rec.playlistName,
                description = rec.description,
                isAIGenerated = true
            )
            for (song in matchedSongs) {
                playlistRepository.addSong(playlistId, song.id)
            }
            onCreated(playlistId)
        }
    }

    fun scanMusic() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isScanning = true)
            musicRepository.scanAndSaveMusic()
            _uiState.value = _uiState.value.copy(isScanning = false)
        }
    }
}
