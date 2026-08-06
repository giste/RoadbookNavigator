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

package org.giste.roadbook

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Semantic color system for the Roadbook module.
 * This decouples the module from specific Material 3 color slots and improves readability.
 */
@Immutable
public data class RoadbookColors(
    val background: Color,
    val onBackground: Color,
    val track: Color,
    val trackSecondary: Color,
    val danger: Color,
    val shortDistanceBackground: Color,
    val onShortDistanceBackground: Color,
    val border: Color,
    val divider: Color,
    val waypointNumberBackground: Color,
    val onWaypointNumberBackground: Color,
)

internal val LocalRoadbookColors = staticCompositionLocalOf<RoadbookColors?> { null }
