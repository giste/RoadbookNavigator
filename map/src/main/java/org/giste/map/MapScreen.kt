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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.giste.map.ui.MapContent
import org.giste.map.ui.MapViewModel

/**
 * Creates and remembers a [MapState] instance.
 */
@Composable
public fun rememberMapState(): MapState {
    val viewModel: MapViewModel = hiltViewModel()
    return remember(viewModel) { MapState(viewModel) }
}

/**
 * Public entry point for the Map module.
 *
 * @param state The state object for the map.
 * @param modifier Modifier for the map container.
 * @param dimensions Custom dimensions for the map. Defaults to compact.
 */
@Composable
public fun MapScreen(
    state: MapState,
    modifier: Modifier = Modifier,
    dimensions: MapDimensions = compactMapDimensions,
) {
    val uiState by state.internals.uiState.collectAsStateWithLifecycle()

    MapContent(
        uiState = uiState,
        modifier = modifier,
        dimensions = dimensions
    )
}
