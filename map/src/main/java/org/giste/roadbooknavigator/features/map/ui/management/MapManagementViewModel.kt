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

package org.giste.roadbooknavigator.features.map.ui.management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.giste.roadbooknavigator.core.util.Logger
import org.giste.roadbooknavigator.features.map.domain.model.DownloadStatus
import org.giste.roadbooknavigator.features.map.domain.model.DownloadedMapInfo
import org.giste.roadbooknavigator.features.map.domain.model.MapFile
import org.giste.roadbooknavigator.features.map.domain.model.RemoteMapFile
import org.giste.roadbooknavigator.features.map.domain.model.RemoteMapFolder
import org.giste.roadbooknavigator.features.map.domain.usecase.DeleteMapUseCase
import org.giste.roadbooknavigator.features.map.domain.usecase.DownloadMapUseCase
import org.giste.roadbooknavigator.features.map.domain.usecase.GetDownloadingMapsUseCase
import org.giste.roadbooknavigator.features.map.domain.usecase.GetMapOverviewUseCase
import javax.inject.Inject

@HiltViewModel
class MapManagementViewModel @Inject constructor(
    getMapOverviewUseCase: GetMapOverviewUseCase,
    private val downloadMapUseCase: DownloadMapUseCase,
    getDownloadingMapsUseCase: GetDownloadingMapsUseCase,
    private val deleteMapUseCase: DeleteMapUseCase,
    private val logger: Logger
) : ViewModel() {

    val uiState: StateFlow<MapManagementUiState> = combine(
        getMapOverviewUseCase(),
        getDownloadingMapsUseCase()
    ) { overview, downloading ->
        MapManagementUiState.Success(
            downloadedMaps = overview.downloadedMaps,
            remoteFolders = overview.remoteFolders,
            downloadingStatus = downloading
        )
    }
        .catch<MapManagementUiState> { e ->
            logger.e(e, "Error loading map overview")
            emit(MapManagementUiState.Error(e.message ?: "Unknown error"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MapManagementUiState.Loading
        )

    fun downloadMap(remoteMapFile: RemoteMapFile) {
        viewModelScope.launch {
            downloadMapUseCase(remoteMapFile).collect {
                // We just trigger the flow, but we observe progress via getDownloadingMapsUseCase
            }
        }
    }

    fun deleteMap(mapFile: MapFile) {
        viewModelScope.launch {
            try {
                deleteMapUseCase(mapFile)
            } catch (e: Exception) {
                logger.e(e, "Error deleting map: %s", mapFile.name)
            }
        }
    }

    fun cancelDownload(url: String) {
        downloadMapUseCase.cancelDownload(url)
    }

    override fun onCleared() {
        // No longer cancelling jobs here as they are managed by WorkManager
    }
}

sealed interface MapManagementUiState {
    data object Loading : MapManagementUiState
    data class Success(
        val downloadedMaps: List<DownloadedMapInfo>,
        val remoteFolders: List<RemoteMapFolder>,
        val downloadingStatus: Map<String, DownloadStatus> = emptyMap()
    ) : MapManagementUiState

    data class Error(val message: String) : MapManagementUiState
}
