package com.retrowave.player.data.local.player

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.media.app.NotificationCompat.MediaStyle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MusicService : android.app.Service() {

    private var notificationManager: NotificationManager? = null
    private var mediaSession: MediaSession? = null
    private var playerListener: Player.Listener? = null
    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())
    private val mainHandler = Handler(Looper.getMainLooper())
    private var progressUpdater: Runnable? = null
    private var lastArtworkBitmap: Bitmap? = null

    override fun onCreate() {
        super.onCreate()
        Log.d("RetroSpot", "MusicService onCreate")
        notificationManager = getSystemService(NotificationManager::class.java)
        createNotificationChannel()

        val p = getPlayer()
        if (p == null) {
            Log.e("RetroSpot", "MusicService: player is null")
            stopSelf()
            return
        }

        try {
            mediaSession = MediaSession.Builder(this, p).build()
            Log.d("RetroSpot", "MediaSession created OK")
        } catch (e: Exception) {
            Log.e("RetroSpot", "MediaSession creation failed", e)
        }

        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    loadArtworkAndUpdate(p)
                }
            }
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                updateNotification(p)
            }
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                loadArtworkAndUpdate(p)
            }
        }
        playerListener = listener
        p.addListener(listener)

        try {
            startForeground(NOTIFICATION_ID, buildNotification(p, null))
            Log.d("RetroSpot", "startForeground OK")
        } catch (e: Exception) {
            Log.e("RetroSpot", "startForeground failed", e)
            stopSelf()
            return
        }

        if (p.playbackState == Player.STATE_READY || p.isPlaying) {
            loadArtworkAndUpdate(p)
        }

        val updater = object : Runnable {
            override fun run() {
                if (p.isPlaying) {
                    notificationManager?.notify(NOTIFICATION_ID, buildNotification(p, lastArtworkBitmap))
                }
                mainHandler.postDelayed(this, 1000)
            }
        }
        progressUpdater = updater
        mainHandler.post(updater)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val p = getPlayer() ?: return START_STICKY
        if (intent != null) {
            when (intent.action) {
                ACTION_PLAY -> p.play()
                ACTION_PAUSE -> p.pause()
                ACTION_SKIP_NEXT -> p.seekToNext()
                ACTION_SKIP_PREV -> p.seekToPrevious()
                ACTION_STOP -> {
                    p.stop()
                    p.clearMediaItems()
                    stopSelf()
                }
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onTaskRemoved(rootIntent: Intent?) {
        val p = getPlayer() ?: return
        if (!p.playWhenReady || p.mediaItemCount == 0) {
            stopSelf()
        }
    }

    override fun onDestroy() {
        progressUpdater?.let { mainHandler.removeCallbacks(it) }
        serviceScope.cancel()
        playerListener?.let { getPlayer()?.removeListener(it) }
        mediaSession?.run {
            release()
            mediaSession = null
        }
        super.onDestroy()
    }

    private fun loadArtworkAndUpdate(player: ExoPlayer) {
        val artworkUri = player.currentMediaItem?.mediaMetadata?.artworkUri
        serviceScope.launch {
            lastArtworkBitmap = if (artworkUri != null) {
                withContext(Dispatchers.IO) { loadBitmap(artworkUri) }
            } else null
            notificationManager?.notify(NOTIFICATION_ID, buildNotification(player, lastArtworkBitmap))
        }
    }

    private fun loadBitmap(uri: Uri): Bitmap? {
        return try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: Exception) {
            Log.e("RetroSpot", "Failed to load artwork bitmap", e)
            null
        }
    }

    private fun updateNotification(player: ExoPlayer) {
        if (player.isPlaying) {
            notificationManager?.notify(NOTIFICATION_ID, buildNotification(player, lastArtworkBitmap))
        }
    }

    private fun buildNotification(player: ExoPlayer, artworkBitmap: Bitmap?): Notification {
        val metadata = player.currentMediaItem?.mediaMetadata
        val title = metadata?.title?.toString() ?: "RetroSpot"
        val artist = metadata?.artist?.toString() ?: ""
        val duration = player.duration
        val position = player.currentPosition

        val openIntent = packageManager?.getLaunchIntentForPackage(packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val openPendingIntent = if (openIntent != null) {
            PendingIntent.getActivity(
                this, 0, openIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else null

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentTitle(title)
            .setContentText(artist)
            .setSubText("RetroSpot")
            .setLargeIcon(artworkBitmap)
            .setContentIntent(openPendingIntent)
            .addAction(
                android.R.drawable.ic_media_previous,
                "Anterior",
                commandIntent(ACTION_SKIP_PREV)
            )
            .addAction(
                if (player.isPlaying) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play,
                if (player.isPlaying) "Pausar" else "Reproducir",
                commandIntent(if (player.isPlaying) ACTION_PAUSE else ACTION_PLAY)
            )
            .addAction(
                android.R.drawable.ic_media_next,
                "Siguiente",
                commandIntent(ACTION_SKIP_NEXT)
            )

        try {
            val token = mediaSession?.sessionCompatToken
            if (token != null) {
                builder.setStyle(
                    MediaStyle()
                        .setMediaSession(token)
                        .setShowActionsInCompactView(0, 1, 2)
                        .setShowCancelButton(true)
                        .setCancelButtonIntent(commandIntent(ACTION_STOP))
                )
            }
        } catch (e: Exception) {
            Log.e("RetroSpot", "Failed to set MediaStyle", e)
        }

        if (duration > 0) {
            builder.setProgress(100, ((position.toFloat() / duration) * 100).toInt(), false)
        }

        return builder
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOngoing(player.isPlaying)
            .build()
    }

    private fun commandIntent(action: String): PendingIntent {
        return PendingIntent.getForegroundService(
            this, action.hashCode(), Intent(this, MusicService::class.java).apply {
                this.action = action
            },
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Reproducción",
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager?.createNotificationChannel(channel)
    }

    companion object {
        private const val CHANNEL_ID = "retrospot_playback"
        private const val NOTIFICATION_ID = 123
        private const val ACTION_PLAY = "com.retrowave.player.ACTION_PLAY"
        private const val ACTION_PAUSE = "com.retrowave.player.ACTION_PAUSE"
        private const val ACTION_SKIP_NEXT = "com.retrowave.player.ACTION_SKIP_NEXT"
        private const val ACTION_SKIP_PREV = "com.retrowave.player.ACTION_SKIP_PREV"
        private const val ACTION_STOP = "com.retrowave.player.ACTION_STOP"

        private var playerRef: ExoPlayer? = null

        fun setPlayer(player: ExoPlayer) {
            playerRef = player
        }

        fun getPlayer(): ExoPlayer? = playerRef
    }
}
