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
import kotlinx.coroutines.flow.stateIn
import org.giste.roadbooknavigator.core.util.Logger
import org.giste.odometer.OdometerController
import org.giste.odometer.domain.Odometer
import org.giste.roadbooknavigator.features.settings.domain.AppSettings
import org.giste.roadbooknavigator.features.settings.domain.input.InputSettings
import org.giste.roadbooknavigator.features.settings.domain.input.usecase.ObserveInputSettingsUseCase
import org.giste.roadbooknavigator.features.settings.domain.usecase.ObserveAppSettingsUseCase
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val odometerController: OdometerController,
    observeAppSettingsUseCase: ObserveAppSettingsUseCase,
    observeInputSettingsUseCase: ObserveInputSettingsUseCase,
    private val logger: Logger
) : ViewModel() {

    private val _showSetPartialDialog = MutableStateFlow(false)
    private val _showResetAllDialog = MutableStateFlow(false)

    private val inputKeySettingsFlow = observeInputSettingsUseCase()

    val uiState: StateFlow<DashboardUiState> = combine(
        odometerController.odometer,
        _showSetPartialDialog,
        _showResetAllDialog,
        observeAppSettingsUseCase(),
        inputKeySettingsFlow
    ) { flows ->
        val odometer = flows[0] as Odometer
        val showPartialDialog = flows[1] as Boolean
        val showResetAllDialog = flows[2] as Boolean
        val settings = flows[3] as AppSettings
        val inputKeys = flows[4] as InputSettings

        DashboardUiState(
            odometer = odometer,
            showSetPartialDialog = showPartialDialog,
            showResetAllDialog = showResetAllDialog,
            isFullScreen = settings.fullScreen,
            landscapeDistanceSectionWeight = settings.landscapeDistanceSectionWeight,
            increasePartialKeys = inputKeys.increasePartialKeys,
            decreasePartialKeys = inputKeys.decreasePartialKeys,
            resetPartialKeys = inputKeys.resetPartialKeys,
            roadbookUpKeys = inputKeys.upKeys,
            roadbookDownKeys = inputKeys.downKeys
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

    fun showResetAllDialog() {
        _showResetAllDialog.value = true
    }

    fun hideResetAllDialog() {
        _showResetAllDialog.value = false
    }

    fun getOdometerController(): OdometerController = odometerController

    fun resetPartialDistance() {
        logger.i("DashboardViewModel: Resetting partial distance")
        odometerController.resetPartial()
    }

    fun resetAllDistances() {
        logger.i("DashboardViewModel: Resetting all distances")
        odometerController.resetAll()
    }

    fun incrementPartialDistance() {
        logger.d("DashboardViewModel: Incrementing partial distance")
        odometerController.increment()
    }

    fun decrementPartialDistance() {
        logger.d("DashboardViewModel: Decrementing partial distance")
        odometerController.decrement()
    }

    fun setPartialDistance(distance: Double) {
        logger.i("DashboardViewModel: Setting partial distance to %f", distance)
        odometerController.setPartial(distance)
    }
}

/**
 * Represents the full screen state, composed of independent modules.
 */
data class DashboardUiState(
    val odometer: Odometer = Odometer(),
    val showSetPartialDialog: Boolean = false,
    val showResetAllDialog: Boolean = false,
    val isFullScreen: Boolean = false,
    val landscapeDistanceSectionWeight: Float = 0.3f,
    val increasePartialKeys: List<Int> = InputSettings.DEFAULT_INCREASE_KEYS,
    val decreasePartialKeys: List<Int> = InputSettings.DEFAULT_DECREASE_KEYS,
    val resetPartialKeys: List<Int> = InputSettings.DEFAULT_RESET_KEYS,
    val roadbookUpKeys: List<Int> = InputSettings.DEFAULT_UP_KEYS,
    val roadbookDownKeys: List<Int> = InputSettings.DEFAULT_DOWN_KEYS
)
