package com.retrowave.player.data.local.dao

import androidx.room.*
import com.retrowave.player.data.local.entity.SongEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {
    @Query("SELECT * FROM songs ORDER BY title ASC")
    fun getAll(): Flow<List<SongEntity>>

    @Query("SELECT * FROM songs WHERE id = :id")
    suspend fun getById(id: Long): SongEntity?

    @Query("SELECT * FROM songs WHERE id IN (:ids)")
    suspend fun getByIds(ids: List<Long>): List<SongEntity>

    @Query("SELECT * FROM songs ORDER BY title ASC")
    suspend fun getAllSongs(): List<SongEntity>

    @Query("SELECT * FROM songs WHERE album = :albumName ORDER BY trackNumber ASC")
    fun getByAlbum(albumName: String): Flow<List<SongEntity>>

    @Query("SELECT * FROM songs WHERE genre = :genre")
    fun getByGenre(genre: String): Flow<List<SongEntity>>

    @Query("SELECT DISTINCT genre FROM songs WHERE genre IS NOT NULL")
    fun getAllGenres(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(songs: List<SongEntity>)

    @Query("DELETE FROM songs")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM songs")
    suspend fun count(): Int

    @Query("DELETE FROM songs WHERE id = :id")
    suspend fun deleteById(id: Long)
}
