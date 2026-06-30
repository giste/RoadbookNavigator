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

package org.giste.roadbooknavigator.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.giste.roadbooknavigator.core.util.logger
import org.giste.roadbooknavigator.features.settings.domain.AppOrientation
import org.giste.roadbooknavigator.features.settings.domain.AppSettings
import org.giste.roadbooknavigator.features.settings.domain.AppTheme
import org.giste.roadbooknavigator.features.settings.domain.usecase.*
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    getSettingsUseCase: GetSettingsUseCase,
    private val updateThemeUseCase: UpdateThemeUseCase,
    private val updateOrientationUseCase: UpdateOrientationUseCase,
    private val updateFullScreenUseCase: UpdateFullScreenUseCase,
    private val updateShortDistanceThresholdUseCase: UpdateShortDistanceThresholdUseCase,
    private val updateOdometerSpeedThresholdUseCase: UpdateOdometerSpeedThresholdUseCase,
    private val updateOdometerMinAccuracyUseCase: UpdateOdometerMinAccuracyUseCase,
    private val updateOdometerMinVerticalAccuracyUseCase: UpdateOdometerMinVerticalAccuracyUseCase,
    private val updateOdometerPollingIntervalUseCase: UpdateOdometerPollingIntervalUseCase,
    private val updateOdometerMinDistanceUseCase: UpdateOdometerMinDistanceUseCase,
    private val restoreOdometerDefaultsUseCase: RestoreOdometerDefaultsUseCase
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = getSettingsUseCase()
        .onEach { logger.v("SettingsViewModel: Settings stream emitted: %s", it) }
        .map { settings -> SettingsUiState.Success(settings) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SettingsUiState.Loading
        )

    fun setTheme(theme: AppTheme) {
        logger.d("SettingsViewModel: setTheme requested: %s", theme)
        viewModelScope.launch {
            updateThemeUseCase(theme)
        }
    }

    fun setOrientation(orientation: AppOrientation) {
        logger.d("SettingsViewModel: setOrientation requested: %s", orientation)
        viewModelScope.launch {
            updateOrientationUseCase(orientation)
        }
    }

    fun setFullScreen(enabled: Boolean) {
        logger.d("SettingsViewModel: setFullScreen requested: %b", enabled)
        viewModelScope.launch {
            updateFullScreenUseCase(enabled)
        }
    }

    fun setShortDistanceThreshold(threshold: Long) {
        logger.d("SettingsViewModel: setShortDistanceThreshold requested: %d", threshold)
        viewModelScope.launch {
            updateShortDistanceThresholdUseCase(threshold)
        }
    }

    fun setOdometerSpeedThreshold(threshold: Float) {
        logger.d("SettingsViewModel: setOdometerSpeedThreshold requested: %f", threshold)
        viewModelScope.launch {
            updateOdometerSpeedThresholdUseCase(threshold)
        }
    }

    fun setOdometerMinAccuracy(accuracy: Float) {
        logger.d("SettingsViewModel: setOdometerMinAccuracy requested: %f", accuracy)
        viewModelScope.launch {
            updateOdometerMinAccuracyUseCase(accuracy)
        }
    }

    fun setOdometerMinVerticalAccuracy(accuracy: Float) {
        logger.d("SettingsViewModel: setOdometerMinVerticalAccuracy requested: %f", accuracy)
        viewModelScope.launch {
            updateOdometerMinVerticalAccuracyUseCase(accuracy)
        }
    }

    fun setOdometerPollingInterval(interval: Long) {
        logger.d("SettingsViewModel: setOdometerPollingInterval requested: %d", interval)
        viewModelScope.launch {
            updateOdometerPollingIntervalUseCase(interval)
        }
    }

    fun setOdometerMinDistance(distance: Float) {
        logger.d("SettingsViewModel: setOdometerMinDistance requested: %f", distance)
        viewModelScope.launch {
            updateOdometerMinDistanceUseCase(distance)
        }
    }

    fun restoreOdometerDefaults() {
        logger.i("SettingsViewModel: restoreOdometerDefaults requested")
        viewModelScope.launch {
            restoreOdometerDefaultsUseCase()
        }
    }
}

sealed interface SettingsUiState {
    data object Loading : SettingsUiState
    data class Success(val settings: AppSettings) : SettingsUiState
}
