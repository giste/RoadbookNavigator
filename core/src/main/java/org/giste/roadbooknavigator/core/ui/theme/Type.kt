/*
 * Copyright (C) 2026  Giste
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.giste.roadbooknavigator.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.sp

/**
 * Material 3 standard baseline.
 * Internal reference used to generate scaled versions.
 */
private val m3Base = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)

/**
 * Extension to scale all text styles in a Typography set by a factor.
 */
private fun TextStyle.scaled(factor: Float): TextStyle {
    return this.copy(
        fontSize = if (fontSize.type == TextUnitType.Sp) (fontSize.value * factor).sp else fontSize,
        lineHeight = if (lineHeight.type == TextUnitType.Sp) (lineHeight.value * factor).sp else lineHeight
    )
}

/**
 * Creates a Typography set by scaling the standard Material 3 tokens.
 */
fun createTypography(scaleFactor: Float): Typography {
    return Typography(
        displayLarge = m3Base.displayLarge.scaled(scaleFactor),
        displayMedium = m3Base.displayMedium.scaled(scaleFactor),
        displaySmall = m3Base.displaySmall.scaled(scaleFactor),
        headlineLarge = m3Base.headlineLarge.scaled(scaleFactor),
        headlineMedium = m3Base.headlineMedium.scaled(scaleFactor),
        headlineSmall = m3Base.headlineSmall.scaled(scaleFactor),
        titleLarge = m3Base.titleLarge.scaled(scaleFactor),
        titleMedium = m3Base.titleMedium.scaled(scaleFactor),
        titleSmall = m3Base.titleSmall.scaled(scaleFactor),
        bodyLarge = m3Base.bodyLarge.scaled(scaleFactor),
        bodyMedium = m3Base.bodyMedium.scaled(scaleFactor),
        bodySmall = m3Base.bodySmall.scaled(scaleFactor),
        labelLarge = m3Base.labelLarge.scaled(scaleFactor),
        labelMedium = m3Base.labelMedium.scaled(scaleFactor),
        labelSmall = m3Base.labelSmall.scaled(scaleFactor)
    )
}

/**
 * Final typography sets used by the app theme.
 * Standard scale for compact devices (phones).
 */
val compactTypography = createTypography(scaleFactor = 1.0f)

/**
 * Slightly enlarged scale for expanded devices (tablets).
 */
val expandedTypography = createTypography(scaleFactor = 1.125f)
