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

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.giste.roadbooknavigator.core.util.Logger
import org.giste.roadbooknavigator.features.location.domain.LocationSettings
import org.giste.roadbooknavigator.features.location.domain.usecase.GetLocationSettingsUseCase
import org.giste.roadbooknavigator.features.location.domain.usecase.RestoreLocationDefaultsUseCase
import org.giste.roadbooknavigator.features.location.domain.usecase.UpdateLocationMinDistanceUseCase
import org.giste.roadbooknavigator.features.location.domain.usecase.UpdateLocationPollingIntervalUseCase
import org.giste.roadbooknavigator.features.odometer.domain.OdometerSettings
import org.giste.roadbooknavigator.features.odometer.domain.usecase.GetOdometerSettingsUseCase
import org.giste.roadbooknavigator.features.odometer.domain.usecase.RestoreOdometerSettingsDefaultsUseCase
import org.giste.roadbooknavigator.features.odometer.domain.usecase.UpdateOdometerMinAccuracyUseCase
import org.giste.roadbooknavigator.features.odometer.domain.usecase.UpdateOdometerMinVerticalAccuracyUseCase
import org.giste.roadbooknavigator.features.odometer.domain.usecase.UpdateOdometerSpeedThresholdUseCase
import org.giste.roadbooknavigator.features.settings.domain.AppOrientation
import org.giste.roadbooknavigator.features.settings.domain.AppSettings
import org.giste.roadbooknavigator.core.settings.domain.AppTheme
import org.giste.roadbooknavigator.features.settings.domain.RemoteKeys
import org.giste.roadbooknavigator.features.settings.domain.RemoteModel
import org.giste.roadbooknavigator.features.settings.domain.usecase.GetSettingsUseCase
import org.giste.roadbooknavigator.features.settings.domain.usecase.UpdateCustomKeysUseCase
import org.giste.roadbooknavigator.features.settings.domain.usecase.UpdateFullScreenUseCase
import org.giste.roadbooknavigator.features.settings.domain.usecase.UpdateOrientationUseCase
import org.giste.roadbooknavigator.features.settings.domain.usecase.UpdateRemoteModelUseCase
import org.giste.roadbooknavigator.features.settings.domain.usecase.UpdateShortDistanceThresholdUseCase
import org.giste.roadbooknavigator.features.settings.domain.usecase.UpdateThemeUseCase
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private val getSettingsUseCase: GetSettingsUseCase = mockk()
    private val getLocationsUseCase: GetLocationSettingsUseCase = mockk()
    private val getOdometerSettingsUseCase: GetOdometerSettingsUseCase = mockk()
    private val updateThemeUseCase: UpdateThemeUseCase = mockk()
    private val updateFullScreenUseCase: UpdateFullScreenUseCase = mockk()
    private val updateOrientationUseCase: UpdateOrientationUseCase = mockk()
    private val updateShortDistanceThresholdUseCase: UpdateShortDistanceThresholdUseCase = mockk()
    private val updateOdometerSpeedThresholdUseCase: UpdateOdometerSpeedThresholdUseCase = mockk()
    private val updateOdometerMinAccuracyUseCase: UpdateOdometerMinAccuracyUseCase = mockk()
    private val updateOdometerMinVerticalAccuracyUseCase: UpdateOdometerMinVerticalAccuracyUseCase = mockk()
    private val restoreOdometerSettingsDefaultsUseCase: RestoreOdometerSettingsDefaultsUseCase = mockk()
    private val updateLocationPollingIntervalUseCase: UpdateLocationPollingIntervalUseCase = mockk()
    private val updateLocationMinDistanceUseCase: UpdateLocationMinDistanceUseCase = mockk()
    private val restoreLocationDefaultsUseCase: RestoreLocationDefaultsUseCase = mockk()
    private val updateRemoteModelUseCase: UpdateRemoteModelUseCase = mockk()
    private val updateCustomKeysUseCase: UpdateCustomKeysUseCase = mockk()
    private val logger: Logger = mockk(relaxed = true)

    private val settingsFlow = MutableStateFlow(AppSettings())
    private val locationSettingsFlow = MutableStateFlow(LocationSettings())
    private val odometerSettingsFlow = MutableStateFlow(OdometerSettings())
    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { getSettingsUseCase() } returns settingsFlow
        every { getLocationsUseCase() } returns locationSettingsFlow
        every { getOdometerSettingsUseCase() } returns odometerSettingsFlow

        viewModel = SettingsViewModel(
            getSettingsUseCase = getSettingsUseCase,
            getLocationSettingsUseCase = getLocationsUseCase,
            getOdometerSettingsUseCase = getOdometerSettingsUseCase,
            updateThemeUseCase = updateThemeUseCase,
            updateOrientationUseCase = updateOrientationUseCase,
            updateFullScreenUseCase = updateFullScreenUseCase,
            updateShortDistanceThresholdUseCase = updateShortDistanceThresholdUseCase,
            updateOdometerSpeedThresholdUseCase = updateOdometerSpeedThresholdUseCase,
            updateOdometerMinAccuracyUseCase = updateOdometerMinAccuracyUseCase,
            updateOdometerMinVerticalAccuracyUseCase = updateOdometerMinVerticalAccuracyUseCase,
            restoreOdometerSettingsDefaultsUseCase = restoreOdometerSettingsDefaultsUseCase,
            updateLocationPollingIntervalUseCase = updateLocationPollingIntervalUseCase,
            updateLocationMinDistanceUseCase = updateLocationMinDistanceUseCase,
            restoreLocationDefaultsUseCase = restoreLocationDefaultsUseCase,
            updateRemoteModelUseCase = updateRemoteModelUseCase,
            updateCustomKeysUseCase = updateCustomKeysUseCase,
            logger = logger
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be Success with default settings`() = runTest {
        backgroundScope.launch(testDispatcher) { viewModel.uiState.collect {} }

        assertEquals(SettingsUiState.Success(AppSettings(), LocationSettings(), OdometerSettings()), viewModel.uiState.value)
    }

    @Test
    fun `setFullScreen should call use case`() = runTest {
        coEvery { updateFullScreenUseCase(any()) } returns Result.success(Unit)
        viewModel.setFullScreen(true)
        coVerify { updateFullScreenUseCase(true) }
    }

    @Test
    fun `setTheme should call use case`() = runTest {
        coEvery { updateThemeUseCase(any()) } returns Result.success(Unit)
        viewModel.setTheme(AppTheme.DARK)
        coVerify { updateThemeUseCase(AppTheme.DARK) }
    }

    @Test
    fun `setOrientation should call use case`() = runTest {
        coEvery { updateOrientationUseCase(any()) } returns Result.success(Unit)
        viewModel.setOrientation(AppOrientation.HORIZONTAL)
        coVerify { updateOrientationUseCase(AppOrientation.HORIZONTAL) }
    }

    @Test
    fun `setShortDistanceThreshold should call use case`() = runTest {
        coEvery { updateShortDistanceThresholdUseCase(any()) } returns Result.success(Unit)
        viewModel.setShortDistanceThreshold(500L)
        coVerify { updateShortDistanceThresholdUseCase(500L) }
    }

    @Test
    fun `setOdometerSpeedThreshold should call use case`() = runTest {
        coEvery { updateOdometerSpeedThresholdUseCase(any()) } returns Result.success(Unit)
        viewModel.setOdometerSpeedThreshold(0.8f)
        coVerify { updateOdometerSpeedThresholdUseCase(0.8f) }
    }

    @Test
    fun `setOdometerMinAccuracy should call use case`() = runTest {
        coEvery { updateOdometerMinAccuracyUseCase(any()) } returns Result.success(Unit)
        viewModel.setOdometerMinAccuracy(15.0f)
        coVerify { updateOdometerMinAccuracyUseCase(15.0f) }
    }

    @Test
    fun `setOdometerMinVerticalAccuracy should call use case`() = runTest {
        coEvery { updateOdometerMinVerticalAccuracyUseCase(any()) } returns Result.success(Unit)
        viewModel.setOdometerMinVerticalAccuracy(5.0f)
        coVerify { updateOdometerMinVerticalAccuracyUseCase(5.0f) }
    }

    @Test
    fun `setLocationPollingInterval should call use case`() = runTest {
        coEvery { updateLocationPollingIntervalUseCase(any()) } returns Result.success(Unit)
        viewModel.setLocationPollingInterval(1000L)
        coVerify { updateLocationPollingIntervalUseCase(1000L) }
    }

    @Test
    fun `setLocationMinDistance should call use case`() = runTest {
        coEvery { updateLocationMinDistanceUseCase(any()) } returns Result.success(Unit)
        viewModel.setLocationMinDistance(2.0f)
        coVerify { updateLocationMinDistanceUseCase(2.0f) }
    }

    @Test
    fun `restoreOdometerDefaults should call use case`() = runTest {
        coEvery { restoreOdometerSettingsDefaultsUseCase() } returns Result.success(Unit)
        coEvery { restoreLocationDefaultsUseCase() } returns Result.success(Unit)
        viewModel.restoreOdometerDefaults()
        coVerify {
            restoreOdometerSettingsDefaultsUseCase()
            restoreLocationDefaultsUseCase()
        }
    }

    @Test
    fun `setRemoteModel should call use case`() = runTest {
        coEvery { updateRemoteModelUseCase(any()) } returns Result.success(Unit)
        viewModel.setRemoteModel(RemoteModel.TERRA_PIRATA)
        coVerify { updateRemoteModelUseCase(RemoteModel.TERRA_PIRATA) }
    }

    @Test
    fun `setCustomKeys should call use case`() = runTest {
        val keys = RemoteKeys.DND2
        coEvery { updateCustomKeysUseCase(any()) } returns Result.success(Unit)
        viewModel.setCustomKeys(keys)
        coVerify { updateCustomKeysUseCase(keys) }
    }
}
