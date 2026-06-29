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

package org.giste.roadbooknavigator.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.giste.roadbooknavigator.core.util.Logger
import org.giste.roadbooknavigator.features.odometer.domain.Odometer
import org.giste.roadbooknavigator.features.odometer.domain.usecase.DecrementPartialDistanceUseCase
import org.giste.roadbooknavigator.features.odometer.domain.usecase.GetOdometerUseCase
import org.giste.roadbooknavigator.features.odometer.domain.usecase.IncrementPartialDistanceUseCase
import org.giste.roadbooknavigator.features.odometer.domain.usecase.ResetAllDistancesUseCase
import org.giste.roadbooknavigator.features.odometer.domain.usecase.ResetPartialDistanceUseCase
import org.giste.roadbooknavigator.features.odometer.domain.usecase.SetPartialDistanceUseCase
import org.giste.roadbooknavigator.features.roadbook.domain.model.RoadbookPosition
import org.giste.roadbooknavigator.features.roadbook.domain.usecase.GetActiveRoadbookUseCase
import org.giste.roadbooknavigator.features.roadbook.domain.usecase.GetRoadbookPositionUseCase
import org.giste.roadbooknavigator.features.roadbook.domain.usecase.ImportRoadbookUseCase
import org.giste.roadbooknavigator.features.roadbook.domain.usecase.SaveRoadbookPositionUseCase
import org.giste.roadbooknavigator.features.roadbook.ui.RoadbookUiState
import org.giste.roadbooknavigator.features.settings.domain.usecase.GetSettingsUseCase
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    getActiveRoadbookUseCase: GetActiveRoadbookUseCase,
    private val importRoadbookUseCase: ImportRoadbookUseCase,
    getOdometerUseCase: GetOdometerUseCase,
    private val resetPartialDistanceUseCase: ResetPartialDistanceUseCase,
    private val resetAllDistancesUseCase: ResetAllDistancesUseCase,
    private val incrementPartialDistanceUseCase: IncrementPartialDistanceUseCase,
    private val decrementPartialDistanceUseCase: DecrementPartialDistanceUseCase,
    private val setPartialDistanceUseCase: SetPartialDistanceUseCase,
    getRoadbookPositionUseCase: GetRoadbookPositionUseCase,
    private val saveRoadbookPositionUseCase: SaveRoadbookPositionUseCase,
    getSettingsUseCase: GetSettingsUseCase,
) : ViewModel() {

    private val _showSetPartialDialog = MutableStateFlow(false)
    private val _transientState = MutableStateFlow<RoadbookUiState?>(null)

    val roadbookState: StateFlow<RoadbookUiState> = combine(
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

    val uiState: StateFlow<DashboardUiState> = combine(
        roadbookState,
        getOdometerUseCase().onStart { emit(Odometer()) },
        _showSetPartialDialog,
        getRoadbookPositionUseCase(),
        getSettingsUseCase(),
    ) { roadbook, odometer, showDialog, scrollPosition, settings ->
        DashboardUiState(
            roadbook = roadbook,
            odometer = odometer,
            showSetPartialDialog = showDialog,
            initialScrollPosition = scrollPosition,
            isFullScreen = settings.fullScreen,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState()
    )

    fun showSetPartialDialog() {
        _showSetPartialDialog.value = true
    }

    fun hideSetPartialDialog() {
        _showSetPartialDialog.value = false
    }

    fun importRoute(inputStream: InputStream) {
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

    fun resetPartialDistance() {
        Logger.i("DashboardViewModel: Resetting partial distance")
        viewModelScope.launch {
            resetPartialDistanceUseCase()
        }
    }

    fun resetAllDistances() {
        Logger.i("DashboardViewModel: Resetting all distances")
        viewModelScope.launch {
            resetAllDistancesUseCase()
        }
    }

    fun incrementPartialDistance() {
        Logger.d("DashboardViewModel: Incrementing partial distance")
        viewModelScope.launch {
            incrementPartialDistanceUseCase()
        }
    }

    fun decrementPartialDistance() {
        Logger.d("DashboardViewModel: Decrementing partial distance")
        viewModelScope.launch {
            decrementPartialDistanceUseCase()
        }
    }

    fun setPartialDistance(distance: Double) {
        Logger.i("DashboardViewModel: Setting partial distance to $distance")
        viewModelScope.launch {
            setPartialDistanceUseCase(distance)
        }
    }

    fun onWaypointVisible(index: Int, offset: Int) {
        viewModelScope.launch {
            saveRoadbookPositionUseCase(index, offset)
        }
    }
}

/**
 * Represents the full screen state, composed of independent modules.
 */
data class DashboardUiState(
    val roadbook: RoadbookUiState = RoadbookUiState.Empty,
    val odometer: Odometer = Odometer(),
    val showSetPartialDialog: Boolean = false,
    val initialScrollPosition: RoadbookPosition = RoadbookPosition(),
    val isFullScreen: Boolean = false,
)
