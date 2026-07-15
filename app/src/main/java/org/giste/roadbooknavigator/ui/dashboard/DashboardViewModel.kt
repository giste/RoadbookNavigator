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
import org.giste.roadbooknavigator.features.odometer.domain.OdometerSettings
import org.giste.roadbooknavigator.features.odometer.domain.usecase.DecrementPartialDistanceUseCase
import org.giste.roadbooknavigator.features.odometer.domain.usecase.GetOdometerSettingsUseCase
import org.giste.roadbooknavigator.features.odometer.domain.usecase.GetOdometerUseCase
import org.giste.roadbooknavigator.features.odometer.domain.usecase.IncrementPartialDistanceUseCase
import org.giste.roadbooknavigator.features.odometer.domain.usecase.ResetAllDistancesUseCase
import org.giste.roadbooknavigator.features.odometer.domain.usecase.ResetPartialDistanceUseCase
import org.giste.roadbooknavigator.features.odometer.domain.usecase.SetPartialDistanceUseCase
import org.giste.roadbooknavigator.features.settings.domain.usecase.GetSettingsUseCase
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    getOdometerUseCase: GetOdometerUseCase,
    private val resetPartialDistanceUseCase: ResetPartialDistanceUseCase,
    private val resetAllDistancesUseCase: ResetAllDistancesUseCase,
    private val incrementPartialDistanceUseCase: IncrementPartialDistanceUseCase,
    private val decrementPartialDistanceUseCase: DecrementPartialDistanceUseCase,
    private val setPartialDistanceUseCase: SetPartialDistanceUseCase,
    getSettingsUseCase: GetSettingsUseCase,
    getOdometerSettingsUseCase: GetOdometerSettingsUseCase,
    private val logger: Logger
) : ViewModel() {

    private val _showSetPartialDialog = MutableStateFlow(false)

    val uiState: StateFlow<DashboardUiState> = combine(
        getOdometerUseCase().onStart { emit(Odometer()) },
        _showSetPartialDialog,
        getSettingsUseCase(),
        getOdometerSettingsUseCase()
    ) { odometer, showDialog, settings, odometerSettings ->
        DashboardUiState(
            odometer = odometer,
            showSetPartialDialog = showDialog,
            isFullScreen = settings.fullScreen,
            increasePartialKeys = odometerSettings.increasePartial,
            decreasePartialKeys = odometerSettings.decreasePartial,
            resetPartialKeys = odometerSettings.resetPartial
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

    fun resetPartialDistance() {
        logger.i("DashboardViewModel: Resetting partial distance")
        viewModelScope.launch {
            resetPartialDistanceUseCase()
        }
    }

    fun resetAllDistances() {
        logger.i("DashboardViewModel: Resetting all distances")
        viewModelScope.launch {
            resetAllDistancesUseCase()
        }
    }

    fun incrementPartialDistance() {
        logger.d("DashboardViewModel: Incrementing partial distance")
        viewModelScope.launch {
            incrementPartialDistanceUseCase()
        }
    }

    fun decrementPartialDistance() {
        logger.d("DashboardViewModel: Decrementing partial distance")
        viewModelScope.launch {
            decrementPartialDistanceUseCase()
        }
    }

    fun setPartialDistance(distance: Double) {
        logger.i("DashboardViewModel: Setting partial distance to %f", distance)
        viewModelScope.launch {
            setPartialDistanceUseCase(distance)
        }
    }
}

/**
 * Represents the full screen state, composed of independent modules.
 */
data class DashboardUiState(
    val odometer: Odometer = Odometer(),
    val showSetPartialDialog: Boolean = false,
    val isFullScreen: Boolean = false,
    val increasePartialKeys: List<Int> = OdometerSettings.DEFAULT_INCREASE_KEYS,
    val decreasePartialKeys: List<Int> = OdometerSettings.DEFAULT_DECREASE_KEYS,
    val resetPartialKeys: List<Int> = OdometerSettings.DEFAULT_RESET_KEYS
)
