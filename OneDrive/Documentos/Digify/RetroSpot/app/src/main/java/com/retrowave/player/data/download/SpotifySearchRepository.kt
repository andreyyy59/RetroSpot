package com.retrowave.player.data.download

import android.util.Base64
import com.google.gson.Gson
import com.retrowave.player.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

data class SpotifyTrackSearchResult(
    val spotifyUrl: String,
    val title: String,
    val artist: String,
    val album: String,
    val coverUrl: String
)

@Singleton
class SpotifySearchRepository @Inject constructor(
    private val searchApi: SpotifySearchApi,
    @Named("file_download") private val client: OkHttpClient
) {
    private var cachedToken: String? = null
    private var tokenExpiresAt: Long = 0L

    private suspend fun getToken(): String = withContext(Dispatchers.IO) {
        val now = System.currentTimeMillis()
        if (cachedToken != null && now < tokenExpiresAt) {
            return@withContext cachedToken!!
        }

        val credentials = "${BuildConfig.SPOTIFY_CLIENT_ID}:${BuildConfig.SPOTIFY_CLIENT_SECRET}"
        val basic = Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)

        val form = FormBody.Builder()
            .add("grant_type", "client_credentials")
            .build()

        val request = Request.Builder()
            .url("https://accounts.spotify.com/api/token")
            .header("Authorization", "Basic $basic")
            .post(form)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                val errBody = response.body?.string() ?: "no body"
                throw Exception("Spotify token error ${response.code}: $errBody")
            }
            val body = response.body!!.string()
            val tokenResp = Gson().fromJson(body, SpotifyTokenResponse::class.java)
            cachedToken = tokenResp.accessToken
            tokenExpiresAt = now + (tokenResp.expiresIn * 1000L) - 60000L
            Timber.i("Spotify token obtained, expires in ${tokenResp.expiresIn}s")
            tokenResp.accessToken
        }
    }

    suspend fun searchTrack(query: String): Result<SpotifyTrackSearchResult> = withContext(Dispatchers.IO) {
        try {
            val token = getToken()
            val response = searchApi.search(
                authorization = "Bearer $token",
                query = query,
                limit = 1
            )
            val track = response.tracks?.items?.firstOrNull()
            if (track == null) {
                return@withContext Result.failure(Exception("No se encontró \"$query\" en Spotify"))
            }

            val result = SpotifyTrackSearchResult(
                spotifyUrl = track.externalUrls.spotify,
                title = track.name,
                artist = track.artists.joinToString(", ") { it.name },
                album = track.album.name,
                coverUrl = track.album.images.firstOrNull()?.url ?: ""
            )
            Timber.i("Spotify search found: ${result.title} - ${result.artist}")
            Result.success(result)
        } catch (e: Exception) {
            Timber.e(e, "Spotify search failed for query: $query")
            Result.failure(e)
        }
    }
}
