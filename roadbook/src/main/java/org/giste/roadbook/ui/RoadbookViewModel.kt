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

package org.giste.roadbook.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.giste.roadbook.RoadbookController
import org.giste.roadbook.RoadbookEvent
import org.giste.roadbook.RoadbookLogger
import org.giste.roadbook.domain.model.RoadbookPosition
import org.giste.roadbook.domain.usecase.GetActiveRoadbookUseCase
import org.giste.roadbook.domain.usecase.GetRoadbookPositionUseCase
import org.giste.roadbook.domain.usecase.GetRoadbookSettingsUseCase
import org.giste.roadbook.domain.usecase.ImportRoadbookUseCase
import org.giste.roadbook.domain.usecase.MoveRoadbookDownUseCase
import org.giste.roadbook.domain.usecase.MoveRoadbookUpUseCase
import org.giste.roadbook.domain.usecase.SaveRoadbookPositionUseCase
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
internal class RoadbookViewModel @Inject constructor(
    getActiveRoadbookUseCase: GetActiveRoadbookUseCase,
    private val importRoadbookUseCase: ImportRoadbookUseCase,
    getRoadbookPositionUseCase: GetRoadbookPositionUseCase,
    private val saveRoadbookPositionUseCase: SaveRoadbookPositionUseCase,
    private val moveRoadbookUpUseCase: MoveRoadbookUpUseCase,
    private val moveRoadbookDownUseCase: MoveRoadbookDownUseCase,
    getRoadbookSettingsUseCase: GetRoadbookSettingsUseCase,
    private val logger: RoadbookLogger
) : ViewModel(), RoadbookController {

    private val _events = MutableSharedFlow<RoadbookEvent>()
    override val events = _events

    override val routeName: StateFlow<String?> = getActiveRoadbookUseCase()
        .map { it?.name }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

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
                    _events.emit(RoadbookEvent.RouteImported)
                }
                .onFailure { error ->
                    _transientState.value =
                        RoadbookUiState.Error(error.message ?: "Failed to process file")
                }
        }
    }

    fun onDistanceSectionLongPressed(distance: Double) {
        logger.i("RoadbookViewModel: Requesting partial distance sync: %f", distance)
        viewModelScope.launch {
            _events.emit(RoadbookEvent.DistanceSectionLongPressed(distance))
        }
    }

    fun onWaypointVisible(index: Int, offset: Int) {
        viewModelScope.launch {
            saveRoadbookPositionUseCase(index, offset)
        }
    }

    override fun scrollUp() {
        logger.d("RoadbookViewModel: Scrolling up")
        viewModelScope.launch {
            moveRoadbookUpUseCase()
        }
    }

    override fun scrollDown() {
        logger.d("RoadbookViewModel: Scrolling down")
        viewModelScope.launch {
            moveRoadbookDownUseCase()
        }
    }
}
