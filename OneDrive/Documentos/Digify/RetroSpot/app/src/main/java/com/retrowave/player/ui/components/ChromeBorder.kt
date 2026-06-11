package com.retrowave.player.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.retrowave.player.ui.theme.RetroChromeGradientEnd
import com.retrowave.player.ui.theme.RetroChromeGradientStart
import com.retrowave.player.ui.theme.RetroChromeShadow
import com.retrowave.player.ui.theme.RetroCyan
import com.retrowave.player.ui.theme.RetroSilver

fun Modifier.chromeBorder(
    width: Dp = 2.dp,
    cornerRadius: Dp = 8.dp,
    glowColor: Color = RetroCyan.copy(alpha = 0.3f)
): Modifier {
    var result = this
    if (glowColor.alpha > 0.01f) {
        result = result.border(
            width = width + 2.dp,
            brush = Brush.horizontalGradient(
                colors = listOf(
                    glowColor.copy(alpha = 0.05f),
                    glowColor,
                    glowColor.copy(alpha = 0.05f)
                )
            ),
            shape = RoundedCornerShape(cornerRadius + 2.dp)
        )
    }
    return result
        .border(
            width = width + 1.dp,
            brush = Brush.horizontalGradient(
                colors = listOf(
                    RetroChromeShadow,
                    RetroChromeGradientEnd,
                    RetroChromeGradientStart,
                    RetroChromeShadow
                )
            ),
            shape = RoundedCornerShape(cornerRadius)
        )
        .border(
            width = width,
            brush = Brush.horizontalGradient(
                colors = listOf(
                    RetroSilver,
                    RetroChromeGradientStart,
                    RetroChromeGradientEnd,
                    RetroSilver
                )
            ),
            shape = RoundedCornerShape(cornerRadius + 1.dp)
        )
}
