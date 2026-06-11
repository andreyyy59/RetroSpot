package com.retrowave.player.data.download

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SpotifySearchApi {
    @GET("search")
    suspend fun search(
        @Header("Authorization") authorization: String,
        @Query("q") query: String,
        @Query("type") type: String = "track",
        @Query("limit") limit: Int = 5
    ): SpotifySearchResponse
}
