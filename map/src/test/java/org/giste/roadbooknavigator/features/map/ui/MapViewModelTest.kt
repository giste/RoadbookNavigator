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

package org.giste.roadbooknavigator.features.map.ui

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.giste.roadbooknavigator.core.util.Logger
import org.giste.roadbooknavigator.features.location.domain.UserLocation
import org.giste.roadbooknavigator.features.location.domain.usecase.ObserveLocationUseCase
import org.giste.roadbooknavigator.features.map.domain.model.MapFile
import org.giste.roadbooknavigator.features.map.domain.model.MapSettings
import org.giste.roadbooknavigator.features.map.domain.usecase.GetLocalMapsUseCase
import org.giste.roadbooknavigator.features.map.domain.usecase.GetMapSettingsUseCase
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MapViewModelTest {

    private val getLocalMapsUseCase: GetLocalMapsUseCase = mockk()
    private val getMapSettingsUseCase: GetMapSettingsUseCase = mockk()
    private val observeLocationUseCase: ObserveLocationUseCase = mockk()
    private val logger: Logger = mockk(relaxed = true)

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState should combine data from use cases`() = runTest {
        val expectedMaps = listOf(MapFile("spain.map", "/path", 100L, 1000L, "europe"))
        val expectedSettings = MapSettings(initialZoom = 12, initialTilt = 30f)
        val expectedLocation = UserLocation(40.0, -3.0, 0.0, 1f, null, 0f, 0f, 0L)

        every { getLocalMapsUseCase() } returns flowOf(expectedMaps)
        every { getMapSettingsUseCase() } returns flowOf(expectedSettings)
        every { observeLocationUseCase() } returns flowOf(expectedLocation)

        val viewModel = MapViewModel(
            getLocalMapsUseCase,
            getMapSettingsUseCase,
            observeLocationUseCase,
            logger,
        )

        val state = viewModel.uiState.first { it.localMaps.isNotEmpty() }
        
        assertEquals(expectedMaps, state.localMaps)
        assertEquals(expectedSettings, state.settings)
        assertEquals(expectedLocation, state.currentLocation)
    }

    @Test
    fun `uiState should update when location updates`() = runTest {
        val maps = emptyList<MapFile>()
        val settings = MapSettings()
        val location1 = UserLocation(40.0, -3.0, 0.0, 1f, null, 0f, 0f, 0L)
        val location2 = UserLocation(41.0, -2.0, 0.0, 1f, null, 0f, 0f, 100L)

        val locationFlow = MutableStateFlow(location1)
        
        every { getLocalMapsUseCase() } returns flowOf(maps)
        every { getMapSettingsUseCase() } returns flowOf(settings)
        every { observeLocationUseCase() } returns locationFlow

        val viewModel = MapViewModel(
            getLocalMapsUseCase,
            getMapSettingsUseCase,
            observeLocationUseCase,
            logger,
        )

        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }
        runCurrent()

        assertEquals(location1, viewModel.uiState.value.currentLocation)

        locationFlow.value = location2
        runCurrent()
        assertEquals(location2, viewModel.uiState.value.currentLocation)
        
        job.cancel()
    }

    @Test
    fun `uiState should reflect empty maps when none are downloaded`() = runTest {
        every { getLocalMapsUseCase() } returns flowOf(emptyList())
        every { getMapSettingsUseCase() } returns flowOf(MapSettings())
        every { observeLocationUseCase() } returns flowOf(UserLocation(0.0, 0.0, 0.0, 0f, null, 0f, 0f, 0L))

        val viewModel = MapViewModel(
            getLocalMapsUseCase,
            getMapSettingsUseCase,
            observeLocationUseCase,
            logger,
        )

        val state = viewModel.uiState.first { it.currentLocation != null }
        assertEquals(0, state.localMaps.size)
    }

    @Test
    fun `uiState should have null location initially if use case has not emitted`() = runTest {
        val pendingLocationFlow = MutableStateFlow(UserLocation(0.0, 0.0, 0.0, 0f, null, 0f, 0f, 0L))
        
        every { getLocalMapsUseCase() } returns flowOf(emptyList())
        every { getMapSettingsUseCase() } returns flowOf(MapSettings())
        every { observeLocationUseCase() } returns pendingLocationFlow
        
        val viewModel = MapViewModel(
            getLocalMapsUseCase,
            getMapSettingsUseCase,
            observeLocationUseCase,
            logger,
        )
        
        assertNull(viewModel.uiState.value.currentLocation)
        assertEquals(emptyList<MapFile>(), viewModel.uiState.value.localMaps)
    }
}
