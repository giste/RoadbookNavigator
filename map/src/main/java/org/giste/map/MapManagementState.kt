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
import org.giste.map.domain.model.MapFile
import org.giste.map.domain.model.RemoteMapFile
import org.giste.map.ui.MapManagementUiState
import org.giste.map.ui.MapManagementViewModel

/**
 * State object for the Map Management screen, following the State Hoisting pattern.
 * This class acts as a public proxy for the internal map management logic.
 */
@Stable
public class MapManagementState internal constructor(
    // Grouped internal implementation details.
    internal val internals: InternalData
) {
    internal class InternalData(
        val uiState: StateFlow<MapManagementUiState>,
        val onDownloadClick: (RemoteMapFile) -> Unit,
        val onDeleteClick: (MapFile) -> Unit,
        val onCancelDownloadClick: (String) -> Unit
    )

    internal constructor(viewModel: MapManagementViewModel) : this(
        internals = InternalData(
            uiState = viewModel.uiState,
            onDownloadClick = viewModel::downloadMap,
            onDeleteClick = viewModel::deleteMap,
            onCancelDownloadClick = viewModel::cancelDownload
        )
    )
}

/**
 * Factory function to create a [MapManagementState] for previews or testing.
 */
public fun MapManagementState(): MapManagementState = MapManagementState(
    internals = MapManagementState.InternalData(
        uiState = MutableStateFlow(MapManagementUiState.Loading),
        onDownloadClick = {},
        onDeleteClick = {},
        onCancelDownloadClick = {}
    )
)
