package com.retrowave.player.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.Canvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val FONT_5x7: Map<Char, ByteArray> = mapOf(
    'A' to byteArrayOf(0b01110, 0b10001, 0b10001, 0b11111, 0b10001, 0b10001, 0b10001),
    'B' to byteArrayOf(0b11110, 0b10001, 0b10001, 0b11110, 0b10001, 0b10001, 0b11110),
    'C' to byteArrayOf(0b01110, 0b10001, 0b10000, 0b10000, 0b10000, 0b10001, 0b01110),
    'D' to byteArrayOf(0b11110, 0b10001, 0b10001, 0b10001, 0b10001, 0b10001, 0b11110),
    'E' to byteArrayOf(0b11111, 0b10000, 0b10000, 0b11110, 0b10000, 0b10000, 0b11111),
    'F' to byteArrayOf(0b11111, 0b10000, 0b10000, 0b11110, 0b10000, 0b10000, 0b10000),
    'G' to byteArrayOf(0b01110, 0b10001, 0b10000, 0b10111, 0b10001, 0b10001, 0b01110),
    'H' to byteArrayOf(0b10001, 0b10001, 0b10001, 0b11111, 0b10001, 0b10001, 0b10001),
    'I' to byteArrayOf(0b01110, 0b00100, 0b00100, 0b00100, 0b00100, 0b00100, 0b01110),
    'J' to byteArrayOf(0b00111, 0b00010, 0b00010, 0b00010, 0b00010, 0b10010, 0b01100),
    'K' to byteArrayOf(0b10001, 0b10010, 0b10100, 0b11000, 0b10100, 0b10010, 0b10001),
    'L' to byteArrayOf(0b10000, 0b10000, 0b10000, 0b10000, 0b10000, 0b10000, 0b11111),
    'M' to byteArrayOf(0b10001, 0b11011, 0b10101, 0b10101, 0b10001, 0b10001, 0b10001),
    'N' to byteArrayOf(0b10001, 0b11001, 0b10101, 0b10011, 0b10001, 0b10001, 0b10001),
    'O' to byteArrayOf(0b01110, 0b10001, 0b10001, 0b10001, 0b10001, 0b10001, 0b01110),
    'P' to byteArrayOf(0b11110, 0b10001, 0b10001, 0b11110, 0b10000, 0b10000, 0b10000),
    'Q' to byteArrayOf(0b01110, 0b10001, 0b10001, 0b10001, 0b10101, 0b10010, 0b01101),
    'R' to byteArrayOf(0b11110, 0b10001, 0b10001, 0b11110, 0b10100, 0b10010, 0b10001),
    'S' to byteArrayOf(0b01110, 0b10001, 0b10000, 0b01110, 0b00001, 0b10001, 0b01110),
    'T' to byteArrayOf(0b11111, 0b00100, 0b00100, 0b00100, 0b00100, 0b00100, 0b00100),
    'U' to byteArrayOf(0b10001, 0b10001, 0b10001, 0b10001, 0b10001, 0b10001, 0b01110),
    'V' to byteArrayOf(0b10001, 0b10001, 0b10001, 0b10001, 0b10001, 0b01010, 0b00100),
    'W' to byteArrayOf(0b10001, 0b10001, 0b10001, 0b10101, 0b10101, 0b11011, 0b10001),
    'X' to byteArrayOf(0b10001, 0b10001, 0b01010, 0b00100, 0b01010, 0b10001, 0b10001),
    'Y' to byteArrayOf(0b10001, 0b10001, 0b01010, 0b00100, 0b00100, 0b00100, 0b00100),
    'Z' to byteArrayOf(0b11111, 0b00001, 0b00010, 0b00100, 0b01000, 0b10000, 0b11111),
    '0' to byteArrayOf(0b01110, 0b10001, 0b10011, 0b10101, 0b11001, 0b10001, 0b01110),
    '1' to byteArrayOf(0b00100, 0b01100, 0b00100, 0b00100, 0b00100, 0b00100, 0b01110),
    '2' to byteArrayOf(0b01110, 0b10001, 0b00001, 0b00010, 0b00100, 0b01000, 0b11111),
    '3' to byteArrayOf(0b01110, 0b10001, 0b00001, 0b00110, 0b00001, 0b10001, 0b01110),
    '4' to byteArrayOf(0b00010, 0b00110, 0b01010, 0b10010, 0b11111, 0b00010, 0b00010),
    '5' to byteArrayOf(0b11111, 0b10000, 0b11110, 0b00001, 0b00001, 0b10001, 0b01110),
    '6' to byteArrayOf(0b01110, 0b10001, 0b10000, 0b11110, 0b10001, 0b10001, 0b01110),
    '7' to byteArrayOf(0b11111, 0b00001, 0b00010, 0b00100, 0b01000, 0b01000, 0b01000),
    '8' to byteArrayOf(0b01110, 0b10001, 0b10001, 0b01110, 0b10001, 0b10001, 0b01110),
    '9' to byteArrayOf(0b01110, 0b10001, 0b10001, 0b01111, 0b00001, 0b10001, 0b01110),
    ' ' to byteArrayOf(0b00000, 0b00000, 0b00000, 0b00000, 0b00000, 0b00000, 0b00000),
    '-' to byteArrayOf(0b00000, 0b00000, 0b00000, 0b11111, 0b00000, 0b00000, 0b00000),
    '.' to byteArrayOf(0b00000, 0b00000, 0b00000, 0b00000, 0b00000, 0b00000, 0b00100),
    '!' to byteArrayOf(0b00100, 0b00100, 0b00100, 0b00100, 0b00100, 0b00000, 0b00100),
    '?' to byteArrayOf(0b01110, 0b10001, 0b00001, 0b00110, 0b00100, 0b00000, 0b00100),
)

private const val CHAR_W = 5
private const val CHAR_H = 7
private const val CHAR_SPACING = 1

@Composable
fun PixelText(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
    pixelSize: Dp = 4.dp
) {
    val density = LocalDensity.current
    val pxSize = with(density) { pixelSize.toPx() }

    val upper = text.uppercase()
    val totalWidth = upper.sumOf { CHAR_W + CHAR_SPACING } * pxSize
    val totalHeight = CHAR_H * pxSize

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(with(density) { totalHeight.toDp() })
    ) {
        var cursorX = 0f

        for (ch in upper) {
            val bitmap = FONT_5x7[ch] ?: FONT_5x7[' ']!!
            for (row in 0 until CHAR_H) {
                val bits = bitmap[row].toInt() and 0xFF
                for (col in 0 until CHAR_W) {
                    if ((bits shr (CHAR_W - 1 - col) and 1) == 1) {
                        drawRect(
                            color = color,
                            topLeft = Offset(
                                cursorX + col * pxSize,
                                row * pxSize
                            ),
                            size = Size(pxSize, pxSize)
                        )
                    }
                }
            }
            cursorX += (CHAR_W + CHAR_SPACING) * pxSize
        }
    }
}
