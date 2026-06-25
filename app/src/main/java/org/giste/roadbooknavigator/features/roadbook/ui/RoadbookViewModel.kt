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

package org.giste.roadbooknavigator.features.roadbook.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.giste.roadbooknavigator.features.roadbook.domain.model.Route
import org.giste.roadbooknavigator.features.roadbook.domain.usecase.GetActiveRoadbookUseCase
import org.giste.roadbooknavigator.features.roadbook.domain.usecase.GetRoadbookPositionUseCase
import org.giste.roadbooknavigator.features.roadbook.domain.usecase.ImportRoadbookUseCase
import org.giste.roadbooknavigator.features.roadbook.domain.usecase.SaveRoadbookPositionUseCase
import org.giste.roadbooknavigator.features.settings.domain.AppSettings
import org.giste.roadbooknavigator.features.settings.domain.usecase.GetSettingsUseCase
import java.io.InputStream
import javax.inject.Inject

/**
 * UI State for the Roadbook feature.
 */
sealed interface RoadbookUiState {
    data object Loading : RoadbookUiState
    data object Empty : RoadbookUiState
    data class Success(
        val route: Route,
        val shortDistanceThreshold: Long = AppSettings.DEFAULT_SHORT_DISTANCE_THRESHOLD,
        val initialIndex: Int = 0,
        val initialOffset: Int = 0
    ) : RoadbookUiState
    data class Error(val message: String) : RoadbookUiState
}

/**
 * ViewModel for the Roadbook feature, responsible for managing the active roadbook route.
 */
@HiltViewModel
class RoadbookViewModel @Inject constructor(
    getActiveRoadbookUseCase: GetActiveRoadbookUseCase,
    private val importRoadbookUseCase: ImportRoadbookUseCase,
    private val getRoadbookPositionUseCase: GetRoadbookPositionUseCase,
    private val saveRoadbookPositionUseCase: SaveRoadbookPositionUseCase,
    getSettingsUseCase: GetSettingsUseCase
) : ViewModel() {

    private val _transientState = MutableStateFlow<RoadbookUiState?>(null)

    val uiState: StateFlow<RoadbookUiState> = combine(
        getActiveRoadbookUseCase(),
        getRoadbookPositionUseCase(),
        getSettingsUseCase()
    ) { route, position, settings ->
        if (route != null) {
            RoadbookUiState.Success(
                route = route,
                shortDistanceThreshold = settings.shortDistanceThreshold,
                initialIndex = position.index,
                initialOffset = position.offset
            )
        } else {
            RoadbookUiState.Empty
        }
    }
        .combine(_transientState) { repositoryState, transient ->
            transient ?: repositoryState
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RoadbookUiState.Empty
        )

    /**
     * Loads a new roadbook from the provided input stream.
     */
    fun onFileSelected(inputStream: InputStream) {
        viewModelScope.launch {
            _transientState.value = RoadbookUiState.Loading
            importRoadbookUseCase(inputStream)
                .onSuccess {
                    _transientState.value = null
                }
                .onFailure { error ->
                    _transientState.value = RoadbookUiState.Error(error.message ?: "Failed to process file")
                }
        }
    }

    /**
     * Persists the current scroll position of the roadbook.
     */
    fun onWaypointVisible(index: Int, offset: Int) {
        viewModelScope.launch {
            saveRoadbookPositionUseCase(index, offset)
        }
    }
}
