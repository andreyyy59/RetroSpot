package com.retrowave.player.ui.player

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.retrowave.player.domain.model.Song
import com.retrowave.player.ui.components.MarqueeText
import com.retrowave.player.ui.components.chromeBorder
import com.retrowave.player.ui.theme.*

@Composable
fun MiniPlayer(
    song: Song?,
    isPlaying: Boolean,
    isVisible: Boolean,
    currentPosition: Long = 0,
    duration: Long = 0,
    onPlayPause: () -> Unit,
    onExpand: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isVisible && song != null,
        enter = slideInVertically(animationSpec = tween(300)) { it } +
                fadeIn(animationSpec = tween(300)),
        exit = slideOutVertically(animationSpec = tween(300)) { it } +
                fadeOut(animationSpec = tween(300)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .chromeBorder(cornerRadius = 14.dp)
                    .background(RetroPanelDark)
                    .clickable(onClick = onExpand)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Album art thumbnail
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(RetroDeepBlue)
                    ) {
                        if (song?.albumArtUri != null) {
                            AsyncImage(
                                model = song.albumArtUri,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.linearGradient(
                                            colors = listOf(RetroMetallicBlue, RetroDeepBlue)
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = song?.title?.take(1)?.uppercase() ?: "?",
                                    color = RetroChrome.copy(alpha = 0.5f),
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Song info
                    Column(modifier = Modifier.weight(1f)) {
                        MarqueeText(
                            text = song?.title?.uppercase() ?: "",
                            color = RetroSilver,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = song?.artist?.uppercase() ?: "",
                            color = RetroDimGray,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Expand button
                    IconButton(
                        onClick = onExpand,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            Icons.Default.ExpandLess,
                            contentDescription = "Abrir reproductor",
                            tint = RetroChrome,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Play/Pause
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(RetroChromeGradientStart, RetroChrome)
                                )
                            )
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onPlayPause
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pausar" else "Reproducir",
                            tint = RetroDeepBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // Close
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = RetroDimGray,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Progress bar below the chrome border (visible, not overlapped)
            val progress = if (duration > 0)
                (currentPosition.toFloat() / duration).coerceIn(0f, 1f) else 0f
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(RetroPanelMid)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .fillMaxHeight()
                        .background(Color.White.copy(alpha = 0.9f))
                )
            }
        }
    }
}
