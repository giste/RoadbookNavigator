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

package org.giste.roadbooknavigator.features.roadbook.ui

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
import org.giste.roadbooknavigator.core.util.Logger
import org.giste.roadbooknavigator.features.roadbook.domain.model.RoadbookPosition
import org.giste.roadbooknavigator.features.roadbook.domain.model.RoadbookSettings
import org.giste.roadbooknavigator.features.roadbook.domain.model.Route
import org.giste.roadbooknavigator.features.roadbook.domain.model.ShortDistanceThreshold
import org.giste.roadbooknavigator.features.roadbook.domain.usecase.GetActiveRoadbookUseCase
import org.giste.roadbooknavigator.features.roadbook.domain.usecase.GetRoadbookPositionUseCase
import org.giste.roadbooknavigator.features.roadbook.domain.usecase.GetRoadbookSettingsUseCase
import org.giste.roadbooknavigator.features.roadbook.domain.usecase.ImportRoadbookUseCase
import org.giste.roadbooknavigator.features.roadbook.domain.usecase.SaveRoadbookPositionUseCase
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.InputStream

@OptIn(ExperimentalCoroutinesApi::class)
class RoadbookViewModelTest {

    private val getActiveRoadbookUseCase: GetActiveRoadbookUseCase = mockk()
    private val importRoadbookUseCase: ImportRoadbookUseCase = mockk()
    private val getRoadbookPositionUseCase: GetRoadbookPositionUseCase = mockk()
    private val saveRoadbookPositionUseCase: SaveRoadbookPositionUseCase = mockk()
    private val getRoadbookSettingsUseCase: GetRoadbookSettingsUseCase = mockk()
    private val logger: Logger = mockk(relaxed = true)

    private val activeRoadbookFlow = MutableStateFlow<Route?>(null)
    private val scrollPositionFlow = MutableStateFlow(RoadbookPosition(0, 0))
    private val settingsFlow = MutableStateFlow(RoadbookSettings())
    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: RoadbookViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { getActiveRoadbookUseCase() } returns activeRoadbookFlow
        every { getRoadbookPositionUseCase() } returns scrollPositionFlow
        every { getRoadbookSettingsUseCase() } returns settingsFlow

        viewModel = RoadbookViewModel(
            getActiveRoadbookUseCase,
            importRoadbookUseCase,
            getRoadbookPositionUseCase,
            saveRoadbookPositionUseCase,
            getRoadbookSettingsUseCase,
            logger
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be Empty`() = runTest {
        backgroundScope.launch(testDispatcher) { viewModel.roadbookState.collect {} }
        assertEquals(RoadbookUiState.Empty, viewModel.roadbookState.value)
    }

    @Test
    fun `success state should be correct when route is available`() = runTest {
        val route = mockk<Route>()
        activeRoadbookFlow.value = route
        scrollPositionFlow.value = RoadbookPosition(5, 10)
        settingsFlow.value = RoadbookSettings(ShortDistanceThreshold(250L))

        backgroundScope.launch(testDispatcher) { viewModel.roadbookState.collect {} }

        val expectedState = RoadbookUiState.Success(
            route = route,
            shortDistanceThreshold = ShortDistanceThreshold(250L),
            initialIndex = 5,
            initialOffset = 10
        )
        assertEquals(expectedState, viewModel.roadbookState.value)
    }

    @Test
    fun `settings update should update roadbookState`() = runTest {
        val route = mockk<Route>()
        activeRoadbookFlow.value = route
        backgroundScope.launch(testDispatcher) { viewModel.roadbookState.collect {} }

        settingsFlow.value = RoadbookSettings(ShortDistanceThreshold(400L))
        assertEquals(400L, (viewModel.roadbookState.value as RoadbookUiState.Success).shortDistanceThreshold.meters)
    }

    @Test
    fun `importRoute should update state to Loading while processing`() = runTest {
        backgroundScope.launch(testDispatcher) { viewModel.roadbookState.collect {} }

        val inputStream = mockk<InputStream>()
        val deferred = CompletableDeferred<Result<Route>>()
        coEvery { importRoadbookUseCase(any()) } coAnswers { deferred.await() }

        viewModel.importRoute(inputStream)

        assertEquals(RoadbookUiState.Loading, viewModel.roadbookState.value)

        val route = mockk<Route>()
        activeRoadbookFlow.value = route
        deferred.complete(Result.success(route))

        // Should return to success (or whatever repository emits)
        assertTrue(viewModel.roadbookState.value is RoadbookUiState.Success)
    }

    @Test
    fun `onWaypointVisible should call saveRoadbookPositionUseCase`() = runTest {
        coEvery { saveRoadbookPositionUseCase(any(), any()) } returns Unit

        viewModel.onWaypointVisible(10, 20)

        coVerify { saveRoadbookPositionUseCase(10, 20) }
    }
}
