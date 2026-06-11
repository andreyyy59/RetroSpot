package com.retrowave.player.ui.components

import android.media.audiofx.Visualizer
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import androidx.media3.exoplayer.ExoPlayer
import kotlin.math.ln
import kotlin.math.sqrt

enum class VisualizerMode { BARS, WAVE }

@Composable
fun AudioVisualizer(
    player: ExoPlayer?,
    mode: VisualizerMode,
    modifier: Modifier = Modifier,
    accentColor: Color? = null
) {
    val magnitudes = remember { mutableStateOf(FloatArray(32)) }
    val waveform = remember { mutableStateOf(FloatArray(128)) }
    val hasData = remember { mutableStateOf(false) }

    val audioSessionId = player?.audioSessionId ?: 0

    val visualizer = remember(audioSessionId) {
        if (audioSessionId == 0) return@remember null
        try {
            val capSize = Visualizer.getCaptureSizeRange()[1]
            Visualizer(audioSessionId).apply {
                enabled = false
                captureSize = capSize
                setDataCaptureListener(
                    object : Visualizer.OnDataCaptureListener {
                        override fun onWaveFormDataCapture(
                            visualizer: Visualizer?,
                            waveformData: ByteArray?,
                            samplingRate: Int
                        ) {
                            if (waveformData != null && waveformData.isNotEmpty()) {
                                val wf = FloatArray(128)
                                for (i in wf.indices) {
                                    val byteIdx = (i * waveformData.size / 128).coerceAtMost(waveformData.size - 1)
                                    wf[i] = waveformData[byteIdx].toFloat() / 128f
                                }
                                waveform.value = wf
                                hasData.value = true
                            }
                        }

                        override fun onFftDataCapture(
                            visualizer: Visualizer?,
                            fftData: ByteArray?,
                            samplingRate: Int
                        ) {
                            if (fftData != null && fftData.isNotEmpty()) {
                                val mag = FloatArray(32)
                                val n = fftData.size / 2
                                for (i in 0 until 32.coerceAtMost(n)) {
                                    val real = fftData[i * 2].toFloat()
                                    val imag = fftData[i * 2 + 1].toFloat()
                                    val value = sqrt(real * real + imag * imag) / 64f
                                    mag[i] = value.coerceIn(0f, 1f)
                                }
                                magnitudes.value = mag
                                hasData.value = true
                            }
                        }
                    },
                    Visualizer.getMaxCaptureRate() / 2,
                    true,
                    true
                )
                enabled = true
            }
        } catch (e: Exception) {
            null
        }
    }

    LaunchedEffect(visualizer) {
        if (visualizer != null && !visualizer.enabled) {
            try {
                visualizer.enabled = true
            } catch (_: Exception) {}
        }
    }

    DisposableEffect(audioSessionId) {
        onDispose {
            try {
                visualizer?.enabled = false
                visualizer?.release()
            } catch (_: Exception) {}
        }
    }

    val barsColor = accentColor ?: Color(0xFF00FF88)
    val waveColor = accentColor?.let { c ->
        val lum = 0.299f * c.red + 0.587f * c.green + 0.114f * c.blue
        if (lum > 0.5f) {
            c.copy(alpha = 0.9f)
        } else {
            Color(
                (c.red + 0.5f).coerceAtMost(1f),
                (c.green + 0.5f).coerceAtMost(1f),
                (c.blue + 0.5f).coerceAtMost(1f),
                0.9f
            )
        }
    } ?: Color(0xFF00CCFF)

    Canvas(modifier = modifier) {
        if (!hasData.value) return@Canvas
        when (mode) {
            VisualizerMode.BARS -> drawBars(magnitudes.value, barsColor)
            VisualizerMode.WAVE -> drawWave(waveform.value, waveColor)
        }
    }
}

private const val MAX_BARS = 32
private const val WAVE_SAMPLES = 128

private fun DrawScope.drawBars(magnitudes: FloatArray, color: Color) {
    val barCount = MAX_BARS.coerceAtMost(magnitudes.size)
    if (barCount == 0) return

    val barWidth = size.width / barCount
    val gap = barWidth * 0.15f
    val barMaxHeight = size.height * 0.85f

    for (i in 0 until barCount) {
        val raw = magnitudes[i].coerceIn(0f, 1f)
        val scaled = ln(1 + raw * 9).toFloat() / ln(10f)

        val barHeight = (scaled * barMaxHeight).coerceAtLeast(2f)
        val x = i * barWidth + gap / 2
        val y = size.height - barHeight

        val segmentCount = 8
        val segHeight = barHeight / segmentCount
        val segWidth = barWidth - gap

        for (seg in 0 until segmentCount) {
            val segY = y + seg * segHeight
            val segFraction = seg.toFloat() / segmentCount
            val darkBase = Color(
                (color.red * 0.15f).coerceIn(0f, 1f),
                (color.green * 0.15f).coerceIn(0f, 1f),
                (color.blue * 0.15f).coerceIn(0f, 1f),
                1f
            )
            val segColor = lerp(darkBase, color, segFraction)
            val alpha = ((1f - segFraction * 0.3f) * 0.6f + 0.4f).coerceIn(0f, 1f)

            drawRect(
                color = segColor.copy(alpha = alpha),
                topLeft = Offset(x + 1f, segY + 1f),
                size = Size(segWidth - 2f, segHeight - 2f)
            )
        }

        val peakY = y - 4f
        if (peakY > 0) {
            drawCircle(
                color = color.copy(alpha = 0.8f),
                radius = 3f,
                center = Offset(x + barWidth / 2, peakY)
            )
        }
    }
}

private fun DrawScope.drawWave(waveform: FloatArray, color: Color) {
    if (waveform.isEmpty()) return

    val path = Path()
    val stepX = size.width / WAVE_SAMPLES
    val midY = size.height / 2f
    val ampScale = size.height * 0.4f

    path.moveTo(0f, midY)
    for (i in 1 until WAVE_SAMPLES.coerceAtMost(waveform.size)) {
        val x = i * stepX
        val y = midY + waveform[i] * ampScale
        path.lineTo(x, y)
    }

    drawPath(
        path = path,
        color = color.copy(alpha = 0.8f),
        style = Stroke(width = 2.5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
    )

    val ghostPath = Path()
    ghostPath.moveTo(0f, midY)
    for (i in 1 until WAVE_SAMPLES.coerceAtMost(waveform.size)) {
        val x = i * stepX
        val y = midY + waveform[i] * ampScale * 0.6f
        ghostPath.lineTo(x, y)
    }
    drawPath(
        path = ghostPath,
        color = color.copy(alpha = 0.25f),
        style = Stroke(width = 5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
    )
}
