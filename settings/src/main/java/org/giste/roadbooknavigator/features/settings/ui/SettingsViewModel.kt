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

package org.giste.roadbooknavigator.features.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.giste.roadbooknavigator.core.settings.domain.AppTheme
import org.giste.roadbooknavigator.core.util.Logger
import org.giste.roadbooknavigator.features.settings.domain.location.LocationSettings
import org.giste.roadbooknavigator.features.settings.domain.location.usecase.ObserveLocationSettingsUseCase
import org.giste.roadbooknavigator.features.settings.domain.location.usecase.RestoreLocationDefaultsUseCase
import org.giste.roadbooknavigator.features.settings.domain.location.usecase.UpdateLocationMinDistanceUseCase
import org.giste.roadbooknavigator.features.settings.domain.location.usecase.UpdateLocationPollingIntervalUseCase
import org.giste.roadbooknavigator.features.map.domain.model.MapSettings
import org.giste.roadbooknavigator.features.map.domain.usecase.GetMapSettingsUseCase
import org.giste.roadbooknavigator.features.map.domain.usecase.SaveMapSettingsUseCase
import org.giste.odometer.domain.OdometerSettings
import org.giste.roadbooknavigator.features.settings.domain.odometer.usecase.ObserveOdometerSettingsUseCase
import org.giste.roadbooknavigator.features.settings.domain.odometer.usecase.RestoreOdometerSettingsDefaultsUseCase
import org.giste.roadbooknavigator.features.settings.domain.odometer.usecase.UpdateOdometerMinAccuracyUseCase
import org.giste.roadbooknavigator.features.settings.domain.odometer.usecase.UpdateOdometerMinVerticalAccuracyUseCase
import org.giste.roadbooknavigator.features.settings.domain.odometer.usecase.UpdateOdometerSpeedThresholdUseCase
import org.giste.roadbook.RoadbookSettings
import org.giste.roadbook.RoadbookSettingsProvider
import org.giste.roadbooknavigator.features.settings.domain.AppOrientation
import org.giste.roadbooknavigator.features.settings.domain.AppSettings
import org.giste.roadbooknavigator.features.settings.domain.input.InputSettings
import org.giste.roadbooknavigator.features.settings.domain.input.RemoteModel
import org.giste.roadbooknavigator.features.settings.domain.input.usecase.ObserveInputSettingsUseCase
import org.giste.roadbooknavigator.features.settings.domain.input.usecase.SelectRemoteModelUseCase
import org.giste.roadbooknavigator.features.settings.domain.input.usecase.UpdateOdometerKeysUseCase
import org.giste.roadbooknavigator.features.settings.domain.input.usecase.UpdateRoadbookKeysUseCase
import org.giste.roadbooknavigator.features.settings.domain.roadbook.usecase.SaveRoadbookSettingsUseCase
import org.giste.roadbooknavigator.features.settings.domain.usecase.ObserveAppSettingsUseCase
import org.giste.roadbooknavigator.features.settings.domain.usecase.UpdateFullScreenUseCase
import org.giste.roadbooknavigator.features.settings.domain.usecase.UpdateLandscapeDistanceSectionWeightUseCase
import org.giste.roadbooknavigator.features.settings.domain.usecase.UpdateOrientationUseCase
import org.giste.roadbooknavigator.features.settings.domain.usecase.UpdateThemeUseCase
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    observeAppSettingsUseCase: ObserveAppSettingsUseCase,
    observeInputSettingsUseCase: ObserveInputSettingsUseCase,
    observeLocationSettingsUseCase: ObserveLocationSettingsUseCase,
    observeOdometerSettingsUseCase: ObserveOdometerSettingsUseCase,
    getMapSettingsUseCase: GetMapSettingsUseCase,
    roadbookSettingsProvider: RoadbookSettingsProvider,
    private val updateThemeUseCase: UpdateThemeUseCase,
    private val updateOrientationUseCase: UpdateOrientationUseCase,
    private val updateFullScreenUseCase: UpdateFullScreenUseCase,
    private val saveRoadbookSettingsUseCase: SaveRoadbookSettingsUseCase,
    private val updateOdometerSpeedThresholdUseCase: UpdateOdometerSpeedThresholdUseCase,
    private val updateOdometerMinAccuracyUseCase: UpdateOdometerMinAccuracyUseCase,
    private val updateOdometerMinVerticalAccuracyUseCase: UpdateOdometerMinVerticalAccuracyUseCase,
    private val restoreOdometerSettingsDefaultsUseCase: RestoreOdometerSettingsDefaultsUseCase,
    private val updateLocationPollingIntervalUseCase: UpdateLocationPollingIntervalUseCase,
    private val updateLocationMinDistanceUseCase: UpdateLocationMinDistanceUseCase,
    private val restoreLocationDefaultsUseCase: RestoreLocationDefaultsUseCase,
    private val selectRemoteModelUseCase: SelectRemoteModelUseCase,
    private val updateRoadbookKeysUseCase: UpdateRoadbookKeysUseCase,
    private val updateOdometerKeysUseCase: UpdateOdometerKeysUseCase,
    private val saveMapSettingsUseCase: SaveMapSettingsUseCase,
    private val updateLandscapeDistanceSectionWeightUseCase: UpdateLandscapeDistanceSectionWeightUseCase,
    private val logger: Logger
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = combine(
        observeAppSettingsUseCase(),
        observeInputSettingsUseCase(),
        observeLocationSettingsUseCase(),
        observeOdometerSettingsUseCase(),
        getMapSettingsUseCase(),
        roadbookSettingsProvider.getSettings()
    ) { flows ->
        SettingsUiState.Success(
            appSettings = flows[0] as AppSettings,
            inputSettings = flows[1] as InputSettings,
            locationSettings = flows[2] as LocationSettings,
            odometerSettings = flows[3] as OdometerSettings,
            mapSettings = flows[4] as MapSettings,
            roadbookSettings = flows[5] as RoadbookSettings
        )
    }
        .onEach { logger.v("SettingsViewModel: Settings stream emitted: %s", it) }
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
            saveRoadbookSettingsUseCase(threshold)
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

    fun setLocationPollingInterval(interval: Long) {
        logger.d("SettingsViewModel: setOdometerPollingInterval requested: %d", interval)
        viewModelScope.launch {
            updateLocationPollingIntervalUseCase(interval)
        }
    }

    fun setLocationMinDistance(distance: Float) {
        logger.d("SettingsViewModel: setOdometerMinDistance requested: %f", distance)
        viewModelScope.launch {
            updateLocationMinDistanceUseCase(distance)
        }
    }

    fun restoreOdometerDefaults() {
        logger.i("SettingsViewModel: restoreOdometerDefaults requested")
        viewModelScope.launch {
            restoreOdometerSettingsDefaultsUseCase()
            restoreLocationDefaultsUseCase()
        }
    }

    fun setRemoteModel(model: RemoteModel) {
        logger.d("SettingsViewModel: setRemoteModel requested: %s", model)
        viewModelScope.launch {
            selectRemoteModelUseCase(model)
        }
    }

    fun setOdometerKeys(increase: List<Int>, decrease: List<Int>, reset: List<Int>) {
        logger.d("SettingsViewModel: setOdometerKeys requested")
        viewModelScope.launch {
            updateOdometerKeysUseCase(increase, decrease, reset)
        }
    }

    fun setRoadbookKeys(up: List<Int>, down: List<Int>) {
        logger.d("SettingsViewModel: setRoadbookKeys requested")
        viewModelScope.launch {
            updateRoadbookKeysUseCase(up, down)
        }
    }

    fun setMapInitialZoom(zoom: Int) {
        logger.d("SettingsViewModel: setMapInitialZoom requested: %d", zoom)
        val currentSettings =
            (uiState.value as? SettingsUiState.Success)?.mapSettings ?: MapSettings()
        viewModelScope.launch {
            saveMapSettingsUseCase(currentSettings.copy(initialZoom = zoom))
        }
    }

    fun setMapInitialTilt(tilt: Float) {
        logger.d("SettingsViewModel: setMapInitialTilt requested: %f", tilt)
        val currentSettings =
            (uiState.value as? SettingsUiState.Success)?.mapSettings ?: MapSettings()
        viewModelScope.launch {
            saveMapSettingsUseCase(currentSettings.copy(initialTilt = tilt))
        }
    }

    fun setLandscapeDistanceSectionWeight(weight: Float) {
        logger.d("SettingsViewModel: setLandscapeDistanceSectionWeight requested: %f", weight)
        viewModelScope.launch {
            updateLandscapeDistanceSectionWeightUseCase(weight)
        }
    }
}

sealed interface SettingsUiState {
    data object Loading : SettingsUiState
    data class Success(
        val appSettings: AppSettings = AppSettings(),
        val inputSettings: InputSettings = InputSettings(),
        val locationSettings: LocationSettings = LocationSettings(),
        val odometerSettings: OdometerSettings = OdometerSettings(),
        val mapSettings: MapSettings = MapSettings(),
        val roadbookSettings: RoadbookSettings = RoadbookSettings(),
    ) : SettingsUiState
}
