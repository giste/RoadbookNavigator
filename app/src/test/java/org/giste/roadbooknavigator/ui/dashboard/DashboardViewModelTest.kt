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
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.giste.roadbooknavigator.features.roadbook.domain.RoadbookRepository
import org.giste.roadbooknavigator.features.roadbook.domain.Route
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.InputStream

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    private val repository: RoadbookRepository = mockk()
    private val activeRoadbookFlow = MutableStateFlow<Route?>(null)
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { repository.activeRoadbook } returns activeRoadbookFlow
        coEvery { repository.loadActiveRoadbook() } returns Result.success(null)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be Empty if no cache exists`() = runTest {
        val viewModel = DashboardViewModel(repository)
        assertEquals(DashboardUiState.Empty, viewModel.uiState.value)
    }

    @Test
    fun `initial state should be Success if cache exists`() = runTest {
        val route = mockk<Route>()
        coEvery { repository.loadActiveRoadbook() } answers {
            activeRoadbookFlow.value = route
            Result.success(route)
        }

        val viewModel = DashboardViewModel(repository)
        assertEquals(DashboardUiState.Success(route), viewModel.uiState.value)
    }

    @Test
    fun `onFileSelected should update state to Success when processing succeeds`() = runTest {
        val viewModel = DashboardViewModel(repository)
        val route = mockk<Route>()
        val inputStream = mockk<InputStream>()

        coEvery { repository.processNewRoadbook(any()) } answers {
            activeRoadbookFlow.value = route
            Result.success(route)
        }

        viewModel.onFileSelected(inputStream)
        assertEquals(DashboardUiState.Success(route), viewModel.uiState.value)
    }

    @Test
    fun `onFileSelected should update state to Error when processing fails`() = runTest {
        val viewModel = DashboardViewModel(repository)
        val inputStream = mockk<InputStream>()
        val errorMessage = "Invalid file format"

        coEvery { repository.processNewRoadbook(any()) } returns Result.failure(Exception(errorMessage))

        viewModel.onFileSelected(inputStream)
        assertEquals(DashboardUiState.Error(errorMessage), viewModel.uiState.value)
    }
}
