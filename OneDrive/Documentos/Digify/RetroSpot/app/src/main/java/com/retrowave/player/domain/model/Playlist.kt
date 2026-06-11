package com.retrowave.player.domain.model

data class Playlist(
    val id: Long = 0,
    val name: String,
    val description: String?,
    val songIds: List<Long> = emptyList(),
    val isAIGenerated: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
