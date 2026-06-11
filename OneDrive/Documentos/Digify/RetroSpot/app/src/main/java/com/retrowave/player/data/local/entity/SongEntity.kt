package com.retrowave.player.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class SongEntity(
    @PrimaryKey
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val durationMs: Long,
    val trackNumber: Int,
    val genre: String?,
    val year: Int?,
    val albumArtUri: String?,
    val dataUri: String,
    val dateAdded: Long
)
