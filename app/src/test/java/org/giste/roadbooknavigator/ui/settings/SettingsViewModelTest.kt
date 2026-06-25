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
import org.giste.roadbooknavigator.features.settings.domain.AppOrientation
import org.giste.roadbooknavigator.features.settings.domain.AppSettings
import org.giste.roadbooknavigator.features.settings.domain.AppTheme
import org.giste.roadbooknavigator.features.settings.domain.usecase.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private val getSettingsUseCase: GetSettingsUseCase = mockk()
    private val updateThemeUseCase: UpdateThemeUseCase = mockk()
    private val updateOrientationUseCase: UpdateOrientationUseCase = mockk()
    private val updateShortDistanceThresholdUseCase: UpdateShortDistanceThresholdUseCase = mockk()
    private val updateOdometerSpeedThresholdUseCase: UpdateOdometerSpeedThresholdUseCase = mockk()
    private val updateOdometerMinAccuracyUseCase: UpdateOdometerMinAccuracyUseCase = mockk()
    private val updateOdometerMinVerticalAccuracyUseCase: UpdateOdometerMinVerticalAccuracyUseCase = mockk()
    private val updateOdometerPollingIntervalUseCase: UpdateOdometerPollingIntervalUseCase = mockk()
    private val updateOdometerMinDistanceUseCase: UpdateOdometerMinDistanceUseCase = mockk()
    private val restoreOdometerDefaultsUseCase: RestoreOdometerDefaultsUseCase = mockk()

    private val settingsFlow = MutableStateFlow(AppSettings())
    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { getSettingsUseCase() } returns settingsFlow

        viewModel = SettingsViewModel(
            getSettingsUseCase,
            updateThemeUseCase,
            updateOrientationUseCase,
            updateShortDistanceThresholdUseCase,
            updateOdometerSpeedThresholdUseCase,
            updateOdometerMinAccuracyUseCase,
            updateOdometerMinVerticalAccuracyUseCase,
            updateOdometerPollingIntervalUseCase,
            updateOdometerMinDistanceUseCase,
            restoreOdometerDefaultsUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be Success with default settings`() = runTest {
        backgroundScope.launch(testDispatcher) { viewModel.uiState.collect {} }

        assertEquals(SettingsUiState.Success(AppSettings()), viewModel.uiState.value)
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
    fun `setOdometerPollingInterval should call use case`() = runTest {
        coEvery { updateOdometerPollingIntervalUseCase(any()) } returns Result.success(Unit)
        viewModel.setOdometerPollingInterval(1000L)
        coVerify { updateOdometerPollingIntervalUseCase(1000L) }
    }

    @Test
    fun `setOdometerMinDistance should call use case`() = runTest {
        coEvery { updateOdometerMinDistanceUseCase(any()) } returns Result.success(Unit)
        viewModel.setOdometerMinDistance(2.0f)
        coVerify { updateOdometerMinDistanceUseCase(2.0f) }
    }

    @Test
    fun `restoreOdometerDefaults should call use case`() = runTest {
        coEvery { restoreOdometerDefaultsUseCase() } returns Result.success(Unit)
        viewModel.restoreOdometerDefaults()
        coVerify { restoreOdometerDefaultsUseCase() }
    }
}
