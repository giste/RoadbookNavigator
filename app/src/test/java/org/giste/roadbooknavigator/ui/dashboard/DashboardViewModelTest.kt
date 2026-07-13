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
import org.giste.roadbooknavigator.features.odometer.domain.Odometer
import org.giste.roadbooknavigator.features.odometer.domain.usecase.*
import org.giste.roadbooknavigator.features.settings.domain.AppSettings
import org.giste.roadbooknavigator.features.settings.domain.usecase.GetSettingsUseCase
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    private val getOdometerUseCase: GetOdometerUseCase = mockk()
    private val resetPartialDistanceUseCase: ResetPartialDistanceUseCase = mockk()
    private val resetAllDistancesUseCase: ResetAllDistancesUseCase = mockk()
    private val incrementPartialDistanceUseCase: IncrementPartialDistanceUseCase = mockk()
    private val decrementPartialDistanceUseCase: DecrementPartialDistanceUseCase = mockk()
    private val setPartialDistanceUseCase: SetPartialDistanceUseCase = mockk()
    private val getSettingsUseCase: GetSettingsUseCase = mockk()
    private val logger: Logger = mockk(relaxed = true)

    private val odometerFlow = MutableStateFlow(Odometer())
    private val settingsFlow = MutableStateFlow(AppSettings())
    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: DashboardViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { getOdometerUseCase() } returns odometerFlow
        every { getSettingsUseCase() } returns settingsFlow

        viewModel = DashboardViewModel(
            getOdometerUseCase,
            resetPartialDistanceUseCase,
            resetAllDistancesUseCase,
            incrementPartialDistanceUseCase,
            decrementPartialDistanceUseCase,
            setPartialDistanceUseCase,
            getSettingsUseCase,
            logger
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be correct`() = runTest {
        backgroundScope.launch(testDispatcher) { viewModel.uiState.collect {} }

        assertEquals(Odometer(), viewModel.uiState.value.odometer)
        assertEquals(false, viewModel.uiState.value.showSetPartialDialog)
        assertEquals(true, viewModel.uiState.value.isFullScreen)
    }

    @Test
    fun `settings update should update isFullScreen in uiState`() = runTest {
        backgroundScope.launch(testDispatcher) { viewModel.uiState.collect {} }

        settingsFlow.value = AppSettings(fullScreen = false)
        assertEquals(false, viewModel.uiState.value.isFullScreen)

        settingsFlow.value = AppSettings(fullScreen = true)
        assertEquals(true, viewModel.uiState.value.isFullScreen)
    }

    @Test
    fun `odometer actions should call respective use cases`() = runTest {
        coEvery { resetPartialDistanceUseCase() } returns Unit
        coEvery { resetAllDistancesUseCase() } returns Unit
        coEvery { incrementPartialDistanceUseCase() } returns Unit
        coEvery { decrementPartialDistanceUseCase() } returns Unit
        coEvery { setPartialDistanceUseCase(any()) } returns Unit

        viewModel.resetPartialDistance()
        coVerify { resetPartialDistanceUseCase() }

        viewModel.resetAllDistances()
        coVerify { resetAllDistancesUseCase() }

        viewModel.incrementPartialDistance()
        coVerify { incrementPartialDistanceUseCase() }

        viewModel.decrementPartialDistance()
        coVerify { decrementPartialDistanceUseCase() }

        viewModel.setPartialDistance(100.0)
        coVerify { setPartialDistanceUseCase(100.0) }
    }

    @Test
    fun `show and hide partial dialog should update uiState`() = runTest {
        backgroundScope.launch(testDispatcher) { viewModel.uiState.collect {} }

        viewModel.showSetPartialDialog()
        assertTrue(viewModel.uiState.value.showSetPartialDialog)

        viewModel.hideSetPartialDialog()
        assertTrue(!viewModel.uiState.value.showSetPartialDialog)
    }
}
