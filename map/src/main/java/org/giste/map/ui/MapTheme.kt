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

package org.giste.map.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import org.giste.map.LocalMapDimensions
import org.giste.map.MapDimensions
import org.giste.map.compactMapDimensions

@Composable
internal fun MapTheme(
    dimensions: MapDimensions = compactMapDimensions,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalMapDimensions provides dimensions,
        content = content
    )
}