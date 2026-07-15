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
import org.giste.roadbooknavigator.features.location.domain.LocationSettings
import org.giste.roadbooknavigator.features.location.domain.usecase.GetLocationSettingsUseCase
import org.giste.roadbooknavigator.features.location.domain.usecase.RestoreLocationDefaultsUseCase
import org.giste.roadbooknavigator.features.location.domain.usecase.UpdateLocationMinDistanceUseCase
import org.giste.roadbooknavigator.features.location.domain.usecase.UpdateLocationPollingIntervalUseCase
import org.giste.roadbooknavigator.features.map.domain.model.MapSettings
import org.giste.roadbooknavigator.features.map.domain.usecase.GetMapSettingsUseCase
import org.giste.roadbooknavigator.features.map.domain.usecase.SaveMapSettingsUseCase
import org.giste.roadbooknavigator.features.odometer.domain.OdometerSettings
import org.giste.roadbooknavigator.features.odometer.domain.usecase.GetOdometerSettingsUseCase
import org.giste.roadbooknavigator.features.odometer.domain.usecase.RestoreOdometerSettingsDefaultsUseCase
import org.giste.roadbooknavigator.features.odometer.domain.usecase.UpdateOdometerMinAccuracyUseCase
import org.giste.roadbooknavigator.features.odometer.domain.usecase.UpdateOdometerMinVerticalAccuracyUseCase
import org.giste.roadbooknavigator.features.odometer.domain.usecase.UpdateOdometerRemoteKeysUseCase
import org.giste.roadbooknavigator.features.odometer.domain.usecase.UpdateOdometerSpeedThresholdUseCase
import org.giste.roadbooknavigator.features.roadbook.domain.model.RoadbookSettings
import org.giste.roadbooknavigator.features.roadbook.domain.usecase.GetRoadbookSettingsUseCase
import org.giste.roadbooknavigator.features.roadbook.domain.usecase.SaveRoadbookSettingsUseCase
import org.giste.roadbooknavigator.features.settings.domain.AppOrientation
import org.giste.roadbooknavigator.features.settings.domain.AppSettings
import org.giste.roadbooknavigator.features.settings.domain.RemoteModel
import org.giste.roadbooknavigator.features.settings.domain.usecase.GetSettingsUseCase
import org.giste.roadbooknavigator.features.settings.domain.usecase.UpdateFullScreenUseCase
import org.giste.roadbooknavigator.features.settings.domain.usecase.UpdateOrientationUseCase
import org.giste.roadbooknavigator.features.settings.domain.usecase.UpdateRemoteModelUseCase
import org.giste.roadbooknavigator.features.settings.domain.usecase.UpdateThemeUseCase
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    getSettingsUseCase: GetSettingsUseCase,
    getLocationSettingsUseCase: GetLocationSettingsUseCase,
    getOdometerSettingsUseCase: GetOdometerSettingsUseCase,
    getMapSettingsUseCase: GetMapSettingsUseCase,
    getRoadbookSettingsUseCase: GetRoadbookSettingsUseCase,
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
    private val updateRemoteModelUseCase: UpdateRemoteModelUseCase,
    private val updateOdometerRemoteKeysUseCase: UpdateOdometerRemoteKeysUseCase,
    private val saveMapSettingsUseCase: SaveMapSettingsUseCase,
    private val logger: Logger
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = combine(
        getSettingsUseCase(),
        getLocationSettingsUseCase(),
        getOdometerSettingsUseCase(),
        getMapSettingsUseCase(),
        getRoadbookSettingsUseCase()
    ) { settings, locationSettings, odometerSettings, mapSettings, roadbookSettings ->
        SettingsUiState.Success(
            settings,
            locationSettings,
            odometerSettings,
            mapSettings,
            roadbookSettings
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
            updateRemoteModelUseCase(model)
            if (model != RemoteModel.CUSTOM) {
                // Update Roadbook keys
                val (rbUp, rbDown) = when (model) {
                    RemoteModel.DND2 -> listOf(19) to listOf(20) // KEYCODE_DPAD_UP/DOWN
                    RemoteModel.TERRA_PIRATA -> listOf(87) to listOf(88) // KEYCODE_MEDIA_NEXT/PREVIOUS
                    RemoteModel.CUSTOM -> return@launch
                }
                saveRoadbookSettingsUseCase.updateRemoteKeys(rbUp, rbDown)

                // Update Odometer keys
                val (odoInc, odoDec, odoRes) = when (model) {
                    RemoteModel.DND2 -> Triple(listOf(22), listOf(21), listOf(136)) // DPAD_RIGHT/LEFT, F6
                    RemoteModel.TERRA_PIRATA -> Triple(
                        listOf(24),
                        listOf(25),
                        listOf(85, 126)
                    ) // VOL_UP/DOWN, PLAY_PAUSE/PLAY
                    RemoteModel.CUSTOM -> return@launch
                }
                updateOdometerRemoteKeysUseCase(odoInc, odoDec, odoRes)
            }
        }
    }

    fun setOdometerKeys(increase: List<Int>, decrease: List<Int>, reset: List<Int>) {
        logger.d("SettingsViewModel: setOdometerKeys requested")
        viewModelScope.launch {
            updateRemoteModelUseCase(RemoteModel.CUSTOM)
            updateOdometerRemoteKeysUseCase(increase, decrease, reset)
        }
    }

    fun setRoadbookKeys(up: List<Int>, down: List<Int>) {
        logger.d("SettingsViewModel: setRoadbookKeys requested")
        viewModelScope.launch {
            updateRemoteModelUseCase(RemoteModel.CUSTOM)
            saveRoadbookSettingsUseCase.updateRemoteKeys(up, down)
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
}

sealed interface SettingsUiState {
    data object Loading : SettingsUiState
    data class Success(
        val appSettings: AppSettings = AppSettings(),
        val locationSettings: LocationSettings = LocationSettings(),
        val odometerSettings: OdometerSettings = OdometerSettings(),
        val mapSettings: MapSettings = MapSettings(),
        val roadbookSettings: RoadbookSettings = RoadbookSettings(),
    ) : SettingsUiState
}
