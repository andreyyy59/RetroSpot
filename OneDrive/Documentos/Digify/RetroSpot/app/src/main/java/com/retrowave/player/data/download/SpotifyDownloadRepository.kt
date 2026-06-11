package com.retrowave.player.data.download

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import com.retrowave.player.data.repository.MusicRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import timber.log.Timber
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class SpotifyDownloadRepository @Inject constructor(
    private val api: SpotifyDownloadApi,
    private val musicRepository: MusicRepository,
    @Named("file_download") private val downloadClient: OkHttpClient,
    @ApplicationContext private val context: Context
) {
    private val spotifyTrackPattern = Pattern.compile("track/([a-zA-Z0-9_-]+)")

    fun extractTrackId(url: String): String? {
        val matcher = spotifyTrackPattern.matcher(url)
        return if (matcher.find()) matcher.group(1) else null
    }

    suspend fun fetchSongInfo(url: String): Result<SpotifySongData> = withContext(Dispatchers.IO) {
        if (extractTrackId(url) == null) {
            return@withContext Result.failure(IllegalArgumentException("URL de Spotify no válida"))
        }

        try {
            val response = api.downloadSong(url)
            if (response.success) {
                Result.success(response.data)
            } else {
                Result.failure(Exception("Error al obtener información de la canción"))
            }
        } catch (e: Exception) {
            Timber.e(e, "Error fetching song info")
            Result.failure(e)
        }
    }

    suspend fun downloadAndSave(songData: SpotifySongData, onProgress: (Float) -> Unit = {}): Result<Long> =
        withContext(Dispatchers.IO) {
            try {
                val fileName = "${songData.artist} - ${songData.title}"
                    .replace(Regex("[/\\\\:*?\"<>|]"), "_")

                val values = ContentValues().apply {
                    put(MediaStore.Audio.Media.TITLE, songData.title)
                    put(MediaStore.Audio.Media.ARTIST, songData.artist)
                    put(MediaStore.Audio.Media.ALBUM, songData.album)
                    put(MediaStore.Audio.Media.MIME_TYPE, "audio/mpeg")
                    put(MediaStore.Audio.Media.IS_MUSIC, true)
                    put(MediaStore.Audio.Media.RELATIVE_PATH, "${Environment.DIRECTORY_MUSIC}/RetroSpot")
                    put(MediaStore.Audio.Media.DISPLAY_NAME, "$fileName.mp3")
                }

                val resolver = context.contentResolver
                val uri = resolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values)
                    ?: return@withContext Result.failure(Exception("No se pudo crear el archivo en MediaStore"))

                resolver.openOutputStream(uri)?.use { outputStream ->
                    val request = okhttp3.Request.Builder()
                        .url(songData.downloadLink)
                        .build()

                    downloadClient.newCall(request).execute().use { response ->
                        if (!response.isSuccessful) {
                            resolver.delete(uri, null, null)
                            return@withContext Result.failure(
                                Exception("Error al descargar: ${response.code}")
                            )
                        }

                        val body = response.body ?: run {
                            resolver.delete(uri, null, null)
                            return@withContext Result.failure(Exception("Respuesta vacía"))
                        }

                        val totalBytes = body.contentLength()
                        var bytesRead = 0L
                        val buffer = ByteArray(8192)
                        val source = body.byteStream()

                        while (true) {
                            val read = source.read(buffer)
                            if (read == -1) break
                            outputStream.write(buffer, 0, read)
                            bytesRead += read
                            if (totalBytes > 0) {
                                onProgress(bytesRead.toFloat() / totalBytes)
                            }
                        }
                        outputStream.flush()
                    }
                }

                val mediaId = uri.lastPathSegment?.toLongOrNull()
                Timber.i("Downloaded and saved: ${songData.title} - ${songData.artist} (id=$mediaId)")

                musicRepository.scanAndSaveMusic()

                Result.success(mediaId ?: -1L)
            } catch (e: Exception) {
                Timber.e(e, "Error downloading song")
                Result.failure(e)
            }
        }
}
