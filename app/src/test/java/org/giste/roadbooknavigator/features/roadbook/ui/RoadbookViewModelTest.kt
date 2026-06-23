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
import org.giste.roadbooknavigator.features.roadbook.domain.Route
import org.giste.roadbooknavigator.features.roadbook.domain.usecase.GetActiveRoadbookUseCase
import org.giste.roadbooknavigator.features.roadbook.domain.usecase.ImportRoadbookUseCase
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.InputStream

@OptIn(ExperimentalCoroutinesApi::class)
class RoadbookViewModelTest {

    private val getActiveRoadbookUseCase: GetActiveRoadbookUseCase = mockk()
    private val importRoadbookUseCase: ImportRoadbookUseCase = mockk()
    private val activeRoadbookFlow = MutableStateFlow<Route?>(null)
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { getActiveRoadbookUseCase() } returns activeRoadbookFlow
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be Empty if no active roadbook exists`() = runTest {
        val viewModel = RoadbookViewModel(getActiveRoadbookUseCase, importRoadbookUseCase)
        backgroundScope.launch(testDispatcher) { viewModel.uiState.collect {} }

        assertEquals(RoadbookUiState.Empty, viewModel.uiState.value)
    }

    @Test
    fun `initial state should be Success if active roadbook exists`() = runTest {
        val route = mockk<Route>()
        activeRoadbookFlow.value = route

        val viewModel = RoadbookViewModel(getActiveRoadbookUseCase, importRoadbookUseCase)
        backgroundScope.launch(testDispatcher) { viewModel.uiState.collect {} }

        assertEquals(RoadbookUiState.Success(route), viewModel.uiState.value)
    }

    @Test
    fun `onFileSelected should update state to Loading while processing`() = runTest {
        val viewModel = RoadbookViewModel(getActiveRoadbookUseCase, importRoadbookUseCase)
        backgroundScope.launch(testDispatcher) { viewModel.uiState.collect {} }

        val inputStream = mockk<InputStream>()
        val deferred = CompletableDeferred<Result<Route>>()

        coEvery { importRoadbookUseCase(any()) } coAnswers { deferred.await() }

        viewModel.onFileSelected(inputStream)

        assertEquals(RoadbookUiState.Loading, viewModel.uiState.value)

        deferred.complete(Result.success(mockk()))
    }

    @Test
    fun `onFileSelected should update state to Success when processing succeeds`() = runTest {
        val viewModel = RoadbookViewModel(getActiveRoadbookUseCase, importRoadbookUseCase)
        backgroundScope.launch(testDispatcher) { viewModel.uiState.collect {} }

        val route = mockk<Route>()
        val inputStream = mockk<InputStream>()

        coEvery { importRoadbookUseCase(any()) } answers {
            activeRoadbookFlow.value = route
            Result.success(route)
        }

        viewModel.onFileSelected(inputStream)
        assertEquals(RoadbookUiState.Success(route), viewModel.uiState.value)
    }

    @Test
    fun `onFileSelected should update state to Error when processing fails`() = runTest {
        val viewModel = RoadbookViewModel(getActiveRoadbookUseCase, importRoadbookUseCase)
        backgroundScope.launch(testDispatcher) { viewModel.uiState.collect {} }

        val inputStream = mockk<InputStream>()
        val errorMessage = "Invalid file format"

        coEvery { importRoadbookUseCase(any()) } returns Result.failure(Exception(errorMessage))

        viewModel.onFileSelected(inputStream)
        assertEquals(RoadbookUiState.Error(errorMessage), viewModel.uiState.value)
    }
}
