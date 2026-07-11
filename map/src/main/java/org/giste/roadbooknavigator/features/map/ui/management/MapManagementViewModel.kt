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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.giste.roadbooknavigator.core.util.Logger
import org.giste.roadbooknavigator.features.map.domain.model.DownloadStatus
import org.giste.roadbooknavigator.features.map.domain.model.DownloadedMapInfo
import org.giste.roadbooknavigator.features.map.domain.model.MapFile
import org.giste.roadbooknavigator.features.map.domain.model.RemoteMapFile
import org.giste.roadbooknavigator.features.map.domain.model.RemoteMapFolder
import org.giste.roadbooknavigator.features.map.domain.usecase.DeleteMapUseCase
import org.giste.roadbooknavigator.features.map.domain.usecase.DownloadMapUseCase
import org.giste.roadbooknavigator.features.map.domain.usecase.GetMapOverviewUseCase
import javax.inject.Inject

@HiltViewModel
class MapManagementViewModel @Inject constructor(
    getMapOverviewUseCase: GetMapOverviewUseCase,
    private val downloadMapUseCase: DownloadMapUseCase,
    private val deleteMapUseCase: DeleteMapUseCase,
    private val logger: Logger
) : ViewModel() {

    private val _downloadingMaps = MutableStateFlow<Map<String, DownloadStatus>>(emptyMap())
    private val downloadingMaps = _downloadingMaps.asStateFlow()

    private val downloadJobs = mutableMapOf<String, Job>()

    val uiState: StateFlow<MapManagementUiState> = combine(
        getMapOverviewUseCase(),
        downloadingMaps
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
        if (downloadJobs.containsKey(remoteMapFile.url)) return

        val job = viewModelScope.launch {
            downloadMapUseCase(remoteMapFile)
                .collect { status ->
                    _downloadingMaps.update { it + (remoteMapFile.url to status) }
                    if (status is DownloadStatus.Success || status is DownloadStatus.Error) {
                        downloadJobs.remove(remoteMapFile.url)
                        // After some time, remove from downloading list if success?
                        // Or let the UI handle it.
                    }
                }
        }
        downloadJobs[remoteMapFile.url] = job
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
        downloadJobs[url]?.cancel()
        downloadJobs.remove(url)
        _downloadingMaps.update { it - url }
    }

    override fun onCleared() {
        super.onCleared()
        downloadJobs.values.forEach { it.cancel() }
    }
}

sealed interface MapManagementUiState {
    data object Loading : MapManagementUiState
    data class Success(
        val downloadedMaps: List<DownloadedMapInfo>,
        val remoteFolders: RemoteMapFolder,
        val downloadingStatus: Map<String, DownloadStatus> = emptyMap()
    ) : MapManagementUiState
    data class Error(val message: String) : MapManagementUiState
}
