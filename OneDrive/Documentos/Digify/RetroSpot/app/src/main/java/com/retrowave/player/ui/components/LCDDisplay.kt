package com.retrowave.player.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.retrowave.player.ui.theme.*

@Composable
fun LCDDisplay(
    line1: String,
    line2: String,
    modifier: Modifier = Modifier,
    color: Color = RetroVfdGreen,
    showIndicators: Boolean = true,
    scrollingText: Boolean = true
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .border(BorderStroke(1.dp, RetroMetallicBlue), RoundedCornerShape(4.dp))
            .background(RetroPanelDark)
            .padding(12.dp)
    ) {
        Column {
            if (showIndicators) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "STEREO",
                        style = MaterialTheme.typography.labelSmall,
                        color = color.copy(alpha = 0.7f)
                    )
                    Text(
                        text = "\u25B6",
                        style = MaterialTheme.typography.labelSmall,
                        color = color.copy(alpha = 0.7f)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "RDS",
                            style = MaterialTheme.typography.labelSmall,
                            color = color.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "PS",
                            style = MaterialTheme.typography.labelSmall,
                            color = color.copy(alpha = 0.2f)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
            }
            Text(
                text = line1,
                fontFamily = DigitalFont,
                fontSize = 20.sp,
                letterSpacing = 2.sp,
                color = color,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = line2,
                fontFamily = DigitalFont,
                fontSize = 12.sp,
                letterSpacing = 1.sp,
                color = color.copy(alpha = 0.7f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "0:00",
                    fontFamily = DigitalFont,
                    fontSize = 10.sp,
                    letterSpacing = 0.sp,
                    color = color.copy(alpha = 0.5f)
                )
                Text(
                    text = "4:32",
                    fontFamily = DigitalFont,
                    fontSize = 10.sp,
                    letterSpacing = 0.sp,
                    color = color.copy(alpha = 0.5f)
                )
            }
        }
        Canvas(modifier = Modifier.fillMaxSize()) {
            val dotSpacing = 4.dp.toPx()
            val scanlineSpacing = 3.dp.toPx()
            val scanlineThickness = 0.5.dp.toPx()
            val gridDotColor = color.copy(alpha = 0.08f)

            var gx = dotSpacing
            while (gx < size.width) {
                var gy = dotSpacing
                while (gy < size.height) {
                    drawCircle(
                        color = gridDotColor,
                        radius = 0.5f,
                        center = Offset(gx, gy)
                    )
                    gy += dotSpacing
                }
                gx += dotSpacing
            }

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        color.copy(alpha = 0.04f),
                        Color.Transparent
                    )
                ),
                radius = size.minDimension / 2f,
                center = Offset(size.width / 2f, size.height / 2f)
            )

            var sy = scanlineSpacing
            while (sy < size.height) {
                drawLine(
                    color = Color.Black.copy(alpha = 0.15f),
                    start = Offset(0f, sy),
                    end = Offset(size.width, sy),
                    strokeWidth = scanlineThickness
                )
                sy += scanlineSpacing
            }
        }
    }
}
