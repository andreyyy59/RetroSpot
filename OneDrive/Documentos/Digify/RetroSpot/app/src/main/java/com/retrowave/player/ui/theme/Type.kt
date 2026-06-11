package com.retrowave.player.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.retrowave.player.R

val DigitalFont = FontFamily(
    Font(R.font.vt323_regular, FontWeight.Normal)
)

val DSDigitalFont = FontFamily(
    Font(R.font.ds_digi, FontWeight.Normal),
    Font(R.font.ds_digib, FontWeight.Bold),
    Font(R.font.ds_digii, FontWeight.Normal, style = androidx.compose.ui.text.font.FontStyle.Italic),
    Font(R.font.ds_digit, FontWeight.Bold, style = androidx.compose.ui.text.font.FontStyle.Italic)
)

val RetroTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = DigitalFont,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        letterSpacing = 3.sp,
        lineHeight = 40.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = DigitalFont,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        letterSpacing = 2.sp,
        lineHeight = 32.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = DigitalFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        letterSpacing = 1.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = DSDigitalFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        letterSpacing = 1.sp
    ),
    titleMedium = TextStyle(
        fontFamily = DSDigitalFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 1.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = DSDigitalFont,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = DSDigitalFont,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        letterSpacing = 0.5.sp
    ),
    labelLarge = TextStyle(
        fontFamily = DSDigitalFont,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        letterSpacing = 1.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = DSDigitalFont,
        fontWeight = FontWeight.Normal,
        fontSize = 9.sp,
        letterSpacing = 1.sp
    )
)
