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

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.giste.map.ui.MapUiState
import org.giste.map.ui.MapViewModel

/**
 * State object for the Map module, following the State Hoisting pattern.
 * This class acts as a public proxy for the internal map logic.
 */
@Stable
public class MapState internal constructor(
    // Grouped internal implementation details to keep the constructor manageable.
    internal val internals: InternalData
) {
    internal class InternalData(
        val uiState: StateFlow<MapUiState>
    )

    internal constructor(viewModel: MapViewModel) : this(
        internals = InternalData(
            uiState = viewModel.uiState
        )
    )
}

/**
 * Factory function to create a [MapState] for previews or testing.
 */
public fun MapState(): MapState = MapState(
    internals = MapState.InternalData(
        uiState = MutableStateFlow(MapUiState())
    )
)
