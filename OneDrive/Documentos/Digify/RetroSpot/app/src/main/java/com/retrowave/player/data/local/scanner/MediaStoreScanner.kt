package com.retrowave.player.data.local.scanner

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.retrowave.player.data.local.entity.SongEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaStoreScanner @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun scanAllSongs(): List<SongEntity> {
        val songs = mutableListOf<SongEntity>()
        val collection = if (android.os.Build.VERSION.SDK_INT >= 29) {
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.TRACK,
            MediaStore.Audio.Media.YEAR,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DATE_ADDED
        )

        val selection = "${MediaStore.Audio.Media.IS_MUSIC} = 1"
        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

        context.contentResolver.query(
            collection, projection, selection, null, sortOrder
        )?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val durationCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val trackCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK)
            val yearCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR)
            val albumIdCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            val dataCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val dateCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idCol)
                val title = cursor.getString(titleCol) ?: "Unknown"
                val artist = cursor.getString(artistCol) ?: "Unknown"
                val album = cursor.getString(albumCol) ?: "Unknown"
                val duration = cursor.getLong(durationCol)
                val track = cursor.getInt(trackCol)
                val year = if (cursor.isNull(yearCol)) null else cursor.getInt(yearCol)
                val albumId = cursor.getLong(albumIdCol)
                val data = cursor.getString(dataCol) ?: continue
                val dateAdded = cursor.getLong(dateCol)

                val albumArtUri = getAlbumArtUri(albumId)
                val genre = getGenre(context.contentResolver, id)

                songs.add(
                    SongEntity(
                        id = id,
                        title = title,
                        artist = artist,
                        album = album,
                        durationMs = duration,
                        trackNumber = track,
                        genre = genre,
                        year = year,
                        albumArtUri = albumArtUri.toString(),
                        dataUri = data,
                        dateAdded = dateAdded
                    )
                )
            }
        }

        return songs
    }

    private fun getAlbumArtUri(albumId: Long): Uri? {
        return try {
            val sArtworkUri = Uri.parse("content://media/external/audio/albumart")
            Uri.withAppendedPath(sArtworkUri, albumId.toString())
        } catch (e: Exception) {
            null
        }
    }

    private fun getGenre(contentResolver: ContentResolver, songId: Long): String? {
        val uri = MediaStore.Audio.Genres.getContentUriForAudioId("external", songId.toInt())
        return try {
            contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.NAME))
                } else null
            }
        } catch (e: Exception) {
            null
        }
    }
}
