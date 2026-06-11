package com.retrowave.player.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.retrowave.player.data.local.dao.PlaylistDao
import com.retrowave.player.data.local.dao.SongDao
import com.retrowave.player.data.local.entity.PlaylistEntity
import com.retrowave.player.data.local.entity.PlaylistSongCrossRef
import com.retrowave.player.data.local.entity.SongEntity

@Database(
    entities = [
        SongEntity::class,
        PlaylistEntity::class,
        PlaylistSongCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun playlistDao(): PlaylistDao
}
