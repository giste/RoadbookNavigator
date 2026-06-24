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
import org.giste.roadbooknavigator.features.roadbook.domain.RoadbookPosition
import org.giste.roadbooknavigator.features.roadbook.domain.Route
import org.giste.roadbooknavigator.features.roadbook.domain.usecase.GetActiveRoadbookUseCase
import org.giste.roadbooknavigator.features.roadbook.domain.usecase.GetRoadbookPositionUseCase
import org.giste.roadbooknavigator.features.roadbook.domain.usecase.ImportRoadbookUseCase
import org.giste.roadbooknavigator.features.roadbook.domain.usecase.SaveRoadbookPositionUseCase
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.InputStream

@OptIn(ExperimentalCoroutinesApi::class)
class RoadbookViewModelTest {

    private val getActiveRoadbookUseCase: GetActiveRoadbookUseCase = mockk()
    private val importRoadbookUseCase: ImportRoadbookUseCase = mockk()
    private val getRoadbookPositionUseCase: GetRoadbookPositionUseCase = mockk()
    private val saveRoadbookPositionUseCase: SaveRoadbookPositionUseCase = mockk()
    
    private val activeRoadbookFlow = MutableStateFlow<Route?>(null)
    private val scrollPositionFlow = MutableStateFlow(RoadbookPosition(0, 0))
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { getActiveRoadbookUseCase() } returns activeRoadbookFlow
        every { getRoadbookPositionUseCase() } returns scrollPositionFlow
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be Empty if no active roadbook exists`() = runTest {
        val viewModel = RoadbookViewModel(
            getActiveRoadbookUseCase,
            importRoadbookUseCase,
            getRoadbookPositionUseCase,
            saveRoadbookPositionUseCase
        )
        backgroundScope.launch(testDispatcher) { viewModel.uiState.collect {} }

        assertEquals(RoadbookUiState.Empty, viewModel.uiState.value)
    }

    @Test
    fun `initial state should be Success if active roadbook exists`() = runTest {
        val route = mockk<Route>()
        activeRoadbookFlow.value = route
        scrollPositionFlow.value = RoadbookPosition(5, 10)

        val viewModel = RoadbookViewModel(
            getActiveRoadbookUseCase,
            importRoadbookUseCase,
            getRoadbookPositionUseCase,
            saveRoadbookPositionUseCase
        )
        backgroundScope.launch(testDispatcher) { viewModel.uiState.collect {} }

        val expectedState = RoadbookUiState.Success(route, initialIndex = 5, initialOffset = 10)
        assertEquals(expectedState, viewModel.uiState.value)
    }

    @Test
    fun `onFileSelected should update state to Loading while processing`() = runTest {
        val viewModel = RoadbookViewModel(
            getActiveRoadbookUseCase,
            importRoadbookUseCase,
            getRoadbookPositionUseCase,
            saveRoadbookPositionUseCase
        )
        backgroundScope.launch(testDispatcher) { viewModel.uiState.collect {} }

        val inputStream = mockk<InputStream>()
        val deferred = CompletableDeferred<Result<Route>>()

        coEvery { importRoadbookUseCase(any()) } coAnswers { deferred.await() }

        viewModel.onFileSelected(inputStream)

        assertEquals(RoadbookUiState.Loading, viewModel.uiState.value)

        deferred.complete(Result.success(mockk()))
    }

    @Test
    fun `onWaypointVisible should call saveRoadbookPositionUseCase`() = runTest {
        val viewModel = RoadbookViewModel(
            getActiveRoadbookUseCase,
            importRoadbookUseCase,
            getRoadbookPositionUseCase,
            saveRoadbookPositionUseCase
        )
        coEvery { saveRoadbookPositionUseCase(any(), any()) } returns Unit

        viewModel.onWaypointVisible(10, 20)

        coVerify { saveRoadbookPositionUseCase(10, 20) }
    }
}
