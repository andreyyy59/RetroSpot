package com.retrowave.player.data.repository

import com.retrowave.player.data.local.dao.SongDao
import com.retrowave.player.data.local.entity.SongEntity
import com.retrowave.player.data.local.scanner.MediaStoreScanner
import com.retrowave.player.domain.model.Album
import com.retrowave.player.domain.model.Artist
import com.retrowave.player.domain.model.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicRepository @Inject constructor(
    private val songDao: SongDao,
    private val scanner: MediaStoreScanner
) {
    fun getAllSongs(): Flow<List<Song>> {
        return songDao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    fun getSongsByGenre(genre: String): Flow<List<Song>> {
        return songDao.getByGenre(genre).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    fun getAllGenres(): Flow<List<String>> {
        return songDao.getAllGenres()
    }

    suspend fun getSongById(id: Long): Song? {
        return songDao.getById(id)?.toDomain()
    }

    suspend fun getSongsByIds(ids: List<Long>): List<Song> {
        if (ids.isEmpty()) return emptyList()
        return songDao.getByIds(ids).map { it.toDomain() }
    }

    fun getSongsByAlbum(albumName: String): Flow<List<Song>> {
        return songDao.getByAlbum(albumName).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun getSongsByGenresOrArtists(genres: List<String>, artists: List<String>): List<Song> {
        val lowerGenres = genres.filter { it.isNotBlank() }.map { it.lowercase() }
        val lowerArtists = artists.filter { it.isNotBlank() }.map { it.lowercase() }
        if (lowerGenres.isEmpty() && lowerArtists.isEmpty()) return emptyList()

        val allSongs = songDao.getAllSongs().map { it.toDomain() }
        return allSongs.filter { song ->
            (lowerGenres.isNotEmpty() && song.genre?.lowercase() in lowerGenres) ||
                    (lowerArtists.isNotEmpty() && song.artist.lowercase() in lowerArtists)
        }
    }

    fun getAlbums(): Flow<List<Album>> {
        return songDao.getAll().map { entities ->
            entities.groupBy { it.album }
                .map { (albumName, songs) ->
                    val first = songs.first()
                    Album(
                        id = first.id,
                        title = albumName,
                        artist = first.artist,
                        songCount = songs.size,
                        albumArtUri = first.albumArtUri,
                        year = first.year
                    )
                }
        }
    }

    fun getArtists(): Flow<List<Artist>> {
        return songDao.getAll().map { entities ->
            entities.groupBy { it.artist }
                .map { (artistName, songs) ->
                    Artist(
                        id = songs.first().id,
                        name = artistName,
                        albumCount = songs.distinctBy { it.album }.size,
                        songCount = songs.size
                    )
                }
        }
    }

    suspend fun scanAndSaveMusic() {
        try {
            val rawSongs = scanner.scanAllSongs()
            val deduplicated = rawSongs.distinctBy {
                "${it.title.lowercase()}|${it.artist.lowercase()}"
            }
            songDao.deleteAll()
            songDao.insertAll(deduplicated)
            val removed = rawSongs.size - deduplicated.size
            if (removed > 0) Timber.i("Removed $removed duplicates")
            Timber.i("Scanned and saved ${deduplicated.size} songs")
        } catch (e: Exception) {
            Timber.e(e, "Error scanning music")
        }
    }

    suspend fun getSongCount(): Int = songDao.count()

    suspend fun deleteSongById(id: Long) {
        songDao.deleteById(id)
    }

    private fun SongEntity.toDomain() = Song(
        id = id,
        title = title,
        artist = artist,
        album = album,
        durationMs = durationMs,
        trackNumber = trackNumber,
        genre = genre,
        year = year,
        albumArtUri = albumArtUri,
        dataUri = dataUri,
        dateAdded = dateAdded
    )
}
