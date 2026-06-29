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
import org.giste.roadbooknavigator.core.util.Logger
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
        .onEach { Logger.v("SettingsViewModel: Settings stream emitted: $it") }
        .map { settings -> SettingsUiState.Success(settings) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SettingsUiState.Loading
        )

    fun setTheme(theme: AppTheme) {
        Logger.d("SettingsViewModel: setTheme requested: $theme")
        viewModelScope.launch {
            updateThemeUseCase(theme)
        }
    }

    fun setOrientation(orientation: AppOrientation) {
        Logger.d("SettingsViewModel: setOrientation requested: $orientation")
        viewModelScope.launch {
            updateOrientationUseCase(orientation)
        }
    }

    fun setFullScreen(enabled: Boolean) {
        Logger.d("SettingsViewModel: setFullScreen requested: $enabled")
        viewModelScope.launch {
            updateFullScreenUseCase(enabled)
        }
    }

    fun setShortDistanceThreshold(threshold: Long) {
        Logger.d("SettingsViewModel: setShortDistanceThreshold requested: $threshold")
        viewModelScope.launch {
            updateShortDistanceThresholdUseCase(threshold)
        }
    }

    fun setOdometerSpeedThreshold(threshold: Float) {
        Logger.d("SettingsViewModel: setOdometerSpeedThreshold requested: $threshold")
        viewModelScope.launch {
            updateOdometerSpeedThresholdUseCase(threshold)
        }
    }

    fun setOdometerMinAccuracy(accuracy: Float) {
        Logger.d("SettingsViewModel: setOdometerMinAccuracy requested: $accuracy")
        viewModelScope.launch {
            updateOdometerMinAccuracyUseCase(accuracy)
        }
    }

    fun setOdometerMinVerticalAccuracy(accuracy: Float) {
        Logger.d("SettingsViewModel: setOdometerMinVerticalAccuracy requested: $accuracy")
        viewModelScope.launch {
            updateOdometerMinVerticalAccuracyUseCase(accuracy)
        }
    }

    fun setOdometerPollingInterval(interval: Long) {
        Logger.d("SettingsViewModel: setOdometerPollingInterval requested: $interval")
        viewModelScope.launch {
            updateOdometerPollingIntervalUseCase(interval)
        }
    }

    fun setOdometerMinDistance(distance: Float) {
        Logger.d("SettingsViewModel: setOdometerMinDistance requested: $distance")
        viewModelScope.launch {
            updateOdometerMinDistanceUseCase(distance)
        }
    }

    fun restoreOdometerDefaults() {
        Logger.i("SettingsViewModel: restoreOdometerDefaults requested")
        viewModelScope.launch {
            restoreOdometerDefaultsUseCase()
        }
    }
}

sealed interface SettingsUiState {
    data object Loading : SettingsUiState
    data class Success(val settings: AppSettings) : SettingsUiState
}
