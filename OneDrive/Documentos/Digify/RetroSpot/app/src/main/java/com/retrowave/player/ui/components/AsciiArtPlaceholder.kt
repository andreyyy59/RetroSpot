package com.retrowave.player.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.retrowave.player.ui.theme.RetroCyan
import com.retrowave.player.ui.theme.RetroDeepBlue
import com.retrowave.player.ui.theme.RetroNeonBlue
import com.retrowave.player.ui.theme.RetroRedAccent
import com.retrowave.player.ui.theme.RetroVfdAmber
import com.retrowave.player.ui.theme.RetroVfdBlue
import com.retrowave.player.ui.theme.RetroVfdGreen
import kotlin.math.abs

private val asciiPalette = listOf(
    RetroVfdGreen, RetroCyan, RetroNeonBlue,
    RetroRedAccent, RetroVfdAmber, RetroVfdBlue
)

private val asciiChars = listOf(
    "█", "▓", "▒", "░", "▄", "▀", "▐", "▌",
    "▖", "▗", "▘", "▝", "▚", "▞", "╱", "╲",
    "◢", "◣", "◤", "◥", "⬒", "⬓", "⬔", "⬕"
)

fun songColor(seed: String): Color {
    val hash = abs(seed.hashCode())
    return asciiPalette[hash % asciiPalette.size]
}

@Composable
fun AsciiArtPlaceholder(
    seed: String,
    modifier: Modifier = Modifier
) {
    val baseColor = remember(seed) {
        val hash = abs(seed.hashCode())
        asciiPalette[hash % asciiPalette.size]
    }

    val chars = remember(seed) {
        val hash = abs(seed.hashCode())
        List(24) { asciiChars[(hash + it * 7) % asciiChars.size] }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "ascii")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    val drift by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "drift"
    )

    Canvas(modifier = modifier) {
        val cols = 6
        val cellW = size.width / cols
        val cellH = size.height / 4

        // Background
        drawRect(
            color = RetroDeepBlue,
            size = size
        )

        // Ascii grid
        for (row in 0 until 4) {
            for (col in 0 until cols) {
                val index = (row * cols + col) % chars.size
                val x = col * cellW
                val y = (row + drift * 0.3f) * cellH
                val alpha = ((row + col) % 3 + 1) * 0.15f + pulse * 0.3f
                drawAsciiBlock(
                    char = chars[index],
                    x = x,
                    y = y,
                    w = cellW,
                    h = cellH,
                    color = baseColor.copy(alpha = alpha.coerceIn(0f, 1f))
                )
            }
        }

        // Horizontal scan lines
        val lineCount = (size.height / 8).toInt()
        for (i in 0 until lineCount) {
            val y = i * 8 + (drift * 8).toInt() % 8
            drawLine(
                color = Color.White.copy(alpha = 0.03f + pulse * 0.02f),
                start = Offset(0f, y.toFloat()),
                end = Offset(size.width, y.toFloat()),
                strokeWidth = 1f
            )
        }
    }
}

private fun DrawScope.drawAsciiBlock(
    char: String,
    x: Float,
    y: Float,
    w: Float,
    h: Float,
    color: Color
) {
    when (char) {
        "█" -> drawRect(color = color, topLeft = Offset(x, y), size = androidx.compose.ui.geometry.Size(w, h))
        "▓" -> {
            drawRect(color = color, topLeft = Offset(x, y), size = androidx.compose.ui.geometry.Size(w, h))
            for (i in 0 until 3) {
                val stripeY = y + i * (h / 3)
                drawRect(
                    color = Color.Black.copy(alpha = 0.3f),
                    topLeft = Offset(x, stripeY),
                    size = androidx.compose.ui.geometry.Size(w, h / 6)
                )
            }
        }
        "▒" -> {
            drawRect(color = color.copy(alpha = color.alpha * 0.5f), topLeft = Offset(x, y), size = androidx.compose.ui.geometry.Size(w, h))
            for (i in 0 until 4) {
                val stripeY = y + i * (h / 4)
                drawRect(
                    color = Color.Black.copy(alpha = 0.15f),
                    topLeft = Offset(x, stripeY),
                    size = androidx.compose.ui.geometry.Size(w, h / 8)
                )
            }
        }
        "░" -> drawRect(color = color.copy(alpha = color.alpha * 0.25f), topLeft = Offset(x, y), size = androidx.compose.ui.geometry.Size(w, h))
        "▄" -> drawRect(color = color, topLeft = Offset(x, y + h / 2), size = androidx.compose.ui.geometry.Size(w, h / 2))
        "▀" -> drawRect(color = color, topLeft = Offset(x, y), size = androidx.compose.ui.geometry.Size(w, h / 2))
        "▐" -> drawRect(color = color, topLeft = Offset(x + w / 2, y), size = androidx.compose.ui.geometry.Size(w / 2, h))
        "▌" -> drawRect(color = color, topLeft = Offset(x, y), size = androidx.compose.ui.geometry.Size(w / 2, h))
        "╱" -> drawLine(color = color, start = Offset(x + w, y), end = Offset(x, y + h), strokeWidth = 2f)
        "╲" -> drawLine(color = color, start = Offset(x, y), end = Offset(x + w, y + h), strokeWidth = 2f)
        "◢" -> {
            drawRect(color = color, topLeft = Offset(x + w / 2, y + h / 2), size = androidx.compose.ui.geometry.Size(w / 2, h / 2))
            drawLine(color = color, start = Offset(x + w / 2, y + h), end = Offset(x + w, y + h / 2), strokeWidth = 2f)
        }
        "◣" -> {
            drawRect(color = color, topLeft = Offset(x, y + h / 2), size = androidx.compose.ui.geometry.Size(w / 2, h / 2))
            drawLine(color = color, start = Offset(x + w / 2, y + h), end = Offset(x, y + h / 2), strokeWidth = 2f)
        }
        "◤" -> {
            drawRect(color = color, topLeft = Offset(x, y), size = androidx.compose.ui.geometry.Size(w / 2, h / 2))
            drawLine(color = color, start = Offset(x, y + h / 2), end = Offset(x + w / 2, y), strokeWidth = 2f)
        }
        "◥" -> {
            drawRect(color = color, topLeft = Offset(x + w / 2, y), size = androidx.compose.ui.geometry.Size(w / 2, h / 2))
            drawLine(color = color, start = Offset(x + w / 2, y), end = Offset(x + w, y + h / 2), strokeWidth = 2f)
        }
        else -> {
            val cx = x + w / 2
            val cy = y + h / 2
            val s = minOf(w, h) * 0.3f
            drawRect(color = color, topLeft = Offset(cx - s / 2, cy - s / 2), size = androidx.compose.ui.geometry.Size(s, s))
        }
    }
}
