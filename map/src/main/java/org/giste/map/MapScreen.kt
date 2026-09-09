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

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.giste.map.ui.MapContent

/**
 * Public entry point for the Map module.
 *
 * @param modifier Modifier for the map container.
 * @param dimensions Custom dimensions for the map. Defaults to compact.
 */
@Composable
public fun MapScreen(
    modifier: Modifier = Modifier,
    dimensions: MapDimensions = compactMapDimensions,
) {
    MapContent(
        modifier = modifier,
        dimensions = dimensions
    )
}
