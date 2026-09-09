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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.giste.map.ui.MapManagementContent
import org.giste.map.ui.MapManagementViewModel

/**
 * Public entry point for the Map Management screen.
 *
 * @param dimensions Custom dimensions for the screen. Defaults to compact.
 */
@Composable
public fun MapManagementScreen(
    dimensions: MapDimensions = compactMapDimensions,
) {
    val viewModel: MapManagementViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MapManagementContent(
        uiState = uiState,
        dimensions = dimensions,
        onDownloadClick = viewModel::downloadMap,
        onDeleteClick = viewModel::deleteMap,
        onCancelDownloadClick = viewModel::cancelDownload
    )
}
