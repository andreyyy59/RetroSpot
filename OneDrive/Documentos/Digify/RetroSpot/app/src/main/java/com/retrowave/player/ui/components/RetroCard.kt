package com.retrowave.player.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.retrowave.player.ui.theme.RetroChrome
import com.retrowave.player.ui.theme.RetroDarkSurface
import com.retrowave.player.ui.components.MarqueeText
import com.retrowave.player.ui.theme.RetroDeepBlue
import com.retrowave.player.ui.theme.RetroDimGray
import com.retrowave.player.ui.theme.RetroSilver

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RetroCard(
    title: String,
    subtitle: String,
    imageUri: String?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    asciiArtSeed: String? = null
) {
    Column(
        modifier = modifier
            .width(150.dp)
            .shadow(4.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(RetroDarkSurface, RetroDeepBlue)
                )
            )
            .chromeBorder(cornerRadius = 12.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            contentAlignment = Alignment.Center
        ) {
            if (!imageUri.isNullOrBlank()) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else if (asciiArtSeed != null) {
                AsciiArtPlaceholder(
                    seed = asciiArtSeed,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(songColor(title).copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title.take(2).uppercase(),
                        color = RetroChrome,
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        MarqueeText(
            text = title,
            color = RetroSilver,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
        Text(
            text = subtitle,
            color = RetroDimGray,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}
