package com.retrowave.player.ui.player

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.retrowave.player.ui.components.AsciiArtPlaceholder
import com.retrowave.player.ui.components.AudioVisualizer
import com.retrowave.player.ui.components.MarqueeText
import com.retrowave.player.ui.components.PixelText
import com.retrowave.player.ui.components.VisualizerMode
import com.retrowave.player.ui.components.chromeBorder
import com.retrowave.player.ui.components.songColor
import com.retrowave.player.ui.theme.*
import java.util.Locale
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel,
    songId: Long,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(songId) {
        viewModel.playSong(songId)
    }

    val albumArt = uiState.currentSong?.albumArtUri
    var showVisualizer by remember { mutableStateOf(true) }
    var visualizerMode by remember { mutableStateOf(VisualizerMode.BARS) }
    var dominantColor by remember { mutableStateOf<Color?>(null) }
    var permissionRequested by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }
    val notifLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) {
            viewModel.onNotificationPermissionDenied()
        }
    }

    LaunchedEffect(Unit) {
        if (!permissionRequested && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (androidx.core.content.ContextCompat.checkSelfPermission(
                    context, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notifLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            permissionRequested = true
        }
    }

    LaunchedEffect(uiState.notificationDenied) {
        if (uiState.notificationDenied) {
            snackbarHostState.showSnackbar(
                "Permiso de notificaciones denegado — los controles no aparecerán fuera de la app"
            )
        }
    }

    LaunchedEffect(albumArt) {
        if (albumArt != null) {
            try {
                val uri = Uri.parse(albumArt)
                val bitmap = context.contentResolver.openInputStream(uri)?.use { stream ->
                    BitmapFactory.decodeStream(stream)
                }
                if (bitmap != null) {
                    val palette = androidx.palette.graphics.Palette.from(bitmap).generate()
                    val colorInt = palette.lightVibrantSwatch?.rgb
                        ?: palette.vibrantSwatch?.rgb
                        ?: palette.lightMutedSwatch?.rgb
                        ?: palette.dominantSwatch?.rgb
                        ?: palette.mutedSwatch?.rgb
                    if (colorInt != null) {
                        dominantColor = Color(colorInt)
                    }
                }
            } catch (_: Exception) {}
        } else {
            dominantColor = null
        }
    }

    val infiniteTransition = rememberInfiniteTransition()
    val scanlineAlpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(RetroDarkerBg)
    ) {
        // Background: dominant color gradient
        val bgTopColor = dominantColor
            ?: (uiState.currentSong?.let { songColor(it.title) }?.copy(alpha = 0.4f))
            ?: RetroPanelMid
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            bgTopColor.copy(alpha = 0.4f),
                            bgTopColor.copy(alpha = 0.15f),
                            RetroDarkerBg
                        )
                    )
                )
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Chrome top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 12.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                RetroDarkChrome.copy(alpha = 0.3f),
                                RetroChrome.copy(alpha = 0.1f),
                                RetroDarkChrome.copy(alpha = 0.3f)
                            )
                        )
                    )
                    .border(
                        width = 1.dp,
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                RetroChromeGradientEnd.copy(alpha = 0.3f),
                                RetroSilverMetallic.copy(alpha = 0.5f),
                                RetroChromeGradientEnd.copy(alpha = 0.3f)
                            )
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = "Cerrar",
                        tint = RetroSilverMetallic
                    )
                }

                PixelText(
                    text = "RETROSPOT",
                    color = RetroCyan,
                    pixelSize = 2.5.dp
                )

                Box(modifier = Modifier.size(48.dp))
            }

            Box(
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxWidth()
            )

            // Album art
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(RoundedCornerShape(24.dp))
                    .chromeBorder(width = 2.dp, cornerRadius = 24.dp)
            ) {
                if (albumArt != null) {
                    AsyncImage(
                        model = albumArt,
                        contentDescription = "Album art",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    val songTitle = uiState.currentSong?.title ?: ""
                    AsciiArtPlaceholder(
                        seed = songTitle.ifBlank { "?" },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Song info
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MarqueeText(
                    text = uiState.currentSong?.title?.uppercase() ?: "SIN REPRODUCCIÓN",
                    color = RetroSilverMetallic,
                    style = MaterialTheme.typography.headlineMedium.copy(letterSpacing = 1.sp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = (uiState.currentSong?.artist?.uppercase() ?: "") +
                            if (uiState.currentSong?.album != null) " • ${uiState.currentSong?.album?.uppercase()}" else "",
                    color = RetroDimGray.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    letterSpacing = 1.sp
                )
            }

            Box(
                modifier = Modifier
                    .weight(0.2f)
                    .fillMaxWidth()
            )

            // Time + Slider
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatDuration(uiState.currentPosition),
                        color = RetroCyan.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.titleMedium,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "-${formatDuration(uiState.duration - uiState.currentPosition)}",
                        color = RetroSilverMetallic.copy(alpha = 0.5f),
                        style = MaterialTheme.typography.titleMedium,
                        letterSpacing = 2.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .chromeBorder(cornerRadius = 4.dp, width = 0.5.dp)
                ) {
                    @Suppress("DEPRECATION")
                    Slider(
                        value = if (uiState.duration > 0)
                            (uiState.currentPosition.toFloat() / uiState.duration).coerceIn(0f, 1f) else 0f,
                        onValueChange = { fraction ->
                            viewModel.seekTo((fraction * uiState.duration).toLong())
                        },
                        colors = SliderDefaults.colors(
                            thumbColor = RetroCyan,
                            activeTrackColor = RetroCyan,
                            inactiveTrackColor = RetroDarkChrome.copy(alpha = 0.5f),
                            activeTickColor = Color.Transparent,
                            inactiveTickColor = Color.Transparent
                        ),
                        thumb = {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(
                                            colors = listOf(
                                                RetroSilverMetallic,
                                                RetroChromeGradientStart,
                                                RetroChromeGradientEnd
                                            )
                                        )
                                    )
                                    .border(
                                        width = 1.5.dp,
                                        brush = Brush.sweepGradient(
                                            colors = listOf(
                                                RetroSilverMetallic,
                                                RetroChromeGradientStart,
                                                RetroChromeShadow,
                                                RetroChromeGradientEnd,
                                                RetroSilverMetallic
                                            )
                                        ),
                                        shape = CircleShape
                                    )
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.toggleShuffle() }) {
                    Icon(
                        Icons.Default.Shuffle,
                        contentDescription = "Aleatorio",
                        tint = if (uiState.isShuffled) RetroCyan else RetroSilverMetallic,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(RetroDarkChrome.copy(alpha = 0.3f))
                ) {
                    IconButton(onClick = { viewModel.skipToPrevious() }) {
                        Icon(
                            Icons.Default.SkipPrevious,
                            contentDescription = "Anterior",
                            tint = RetroSilverMetallic,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                // Big play/pause button
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .border(
                            width = 2.dp,
                            brush = Brush.sweepGradient(
                                colors = listOf(
                                    RetroSilverMetallic,
                                    RetroChromeGradientStart,
                                    RetroChromeGradientEnd,
                                    RetroChromeShadow,
                                    RetroSilverMetallic
                                )
                            ),
                            shape = CircleShape
                        )
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    RetroSilverMetallic,
                                    RetroChromeGradientStart,
                                    RetroChromeGradientEnd
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = { viewModel.togglePlayPause() },
                        modifier = Modifier.size(68.dp)
                    ) {
                        Icon(
                            if (uiState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (uiState.isPlaying) "Pausar" else "Reproducir",
                            tint = RetroDarkerBg,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(RetroDarkChrome.copy(alpha = 0.3f))
                ) {
                    IconButton(onClick = { viewModel.skipToNext() }) {
                        Icon(
                            Icons.Default.SkipNext,
                            contentDescription = "Siguiente",
                            tint = RetroSilverMetallic,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                IconButton(onClick = { viewModel.cycleRepeatMode() }) {
                    Icon(
                        when (uiState.repeatMode) {
                            androidx.media3.common.Player.REPEAT_MODE_ONE -> Icons.Default.RepeatOne
                            else -> Icons.Default.Repeat
                        },
                        contentDescription = "Repetir",
                        tint = if (uiState.repeatMode != androidx.media3.common.Player.REPEAT_MODE_OFF)
                            RetroCyan else RetroSilverMetallic,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Visualizer controls + visualizer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (visualizerMode == VisualizerMode.BARS) "[ B ]" else "[ ~ ]",
                    color = RetroCyan.copy(alpha = 0.6f),
                    fontSize = 11.sp,
                    letterSpacing = 2.sp,
                    modifier = Modifier
                        .clickable {
                            visualizerMode = if (visualizerMode == VisualizerMode.BARS)
                                VisualizerMode.WAVE else VisualizerMode.BARS
                        }
                        .padding(vertical = 4.dp)
                )
                Text(
                    text = if (showVisualizer) "OCULTAR" else "MOSTRAR",
                    color = RetroDimGray.copy(alpha = 0.5f),
                    fontSize = 9.sp,
                    letterSpacing = 1.sp,
                    modifier = Modifier
                        .clickable { showVisualizer = !showVisualizer }
                        .padding(vertical = 4.dp)
                )
            }

            if (showVisualizer) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    AudioVisualizer(
                        player = viewModel.player,
                        mode = visualizerMode,
                        modifier = Modifier.fillMaxSize(),
                        accentColor = dominantColor
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            }
        }

        // Scanline overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(RetroSilver.copy(alpha = scanlineAlpha))
        )

        // Chrome bottom accent line
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            RetroSilverMetallic.copy(alpha = 0.4f),
                            RetroCyan.copy(alpha = 0.3f),
                            RetroSilverMetallic.copy(alpha = 0.4f),
                            Color.Transparent
                        )
                    )
                )
        )

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp),
            snackbar = { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = RetroPanelDark,
                    contentColor = RetroSilver,
                    actionColor = RetroCyan
                )
            }
        )
    }
}

private fun formatDuration(durationMs: Long): String {
    val safe = durationMs.coerceAtLeast(0)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(safe)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(safe) % 60
    return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
}
