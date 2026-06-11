package com.retrowave.player.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.retrowave.player.ui.theme.RetroChrome
import com.retrowave.player.ui.theme.RetroChromeGradientStart
import com.retrowave.player.ui.theme.RetroChromeShadow
import com.retrowave.player.ui.theme.RetroCyan
import com.retrowave.player.ui.theme.RetroDarkSurface
import com.retrowave.player.ui.theme.RetroDeepBlue
import com.retrowave.player.ui.theme.RetroMetallicBlue
import com.retrowave.player.ui.theme.RetroPanelDark
import com.retrowave.player.ui.theme.RetroSilver
import com.retrowave.player.ui.theme.RetroSilverMetallic

@Composable
fun RetroButton(
    icon: ImageVector? = null,
    text: String? = null,
    modifier: Modifier = Modifier,
    isActive: Boolean = false,
    activeColor: Color = RetroCyan,
    onClick: () -> Unit = {}
) {
    var isPressed by remember { mutableStateOf(false) }

    val offsetY by animateDpAsState(
        targetValue = if (isPressed) 1.dp else 0.dp,
        animationSpec = tween(80)
    )

    Box(
        modifier = modifier
            .offset(y = offsetY)
            .clip(RoundedCornerShape(6.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(RetroPanelDark, RetroDeepBlue)
                )
            )
            .border(
                width = 1.5.dp,
                brush = Brush.linearGradient(
                    colors = if (isPressed)
                        listOf(RetroChromeShadow, RetroChromeGradientStart)
                    else
                        listOf(RetroChromeGradientStart, RetroChromeShadow),
                    start = Offset.Zero,
                    end = Offset(500f, 500f)
                ),
                shape = RoundedCornerShape(6.dp)
            )
            .pointerInput(onClick) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = { onClick() }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = text,
                    tint = if (isActive) activeColor else RetroChrome,
                    modifier = Modifier.size(18.dp)
                )
            }
            if (text != null) {
                if (icon != null) Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = text,
                    color = if (isActive) activeColor else RetroSilver,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
