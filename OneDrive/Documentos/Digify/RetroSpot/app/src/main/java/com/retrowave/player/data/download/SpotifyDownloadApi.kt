package com.retrowave.player.data.download

import retrofit2.http.GET
import retrofit2.http.Query

interface SpotifyDownloadApi {

    @GET("downloadSong")
    suspend fun downloadSong(
        @Query("songId") songId: String
    ): SpotifyDownloadResponse
}
