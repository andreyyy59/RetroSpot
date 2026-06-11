package com.retrowave.player.data.download

import com.google.gson.annotations.SerializedName

data class SpotifyTokenResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("expires_in") val expiresIn: Int
)

data class SpotifySearchResponse(
    val tracks: SpotifyTrackList?
)

data class SpotifyTrackList(
    val items: List<SpotifyTrackObject>
)

data class SpotifyTrackObject(
    val id: String,
    val name: String,
    val artists: List<SpotifyArtistObject>,
    val album: SpotifyAlbumObject,
    @SerializedName("external_urls") val externalUrls: SpotifyExternalUrls,
    @SerializedName("duration_ms") val durationMs: Long
)

data class SpotifyArtistObject(
    val name: String
)

data class SpotifyAlbumObject(
    val name: String,
    val images: List<SpotifyImageObject>
)

data class SpotifyImageObject(
    val url: String,
    val height: Int?,
    val width: Int?
)

data class SpotifyExternalUrls(
    val spotify: String
)
