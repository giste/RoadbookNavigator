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

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
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
import org.giste.roadbooknavigator.features.settings.domain.input.usecase.UpdateOdometerRemoteKeysUseCase
import org.giste.roadbooknavigator.features.settings.domain.odometer.usecase.UpdateOdometerSpeedThresholdUseCase
import org.giste.roadbooknavigator.features.roadbook.domain.model.RoadbookSettings
import org.giste.roadbooknavigator.features.roadbook.domain.usecase.GetRoadbookSettingsUseCase
import org.giste.roadbooknavigator.features.settings.domain.AppOrientation
import org.giste.roadbooknavigator.features.settings.domain.AppSettings
import org.giste.roadbooknavigator.features.settings.domain.input.RemoteModel
import org.giste.roadbooknavigator.features.settings.domain.input.usecase.UpdateRemoteModelUseCase
import org.giste.roadbooknavigator.features.settings.domain.input.usecase.UpdateRoadbookKeySettingsUseCase
import org.giste.roadbooknavigator.features.settings.domain.roadbook.usecase.SaveRoadbookSettingsUseCase
import org.giste.roadbooknavigator.features.settings.domain.usecase.GetSettingsUseCase
import org.giste.roadbooknavigator.features.settings.domain.usecase.UpdateFullScreenUseCase
import org.giste.roadbooknavigator.features.settings.domain.usecase.UpdateLandscapeDistanceSectionWeightUseCase
import org.giste.roadbooknavigator.features.settings.domain.usecase.UpdateOrientationUseCase
import org.giste.roadbooknavigator.features.settings.domain.usecase.UpdateThemeUseCase
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val getSettingsUseCase: GetSettingsUseCase = mockk()
    private val observeLocationSettingsUseCase: ObserveLocationSettingsUseCase = mockk()
    private val observeOdometerSettingsUseCase: ObserveOdometerSettingsUseCase = mockk()
    private val getMapSettingsUseCase: GetMapSettingsUseCase = mockk()
    private val getRoadbookSettingsUseCase: GetRoadbookSettingsUseCase = mockk()
    private val updateThemeUseCase: UpdateThemeUseCase = mockk()
    private val updateOrientationUseCase: UpdateOrientationUseCase = mockk()
    private val updateFullScreenUseCase: UpdateFullScreenUseCase = mockk()
    private val saveRoadbookSettingsUseCase: SaveRoadbookSettingsUseCase = mockk()
    private val updateOdometerSpeedThresholdUseCase: UpdateOdometerSpeedThresholdUseCase = mockk()
    private val updateOdometerMinAccuracyUseCase: UpdateOdometerMinAccuracyUseCase = mockk()
    private val updateOdometerMinVerticalAccuracyUseCase: UpdateOdometerMinVerticalAccuracyUseCase = mockk()
    private val restoreOdometerSettingsDefaultsUseCase: RestoreOdometerSettingsDefaultsUseCase = mockk()
    private val updateLocationPollingIntervalUseCase: UpdateLocationPollingIntervalUseCase = mockk()
    private val updateLocationMinDistanceUseCase: UpdateLocationMinDistanceUseCase = mockk()
    private val restoreLocationDefaultsUseCase: RestoreLocationDefaultsUseCase = mockk()
    private val updateRemoteModelUseCase: UpdateRemoteModelUseCase = mockk()
    private val updateOdometerRemoteKeysUseCase: UpdateOdometerRemoteKeysUseCase = mockk()
    private val updateRoadbookKeySettingsUseCase: UpdateRoadbookKeySettingsUseCase = mockk()
    private val saveMapSettingsUseCase: SaveMapSettingsUseCase = mockk()
    private val updateLandscapeDistanceSectionWeightUseCase: UpdateLandscapeDistanceSectionWeightUseCase = mockk()
    private val logger: Logger = mockk(relaxed = true)

    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        every { getSettingsUseCase() } returns flowOf(AppSettings())
        every { observeLocationSettingsUseCase() } returns flowOf(LocationSettings())
        every { observeOdometerSettingsUseCase() } returns flowOf(OdometerSettings())
        every { getMapSettingsUseCase() } returns flowOf(MapSettings())
        every { getRoadbookSettingsUseCase() } returns flowOf(RoadbookSettings())

        viewModel = SettingsViewModel(
            getSettingsUseCase = getSettingsUseCase,
            observeLocationSettingsUseCase = observeLocationSettingsUseCase,
            observeOdometerSettingsUseCase = observeOdometerSettingsUseCase,
            getMapSettingsUseCase = getMapSettingsUseCase,
            getRoadbookSettingsUseCase = getRoadbookSettingsUseCase,
            updateThemeUseCase = updateThemeUseCase,
            updateOrientationUseCase = updateOrientationUseCase,
            updateFullScreenUseCase = updateFullScreenUseCase,
            saveRoadbookSettingsUseCase = saveRoadbookSettingsUseCase,
            updateOdometerSpeedThresholdUseCase = updateOdometerSpeedThresholdUseCase,
            updateOdometerMinAccuracyUseCase = updateOdometerMinAccuracyUseCase,
            updateOdometerMinVerticalAccuracyUseCase = updateOdometerMinVerticalAccuracyUseCase,
            restoreOdometerSettingsDefaultsUseCase = restoreOdometerSettingsDefaultsUseCase,
            updateLocationPollingIntervalUseCase = updateLocationPollingIntervalUseCase,
            updateLocationMinDistanceUseCase = updateLocationMinDistanceUseCase,
            restoreLocationDefaultsUseCase = restoreLocationDefaultsUseCase,
            updateRemoteModelUseCase = updateRemoteModelUseCase,
            updateOdometerRemoteKeysUseCase = updateOdometerRemoteKeysUseCase,
            updateRoadbookKeySettingsUseCase = updateRoadbookKeySettingsUseCase,
            saveMapSettingsUseCase = saveMapSettingsUseCase,
            updateLandscapeDistanceSectionWeightUseCase = updateLandscapeDistanceSectionWeightUseCase,
            logger = logger
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading then Success`() = runTest {
        val results = mutableListOf<SettingsUiState>()
        val job = backgroundScope.launch {
            viewModel.uiState.collect { results.add(it) }
        }
        
        advanceUntilIdle()
        
        assertTrue(results.any { it is SettingsUiState.Success })
        job.cancel()
    }

    @Test
    fun `setTheme calls use case`() = runTest {
        val theme = AppTheme.DARK
        coEvery { updateThemeUseCase(theme) } returns Result.success(Unit)

        viewModel.setTheme(theme)
        advanceUntilIdle()

        coVerify { updateThemeUseCase(theme) }
    }

    @Test
    fun `setOrientation calls use case`() = runTest {
        val orientation = AppOrientation.HORIZONTAL
        coEvery { updateOrientationUseCase(orientation) } returns Result.success(Unit)

        viewModel.setOrientation(orientation)
        advanceUntilIdle()

        coVerify { updateOrientationUseCase(orientation) }
    }

    @Test
    fun `setFullScreen calls use case`() = runTest {
        coEvery { updateFullScreenUseCase(true) } returns Result.success(Unit)

        viewModel.setFullScreen(true)
        advanceUntilIdle()

        coVerify { updateFullScreenUseCase(true) }
    }

    @Test
    fun `setShortDistanceThreshold calls use case`() = runTest {
        val threshold = 500L
        coEvery { saveRoadbookSettingsUseCase(threshold) } returns Unit

        viewModel.setShortDistanceThreshold(threshold)
        advanceUntilIdle()

        coVerify { saveRoadbookSettingsUseCase(threshold) }
    }

    @Test
    fun `setOdometerSpeedThreshold calls use case`() = runTest {
        val threshold = 1.0f
        coEvery { updateOdometerSpeedThresholdUseCase(threshold) } returns Result.success(Unit)

        viewModel.setOdometerSpeedThreshold(threshold)
        advanceUntilIdle()

        coVerify { updateOdometerSpeedThresholdUseCase(threshold) }
    }

    @Test
    fun `setOdometerMinAccuracy calls use case`() = runTest {
        val accuracy = 10.0f
        coEvery { updateOdometerMinAccuracyUseCase(accuracy) } returns Result.success(Unit)

        viewModel.setOdometerMinAccuracy(accuracy)
        advanceUntilIdle()

        coVerify { updateOdometerMinAccuracyUseCase(accuracy) }
    }

    @Test
    fun `setOdometerMinVerticalAccuracy calls use case`() = runTest {
        val accuracy = 5.0f
        coEvery { updateOdometerMinVerticalAccuracyUseCase(accuracy) } returns Result.success(Unit)

        viewModel.setOdometerMinVerticalAccuracy(accuracy)
        advanceUntilIdle()

        coVerify { updateOdometerMinVerticalAccuracyUseCase(accuracy) }
    }

    @Test
    fun `setLocationPollingInterval calls use case`() = runTest {
        val interval = 2000L
        coEvery { updateLocationPollingIntervalUseCase(interval) } returns Result.success(Unit)

        viewModel.setLocationPollingInterval(interval)
        advanceUntilIdle()

        coVerify { updateLocationPollingIntervalUseCase(interval) }
    }

    @Test
    fun `setLocationMinDistance calls use case`() = runTest {
        val distance = 5.0f
        coEvery { updateLocationMinDistanceUseCase(distance) } returns Result.success(Unit)

        viewModel.setLocationMinDistance(distance)
        advanceUntilIdle()

        coVerify { updateLocationMinDistanceUseCase(distance) }
    }

    @Test
    fun `restoreOdometerDefaults calls use cases`() = runTest {
        coEvery { restoreOdometerSettingsDefaultsUseCase() } returns Result.success(Unit)
        coEvery { restoreLocationDefaultsUseCase() } returns Result.success(Unit)

        viewModel.restoreOdometerDefaults()
        advanceUntilIdle()

        coVerify { restoreOdometerSettingsDefaultsUseCase() }
        coVerify { restoreLocationDefaultsUseCase() }
    }

    @Test
    fun `setRemoteModel DND2 updates keys correctly`() = runTest {
        coEvery { updateRemoteModelUseCase(RemoteModel.DND2) } returns Result.success(Unit)
        coEvery { updateRoadbookKeySettingsUseCase(any(), any()) } returns Unit
        coEvery { updateOdometerRemoteKeysUseCase(any(), any(), any()) } returns Result.success(Unit)

        viewModel.setRemoteModel(RemoteModel.DND2)
        advanceUntilIdle()

        coVerify { updateRemoteModelUseCase(RemoteModel.DND2) }
        coVerify { updateRoadbookKeySettingsUseCase(listOf(19), listOf(20)) }
        coVerify { updateOdometerRemoteKeysUseCase(listOf(22), listOf(21), listOf(136)) }
    }

    @Test
    fun `setOdometerKeys calls use case and sets custom model`() = runTest {
        val inc = listOf(1)
        val dec = listOf(2)
        val res = listOf(3)
        coEvery { updateRemoteModelUseCase(RemoteModel.CUSTOM) } returns Result.success(Unit)
        coEvery { updateOdometerRemoteKeysUseCase(inc, dec, res) } returns Result.success(Unit)

        viewModel.setOdometerKeys(inc, dec, res)
        advanceUntilIdle()

        coVerify { updateRemoteModelUseCase(RemoteModel.CUSTOM) }
        coVerify { updateOdometerRemoteKeysUseCase(inc, dec, res) }
    }

    @Test
    fun `setRoadbookKeys calls use case and sets custom model`() = runTest {
        val up = listOf(1)
        val down = listOf(2)
        coEvery { updateRemoteModelUseCase(RemoteModel.CUSTOM) } returns Result.success(Unit)
        coEvery { updateRoadbookKeySettingsUseCase(up, down) } returns Unit

        viewModel.setRoadbookKeys(up, down)
        advanceUntilIdle()

        coVerify { updateRemoteModelUseCase(RemoteModel.CUSTOM) }
        coVerify { updateRoadbookKeySettingsUseCase(up, down) }
    }

    @Test
    fun `setMapInitialZoom calls use case`() = runTest {
        val zoom = 15
        coEvery { saveMapSettingsUseCase(any()) } returns Unit

        viewModel.setMapInitialZoom(zoom)
        advanceUntilIdle()

        coVerify { saveMapSettingsUseCase(match { it.initialZoom == zoom }) }
    }

    @Test
    fun `setLandscapeDistanceSectionWeight calls use case`() = runTest {
        val weight = 0.5f
        coEvery { updateLandscapeDistanceSectionWeightUseCase(weight) } returns Result.success(Unit)

        viewModel.setLandscapeDistanceSectionWeight(weight)
        advanceUntilIdle()

        coVerify { updateLandscapeDistanceSectionWeightUseCase(weight) }
    }
}
