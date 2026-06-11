package com.retrowave.player.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.coroutines.delay

@Composable
fun MarqueeText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = Color.Unspecified
) {
    if (text.isEmpty()) return

    val scrollState = rememberScrollState()
    var textWidth by remember { mutableFloatStateOf(0f) }
    var containerWidth by remember { mutableFloatStateOf(0f) }
    val needsScroll = textWidth > containerWidth && containerWidth > 0f

    LaunchedEffect(text, needsScroll) {
        if (needsScroll) {
            while (true) {
                val maxScroll = (textWidth - containerWidth + 20f).toInt().coerceAtLeast(0)
                scrollState.animateScrollTo(
                    maxScroll,
                    animationSpec = tween(
                        durationMillis = (text.length * 150).coerceIn(3000, 12000),
                        easing = LinearEasing
                    )
                )
                delay(1500)
                scrollState.scrollTo(0)
                delay(500)
            }
        }
    }

    Box(
        modifier = modifier
            .clipToBounds()
            .onSizeChanged { containerWidth = it.width.toFloat() }
    ) {
        Text(
            text = text,
            style = style,
            color = color,
            maxLines = 1,
            overflow = TextOverflow.Clip,
            softWrap = false,
            modifier = Modifier
                .horizontalScroll(scrollState)
                .onSizeChanged { textWidth = it.width.toFloat() }
        )
    }
}
