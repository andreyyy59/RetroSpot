package com.retrowave.player.domain.model

data class Song(
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
