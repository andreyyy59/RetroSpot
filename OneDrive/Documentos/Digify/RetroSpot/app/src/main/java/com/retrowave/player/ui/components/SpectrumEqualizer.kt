package com.retrowave.player.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.retrowave.player.ui.theme.RetroLcdGreen
import com.retrowave.player.ui.theme.SpecGreen
import com.retrowave.player.ui.theme.SpecOrange
import com.retrowave.player.ui.theme.SpecRed
import com.retrowave.player.ui.theme.SpecYellow
import kotlin.random.Random
import kotlinx.coroutines.delay

@Composable
fun SpectrumEqualizer(
    modifier: Modifier = Modifier,
    levels: List<Float> = List(16) { Random.nextFloat() * 0.8f + 0.2f },
    barCount: Int = 16,
    color: Color = RetroLcdGreen,
    peakHold: Boolean = true
) {
    val segmentsPerBar = 7
    val peakValues = remember { FloatArray(barCount) { 0f } }

    LaunchedEffect(Unit) {
        while (true) {
            delay(50)
            for (i in levels.indices) {
                if (i < peakValues.size) {
                    val lvl = levels.getOrElse(i) { 0f }
                    peakValues[i] = maxOf(peakValues[i] * 0.93f, lvl)
                }
            }
        }
    }

    Canvas(modifier = modifier) {
        val gap = size.width / barCount * 0.15f
        val barW = (size.width / barCount) - gap
        val segH = (size.height * 0.85f) / segmentsPerBar

        drawRect(color = Color(0xFF0A0F1A), size = size)

        for (i in 0 until barCount) {
            val level = levels.getOrElse(i) { 0f }
            val filledSegs = (level * segmentsPerBar).toInt().coerceIn(0, segmentsPerBar)
            val x = i * (barW + gap) + gap / 2f

            if (i > 0) {
                val sepX = i * (barW + gap) - gap / 2f
                drawRect(
                    color = Color.White.copy(alpha = 0.03f),
                    topLeft = Offset(sepX, 0f),
                    size = Size(1f, size.height)
                )
            }

            for (seg in 0 until segmentsPerBar) {
                val isFilled = seg < filledSegs
                val segY = size.height - segH * (seg + 1) - 2.dp.toPx()
                val segRatio = seg.toFloat() / segmentsPerBar

                val segColor = when {
                    segRatio < 0.4f -> SpecGreen
                    segRatio < 0.7f -> SpecYellow
                    segRatio < 0.9f -> SpecOrange
                    else -> SpecRed
                }

                val fillColor = if (isFilled) segColor else segColor.copy(alpha = 0.06f)

                drawRoundRect(
                    color = fillColor,
                    topLeft = Offset(x, segY),
                    size = Size(barW, segH - 1.dp.toPx()),
                    cornerRadius = CornerRadius(1.dp.toPx())
                )

                if (isFilled) {
                    drawRoundRect(
                        color = segColor.copy(alpha = 0.2f),
                        topLeft = Offset(x, segY),
                        size = Size(barW, (segH - 1.dp.toPx()) * 0.5f),
                        cornerRadius = CornerRadius(1.dp.toPx())
                    )
                }
            }

            if (peakHold && i < peakValues.size) {
                val peakLevel = peakValues[i]
                val peakSegs = (peakLevel * segmentsPerBar).toInt().coerceIn(0, segmentsPerBar)
                val peakY = size.height - peakSegs * segH - 2.dp.toPx()

                drawCircle(
                    color = SpecRed,
                    radius = 2.5.dp.toPx(),
                    center = Offset(x + barW / 2f, peakY)
                )
                drawCircle(
                    color = SpecRed.copy(alpha = 0.3f),
                    radius = 5.dp.toPx(),
                    center = Offset(x + barW / 2f, peakY)
                )
            }
        }
    }
}
