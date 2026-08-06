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

package org.giste.roadbook.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import org.giste.roadbook.LocalRoadbookColors
import org.giste.roadbook.LocalRoadbookDimensions
import org.giste.roadbook.RoadbookColors
import org.giste.roadbook.RoadbookDimensions
import org.giste.roadbook.compactRoadbookDimensions

/**
 * Access object for the Roadbook specific theme properties.
 */
internal object RoadbookTheme {
    val dimensions: RoadbookDimensions
        @Composable
        @ReadOnlyComposable
        get() = LocalRoadbookDimensions.current

    val colors: RoadbookColors
        @Composable
        @ReadOnlyComposable
        get() = LocalRoadbookColors.current ?: calculateDefaultColors()

    @Composable
    @ReadOnlyComposable
    internal fun calculateDefaultColors(): RoadbookColors {
        val colorScheme = MaterialTheme.colorScheme
        return RoadbookColors(
            background = colorScheme.surface,
            onBackground = colorScheme.onSurface,
            track = colorScheme.primary,
            trackSecondary = colorScheme.secondary,
            danger = colorScheme.error,
            shortDistanceBackground = colorScheme.tertiaryContainer,
            onShortDistanceBackground = colorScheme.onTertiaryContainer,
            border = colorScheme.outline,
            divider = colorScheme.onSurface,
            waypointNumberBackground = colorScheme.inverseSurface,
            onWaypointNumberBackground = colorScheme.inverseOnSurface
        )
    }
}

/**
 * Theme wrapper for the Roadbook module.
 * Provides custom dimensions and semantic colors to roadbook components.
 *
 * @param dimensions The dimensions to use. Defaults to compact if not provided.
 * @param colors Custom semantic colors. If null, they will be mapped from [MaterialTheme.colorScheme].
 * @param useDarkTheme Whether to use a dark color scheme. If null, it will not wrap MaterialTheme.
 * @param content The composable content.
 */
@Composable
internal fun RoadbookTheme(
    dimensions: RoadbookDimensions = compactRoadbookDimensions,
    colors: RoadbookColors? = null,
    useDarkTheme: Boolean? = null,
    content: @Composable () -> Unit
) {
    val colorScheme = if (useDarkTheme != null) {
        if (useDarkTheme) darkColorScheme() else lightColorScheme()
    } else {
        MaterialTheme.colorScheme
    }

    val roadbookColors = colors ?: RoadbookColors(
        background = colorScheme.surface,
        onBackground = colorScheme.onSurface,
        track = colorScheme.primary,
        trackSecondary = colorScheme.secondary,
        danger = colorScheme.error,
        shortDistanceBackground = colorScheme.tertiaryContainer,
        onShortDistanceBackground = colorScheme.onTertiaryContainer,
        border = colorScheme.outline,
        divider = colorScheme.onSurface,
        waypointNumberBackground = colorScheme.inverseSurface,
        onWaypointNumberBackground = colorScheme.inverseOnSurface
    )

    val themeContent = @Composable {
        CompositionLocalProvider(
            LocalRoadbookDimensions provides dimensions,
            LocalRoadbookColors provides roadbookColors,
            content = content
        )
    }

    if (useDarkTheme != null) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = themeContent
        )
    } else {
        themeContent()
    }
}
