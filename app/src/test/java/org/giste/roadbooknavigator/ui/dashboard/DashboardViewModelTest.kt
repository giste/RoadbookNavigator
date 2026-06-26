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
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.giste.roadbooknavigator.features.odometer.domain.Odometer
import org.giste.roadbooknavigator.features.odometer.domain.usecase.*
import org.giste.roadbooknavigator.features.roadbook.domain.model.RoadbookPosition
import org.giste.roadbooknavigator.features.roadbook.domain.model.Route
import org.giste.roadbooknavigator.features.roadbook.domain.usecase.*
import org.giste.roadbooknavigator.features.roadbook.ui.RoadbookUiState
import org.giste.roadbooknavigator.features.settings.domain.AppSettings
import org.giste.roadbooknavigator.features.settings.domain.usecase.GetSettingsUseCase
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.InputStream

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    private val getActiveRoadbookUseCase: GetActiveRoadbookUseCase = mockk()
    private val importRoadbookUseCase: ImportRoadbookUseCase = mockk()
    private val getOdometerUseCase: GetOdometerUseCase = mockk()
    private val resetPartialDistanceUseCase: ResetPartialDistanceUseCase = mockk()
    private val resetAllDistancesUseCase: ResetAllDistancesUseCase = mockk()
    private val incrementPartialDistanceUseCase: IncrementPartialDistanceUseCase = mockk()
    private val decrementPartialDistanceUseCase: DecrementPartialDistanceUseCase = mockk()
    private val setPartialDistanceUseCase: SetPartialDistanceUseCase = mockk()
    private val getRoadbookPositionUseCase: GetRoadbookPositionUseCase = mockk()
    private val saveRoadbookPositionUseCase: SaveRoadbookPositionUseCase = mockk()
    private val getSettingsUseCase: GetSettingsUseCase = mockk()

    private val activeRoadbookFlow = MutableStateFlow<Route?>(null)
    private val odometerFlow = MutableStateFlow(Odometer())
    private val scrollPositionFlow = MutableStateFlow(RoadbookPosition(0, 0))
    private val settingsFlow = MutableStateFlow(AppSettings())
    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: DashboardViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { getActiveRoadbookUseCase() } returns activeRoadbookFlow
        every { getOdometerUseCase() } returns odometerFlow
        every { getRoadbookPositionUseCase() } returns scrollPositionFlow
        every { getSettingsUseCase() } returns settingsFlow

        viewModel = DashboardViewModel(
            getActiveRoadbookUseCase,
            importRoadbookUseCase,
            getOdometerUseCase,
            resetPartialDistanceUseCase,
            resetAllDistancesUseCase,
            incrementPartialDistanceUseCase,
            decrementPartialDistanceUseCase,
            setPartialDistanceUseCase,
            getRoadbookPositionUseCase,
            saveRoadbookPositionUseCase,
            getSettingsUseCase,
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be correct`() = runTest {
        backgroundScope.launch(testDispatcher) { viewModel.uiState.collect {} }

        assertEquals(RoadbookUiState.Empty, viewModel.uiState.value.roadbook)
        assertEquals(Odometer(), viewModel.uiState.value.odometer)
        assertEquals(false, viewModel.uiState.value.showSetPartialDialog)
    }

    @Test
    fun `roadbook success state should be correct`() = runTest {
        val route = mockk<Route>()
        activeRoadbookFlow.value = route
        scrollPositionFlow.value = RoadbookPosition(5, 10)
        settingsFlow.value = AppSettings(shortDistanceThreshold = 250L)

        backgroundScope.launch(testDispatcher) { viewModel.uiState.collect {} }

        val expectedRoadbookState = RoadbookUiState.Success(
            route,
            shortDistanceThreshold = 250L,
            initialIndex = 5,
            initialOffset = 10
        )
        assertEquals(expectedRoadbookState, viewModel.uiState.value.roadbook)
        assertEquals(RoadbookPosition(5, 10), viewModel.uiState.value.initialScrollPosition)

        // Test settings update
        settingsFlow.value = AppSettings(shortDistanceThreshold = 500L)
        assertEquals(500L, (viewModel.uiState.value.roadbook as RoadbookUiState.Success).shortDistanceThreshold)
    }

    @Test
    fun `importRoute should update state to Loading while processing and override success state`() = runTest {
        // Start with a success state
        activeRoadbookFlow.value = mockk<Route>()
        backgroundScope.launch(testDispatcher) { viewModel.uiState.collect {} }
        assertTrue(viewModel.uiState.value.roadbook is RoadbookUiState.Success)

        val inputStream = mockk<InputStream>()
        val deferred = CompletableDeferred<Result<Route>>()

        coEvery { importRoadbookUseCase(any()) } coAnswers { deferred.await() }

        viewModel.importRoute(inputStream)

        // Loading should override Success
        assertEquals(RoadbookUiState.Loading, viewModel.uiState.value.roadbook)

        deferred.complete(Result.success(mockk()))
        // After loading, it should return to repository state (which is still success in this test)
        assertTrue(viewModel.uiState.value.roadbook is RoadbookUiState.Success)
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

    @Test
    fun `onWaypointVisible should call saveRoadbookPositionUseCase`() = runTest {
        coEvery { saveRoadbookPositionUseCase(any(), any()) } returns Unit

        viewModel.onWaypointVisible(10, 20)

        coVerify { saveRoadbookPositionUseCase(10, 20) }
    }
}
