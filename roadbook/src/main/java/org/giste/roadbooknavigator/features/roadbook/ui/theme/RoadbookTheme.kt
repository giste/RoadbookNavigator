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

package org.giste.roadbooknavigator.features.roadbook.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

/**
 * Access object for the Roadbook specific theme properties.
 */
public object RoadbookTheme {
    public val dimensions: RoadbookDimensions
        @Composable
        @ReadOnlyComposable
        get() = LocalRoadbookDimensions.current
}

/**
 * Theme wrapper for the Roadbook module.
 * Provides custom dimensions and ensures they are accessible to roadbook components.
 *
 * @param dimensions The dimensions to use. Defaults to compact if not provided.
 * @param useDarkTheme Whether to use a dark color scheme. If null, it will not wrap MaterialTheme.
 * @param content The composable content.
 */
@Composable
public fun RoadbookTheme(
    dimensions: RoadbookDimensions = compactRoadbookDimensions,
    useDarkTheme: Boolean? = null,
    content: @Composable () -> Unit
) {
    val themeContent = @Composable {
        CompositionLocalProvider(
            LocalRoadbookDimensions provides dimensions,
            content = content
        )
    }

    if (useDarkTheme != null) {
        MaterialTheme(
            colorScheme = if (useDarkTheme) darkColorScheme() else lightColorScheme(),
            content = themeContent
        )
    } else {
        themeContent()
    }
}
