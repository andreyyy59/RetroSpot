package com.retrowave.player.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.retrowave.player.ui.theme.RetroChrome
import com.retrowave.player.ui.theme.RetroChromeGradientEnd
import com.retrowave.player.ui.theme.RetroChromeGradientStart
import com.retrowave.player.ui.theme.RetroChromeShadow
import com.retrowave.player.ui.theme.RetroCyan
import com.retrowave.player.ui.theme.RetroDarkChrome
import com.retrowave.player.ui.theme.RetroDeepBlue
import com.retrowave.player.ui.theme.RetroPanelDark
import com.retrowave.player.ui.theme.RetroSilverMetallic
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RetroKnob(
    modifier: Modifier = Modifier,
    progress: Float = 0.5f,
    label: String = "VOL",
    knobSize: Dp = 64.dp,
    activeColor: Color = RetroCyan
) {
    Column(
        modifier = modifier.width(knobSize + 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.size(knobSize)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2, size.height / 2)
                val radius = size.minDimension / 2

                // 3D shadow
                drawCircle(
                    color = Color.Black.copy(alpha = 0.4f),
                    radius = radius,
                    center = Offset(center.x + 2.5.dp.toPx(), center.y + 2.5.dp.toPx())
                )

                // Grip ridges on outer ring
                val gripRadius = radius - 1.dp.toPx()
                val ridgeCount = 24
                for (i in 0 until ridgeCount) {
                    val angle = (360f / ridgeCount) * i
                    val isLit = i % 2 == 0
                    drawArc(
                        color = if (isLit) RetroSilverMetallic else RetroDarkChrome,
                        startAngle = angle,
                        sweepAngle = 360f / ridgeCount * 0.7f,
                        useCenter = false,
                        topLeft = Offset(
                            center.x - gripRadius,
                            center.y - gripRadius
                        ),
                        size = Size(gripRadius * 2, gripRadius * 2),
                        style = Stroke(width = 2.dp.toPx())
                    )
                }

                // Chrome ring just inside grip
                val chromeRadius = radius - 3.dp.toPx()
                drawCircle(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            RetroChromeGradientStart,
                            RetroChrome,
                            RetroChromeGradientEnd,
                            RetroChromeShadow,
                            RetroChromeGradientStart
                        )
                    ),
                    radius = chromeRadius,
                    center = center,
                    style = Stroke(width = 1.5.dp.toPx())
                )

                // Knob body (dark with 3D radial gradient)
                val bodyRadius = radius - 5.dp.toPx()
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            RetroDeepBlue.copy(alpha = 0.9f),
                            RetroPanelDark
                        ),
                        center = Offset(
                            center.x - bodyRadius * 0.2f,
                            center.y - bodyRadius * 0.2f
                        )
                    ),
                    radius = bodyRadius,
                    center = center
                )

                // Progress arc
                val arcRadius = chromeRadius - 1.dp.toPx()
                drawArc(
                    brush = Brush.sweepGradient(
                        colors = listOf(activeColor, activeColor.copy(alpha = 0.3f))
                    ),
                    startAngle = -135f,
                    sweepAngle = 270f * progress,
                    useCenter = false,
                    topLeft = Offset(
                        center.x - arcRadius,
                        center.y - arcRadius
                    ),
                    size = Size(arcRadius * 2, arcRadius * 2),
                    style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                )

                // Indicator notch
                val notchAngleDeg = -135f + 270f * progress
                val notchRad = Math.toRadians(notchAngleDeg.toDouble())
                val notchRadius = bodyRadius * 0.7f
                drawCircle(
                    color = RetroChrome,
                    radius = 1.5.dp.toPx(),
                    center = Offset(
                        center.x + (notchRadius * cos(notchRad)).toFloat(),
                        center.y + (notchRadius * sin(notchRad)).toFloat()
                    )
                )
                // Line from center toward notch
                drawLine(
                    color = RetroChrome.copy(alpha = 0.5f),
                    start = Offset(
                        center.x + (notchRadius * 0.5f * cos(notchRad)).toFloat(),
                        center.y + (notchRadius * 0.5f * sin(notchRad)).toFloat()
                    ),
                    end = Offset(
                        center.x + (notchRadius * 0.9f * cos(notchRad)).toFloat(),
                        center.y + (notchRadius * 0.9f * sin(notchRad)).toFloat()
                    ),
                    strokeWidth = 1.dp.toPx()
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = RetroChrome,
            textAlign = TextAlign.Center
        )
    }
}
