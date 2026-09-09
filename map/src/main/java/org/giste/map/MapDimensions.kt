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

package org.giste.map

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Dimensions specific to the Map module.
 */
@Immutable
public data class MapDimensions(
    val paddingSmall: Dp,
    val paddingMedium: Dp,
    val paddingLarge: Dp,
    val iconSize: Dp,
    val actionIconSize: Dp,
    val sectionBorder: Dp,
)

public val compactMapDimensions: MapDimensions = MapDimensions(
    paddingSmall = 4.dp,
    paddingMedium = 8.dp,
    paddingLarge = 16.dp,
    iconSize = 24.dp,
    actionIconSize = 48.dp,
    sectionBorder = 1.dp,
)

public val expandedMapDimensions: MapDimensions = MapDimensions(
    paddingSmall = 4.dp,
    paddingMedium = 12.dp,
    paddingLarge = 24.dp,
    iconSize = 32.dp,
    actionIconSize = 56.dp,
    sectionBorder = 1.dp,
)

internal val LocalMapDimensions = staticCompositionLocalOf { compactMapDimensions }
