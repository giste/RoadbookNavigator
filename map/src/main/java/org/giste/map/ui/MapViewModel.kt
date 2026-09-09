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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import org.giste.map.MapLocation
import org.giste.map.MapLocationProvider
import org.giste.map.MapLogger
import org.giste.map.MapSettings
import org.giste.map.domain.model.MapFile
import org.giste.map.domain.usecase.GetLocalMapsUseCase
import org.giste.map.domain.usecase.GetMapSettingsUseCase
import javax.inject.Inject

@HiltViewModel
internal class MapViewModel @Inject constructor(
    getLocalMapsUseCase: GetLocalMapsUseCase,
    getMapSettingsUseCase: GetMapSettingsUseCase,
    locationProvider: MapLocationProvider,
    private val logger: MapLogger,
) : ViewModel() {

    val uiState: StateFlow<MapUiState> = combine(
        getLocalMapsUseCase(),
        getMapSettingsUseCase(),
        locationProvider.observeLocation()
            .map { it as MapLocation? }
            .onStart { emit(null) }
    ) { localMaps, settings, location ->
        val mapUiState = MapUiState(
            localMaps = localMaps,
            settings = settings,
            currentLocation = location
        )
        logger.d("MapViewModel uiState: %s", mapUiState.toString())
        mapUiState
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MapUiState()
    )
}

internal data class MapUiState(
    val localMaps: List<MapFile> = emptyList(),
    val settings: MapSettings = MapSettings(),
    val currentLocation: MapLocation? = null
)
