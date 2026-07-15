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
import org.giste.roadbooknavigator.core.util.Logger
import org.giste.roadbooknavigator.features.roadbook.domain.model.RoadbookPosition
import org.giste.roadbooknavigator.features.roadbook.domain.usecase.GetActiveRoadbookUseCase
import org.giste.roadbooknavigator.features.roadbook.domain.usecase.GetRoadbookPositionUseCase
import org.giste.roadbooknavigator.features.roadbook.domain.usecase.GetRoadbookSettingsUseCase
import org.giste.roadbooknavigator.features.roadbook.domain.usecase.ImportRoadbookUseCase
import org.giste.roadbooknavigator.features.roadbook.domain.usecase.SaveRoadbookPositionUseCase
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class RoadbookViewModel @Inject constructor(
    getActiveRoadbookUseCase: GetActiveRoadbookUseCase,
    private val importRoadbookUseCase: ImportRoadbookUseCase,
    getRoadbookPositionUseCase: GetRoadbookPositionUseCase,
    private val saveRoadbookPositionUseCase: SaveRoadbookPositionUseCase,
    getRoadbookSettingsUseCase: GetRoadbookSettingsUseCase,
    private val logger: Logger
) : ViewModel() {

    private val _transientState = MutableStateFlow<RoadbookUiState?>(null)

    val roadbookState: StateFlow<RoadbookUiState> = combine(
        getActiveRoadbookUseCase(),
        getRoadbookPositionUseCase(),
        getRoadbookSettingsUseCase()
    ) { route, position, settings ->
        if (route != null) {
            RoadbookUiState.Success(
                route = route,
                shortDistanceThreshold = settings.shortDistanceThreshold,
                initialIndex = position.index,
                initialOffset = position.offset,
                roadbookUp = settings.roadbookUp,
                roadbookDown = settings.roadbookDown
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

    val initialScrollPosition: StateFlow<RoadbookPosition> = getRoadbookPositionUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RoadbookPosition()
        )

    fun importRoute(inputStream: InputStream) {
        logger.i("RoadbookViewModel: Importing route")
        viewModelScope.launch {
            _transientState.value = RoadbookUiState.Loading
            importRoadbookUseCase(inputStream)
                .onSuccess {
                    _transientState.value = null
                }
                .onFailure { error ->
                    _transientState.value =
                        RoadbookUiState.Error(error.message ?: "Failed to process file")
                }
        }
    }

    fun onWaypointVisible(index: Int, offset: Int) {
        viewModelScope.launch {
            saveRoadbookPositionUseCase(index, offset)
        }
    }
}
