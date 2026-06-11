package com.retrowave.player.data.download

import com.google.gson.annotations.SerializedName

data class SpotifyDownloadResponse(
    val success: Boolean,
    val data: SpotifySongData,
    val generatedTimeStamp: Long
)

data class SpotifySongData(
    val id: String,
    val artist: String,
    val title: String,
    val album: String,
    val cover: String,
    @SerializedName("downloadLink")
    val downloadLink: String
)

data class SpotifyDownloadUiState(
    val isLoading: Boolean = false,
    val isDownloading: Boolean = false,
    val songInfo: SpotifySongData? = null,
    val error: String? = null,
    val downloadedSongId: Long? = null,
    val downloadProgress: Float = 0f
)
