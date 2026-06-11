package com.retrowave.player.data.repository

import com.retrowave.player.data.local.dao.PlaylistDao
import com.retrowave.player.data.local.entity.PlaylistEntity
import com.retrowave.player.data.local.entity.PlaylistSongCrossRef
import com.retrowave.player.domain.model.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaylistRepository @Inject constructor(
    private val playlistDao: PlaylistDao
) {
    fun getAll(): Flow<List<Playlist>> {
        return playlistDao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    fun getAIPlaylists(): Flow<List<Playlist>> {
        return playlistDao.getAIPlaylists().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun create(name: String, description: String?, isAIGenerated: Boolean = false): Long {
        return playlistDao.insert(
            PlaylistEntity(
                name = name,
                description = description,
                isAIGenerated = isAIGenerated
            )
        )
    }

    suspend fun addSong(playlistId: Long, songId: Long) {
        playlistDao.addSongToPlaylist(
            PlaylistSongCrossRef(playlistId = playlistId, songId = songId)
        )
    }

    suspend fun getSongIds(playlistId: Long): List<Long> {
        return playlistDao.getSongIds(playlistId)
    }

    suspend fun getPlaylistWithSongs(playlistId: Long): Playlist? {
        val entity = playlistDao.getById(playlistId) ?: return null
        val songIds = playlistDao.getSongIds(playlistId)
        return entity.toDomain(songIds)
    }

    suspend fun deleteById(playlistId: Long) {
        playlistDao.deleteById(playlistId)
    }

    suspend fun rename(playlistId: Long, newName: String) {
        val entity = playlistDao.getById(playlistId) ?: return
        playlistDao.update(entity.copy(name = newName))
    }

    private fun PlaylistEntity.toDomain(songIds: List<Long> = emptyList()) = Playlist(
        id = id,
        name = name,
        description = description,
        songIds = songIds,
        isAIGenerated = isAIGenerated,
        createdAt = createdAt
    )
}
